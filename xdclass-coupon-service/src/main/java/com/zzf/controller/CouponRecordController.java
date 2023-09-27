package com.zzf.controller;


import com.zzf.dto.CouponRecordDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.model.CouponRecordDO;
import com.zzf.request.LockCouponRecordRequest;
import com.zzf.request.NewUserCouponRequest;
import com.zzf.service.CouponRecordService;
import com.zzf.util.JsonData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("get user coupon record detail by record id")
    @GetMapping("detail/{record_id}")
    public JsonData getCouponRecordDetailBy(@ApiParam(value = "record_id")  @PathVariable("record_id") long recordId){

        CouponRecordDTO couponRecordDTO = couponRecordService.findById(recordId);

        return couponRecordDTO == null ? JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS):JsonData.buildSuccess(couponRecordDTO);

    }

    @ApiOperation("rpc-lock coupon record")
    @PostMapping("lock_records")
    public JsonData lockCouponRecords(@ApiParam("LockCouponRecordRequest") @RequestBody LockCouponRecordRequest recordRequest){


        JsonData jsonData = couponRecordService.lockCouponRecords(recordRequest);

        return jsonData;

    }


}

