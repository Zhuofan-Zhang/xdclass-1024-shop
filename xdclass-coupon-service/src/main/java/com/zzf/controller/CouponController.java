package com.zzf.controller;


import com.zzf.service.CouponService;
import com.zzf.util.JsonData;
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
 * 前端控制器
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {


    @Autowired
    private CouponService couponService;

    @ApiOperation("get coupon list with pagination")
    @GetMapping("page_coupon")
    public JsonData pageCouponList(
            @ApiParam(value = "page_num") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "page_size") @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Map<String, Object> pageMap = couponService.getCouponListWithPagination(page, size);
        return JsonData.buildSuccess(pageMap);
    }
}

