package com.lyx.test.provider.service.impl;

import com.lyx.core.anno.ExposeService;
import com.lyx.test.api.TestService;

@ExposeService
public class TestServiceImpl implements TestService {
    @Override
    public String test(String test) {
        return "hello, i am Easydubbo,"+test;
    }
}
