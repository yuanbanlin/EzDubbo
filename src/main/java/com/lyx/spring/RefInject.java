package com.lyx.spring;

import com.lyx.core.anno.Ref;
import com.lyx.core.utils.ProxyFactory;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;


/*BeanPostProcessor
* 完成属性的注入*/
public class RefInject implements BeanPostProcessor {


    /*
    *
    * 同样，这里可能也是初始化的时候会执行这个函数。
    * 那种动态初始化的，还是很可以的
    *
    *
    * */
    private ProxyFactory proxyFactory= ProxyFactory.INSTANCE;

    @Override
    /*初始化之前
    * 不是很安全，因为对象init没有执行*/
    /*初始化！*/
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }




    /*在初始化方法执行后
     * postProcessAfterInitialization ：该方法循环内部使用
     * 启动多个！spring会帮你执行，初始化的时候
     *
     * */

    /*所有bean初始化之后*/
    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {

        /*循环内部的方法！*/
        /*这个o是被注入属性的对象*/
        /*这里注入的属性是要在bean里面的，也就是ioc容器里面的*/

        Field[]fields= o.getClass().getDeclaredFields();
        for(Field field:fields)
        {
            if(field.getAnnotation(Ref.class)!=null)
            {
                //该属性有这个注解
                field.setAccessible(true);
                Object proxy= proxyFactory.getProxy(field.getType());
                field.set(o,proxy);
                field.setAccessible(false);
            }
        }
        return o;
    }
}
