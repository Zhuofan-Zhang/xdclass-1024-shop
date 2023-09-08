package com.zzf.controller;


import com.zzf.request.AddressRequest;
import com.zzf.service.AddressService;
import com.zzf.util.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonData createAddress(@ApiParam("AddressRequest") @RequestBody AddressRequest addressRequest){

        addressService.add(addressRequest);

        return JsonData.buildSuccess();
    }

    @ApiOperation("find address detail by id")
    @GetMapping("find/{address_id}")
    public Object detail(
            @ApiParam(value = "address_id", required = true)
            @PathVariable("address_id") long addressId) {
        System.out.println("in");
        return addressService.detail(addressId);
    }
}

