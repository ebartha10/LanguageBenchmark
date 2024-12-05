package com.example.fxproject.javaTests;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JavaBenchmark {
    volatile int foo = 0;

    @Benchmark()
    @Fork(1)
    @Warmup(iterations = 5)
    @Measurement(iterations = 20)
    @BenchmarkMode(Mode.AverageTime)
    public void testMemoryAllocation() {
        int[] var = new int[(int) 1e4];
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 0)
    @Measurement(iterations = 20)
    @BenchmarkMode(Mode.AverageTime)
    public void testStaticMemory() {
        for (int i = 0; i < (int) 1e4; i++) {
            foo = i;
        }
    }
    volatile int[] vect = new int[(int) 1e6];
    @Benchmark
    @Fork(1)
    @Warmup(iterations = 0)
    @Measurement(iterations = 20)
    @BenchmarkMode(Mode.AverageTime)

    public void testDynamicMemory() {
        vect = new int[(int)1e6];
        for (int i = 0; i < (int) 1e6; i++) {
            foo = vect[i];
        }
    }

    // This method simulates the work each thread will do
    private static class DummyThread extends Thread {
        @Override
        public void run() {
            // Simulating minimal work by doing nothing
        }
    }
    @Benchmark
    @Fork(1)
    @Warmup(iterations = 0)
    @Measurement(iterations = 20)
    @BenchmarkMode(Mode.AverageTime)

    public void testThreadCreation() {
        List<Thread> threads = new ArrayList<>((int)1e2);

        for (int i = 0; i < (int)1e2; i++) {
            Thread thread = new DummyThread();
            threads.add(thread);
            thread.start();
        }
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            }
            catch (InterruptedException e) {
                System.out.println("Error thread join.");
            }
        }
    }

    private static final int NUM_THREADS = 2; // Number of threads to create
    private static final int SWITCH_AMOUNT = 100_000; // Number of increments
    private static volatile int sharedInt = 0; // Shared counter
    private static final ReentrantLock lock = new ReentrantLock(); // Lock for synchronized access

    // Thread function to increment the shared counter
    public static class IncrementTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < SWITCH_AMOUNT; i++) {
                lock.lock();
                try {
                    sharedInt++;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 0)
    @Measurement(iterations = 20)
    @BenchmarkMode(Mode.AverageTime)
    public void testContextSwitch() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        sharedInt = 0; // Reset the counter

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new IncrementTask());
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

    }
}
