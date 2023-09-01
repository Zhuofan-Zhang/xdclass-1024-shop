package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.SendCodeEnum;
import com.zzf.mapper.UserMapper;
import com.zzf.model.UserDO;
import com.zzf.request.NewUserCouponRequest;
import com.zzf.request.UserRegisterRequest;
import com.zzf.service.NotifyService;
import com.zzf.service.UserService;
import com.zzf.util.CommonUtil;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserMapper userMapper;
    @Override
    public JsonData register(UserRegisterRequest userRegisterRequest) {
        boolean codeVerification = false;
        if(StringUtils.isNotBlank(userRegisterRequest.getCode())){
            codeVerification = notifyService.verifyCode(SendCodeEnum.USER_REGISTER,userRegisterRequest.getEmail(), userRegisterRequest.getCode());
        }
        if(!codeVerification){
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest,userDo);
        userDo.setCreatedAt(new Date());
        userDo.setSlogan(userRegisterRequest.getSlogan());
        //设置密码 生成秘钥 盐
        userDo.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        //密码+盐处理
        String cryptPwd = Md5Crypt.md5Crypt(userRegisterRequest.getPwd().getBytes(), userDo.getSecret());
        userDo.setPwd(cryptPwd);
        //account unique check TODO

        if(isUnique(userDo.getEmail())) {
            int rows = userMapper.insert(userDo);
            log.info("rows {},successfully registered", rows);
            userRegisterInitTask(userDo);
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    /**
     * 初始化福利信息
     * @param email
     */

    private boolean isUnique(String email){
        QueryWrapper queryWrapper = new QueryWrapper<UserDO>().eq("email", email);

        List<UserDO> list = userMapper.selectList(queryWrapper);

        return list.size() > 0 ? false : true;
    }
    private void userRegisterInitTask(UserDO userDO){
        NewUserCouponRequest request = new NewUserCouponRequest();
        request.setName(userDO.getName());
        request.setUserId(userDO.getId());
//        JsonData jsonData = couponFeignService.addNewUserCoupon(request);
//        if(jsonData.getCode()!=0){
//            throw new RuntimeException("发放优惠券异常");
//        }
//        log.info("发放新用户注册优惠券：{},结果:{}",request.toString(),jsonData.toString());
    }
}
