package com.zzf.controller;


import com.zzf.service.BannerService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
@RestController
@Api("product banner view")
@RequestMapping("/api/banner/v1")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation("products banner view")
    @GetMapping("list")
    public JsonData getBannerList() {
        return JsonData.buildSuccess(bannerService.list());
    }

}

