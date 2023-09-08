package com.zzf.service;

import com.zzf.dto.AddressDTO;
import com.zzf.model.AddressDO;
import com.zzf.request.AddressRequest;
import com.zzf.util.JsonData;

import java.util.List;

public interface AddressService {
    AddressDTO detail(long id);

    void add(AddressRequest addressRequest);

    int del(int addressId);

    List<AddressDTO> listUserAllAddress();
}
