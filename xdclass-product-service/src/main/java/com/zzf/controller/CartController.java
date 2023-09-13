package com.zzf.controller;


import com.zzf.request.CartItemRequest;
import com.zzf.service.CartService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("cart")
@RestController
@RequestMapping("/api/cart/v1")
public class CartController {


    @Autowired
    private CartService cartService;


    @ApiOperation("add to cart")
    @PostMapping("add")
    public JsonData addToCart(@ApiParam("CartItem") @RequestBody CartItemRequest cartItemRequest){
        cartService.addToCart(cartItemRequest);
        return JsonData.buildSuccess();
    }
}
