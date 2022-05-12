package com.lyx.core.anno;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*这个注解放在什么上面来生效,放在类上*/

/*
* 创建一个注解标记在类上面，功能是暴露服务
* 同时创建该类的对象，放在容器里面
*
* 我的想法就是其实大部分spring注解就这两个功能都是这么来做的
* */

/*先使用component让spring知道这些先生成对象。*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ExposeService  {




}
