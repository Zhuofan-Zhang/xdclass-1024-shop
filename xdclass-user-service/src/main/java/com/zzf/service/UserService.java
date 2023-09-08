package com.zzf.service;

import com.zzf.dto.UserDTO;
import com.zzf.request.UserLoginRequest;
import com.zzf.request.UserRegisterRequest;
import com.zzf.util.JsonData;

public interface UserService {

    JsonData register(UserRegisterRequest userRegisterRequest);
    JsonData login(UserLoginRequest userLoginRequest);

    UserDTO getUserDetail();
}
