package com.lyx.core.coder;

public interface Decoder {

    /*就是好像，如果你对象不确定是什么肯定是object，但是你确定的可能就要用class的范型来操作了*/
    /*接口的好处就是抽象出来，然后具体的实现方案你自己来定很妙。*/
    Object decode(byte[] bs);
}
