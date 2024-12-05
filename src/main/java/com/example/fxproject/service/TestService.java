package com.example.fxproject.service;

import com.example.fxproject.backend.Test;
import com.example.fxproject.backend.impl.*;

import java.util.Arrays;

public class TestService {
    MemoryAllocationTest memoryAllocationTest = new MemoryAllocationTest();
    StaticMemoryTest staticMemoryTest = new StaticMemoryTest();
    DynamicMemoryTest dynamicMemoryTest = new DynamicMemoryTest();
    ThreadCreationTest threadCreationTest = new ThreadCreationTest();
    ThreadContextSwitch threadContextSwitch = new ThreadContextSwitch();
    ThreadMigrationTest threadMigrationTest  = new ThreadMigrationTest();

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
            case Test.TEST_THREAD_MIGRATION -> {
                return threadMigrationTest.runCpp();
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
                return Arrays.stream(memoryAllocationTest.runJava(givenIterations)).map(e -> e/(int)1e4).toArray();
            }
            case Test.TEST_STATIC_MEMORY -> {
                return Arrays.stream(staticMemoryTest.runJava(givenIterations)).map(e -> e/ (int)1e4).toArray();
            }
            case Test.TEST_DYNAMIC_MEMORY -> {
                return Arrays.stream(dynamicMemoryTest.runJava(givenIterations)).map(e -> e / (int) 1e6).toArray();
            }
            case Test.TEST_THREAD_CREATION -> {
                return Arrays.stream(threadCreationTest.runJava(givenIterations)).map(e -> e / (int) 1e2).toArray();
            }
            case Test.TEST_THREAD_CONTEST_SWITCH -> {
                return Arrays.stream(threadContextSwitch.runJava(givenIterations)).map(e -> e / (int) 1e5).toArray();
            }
            case Test.TEST_THREAD_MIGRATION -> {
                return Arrays.stream(threadMigrationTest.runJava(givenIterations)).map(e -> e / (int)1e9).toArray();
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
            case Test.TEST_THREAD_MIGRATION -> {
                return threadMigrationTest.runPython();
            }
            default -> {
                return -1;
            }
        }
    }
}
