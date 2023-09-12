package com.zzf.service;

import com.zzf.enums.CouponCategoryEnum;
import com.zzf.request.NewUserCouponRequest;
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
public interface CouponService {
    Map<String, Object> getCouponListWithPagination(int page, int size);

    JsonData addCoupon(long couponId, CouponCategoryEnum category);

    JsonData issueNewUserCoupon(NewUserCouponRequest newUserCouponRequest);

}
