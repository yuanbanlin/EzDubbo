package com.lyx.core.utils;

import com.lyx.core.invoker.Impl.LocalMethodInvoker;
import com.lyx.core.invoker.Impl.RpcMethodInvoker;
import com.lyx.core.invoker.MethodInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    /*这玩意是干啥的*/

    public static final ProxyFactory INSTANCE=new ProxyFactory();


    /*单例模式*/
    private MethodInvoker methodInvoker =RpcMethodInvoker.INSTANCE;

    private  ProxyFactory(){}
    /*只有代理对象这里*/
    /*就是一般来说你传入的是深，就是什么*/
    /*要用这个T来做返回的！*/
    @SuppressWarnings("unchecked")
    public   <T> T getProxy(Class<T> interfaces){


       return  (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class<?>[]{interfaces}, new InvocationHandler() {

           @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               /*代理对象的使用
               * 对toString 和HashCode的排除处理
               *
               * */
               /*代理这就是把你要调的方法
               和参数写过来*/
               String methodName=method.getName();
               if("toString".equals(methodName))
               {
                 return interfaces.getName()+"$Proxy";
               }
                return methodInvoker.invoker(method,args);

           }
        });



    }

}
