package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.SendCodeEnum;
import com.zzf.mapper.UserMapper;
import com.zzf.model.LoginUser;
import com.zzf.model.UserDO;
import com.zzf.request.NewUserCouponRequest;
import com.zzf.request.UserLoginRequest;
import com.zzf.request.UserRegisterRequest;
import com.zzf.service.NotifyService;
import com.zzf.service.UserService;
import com.zzf.util.CommonUtil;
import com.zzf.util.JWTUtil;
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
        if (StringUtils.isNotBlank(userRegisterRequest.getCode())) {
            codeVerification = notifyService.verifyCode(SendCodeEnum.USER_REGISTER, userRegisterRequest.getEmail(), userRegisterRequest.getCode());
        }
        if (!codeVerification) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest, userDo);
        userDo.setCreatedAt(new Date());
        userDo.setSlogan(userRegisterRequest.getSlogan());
        //设置密码 生成秘钥 盐
        userDo.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        //密码+盐处理
        String cryptPwd = Md5Crypt.md5Crypt(userRegisterRequest.getPwd().getBytes(), userDo.getSecret());
        userDo.setPwd(cryptPwd);
        //account unique check TODO

        if (isUnique(userDo.getEmail())) {
            int rows = userMapper.insert(userDo);
            log.info("rows {},successfully registered", rows);
            userRegisterInitTask(userDo);
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {
        List<UserDO> userDOList = userMapper.selectList(new QueryWrapper<UserDO>().eq("email", userLoginRequest.getEmail()));

        if (userDOList != null && userDOList.size() == 1) {
            //已经注册
            UserDO userDO = userDOList.get(0);
            String cryptPwd = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(), userDO.getSecret());
            if (cryptPwd.equals(userDO.getPwd())) {
                //登录成功,生成token TODO

                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(userDO, loginUser);

                String accessToken = JWTUtil.geneJsonWebToken(loginUser);

                //JWT过期自动刷新方案介绍 TODO
                // accessToken
                // accessToken的过期时间
                // UUID生成一个token
                //String refreshToken = CommonUtil.generateUUID();
                //redisTemplate.opsForValue().set(refreshToken,"1",1000*60*60*24*30);

                return JsonData.buildSuccess(accessToken);

            } else {

                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        } else {
            //未注册
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
    }

    /**
     * 初始化福利信息
     *
     * @param email
     */

    private boolean isUnique(String email) {
        QueryWrapper queryWrapper = new QueryWrapper<UserDO>().eq("email", email);

        List<UserDO> list = userMapper.selectList(queryWrapper);

        return list.size() > 0 ? false : true;
    }

    private void userRegisterInitTask(UserDO userDO) {
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
