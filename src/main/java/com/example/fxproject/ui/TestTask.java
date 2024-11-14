package com.example.fxproject.ui;

import com.example.fxproject.misc.TestResult;
import com.example.fxproject.service.TestService;
import com.example.fxproject.singleton.TestServiceSingleton;
import javafx.concurrent.Task;

import java.time.LocalTime;

public class TestTask extends Task<TestResult> {
    private double totalTime = 0.0;
    private int maxIterations;
    private int testType;
    private final int languageUsed;
    private TestService testService = TestServiceSingleton.getTestService();

    public TestTask( int maxIterations, int testType, int languageUsed) {
        this.maxIterations = maxIterations;
        this.testType = testType;
        this.languageUsed = languageUsed;
    }

    @Override
    protected TestResult call() throws Exception {
        double[] testResults = new double[maxIterations];
        updateProgress(0,maxIterations);
        updateMessage(LocalTime.now().toString());
        if(languageUsed == 2){
            testResults = testService.runJavaTest(testType, maxIterations);
        }
        else {
            for (int currentIteration = 0; currentIteration < maxIterations; currentIteration++) {
                // Run the test
                double result = 0;
                if (languageUsed == 0) {
                    result = testService.runCppTest(testType);
                } else if (languageUsed == 1) {
                    result = testService.runPythonTest(testType);
                }
                totalTime += result;
                testResults[currentIteration] = result;

                updateProgress(currentIteration, maxIterations);
                System.out.println((languageUsed == 0 ? "C++: " : "Python: ") + "Result of Test #" + currentIteration + ": " + result);
            }
        }
        updateProgress(20,maxIterations);
        return new TestResult(maxIterations, testResults, totalTime / maxIterations);
    }
}