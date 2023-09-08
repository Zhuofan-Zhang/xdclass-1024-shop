package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzf.enums.AddressStatusEnum;
import com.zzf.interceptor.LoginInterceptor;
import com.zzf.mapper.AddressMapper;
import com.zzf.model.AddressDO;
import com.zzf.model.LoginUser;
import com.zzf.request.AddressRequest;
import com.zzf.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressDO detail(long id) {
        return addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id", id));
    }

    @Override
    public void add(AddressRequest addressRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = new AddressDO();
        addressDO.setCreatedAt(new Date());
        addressDO.setUserId(loginUser.getId());

        BeanUtils.copyProperties(addressRequest,addressDO);


        //是否有默认收货地址
        if(addressDO.getDefaultStatus() == AddressStatusEnum.DEFAULT_STATUS.getStatus()){
            //查找数据库是否有默认地址
            AddressDO defaultAddressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                    .eq("user_id",loginUser.getId())
                    .eq("default_status", AddressStatusEnum.DEFAULT_STATUS.getStatus()));

            if(defaultAddressDO != null){
                //修改为非默认收货地址
                defaultAddressDO.setDefaultStatus(AddressStatusEnum.COMMON_STATUS.getStatus());
                addressMapper.update(defaultAddressDO,new QueryWrapper<AddressDO>().eq("id",defaultAddressDO.getId()));
            }
        }

        int rows = addressMapper.insert(addressDO);

        log.info("created new address:rows={},data={}",rows,addressDO);
    }
}
