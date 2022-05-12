package com.lyx.core.loadbalance.Impl;

import com.lyx.core.loadbalance.LoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {

    /*类有个实例，同时一个类只有一个，创建类的时候就实例化一个对象,同时给一个访问的接口*/

    public static final RandomLoadBalance INSTANCE=new RandomLoadBalance();

    private RandomLoadBalance() {}

    /*这个就是static*/
    private static Random RDM=new Random();

    @Override
    public String loadBalance(List<String> services) {

        return services.get(RDM.nextInt(services.size()));

    }
}
