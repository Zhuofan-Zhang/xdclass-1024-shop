package com.zzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzf.model.CouponTaskDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponTaskMapper extends BaseMapper<CouponTaskDO> {

    /**
     * 批量插入
     * @param couponTaskDOList
     * @return
     */
    int insertBatch(@Param("couponTaskList") List<CouponTaskDO> couponTaskDOList);
}
