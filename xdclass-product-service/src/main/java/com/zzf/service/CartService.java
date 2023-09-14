package com.zzf.service;

import com.zzf.dto.CartDTO;
import com.zzf.dto.CartItemDTO;
import com.zzf.request.CartItemRequest;

import java.util.List;

public interface CartService {

    /**
     * 添加是商品到购物车
     * @param cartItemRequest
     */
    void addToCart(CartItemRequest cartItemRequest);

    void clear();

    CartDTO getCart();
}
