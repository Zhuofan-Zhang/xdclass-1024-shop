package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzf.mapper.AddressMapper;
import com.zzf.model.AddressDO;
import com.zzf.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Override
    public AddressDO detail(long id) {
        return addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id",id));
    }
}
