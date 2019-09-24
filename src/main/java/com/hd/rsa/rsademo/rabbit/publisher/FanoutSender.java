package com.hd.rsa.rsademo.rabbit.publisher;


import com.hd.rsa.rsademo.model.UserInfo;
import com.hd.rsa.rsademo.rabbit.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(UserInfo user) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.FANOUT_EXCHANGE, "", user);
    }
}

