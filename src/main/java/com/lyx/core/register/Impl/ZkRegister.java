package com.lyx.core.register.Impl;

import com.lyx.core.register.Register;
import com.lyx.core.utils.Zkutils;

import java.util.List;

public class ZkRegister implements Register {

    /*直接底层调用zk的工具类既可以了
    * */

    /*注册中心底层用的是zkutil，
    * 我明白了，其实你定义了register这个接口对吧，那么你就要一个类去实现他，
    * 面向接口编程这是对的，但是你又不能用静态类去实现，所以只能搞单例，让大家更方便的管理和使用。
    *
    *
    *这里为什么，构造函数设置为private，那么就是要保证只有内部可以访问
    * */

    public  static final ZkRegister INSTANCE= new ZkRegister();

    private  ZkRegister(){};

    @Override
    public List<String> discover(String serviceName) {
        return Zkutils.discover(serviceName);

    }

    @Override
    public void register(String serviceName, String serviceUri) {
        Zkutils.register(serviceName,serviceUri);
    }
}
