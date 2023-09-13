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
            CartItemDTO cartItemVO = new CartItemDTO();

            ProductDTO productVO = productService.findDetailById(productId);
            if(productVO == null){throw new BizException(BizCodeEnum.CART_FAIL);}

            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setPurchaseNum(buyNum);
            cartItemVO.setProductId(productId);
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());
            myCart.put(productId,JSON.toJSONString(cartItemVO));

        }else {
            //存在商品，修改数量
            CartItemDTO cartItem = JSON.parseObject(result,CartItemDTO.class);
            cartItem.setPurchaseNum(cartItem.getPurchaseNum()+buyNum);
            myCart.put(productId,JSON.toJSONString(cartItem));
        }

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


}
