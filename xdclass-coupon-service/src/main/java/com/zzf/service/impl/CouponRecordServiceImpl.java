package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.dto.CouponRecordDTO;
import com.zzf.interceptor.LoginInterceptor;
import com.zzf.mapper.CouponRecordMapper;
import com.zzf.model.CouponRecordDO;
import com.zzf.model.LoginUser;
import com.zzf.service.CouponRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {

    @Autowired
    private CouponRecordMapper couponRecordMapper;
    
    @Override
    public Map<String, Object> page(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        //封装分页信息
        Page<CouponRecordDO> pageInfo = new Page<>(page,size);

        IPage<CouponRecordDO> recordDOIPage =  couponRecordMapper.selectPage(pageInfo,new QueryWrapper<CouponRecordDO>()
                .eq("user_id",loginUser.getId()).orderByDesc("created_at"));

        Map<String,Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record",recordDOIPage.getTotal());
        pageMap.put("total_page",recordDOIPage.getPages());
        pageMap.put("current_data",recordDOIPage.getRecords().stream().map(obj-> mapCouponRecord(obj)).collect(Collectors.toList()));

        return pageMap;
    }

    @Override
    public CouponRecordDTO findById(long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId).eq("user_id",loginUser.getId()));

        if(couponRecordDO == null ){return null;}

        return mapCouponRecord(couponRecordDO);
    }

    private CouponRecordDTO mapCouponRecord(CouponRecordDO couponRecordDO) {


        CouponRecordDTO CouponRecordDTO = new CouponRecordDTO();
        BeanUtils.copyProperties(couponRecordDO,CouponRecordDTO);
        return CouponRecordDTO;
    }
}
