package com.zzf.service;

import com.zzf.request.UserRegisterRequest;
import com.zzf.util.JsonData;

public interface UserService {

    JsonData register(UserRegisterRequest userRegisterRequest);
}
