package com.zzf.controller;


import com.zzf.service.ProductService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
@RestController
@Api("product details service")
@RequestMapping("/api/product/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("get product details with pagination")
    @GetMapping("page")
    JsonData getProductsWithPagination(
            @ApiParam(value = "page")  @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "size") @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Map<String,Object> pageResult = productService.page(page,size);

        return JsonData.buildSuccess(pageResult);
    }


}

