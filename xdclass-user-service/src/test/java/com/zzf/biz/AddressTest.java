package com.zzf.biz;

import com.zzf.UserApplication;
import com.zzf.dto.AddressDTO;
import com.zzf.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class AddressTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void testAddressDetail(){
        AddressDTO addressDTO = addressService.detail(40);
        log.info(addressDTO.getDetailedAddress());
        Assert.assertNotNull(addressDTO);
    }
}
