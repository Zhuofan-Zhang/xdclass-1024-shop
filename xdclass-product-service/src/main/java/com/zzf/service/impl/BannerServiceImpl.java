package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzf.model.BannerDO;
import com.zzf.mapper.BannerMapper;
import com.zzf.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerDO> getBannerList() {
        List<BannerDO> bannerDOList = bannerMapper.selectList(new QueryWrapper<BannerDO>().orderByDesc("weight"));
        return bannerDOList;
    }
}
