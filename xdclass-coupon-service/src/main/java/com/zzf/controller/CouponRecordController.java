package com.zzf.controller;


import com.zzf.service.CouponRecordService;
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
 *  前端控制器
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {



    @Autowired
    private CouponRecordService couponRecordService;



    @ApiOperation("get user's coupon record with pagination")
    @GetMapping("page")
    public JsonData page(@ApiParam(value = "page")  @RequestParam(value = "page", defaultValue = "1") int page,
                         @ApiParam(value = "pageSize") @RequestParam(value = "size", defaultValue = "10") int size){


        Map<String,Object> pageResult = couponRecordService.page(page,size);

        return JsonData.buildSuccess(pageResult);
    }

}

