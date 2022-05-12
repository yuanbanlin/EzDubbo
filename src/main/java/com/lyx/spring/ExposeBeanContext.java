package com.lyx.spring;

import com.lyx.core.anno.ExposeService;
import com.lyx.core.coder.Coder;
import com.lyx.core.coder.Decoder;
import com.lyx.core.coder.Impl.JDKCoderImpl;
import com.lyx.core.coder.Impl.JDKDecodeImpl;
import com.lyx.core.invoker.Impl.LocalMethodInvoker;
import com.lyx.core.invoker.MethodInvoker;
import com.lyx.core.model.Request;
import com.lyx.core.register.Impl.ZkRegister;
import com.lyx.core.register.Register;
import com.lyx.core.transport.Transport;
import com.lyx.core.transport.impl.TransportBIOImpl;
import com.sun.jmx.defaults.ServiceName;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/*自动执行。*/

public class ExposeBeanContext implements ApplicationContextAware , InitializingBean {



    /*注册属性*/

    private Register register =ZkRegister.INSTANCE;
    /*imooc上那门课也是这样的，就是集成一个接口，定义一个属性，然后把接口传过来的复制给属性*/
    /*这几个就相当于用spring启动的时候，帮助两个注解的东西，很不错！！！！*/
    /*对象初始化成了，我们怎么使用@ExposeService 标记的对象
    * 那个.xml文件把    */

    /*ApplicationContextAware接口只有一个方法，如果实现了这个方法，
    那么Spring创建这个实现类的时候就会自动执行这个方法，把ApplicationContext注入到这个类中，也
    就是说，spring 在启动的时候就需要实例化这个 class（如果是懒加载就是你需要用到的时候实例化），
    在实例化这个 class 的时候，发现它包含这个 ApplicationContextAware 接口的话，sping 就会调用这个对象的
     setApplicationContext 方法，把 applicationContext Set 进去了。*/

    /*有的就是接口来操作的。定义属性的*/


//    /*这种自动执行的，就可以通过再方法中初始化！！！我觉得很妙，imooc那个也是*/
    /*两个是共用的*/
    private ApplicationContext ioc;
    private Set<String> serviceName;
    private Integer port = 8888;
    private Transport transport = TransportBIOImpl.INSTANCE;
    private Decoder decoder = JDKDecodeImpl.INSTANCE;
    private  Coder coder = JDKCoderImpl.INSTANCE;

