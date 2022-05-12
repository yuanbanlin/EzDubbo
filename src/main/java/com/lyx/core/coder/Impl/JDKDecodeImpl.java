package com.lyx.core.coder.Impl;

import com.lyx.core.coder.Decoder;

import java.io.*;


/*
 序列化只是一种实现方式，我们采用的是jdk自带的流序列化，当然你可以用fastjson 序列化！！！
* 这也是合理的。
我的哪个springcloud的项目其实就是json序列化，代码非常干练！！！！！！
*
*
* */
public class JDKDecodeImpl implements Decoder {


    public static final JDKDecodeImpl INSTANCE=new JDKDecodeImpl();

    //给一个无参的构造方法。
    private  JDKDecodeImpl(){}

    @Override
    public Object decode(byte[] bs)  {

        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bs);
        /*你的结果和包装的流先定义好*/
        /*通过两层包装*/
        ObjectInputStream objectInputStream=null;
        Object result=null;
        try {
            /*包装的时候可能会异常，装饰器模式的优化！*/
            objectInputStream=new ObjectInputStream(byteArrayInputStream);
            /*object是这么读的*/
            result = objectInputStream.readObject();


        }catch (Exception e)
        {

            e.printStackTrace();
        }finally {
            /*关闭*/

        }


    return result;


    }
}
