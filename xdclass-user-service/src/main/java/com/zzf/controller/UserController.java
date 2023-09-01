package com.zzf.controller;


import com.zzf.request.UserRegisterRequest;
import com.zzf.service.FileService;
import com.zzf.service.UserService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apiguardian.api.API;
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
    @ApiOperation("用户头像上传")
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

}

