package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import com.zzf.mapper.BannerMapper;
import com.zzf.model.BannerDO;
import com.zzf.service.BannerService;
import com.zzf.vo.BannerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerVO> list() {

        List<BannerDO> bannerDOList =  bannerMapper.selectList(new QueryWrapper<BannerDO>().orderByAsc("weight"));

        List<BannerVO> bannerVOList =  bannerDOList.stream().map(obj->{
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(obj,bannerVO);
            return bannerVO;
        }).collect(Collectors.toList());

        return bannerVOList;
    }
}
