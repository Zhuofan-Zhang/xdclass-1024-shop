package com.zzf.service;

import com.zzf.model.AddressDO;
import com.zzf.request.AddressRequest;
import com.zzf.util.JsonData;

public interface AddressService {
    AddressDO detail(long id);

    void add(AddressRequest addressRequest);
}
