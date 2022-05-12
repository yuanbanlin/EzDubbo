package com.lyx.core.invoker.Impl;

import com.lyx.core.coder.Coder;
import com.lyx.core.coder.Decoder;
import com.lyx.core.coder.Impl.JDKCoderImpl;
import com.lyx.core.coder.Impl.JDKDecodeImpl;
import com.lyx.core.invoker.MethodInvoker;
import com.lyx.core.loadbalance.Impl.RandomLoadBalance;
import com.lyx.core.loadbalance.LoadBalance;
import com.lyx.core.model.Request;
import com.lyx.core.register.Impl.ZkRegister;
import com.lyx.core.register.Register;
import com.lyx.core.transport.Transport;
import com.lyx.core.transport.impl.TransportBIOImpl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


/*
* 到底是我调用代理对象，还是代理对象调用我。
*
*
* */
public class RpcMethodInvoker implements MethodInvoker {

    /*这里是rpc的远程调用*/

    /*这里先不忙着写，因为反正都要用到代理对象，那么我们就先把代理对象创建出来*/

    /*每个要有的地方，用这些东西
    *
    * */


    /**/


    /*
    * 这里是rpc远程的方法调用！。
    * 其实这里就是，我们的rpc方法调用，和ExposeBeanContext 用的方法属性比较多。
    * 重点方法记录一下
    * */

    /*只用接口，不用实现类*/
    /*有这些东西就直接定义，然后放在属性上使用*/

    /*基本上都是单例，那么我这里也要提供一个单利模式， RpcMethodInvoker 提供单例实现，本地方法调用，
    因为需要一个localobject需要初始化，所以不提供单例模式
    *
    *
    *
    * */

    public  static final RpcMethodInvoker INSTANCE=new RpcMethodInvoker();
    private Transport transport = TransportBIOImpl.INSTANCE;
    private Coder coder = JDKCoderImpl.INSTANCE;
    private Decoder decoder = JDKDecodeImpl.INSTANCE;
    private Register register = ZkRegister.INSTANCE;
    private LoadBalance loadBalance = RandomLoadBalance.INSTANCE;

    private RpcMethodInvoker(){};

    @Override
    public Object invoker(Method method, Object[] args) {

        /*讲道理给了一个method
        * 确实可以通过这个method来得到生命他的接口*/
        /*大部分服务都是用string描述的*/

        /* 这里是rpc的远程调用过程，当然要通过socket来传输啊。
        讲解一下这里的思路：方法的抽象就是有一个执行的方法和参数。

        * 1。给你的方法和参数，怎么构建一个request请求呢，首先，先通过方法拿到生命它的接口，即服务名称。
        这么反向得到的形式也是挺多的。然后，把方法的参数得到的名字以及参数都放到request对象中。set
        2。那么这下就是要发送request给相应的服务了。 ip+端口号。当然这里传输的时候，用的是 transport里面要求一个socket以及字节数组。
        3.socket是怎么来的呢：那必然是从注册中心发现的

        * */
        Request request=new Request();
        Class<?> interfaces=method.getDeclaringClass();
        request.setInterfaceName(interfaces.getName());
        request.setMethodName(method.getName());
        request.setArgs(args);


        List<String> uris=register.discover(interfaces.getName());
        String uri =  loadBalance.loadBalance(uris);
        String [] hostAndPort = uri.split(":");

        /*拿到的数据都是这样的*/
        Socket socket= null;
        Object res=null;

        /*try_catch 里面可以这么写*/
        try {
            socket = new Socket(hostAndPort[0],Integer.valueOf(hostAndPort[1]));
            /*将该请求对象发送给服务的提供者*/

            transport.write(coder.code(request),socket);


            /*从远程得到结果*/

            byte[] bs= transport.read(socket);
            res=decoder.decode(bs);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;



    }
}
