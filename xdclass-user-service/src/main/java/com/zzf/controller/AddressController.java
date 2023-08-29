package com.zzf.controller;


import com.zzf.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzf
 * @since 2023-08-29
 */
@Api(tags = "post address")
@RestController
@RequestMapping("/api/address/v1/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("find address detail by id")
    @GetMapping("find/{address_id}")
    public Object detail(
            @ApiParam(value = "address_id", required = true)
            @PathVariable("address_id") long addressId) {
        System.out.println("in");
        return addressService.detail(addressId);
    }
}

