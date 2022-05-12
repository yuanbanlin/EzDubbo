package com.lyx.core.transport.impl;

import com.lyx.core.transport.Transport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TransportBIOImpl implements Transport {

    /*不需要处理
    *
    * */

    public  static  final TransportBIOImpl INSTANCE=new TransportBIOImpl();
    private  TransportBIOImpl(){};

    @Override
    public void write(byte[] bs, Socket conn) {

        /*
        * 这里是就是给了一个字节数组写到socket连接中。
        * */

        OutputStream outputStream=null;
        try {

            outputStream=conn.getOutputStream();
            outputStream.write(bs);


        }catch (Exception e)
        {

            e.printStackTrace();

        }

    }

    @Override
    public byte[] read(Socket conn) {

        /*这里相当于给了个写数据的socket 连接*/
//        用这个来读出一些数据。

        /*怎么拿到字节流呢
        * 首先，你inputstream先读到缓冲区里面，
        * 然后，在从缓冲区里面写入bytearray 输出流。然后转换一下编程字节数组。
        *
        *
        *
        * */
        byte[]bs=null;

        try {

            InputStream inputStream=conn.getInputStream();
            byte[] buff=new byte[1024];
            int len=0;


            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            /*不停地从缓冲区里面读取*/
            /*这里有一点问题，就是第一次读218 ，后面再次进入循环的时候，那边没写，这边可能就不会读了
            *
            * */

            while((len= inputStream.read(buff))!=-1)
            {
                /*写还是挺好的*/
                byteArrayOutputStream.write(buff,0,len);
                if(buff[len-1]==-1) break;

            }
                byteArrayOutputStream.flush();
                bs=byteArrayOutputStream.toByteArray();
        }catch (Exception e)
        {

            e.printStackTrace();

        }finally {

        }

        return bs;




    }
}
