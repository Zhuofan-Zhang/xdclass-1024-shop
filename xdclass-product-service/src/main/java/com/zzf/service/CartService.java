package com.zzf.service;

import com.zzf.request.CartItemRequest;
import com.zzf.vo.CartItemVO;
import com.zzf.vo.CartVO;

import java.util.List;

public interface CartService {

    /**
     * 添加是商品到购物车
     * @param cartItemRequest
     */
    void addToCart(CartItemRequest cartItemRequest);

    /**
     * 清空购物车
     */
    void clear();

    /**
     * 查看我的购物车
     * @return
     */
    CartVO getMyCart();

    /**
     * 删除购物项
     * @param productId
     */
    void deleteItem(long productId);

    /**
     * 修改购物车商品数量
     * @param cartItemRequest
     */
    void changeItemNum(CartItemRequest cartItemRequest);

    List<CartItemVO> confirmOrderCartItems(List<Long> productIdList);
}
