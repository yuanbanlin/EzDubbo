package com.lyx.core.loadbalance;

import java.util.List;

/*设计负载均衡的接口*/
public interface LoadBalance {

    String loadBalance(List<String> services);

}
