package biz;

import lombok.extern.slf4j.Slf4j;
import com.zzf.OrderApplication;
import com.zzf.model.OrderMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
public class MQTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendDelayMsg(){

        rabbitTemplate.convertAndSend("order.event.exchange","order.close.delay.routing.key","this is new order  msg");

    }



    @Test
    public void testOrderClose(){

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOutTradeNo("123456bac");

        rabbitTemplate.convertAndSend("order.event.exchange","order.close.delay.routing.key",orderMessage);

    }




}



