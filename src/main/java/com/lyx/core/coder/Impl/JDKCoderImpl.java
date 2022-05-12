package com.lyx.core.coder.Impl;

import com.lyx.core.coder.Coder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class JDKCoderImpl implements Coder {


    /*spring是用立即加载使用。。

显然单例模式的要点有三个；一是某个类只能有一个实例；二是它必须自行创建这个实例；三是它必须自行向整个系统提供这个实例。
比如encoder和decoder，这种通用的工具类，可能就需要创建单一实例。
然后，我的思路就是。面向接口编程，然后就是单例是在实现类里面搞的，而且获取也必须是静态的！

*/

    public static final JDKCoderImpl INSTANCE=new JDKCoderImpl();

    //给一个无参的构造方法。
    private  JDKCoderImpl(){}

    @Override
    public byte[] code(Object object) {

        /*输出流是往流里面写东西！*/
        /*输出字节数组流。往字节数组输出流里面写东西，然后可以通过toByteArray转化为字节数组*/

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        /*用这个把bytearry流包装起来*/
        ObjectOutputStream objectOutputStream=null;

        try {

            objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);


            /*这里要更改一下，就是就是要code的时候写一个 -1，标志服*/
            /*写完之后记得清除一下缓冲*/
            /*用write*/
            /*这是byte 你懂吗*/
            /*-1 就不是一个字节*/
            objectOutputStream.writeByte(-1);
            objectOutputStream.flush();


        }catch (Exception e)
        {

            e.printStackTrace();
        }finally {
            /*关闭资源*/

        }

        byte []bs =byteArrayOutputStream.toByteArray();

        return  bs;
    }
}
