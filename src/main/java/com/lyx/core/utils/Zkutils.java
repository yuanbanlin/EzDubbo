package com.lyx.core.utils;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.util.*;

public class Zkutils {


    /*工具类里面实现zk
    * */
    /*初始化zk*/
    private static ZkClient zkClient=null;
    private static final Map<String,List<String>> cache=new HashMap<>();

    /*服务的注册 */
    /*全部都是静态的方法和变量！*/

    static {

        /*服务器的地址*/
        /**//*持久节点，临时节点*/
        /*有序，没序*/

        /*都是通过client来订阅的*/
        zkClient=new ZkClient("127.0.0.1:2181",5000,5000);

        /*直接定义一个数据结构放在内存中*/
//        其实就是一个map

    }



    /*这里要加上注解！*/
    public  static void register(String serviceName,String serviceAddress)
        {

            /*两个不存在的节点放入*/
            if(serviceName==null||serviceName.equals(""))
            {
                throw  new RuntimeException("服务器名称不能为空");
            }
            /*创建服务节点*/
            if(!zkClient.exists("/"+serviceName))
            {
                zkClient.createPersistent("/"+serviceName);
            }

            /*儿子存方具体地址，但是要临时的，且不能重复！*/
            if(!zkClient.exists("/"+serviceName+"/"+serviceAddress)){
                zkClient.createEphemeral("/"+serviceName+"/"+serviceAddress);
            }

            System.out.println(serviceName+"创建节点成功"+"/"+serviceName+"/"+serviceAddress);



        }

/*注册子节点和父节点都需要先判断有没有*/


/*服务的发现 */
/*这里很奇怪*/
/*cache这个东西，就很奇怪*/

        public  static List<String> discover(String serviceName)
        {
            if(serviceName==null||serviceName.equals(""))
            {
                throw  new RuntimeException("服务器名称不能为空");
            }
            if(cache.containsKey(serviceName))
            {
                /*若缓存里面有，则从缓存里面查询*/
                System.out.println("缓存命中！");
                return cache.get(serviceName);
            }

            /*拿到所有的地址 */
            /*就是很简单的一个玩意*/
            /*getchildren的时候得到是list，插入查询的时候都是"/"*/
            /*对zk的输入都是要加上"/"的*/

            /*查的时候不用判断null，因为查不到就会返回null*/
            /*默认考虑查的到的情况！*/
            List<String> children =zkClient.getChildren("/"+serviceName);
            cache.put(serviceName,children);

            /*监听某一个服务*/

            /*这个是动态的，就是一值在监听，因为这个文件已经读入到字节码里面了呀，所以是动态保存的*/
            /*订阅一个服务，匿名类一个listener*/
            zkClient.subscribeChildChanges("/" + serviceName, new IZkChildListener() {
                @Override
                public void handleChildChange(String s, List<String> list) throws Exception {

                /*进来就节点改变了*/
                /*这里是给的发生变化的父亲节点和父亲节点的list*/

                cache.put(serviceName,list);
                System.out.println("完成缓存的更新");


                }
            });

            /*这里有问题。有缓存的时候考虑，服务器下线导致的脏读问题*/
            /*很多框架地方提供监听机制*/



            return children;


        }

    public static void main(String[] args) throws IOException {
//        register("java开发","120");
//        register("java开发","120");
//        register("java开发","120");

        /*注意监听的时候，主线程不要死*/
        /*zk的 zkClient.subscribeChildrenChanges*//*订阅这么一个东西*/
//        /*就是给他添加监听事件，添加一个触发的东西IZKChildListener*/ 发生改变就会触发
        System.in.read();
    }


}
