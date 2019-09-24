package com.hd.rsa.rsademo.rabbit.customer;

import com.hd.rsa.rsademo.model.UserInfo;
import com.hd.rsa.rsademo.rabbit.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiver {

    // queues是指要监听的队列的名字
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE1)
    public void receiveTopic1(UserInfo user) {
        System.out.println("【receiveFanout1监听到消息】" + user);
    }

    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE2)
    public void receiveTopic2(UserInfo user) {
        System.out.println("【receiveFanout2监听到消息】" + user);
    }
}

