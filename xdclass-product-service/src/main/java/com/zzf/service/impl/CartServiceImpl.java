package com.zzf.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzf.constants.CacheKey;
import com.zzf.dto.CartDTO;
import com.zzf.dto.CartItemDTO;
import com.zzf.dto.ProductDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.exception.BizException;
import com.zzf.interceptor.LoginInterceptor;
import com.zzf.model.LoginUser;
import com.zzf.request.CartItemRequest;
import com.zzf.service.CartService;
import com.zzf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addToCart(CartItemRequest cartItemRequest) {

        long productId = cartItemRequest.getProductId();
        int buyNum = cartItemRequest.getPurchaseNum();

        //获取购物车
        BoundHashOperations<String,Object,Object> myCart =  getMyCartOps();

        Object cacheObj = myCart.get(productId);
        String result = "";

        if(cacheObj!=null){
            result =  (String)cacheObj;
        }

        if(StringUtils.isBlank(result)){
            //不存在则新建一个商品
            CartItemDTO cartItemDTO = new CartItemDTO();


            ProductDTO productVO = productService.findDetailById(productId);
            if(productVO == null){throw new BizException(BizCodeEnum.CART_FAIL);}
            cartItemDTO.setAmount(productVO.getAmount());
            cartItemDTO.setPurchaseNum(buyNum);
            cartItemDTO.setProductId(productId);
            cartItemDTO.setProductImg(productVO.getCoverImg());
            cartItemDTO.setProductTitle(productVO.getTitle());
            myCart.put(productId,JSON.toJSONString(cartItemDTO));

            myCart.put(productId,JSON.toJSONString(cartItemDTO));

        }else {
            //存在商品，修改数量
            CartItemDTO cartItem = JSON.parseObject(result,CartItemDTO.class);
            cartItem.setPurchaseNum(cartItem.getPurchaseNum()+buyNum);
            myCart.put(productId,JSON.toJSONString(cartItem));
        }

    }

    @Override
    public void clear() {
        String cartKey = getCartKey();
        redisTemplate.delete(cartKey);
    }

    @Override
    public CartDTO getCart() {
        //获取全部购物项
        List<CartItemDTO> cartItemVOList = buildCartItem(false);

        //封装成cartvo
        CartDTO cartVO = new CartDTO();
        cartVO.setCartItems(cartItemVOList);

        return cartVO;
    }

    @Override
    public void deleteItem(long productId) {
        BoundHashOperations<String,Object,Object> myCart =  getMyCartOps();

        myCart.delete(productId);
    }

    @Override
    public void changeItemNum(CartItemRequest cartItemRequest) {
        BoundHashOperations<String,Object,Object> mycart =  getMyCartOps();

        Object cacheObj = mycart.get(cartItemRequest.getProductId());

        if(cacheObj==null){throw new BizException(BizCodeEnum.CART_FAIL);}

        String obj = (String)cacheObj;

        CartItemDTO cartItemVO =  JSON.parseObject(obj,CartItemDTO.class);
        cartItemVO.setPurchaseNum(cartItemRequest.getPurchaseNum());
        mycart.put(cartItemRequest.getProductId(),JSON.toJSONString(cartItemVO));
    }

    private BoundHashOperations<String,Object,Object> getMyCartOps(){
        String cartKey = getCartKey();
        return redisTemplate.boundHashOps(cartKey);
    }

    private String getCartKey(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String cartKey = String.format(CacheKey.CART_KEY,loginUser.getId());
        return cartKey;

    }

    private List<CartItemDTO> buildCartItem(boolean latestPrice) {

        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();

        List<Object> itemList = myCart.values();

        List<CartItemDTO> cartItemVOList = new ArrayList<>();

        //拼接id列表查询最新价格
        List<Long> productIdList = new ArrayList<>();

        for(Object item: itemList){
            CartItemDTO cartItemVO = JSON.parseObject((String)item,CartItemDTO.class);
            cartItemVOList.add(cartItemVO);

            productIdList.add(cartItemVO.getProductId());
        }

        //查询最新的商品价格
        if(latestPrice){
            setProductLatestPrice(cartItemVOList,productIdList);
        }

        return cartItemVOList;

    }
    private void setProductLatestPrice(List<CartItemDTO> cartItems, List<Long> productIdList) {

        //批量查询
        List<ProductDTO> productVOList = productService.findProductsByIdBatch(productIdList);

        //分组
        Map<Long,ProductDTO> maps = productVOList.stream().collect(Collectors.toMap(ProductDTO::getId, Function.identity()));


        cartItems.forEach(item->{
            ProductDTO productVO = maps.get(item.getProductId());
            item.setProductTitle(productVO.getTitle());
            item.setProductImg(productVO.getCoverImg());
            item.setAmount(productVO.getAmount());
        });
    }


}
