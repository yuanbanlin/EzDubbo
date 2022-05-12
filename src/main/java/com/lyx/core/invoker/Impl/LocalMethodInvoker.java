package com.lyx.core.invoker.Impl;

import com.lyx.core.invoker.MethodInvoker;

import java.lang.reflect.Method;

/*本地方法的调用
* 必须有个实现类对象
*
*
* */
public class LocalMethodInvoker implements MethodInvoker {

    /*因为本地必须要一个方法的实现类*/
     /*所以先用构造器传进来*/
    /*当然也可以用set传进来。*/
    private Object localObject;

    public LocalMethodInvoker(){

    }


    public LocalMethodInvoker(Object localObject) {
        this.localObject = localObject;
    }

    public  void setLocalObject(Object localObject)
    {

        this.localObject=localObject;
    }


    @Override
    public Object invoker(Method method, Object[] args) {

        Object result=null;

        try{
/*完成本地的调用
* 关键就是本地是来自于哪里
* 
*
* */
            result= method.invoke(localObject,args);

        }catch (Exception e)
        {
            e.printStackTrace();

        }finally {

        }
        return result;
    }
}
