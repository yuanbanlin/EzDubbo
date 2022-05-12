package com.lyx.test.consumer.service.impl;

import com.lyx.core.anno.Ref;
import com.lyx.test.api.TestService;
import com.lyx.test.consumer.service.ConsumerService;
import org.springframework.stereotype.Service;


/*这里要用service，是因为要注入对象的话，因为ref是这样的啊，*/
@Service
public class ConsumerServiceImpl implements ConsumerService {


    /*我有点懂了，为啥要这么写*/
    /*跟springboot有点像，就是用service来对调用实现各种方法*/
    /*就是用了一些service调用*/
    /*完成类似于dubbo的调用*/


    /*因为*/
    /*这是测试的组件，用这个来生成代理类*/

    /*其实有个中间api作为桥梁*/
    /*它只定义了服务的接口*/
    /*consumer和provider只知道api*/

    /*我没太懂上下文是啥东西！*/
    /*要实现的是整体流程就是：
    * 1。首先在api包里面定义你想要的服务即接口方法。
    * 2。在provider里面 service.impl里面实现这个api里面的接口，并加上@ExposeService注解，暴露provider的服务，
    * 并创建bean放在容器里面，这是我思考的问题。
    * 3。在consumer里面定义一个ConsumerService，然后用@ref注解给testService生成一个代理对象，放在bean中。
    * 4。 在这个服务中直接用testService的功能进行调用。然后
    *5。 Spring测试就是测试的时候，用这玩意来操作把！
    * */
    @Ref
    private TestService testService;

    @Override
    public void testRpc() {

        String result=testService.test("你好");
        System.out.println(result);

    }
}
