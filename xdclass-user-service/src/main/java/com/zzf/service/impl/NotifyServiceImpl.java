package com.zzf.service.impl;

import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.SendCodeEnum;
import com.zzf.component.MailService;
import com.zzf.service.NotifyService;
import com.zzf.util.CheckUtil;
import com.zzf.util.CommonUtil;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    private static final String SUBJECT = "VERIFICATION_CODE";
    private static final String CONTENT = "Your verification code is %s. Valid for 60s.";
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        if(CheckUtil.isEmail(to)){
            String code = CommonUtil.getRandomCode(6);
            mailService.sendMail(to,SUBJECT,String.format(CONTENT, code));
            return JsonData.buildSuccess();
        } else if(CheckUtil.isPhone(to)){

        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
