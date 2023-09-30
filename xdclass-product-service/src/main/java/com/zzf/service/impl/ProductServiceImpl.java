package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.config.RabbitMQConfig;
import com.zzf.dto.ProductDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.ProductOrderStateEnum;
import com.zzf.enums.StockTaskStateEnum;
import com.zzf.exception.BizException;
import com.zzf.feign.ProductOrderFeignSerivce;
import com.zzf.mapper.ProductTaskMapper;
import com.zzf.model.ProductDO;
import com.zzf.mapper.ProductMapper;
import com.zzf.model.ProductMessage;
import com.zzf.model.ProductTaskDO;
import com.zzf.request.LockProductRequest;
import com.zzf.request.OrderItemRequest;
import com.zzf.service.ProductService;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private ProductTaskMapper productTaskMapper;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private ProductOrderFeignSerivce productOrderFeignSerivce;

    @Override
    public Map<String, Object> page(int page, int size) {

        Page<ProductDO> pageInfo = new Page<>(page, size);
        IPage<ProductDO> productDOIPage = productMapper.selectPage(pageInfo, null);
        Map<String, Object> productResultMap = new HashMap<>(3);
        productResultMap.put("total_record", productDOIPage.getTotal());
        productResultMap.put("total_page", productDOIPage.getPages());
        productResultMap.put("current_data", productDOIPage.getRecords().stream().map(this::mapProductDTO).collect(Collectors.toList()));

        return productResultMap;
    }

    @Override
    public ProductDTO findDetailById(long id) {
        ProductDO productDO = productMapper.selectById(id);

        return mapProductDTO(productDO);
    }

    @Override
    public List<ProductDTO> findProductsByIdBatch(List<Long> productIdList) {
        List<ProductDO> products = productMapper.selectList(new QueryWrapper<ProductDO>().in("id", productIdList));
        List<ProductDTO> productDTOS = products.stream().map(this::mapProductDTO).collect(Collectors.toList());
        return productDTOS;
    }

    @Override
    public JsonData lockProductStock(LockProductRequest lockProductRequest) {

        String outTradeNo = lockProductRequest.getOrderOutTradeNo();
        List<OrderItemRequest> itemList = lockProductRequest.getOrderItemList();

        //一行代码，提取对象里面的id并加入到集合里面
        List<Long> productIdList = itemList.stream().map(OrderItemRequest::getProductId).collect(Collectors.toList());
        //批量查询
        List<ProductDTO> ProductDTOList = this.findProductsByIdBatch(productIdList);
        //分组
        Map<Long, ProductDTO> productMap = ProductDTOList.stream().collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

        for (OrderItemRequest item : itemList) {
            //锁定商品记录
            int rows = productMapper.lockProductStock(item.getProductId(), item.getPurchaseNum());
            if (rows != 1) {
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
            } else {
                //插入商品product_task
                ProductDTO ProductDTO = productMap.get(item.getProductId());
                ProductTaskDO productTaskDO = new ProductTaskDO();
                productTaskDO.setBuyNum(item.getPurchaseNum());
                productTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
                productTaskDO.setProductId(item.getProductId());
                productTaskDO.setProductName(ProductDTO.getTitle());
                productTaskDO.setOutTradeNo(outTradeNo);
                productTaskMapper.insert(productTaskDO);
                log.info("商品库存锁定-插入商品product_task成功:{}", productTaskDO);

                // 发送MQ延迟消息，介绍商品库存
                ProductMessage productMessage = new ProductMessage();
                productMessage.setOutTradeNo(outTradeNo);
                productMessage.setTaskId(productTaskDO.getId());

                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(), rabbitMQConfig.getStockReleaseDelayRoutingKey(), productMessage);
                log.info("商品库存锁定信息延迟消息发送成功:{}", productMessage);

            }

        }
        return JsonData.buildSuccess();
    }

    @Override
    public boolean releaseProductStock(ProductMessage productMessage) {

        //查询工作单状态
        ProductTaskDO taskDO = productTaskMapper.selectOne(new QueryWrapper<ProductTaskDO>().eq("id", productMessage.getTaskId()));
        if (taskDO == null) {
            log.warn("工作单不存在，消息体为:{}", productMessage);
        }

        //lock状态才处理
        if (taskDO.getLockState().equalsIgnoreCase(StockTaskStateEnum.LOCK.name())) {

            //查询订单状态
            JsonData jsonData = productOrderFeignSerivce.queryProductOrderState(productMessage.getOutTradeNo());

            if (jsonData.getCode() == 0) {

                String state = jsonData.getData().toString();

                if (ProductOrderStateEnum.NEW.name().equalsIgnoreCase(state)) {
                    //状态是NEW新建状态，则返回给消息队，列重新投递
                    log.warn("订单状态是NEW,返回给消息队列，重新投递:{}", productMessage);
                    return false;
                }

                //如果是已经支付
                if (ProductOrderStateEnum.PAY.name().equalsIgnoreCase(state)) {
                    //如果已经支付，修改task状态为finish
                    taskDO.setLockState(StockTaskStateEnum.FINISH.name());
                    productTaskMapper.update(taskDO, new QueryWrapper<ProductTaskDO>().eq("id", productMessage.getTaskId()));
                    log.info("订单已经支付，修改库存锁定工作单FINISH状态:{}", productMessage);
                    return true;
                }
            }

            //订单不存在，或者订单被取消，确认消息,修改task状态为CANCEL,恢复优惠券使用记录为NEW
            log.warn("订单不存在，或者订单被取消，确认消息,修改task状态为CANCEL,恢复商品库存,message:{}", productMessage);
            taskDO.setLockState(StockTaskStateEnum.CANCEL.name());
            productTaskMapper.update(taskDO, new QueryWrapper<ProductTaskDO>().eq("id", productMessage.getTaskId()));


            //恢复商品库存，集锁定库存的值减去当前购买的值
            productMapper.unlockProductStock(taskDO.getProductId(), taskDO.getBuyNum());

            return true;

        } else {
            log.warn("工作单状态不是LOCK,state={},消息体={}", taskDO.getLockState(), productMessage);
            return true;
        }

    }

    private ProductDTO mapProductDTO(ProductDO productDO) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productDO, productDTO);
        productDTO.setStock(productDO.getInventory() - productDO.getLockedInventory());
        return productDTO;
    }
}
