package com.zzf.biz;

import com.zzf.UserApplication;
import com.zzf.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MD5Test {



    @Test
    public void testMd5(){

        log.info(CommonUtil.MD5("123456"));

    }

}
