package com.zzf.biz;


import com.zzf.UserApplication;
import com.zzf.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailTest {


    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail(){
        mailService.sendMail("794666","test email","test test test");
    }

}
