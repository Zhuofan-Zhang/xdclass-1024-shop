package com.zzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.CouponDO;

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

}
