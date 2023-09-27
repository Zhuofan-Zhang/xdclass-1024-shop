package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.config.RabbitMQConfig;
import com.zzf.dto.CouponRecordDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.CouponStateEnum;
import com.zzf.enums.StockTaskStateEnum;
import com.zzf.exception.BizException;
import com.zzf.interceptor.LoginInterceptor;
import com.zzf.mapper.CouponRecordMapper;
import com.zzf.mapper.CouponTaskMapper;
import com.zzf.model.CouponRecordDO;
import com.zzf.model.CouponRecordMessage;
import com.zzf.model.CouponTaskDO;
import com.zzf.model.LoginUser;
import com.zzf.request.LockCouponRecordRequest;
import com.zzf.service.CouponRecordService;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {

    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Autowired
    private CouponTaskMapper couponTaskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    
    @Override
    public Map<String, Object> page(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        //封装分页信息
        Page<CouponRecordDO> pageInfo = new Page<>(page,size);

        IPage<CouponRecordDO> recordDOIPage =  couponRecordMapper.selectPage(pageInfo,new QueryWrapper<CouponRecordDO>()
                .eq("user_id",loginUser.getId()).orderByDesc("created_at"));

        Map<String,Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record",recordDOIPage.getTotal());
        pageMap.put("total_page",recordDOIPage.getPages());
        pageMap.put("current_data",recordDOIPage.getRecords().stream().map(obj-> mapCouponRecord(obj)).collect(Collectors.toList()));

        return pageMap;
    }

    @Override
    public CouponRecordDTO findById(long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId).eq("user_id",loginUser.getId()));

        if(couponRecordDO == null ){return null;}

        return mapCouponRecord(couponRecordDO);
    }

    @Override
    public JsonData lockCouponRecords(LockCouponRecordRequest recordRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        String orderOutTradeNo = recordRequest.getOrderOutTradeNo();
        List<Long> lockCouponRecordIds = recordRequest.getLockCouponRecordIds();


        int updateRows = couponRecordMapper.lockUseStateBatch(loginUser.getId(), CouponStateEnum.USED.name(),lockCouponRecordIds);

        List<CouponTaskDO> couponTaskDOList =  lockCouponRecordIds.stream().map(obj->{
            CouponTaskDO couponTaskDO = new CouponTaskDO();
            couponTaskDO.setCreateTime(new Date());
            couponTaskDO.setOutTradeNo(orderOutTradeNo);
            couponTaskDO.setCouponRecordId(obj);
            couponTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
            return couponTaskDO;
        }).collect(Collectors.toList());

        int insertRows = couponTaskMapper.insertBatch(couponTaskDOList);

        log.info("优惠券记录锁定updateRows={}",updateRows);
        log.info("新增优惠券记录task insertRows={}",insertRows);


        if(lockCouponRecordIds.size() == insertRows && insertRows==updateRows){
            //发送延迟消息

            for(CouponTaskDO couponTaskDO : couponTaskDOList){
                CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
                couponRecordMessage.setOutTradeNo(orderOutTradeNo);
                couponRecordMessage.setTaskId(couponTaskDO.getId());

                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getCouponReleaseDelayRoutingKey(),couponRecordMessage);
                log.info("优惠券锁定消息发送成功:{}",couponRecordMessage.toString());
            }


            return JsonData.buildSuccess();
        }else {

            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }
    }

    private CouponRecordDTO mapCouponRecord(CouponRecordDO couponRecordDO) {


        CouponRecordDTO CouponRecordDTO = new CouponRecordDTO();
        BeanUtils.copyProperties(couponRecordDO,CouponRecordDTO);
        return CouponRecordDTO;
    }
}
