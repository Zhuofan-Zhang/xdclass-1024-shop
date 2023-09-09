package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.dto.CouponDTO;
import com.zzf.enums.CouponCategoryEnum;
import com.zzf.enums.CouponPublishEnum;
import com.zzf.mapper.CouponMapper;
import com.zzf.model.CouponDO;
import com.zzf.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Map<String, Object> getCouponListWithPagination(int page, int size) {

        Page<CouponDO> pageInfo = new Page<>(page, size);

        IPage<CouponDO> couponDOIPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category", CouponCategoryEnum.PROMOTION)
                .orderByDesc("created_at"));
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", couponDOIPage.getTotal());
        pageMap.put("total_page",couponDOIPage.getPages());
        pageMap.put("current_data", couponDOIPage.getRecords().stream().map(obj -> processObj(obj)).collect(Collectors.toList()));


        return pageMap;
    }

    private CouponDTO processObj(CouponDO couponDO) {
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponDO,couponDTO);
        return couponDTO;
    }
}
