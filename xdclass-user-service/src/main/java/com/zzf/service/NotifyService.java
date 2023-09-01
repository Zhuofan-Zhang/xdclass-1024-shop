package com.zzf.service;

import com.zzf.enums.SendCodeEnum;
import com.zzf.util.JsonData;

public interface NotifyService {
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);

    boolean verifyCode(SendCodeEnum sendCodeEnum, String to, String code);
}
