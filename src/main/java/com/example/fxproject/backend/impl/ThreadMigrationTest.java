package com.example.fxproject.backend.impl;

import com.example.fxproject.backend.Test;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ThreadMigrationTest implements Test {
    public double runCpp(){
        Path memoryAllocationExePath = getResourcePath("/com/example/fxproject/testUtils/threadMigration.cpp");
        if ( memoryAllocationExePath == null){
            System.out.println("Null path.");
            return -1;
        }

        return runCpp(memoryAllocationExePath.toString());
    }

    public double runPython(){
        Path memoryAllocationExePath = getResourcePath("/com/example/fxproject/testUtils/python/threadMigration.py");
        if ( memoryAllocationExePath == null){
            System.out.println("Null path.");
            return -1;
        }
        return runPython(memoryAllocationExePath.toString());
    }

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        int GetCurrentProcessorNumber(); // Gets the current processor number
    }
    public double[] runJava(int givenIterations){
        List<Double> migrationIntervals = new ArrayList<Double>();
        // Thread to perform the computation and check for migrations
        Thread computationThread = new Thread(() -> {
            int migrationCount = 0;
            int previousCore = -1;
            long previousTime = System.nanoTime();

            long dummyComputation = 0; // For performing some dummy computations
            while (migrationCount < givenIterations) {
                // Perform computation
                dummyComputation += Math.sqrt(dummyComputation + 1);
                long currentTime = System.nanoTime();

                // Check the current core
                int currentCore = Kernel32.INSTANCE.GetCurrentProcessorNumber();

                // Detect migration
                if (currentCore != previousCore && previousCore != -1) {
                    double interval = (currentTime - previousTime) / 1.0;
                    synchronized (migrationIntervals) {
                        migrationIntervals.add(interval);
                    }
                    migrationCount++;

                    System.out.println("Migration " + migrationCount + ": Core changed from " +
                                               previousCore + " to " + currentCore + ". Interval: " + interval + " ns");
                }

                // Update the previous core and time
                previousCore = currentCore;
                previousTime = currentTime;
            }
        });
        // Start the computation thread
        computationThread.start();

        // Wait for the thread to finish
        try {
            computationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return migrationIntervals.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
