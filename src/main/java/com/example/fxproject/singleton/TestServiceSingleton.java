package com.example.fxproject.singleton;

import com.example.fxproject.service.TestService;

public class TestServiceSingleton {
    private static final TestService testService = new TestService();
    public static TestService getTestService()
    {
        return testService;
    }
}
