package com.lyx.test.provider;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/*启动类*/
public class ProviderApp {


    public static void main(String[] args) {

        /*启动ioc容器成功*/
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("classpath:spring-provider.xml");

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
