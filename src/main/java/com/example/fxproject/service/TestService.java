package com.example.fxproject.service;

import com.example.fxproject.backend.Test;
import com.example.fxproject.backend.impl.*;

public class TestService {
    MemoryAllocationTest memoryAllocationTest = new MemoryAllocationTest();
    StaticMemoryTest staticMemoryTest = new StaticMemoryTest();
    DynamicMemoryTest dynamicMemoryTest = new DynamicMemoryTest();
    ThreadCreationTest threadCreationTest = new ThreadCreationTest();
    ThreadContextSwitch threadContextSwitch = new ThreadContextSwitch();

    public double runCppTest(int testType){
        switch (testType) {
            case Test
                    .TEST_MEMORY_ALLOC -> {
                return memoryAllocationTest.runCpp();
            }
            case Test.TEST_STATIC_MEMORY -> {
                return staticMemoryTest.runCpp();
            }
            case Test.TEST_DYNAMIC_MEMORY -> {
                return dynamicMemoryTest.runCpp();
            }
            case Test.TEST_THREAD_CREATION -> {
                return threadCreationTest.runCpp();
            }
            case Test.TEST_THREAD_CONTEST_SWITCH -> {
                return threadContextSwitch.runCpp();
            }
            default -> {
                return -1;
            }
        }
    }
    public double[] runJavaTest(int testType, int givenIterations){
        switch (testType) {
            case Test
                    .TEST_MEMORY_ALLOC -> {
                return memoryAllocationTest.runJava(givenIterations);
            }
            case Test.TEST_STATIC_MEMORY -> {
                return staticMemoryTest.runJava(givenIterations);
            }
            case Test.TEST_DYNAMIC_MEMORY -> {
                return dynamicMemoryTest.runJava(givenIterations);
            }
            case Test.TEST_THREAD_CREATION -> {
                return threadCreationTest.runJava(givenIterations);
            }
            case Test.TEST_THREAD_CONTEST_SWITCH -> {
                return threadContextSwitch.runJava(givenIterations);
            }
            default -> {
                return null;
            }
        }
    }
    public double runPythonTest(int testType){
        switch (testType) {
            case Test
                    .TEST_MEMORY_ALLOC -> {
                return memoryAllocationTest.runPython();
            }
            case Test.TEST_STATIC_MEMORY -> {
                return staticMemoryTest.runPython();
            }
            case Test.TEST_DYNAMIC_MEMORY -> {
                return dynamicMemoryTest.runPython();
            }
            case Test.TEST_THREAD_CREATION -> {
                return threadCreationTest.runPython();
            }
            case Test.TEST_THREAD_CONTEST_SWITCH -> {
                return threadContextSwitch.runPython();
            }
            default -> {
                return -1;
            }
        }
    }
}
