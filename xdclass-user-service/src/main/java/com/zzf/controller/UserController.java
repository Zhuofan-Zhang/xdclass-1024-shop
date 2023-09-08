package com.zzf.controller;


import com.zzf.dto.UserDTO;
import com.zzf.request.UserLoginRequest;
import com.zzf.request.UserRegisterRequest;
import com.zzf.service.FileService;
import com.zzf.service.UserService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zzf.enums.BizCodeEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zzf
 * @since 2023-08-29
 */

@Api(tags = "user module")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;
    @ApiOperation("user upload avatar image")
    @PostMapping(value = "upload")
    public JsonData uploadHeaderImg(@ApiParam(value = "文件上传", required = true) @RequestPart("file") MultipartFile file) {

        String result = fileService.uploadUserHeadImg(file);

        return result != null ? JsonData.buildSuccess(result) : JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);

    }

    @ApiOperation("user registration")
    @PostMapping("register")
    public JsonData register(@ApiParam("userRegisterRequest") @RequestBody UserRegisterRequest userRegisterRequest){
        return userService.register(userRegisterRequest);
    }


    @ApiOperation("user login")
    @PostMapping("login")
    public JsonData login(@ApiParam("用户登录对象") @RequestBody UserLoginRequest userLoginRequest){


        return userService.login(userLoginRequest);
    }

    @ApiOperation("get customer information")
    @GetMapping("detail")
    public JsonData detail(){

        UserDTO userDTO = userService.getUserDetail();

        return JsonData.buildSuccess(userDTO);
    }

    //    刷新token的方案 TODO
//    @PostMapping("refresh_token")
//    public JsonData getRefreshToken(Map<String,Object> param){
//
//        //先去redis,找refresh_token是否存在
//        //refresh_token存在，解密accessToken
//        //重新调用JWTUtil.geneJsonWebToken() 生成accessToken
//        //重新生成refresh_token，并存储redis，设置30天过期时间
//        //返回给前端
//        return null;
//    }

}

