package com.lyx.core.model;

/*接口。*/


import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/*描述一个服务*/
@Data
public class Request implements Serializable {

    /*接口名称*/
    private String interfaceName ;


    //*方法名称*/
    private String MethodName;


    /*方法参数*/
    /*这里不知道是啥，所以Object！*/
    private Object[] args;

    @Override
    public String toString() {
        return "Request{" +
                "interfaceName='" + interfaceName + '\'' +
                ", MethodName='" + MethodName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
