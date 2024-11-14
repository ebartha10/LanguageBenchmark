package com.example.fxproject.backend.impl;

import com.example.fxproject.backend.Test;
import com.example.fxproject.javaTests.JavaBenchmark;

import java.nio.file.Path;

public class DynamicMemoryTest implements Test {
    public double runCpp(){
        Path memoryAllocationExePath = getResourcePath("/com/example/fxproject/testUtils/dynamicMemory.cpp");
        if ( memoryAllocationExePath == null){
            System.out.println("Null path.");
            return -1;
        }

        return runCpp(memoryAllocationExePath.toString());
    }

    public double runPython(){
        Path memoryAllocationExePath = getResourcePath("/com/example/fxproject/testUtils/python/dynamicMemory.py");
        if ( memoryAllocationExePath == null){
            System.out.println("Null path.");
            return -1;
        }
        return runPython(memoryAllocationExePath.toString());
    }

    public double[] runJava(int givenIterations){
        return runJava(JavaBenchmark.class.getSimpleName() + ".testDynamicMemory", givenIterations);
    }
}