package com.zzf.mapper;

import com.zzf.model.ProductOrderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzf.model.ProductOrderItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzf
 * @since 2023-09-16
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrderDO> {
    void updateOrderPayState(@Param("outTradeNo") String outTradeNo,@Param("newState") String newState, @Param("oldState") String oldState);

}
