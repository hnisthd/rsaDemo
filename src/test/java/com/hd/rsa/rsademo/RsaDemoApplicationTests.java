package com.hd.rsa.rsademo;

import com.hd.rsa.rsademo.model.UserInfo;
import com.hd.rsa.rsademo.rabbit.publisher.DirectSender;
import com.hd.rsa.rsademo.rabbit.publisher.FanoutSender;
import com.hd.rsa.rsademo.rabbit.publisher.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RsaDemoApplicationTests {

    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private DirectSender directSender;

    /**
     * Fanout测试
     * @throws Exception
     */
    @Test
    public void testFanout() throws Exception {
        UserInfo user=new UserInfo();
        user.setUserId("1");
        user.setUserName("pwl");
        fanoutSender.send(user);
    }



    /**
     * TOPIC测试
     * @throws Exception
     */
    @Test
    public void testTopic() throws Exception {
        UserInfo user=new UserInfo();
        user.setUserId("1");
        user.setUserName("pwl");
        topicSender.send(user);
    }

    /**
     * DIRECT测试
     * @throws Exception
     */
    @Test
    public void testDirect() throws Exception {
        UserInfo user=new UserInfo();
        user.setUserId("1");
        user.setUserName("pwl");
        directSender.send(user);
    }


}
