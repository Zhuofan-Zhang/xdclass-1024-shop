package com.zzf.controller;


import com.zzf.dto.CartDTO;
import com.zzf.request.CartItemRequest;
import com.zzf.service.CartService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("clear cart items")
    @DeleteMapping("clear")
    public JsonData clearCart(){
        cartService.clear();
        return JsonData.buildSuccess();
    }

    @ApiOperation("view my cart")
    @GetMapping("cart")
    public JsonData getMyCart(){
        CartDTO cartDTO = cartService.getCart();
        return JsonData.buildSuccess(cartDTO);
    }
}
