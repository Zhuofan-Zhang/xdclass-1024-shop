package com.zzf.service.impl;

import com.zzf.constants.CacheKey;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.SendCodeEnum;
import com.zzf.component.MailService;
import com.zzf.service.NotifyService;
import com.zzf.util.CheckUtil;
import com.zzf.util.CommonUtil;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    private static final String SUBJECT = "VERIFICATION_CODE";
    private static final String CONTENT = "Your verification code is %s. Valid for 60s.";
    private static final int CODE_EXPIRED = 60*1000*10;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY,sendCodeEnum.name(),to);

        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        //如果不为空，则判断是否60秒内重复发送
        if(StringUtils.isNotBlank(cacheValue)){
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-验证码发送时间戳，如果小于60秒，则不给重复发送
            if(CommonUtil.getCurrentTimestamp() - ttl < 1000*60){
                log.info("重复发送验证码,时间间隔:{} 秒",(CommonUtil.getCurrentTimestamp()-ttl)/1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        //拼接验证码 2322_324243232424324
        String code = CommonUtil.getRandomCode(6);

        String value = code+"_"+CommonUtil.getCurrentTimestamp();

        redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRED, TimeUnit.MILLISECONDS);

        if(CheckUtil.isEmail(to)){
            //邮箱验证码
            mailService.sendMail(to,SUBJECT,String.format(CONTENT,code));

            return JsonData.buildSuccess();

        } else if(CheckUtil.isPhone(to)){
            //短信验证码

        }

        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
