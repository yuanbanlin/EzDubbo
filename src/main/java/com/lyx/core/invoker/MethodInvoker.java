package com.lyx.core.invoker;


import java.lang.reflect.Method;

/*提供者是本地调用
* 消费者是远程调用
* */
public interface MethodInvoker {
    /*方法的调用抽象出来
    * 两种实现类，一个本地调用，一个远程调用。
    * 其实接口就是定义一个规范，然后用不同的实现类来操作。
    * */

    /*
    * 1。本地调用：
    *2。远程调用
    * 把方法抽象出来，没见过！
    *
    *
    *
    *
    *
    *
    *
    *
    * */



    Object invoker(Method method,Object[] args);
}