    /*直接一个初始化的过程。*/
    private ThreadPoolExecutor threadPool= new ThreadPoolExecutor(

            /*核心先层数，当有任务的时候，可以创建线程，放在线程池子中，然后执行任务，但是创建的线程超过了核心线程，那么就先把任务放在队列中
             * 一般是 是 cpu*2
             * */
            12,
            /*当队列里面满了，创建额外的线程来执行，但是创建的数最大不超过这个数字*/
            /*/

            /*假如当前的线程数超过了corePoolsize，但是这些线程超过这个事件没有被执行，就会被销毁*/
            16,
            4,
            /*事件单位*/
            TimeUnit.SECONDS,
//     任务队列，这里用阻塞队列。
            /*阻塞队列是一种队列，一种可以在多线程环境下使用，并且支持阻塞等待的队列。也就是说，阻塞队列和一般的队列的区别就在于：

多线程环境支持，多个线程可以安全的访问队列

支持生产和消费等待，多个线程之间互相配合，当队列为空的时候，消费线程会阻塞，等待队列不为空；当队列满了的时候，生产线程就会阻塞，直到队列不满。*/
//协程就是用户级线程
//Windows 中的 钎程
//指定执行入口，底层需要分配协程栈，让出执行权时，也需要保存现场。
//关键在于控制流的让出与恢复
//发展协程的原因：高并发场景下：多进程需要使用大量内存
//多线程由于线程间切换时需要切换对应的内核栈，导致切换代价高。
//所以有了协程  用户态调度模型  IO复用高并发
//            1、建立 ServerSocketChannel
//            2、通过 Selector.open() 打开一个 Selector.
//            3、将 Channel 注册到 Selector 中, 并设置需要监听的事件
//            4、循环:
//            1、调用 select() 方法
//2、调用 selector.selectedKeys() 获取 就绪 Channel
//3、迭代每个 selected key:
//            注册和取消注册！那是杂多的，我忘了。


            new LinkedBlockingQueue<Runnable>(),


            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    /*问题*/
                    return new Thread(r);
                }
            },
            /*怎么创建线程*/

            /*当线程达到最大数字，怎么拒绝任务*/

            /*队列满了，线程数也达到最大了*/
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                    System.out.println("单子太多了，不接了");
                    throw  new RuntimeException("xxxxx");

                    /*就是，问题的关键就在于线程池和selecotr*/
                    /*当然可以重开一个线程，然后继续执行*/
                    /*重写的时候*/
                    /*这里的r是拒绝的runnable任务*/
                    /*exector是线程池*/



                }
            }

    );

    /*队列这个东西，就很恶心，你知道吗。*/

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /*把ioc放到属性里面，也可以方便其他方法用*/
         this.ioc=applicationContext;
         /*通过注解得到对象
         * *
         因为首先你加入了@Componet的标准
         /
          */
        /*有的时候就是给你的这个东西是自动有用的！*/
        /*这个是个map*/
         Map<String,Object> beans = applicationContext.getBeansWithAnnotation(ExposeService.class);

        /*完成注册
        * 拿到每个东西！
        *
        *  */
        Collection<Object> objects=beans.values();
        serviceName=new HashSet<>(objects.size());
        for(Object o:objects)
        {
            /*这里第一个接口，不太懂
            * 对象拿到接口getclass（）。getINterfaces
            * 就是实现类的第一个接口*/
            Class<?> interfaces=  o.getClass().getInterfaces()[0];
            serviceName.add(interfaces.getName());
        }
        /*这里是初始化的过程，
        * 就是bean初始化后
        *
        * */

        /*因为该方法仅仅代表对象创建成拱了，但是对象并不完整*/


    }
    /*initialzingBean
    * 对象的属性注入后，已经完整了*/
    @Override
    public void afterPropertiesSet() throws Exception {

        /*ioc容器整体完成之后*/
        for(String server:serviceName)
        {
            register.register(server,"localhost:"+port);

        }

        /*线程当然可以抽象为类*/
        /*但是我们还是习惯这么来做*/

        /*现在这里写把，注册服务之后，Spring初始化后，我们不能在主线程里面监听，而是新开一个线程
        * 来接受服务
        *
        * */

    /*这个是有生命周期的*/

        /*来理一下这里的逻辑
        （这里端口是死的）
        * 首先，你一个端口的所有服务都加载完毕，那么就要监听啊！！！。
        * 1。首先，这里创建监听的serverSocket;
        * 2。然后，接受一个socket连接。
        * 3。之后呢，就是 transport.read(Socket) 读出来字节数组。然后decode 转化为Request，这里你知道是request所以肯定直接是request对象。
        * 4。然后将request的接口名字，方法名字以及参数取出来。
        * 5。你本地方法调用是不是需要method 以及 一个本地对象 以及参数。
        * 6。 首先通过接口名称得到类，Class.forname();
        * 7。 类里面有方法，但是要名字和参数类型，然后我们通过args.getClass()得到相应的types 数组 通过getMethod(name, type);
        . 8. 一个本地的localObject， 就是通过ioc的getbean得到具体的实现类，然后传入进去。
        9。得到接口后用socket 和tranport.write写回去。
        10。 当然，这里是先定义一个serversokcet 然后一直while handlerRequest



        * */
        /*另外开一个线程*/
        new Thread(new Runnable() {
            @Override
            public void run() {

                ServerSocket serverSocket = null;
                System.out.println("服务已经开始监听"+port);
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*就是定义一个socket*/
                while(true)
                {
                    handlerRequest(serverSocket);
                }
            }

            private void handlerRequest(ServerSocket serverSocket) {
                Socket socket;
                

                    /*处理请求。*/

                    /*
                    *
                    *
                    * */

                try {
                    socket =serverSocket.accept();
                    threadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            handlerEach(socket);
                        }
                    });
                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            private void handlerEach(Socket socket) {
                try {
                byte[] bs = transport.read(socket);
                Request request = (Request) decoder.decode(bs);
                /*这里只有强转了，才能*/
                String interfaceName = request.getInterfaceName();
                String methodName = request.getMethodName();
                Class<?> interfaces;


                interfaces = Class.forName(interfaceName);
                Object[] args = request.getArgs();
                Class<?>[] types = null;
                if (args != null && args.length > 0) {
                    types = new Class<?>[args.length];
                    for (int i = 0; i < args.length; i++) {
                        types[i] = args[i].getClass();
                    }
                }

                Method method = interfaces.getMethod(methodName, types);

                Object localobject = ioc.getBean(interfaces);
                /*初始化*/
                /*直接在这里定义的*/
//                  就是不定义在属性中，就是你要new 出来的，new一个方法调用出来是很不错的。
                /*
               * 因为是本地方法调用，那么他需要一个本地object，通过初始化传过去
               *
               *  */

                MethodInvoker methodInvoker = new LocalMethodInvoker(localobject);

                Object obj = methodInvoker.invoker(method, request.getArgs());
                transport.write(coder.code(obj), socket);


            } catch (Exception e) {

                e.printStackTrace();


            }
            finally {
                /*关闭资源*/
            }
            }

        }).start();



}
}
