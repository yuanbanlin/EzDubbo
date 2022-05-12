package com.lyx.test.consumer;

import com.lyx.test.consumer.service.ConsumerService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("classpath:spring-provider.xml");

        ConsumerService consumerService=app.getBean(ConsumerService.class);
        consumerService.testRpc();
        try{

            System.out.println("服务已经启动");
            System.in.read();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
