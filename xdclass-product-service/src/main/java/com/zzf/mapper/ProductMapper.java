package com.zzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzf.model.ProductDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
public interface ProductMapper extends BaseMapper<ProductDO> {

    int lockProductStock(@Param("productId") long productId, @Param("buyNum") int buyNum);

    void unlockProductStock(@Param("productId")Long productId, @Param("buyNum")Integer buyNum);

}
