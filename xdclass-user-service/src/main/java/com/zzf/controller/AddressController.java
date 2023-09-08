package com.zzf.controller;


import com.zzf.dto.AddressDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.request.AddressRequest;
import com.zzf.service.AddressService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @ApiOperation("add delivery address")
    @PostMapping("add")
    public JsonData createAddress(@ApiParam("AddressRequest") @RequestBody AddressRequest addressRequest) {

        addressService.add(addressRequest);

        return JsonData.buildSuccess();
    }

    @ApiOperation("find address detail by id")
    @GetMapping("find/{address_id}")
    public Object detail(
            @ApiParam(value = "address_id", required = true)
            @PathVariable("address_id") long addressId) {
        AddressDTO addressDTO = addressService.detail(addressId);

        return addressDTO == null ? JsonData.buildResult(BizCodeEnum.ADDRESS_NO_EXITS):JsonData.buildSuccess(addressDTO);
    }

    @ApiOperation("delete address by id")
    @DeleteMapping("/del/{address_id}")
    public JsonData del(
            @ApiParam(value = "address id", required = true)
            @PathVariable("address_id") int addressId) {

        int rows = addressService.del(addressId);

        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }

    @ApiOperation("get full list of user's addresses")
    @GetMapping("/list")
    public JsonData findUserAllAddress() {

        List<AddressDTO> list = addressService.listUserAllAddress();

        return JsonData.buildSuccess(list);
    }

}

