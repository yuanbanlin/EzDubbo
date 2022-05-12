package com.lyx.core.register;


import java.util.List;

/*
* 也是接口设计，
* 设计注册中心功能。
*
* */
public interface Register {
    /*服务发现*/
    List<String> discover(String serviceName);
    void register(String serviceName,String serviceUri);

}
