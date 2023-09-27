package com.zzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.dto.CouponRecordDTO;
import com.zzf.model.CouponRecordDO;
import com.zzf.model.CouponRecordMessage;
import com.zzf.request.LockCouponRecordRequest;
import com.zzf.util.JsonData;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
public interface CouponRecordService{

    Map<String, Object> page(int page, int size);

    CouponRecordDTO findById(long recordId);

    JsonData lockCouponRecords(LockCouponRecordRequest recordRequest);

    boolean releaseCouponRecord(CouponRecordMessage recordMessage);
}
