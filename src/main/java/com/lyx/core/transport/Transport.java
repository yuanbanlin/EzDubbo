package com.lyx.core.transport;

/*网络传输的抽象
*
*
*
*
* */


import java.net.Socket;

public interface Transport {

    /*写数据读数据*/
    void write(byte [] bs, Socket conn);

    byte[] read (Socket conn);


}
