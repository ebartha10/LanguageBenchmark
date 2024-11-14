package com.example.fxproject.misc;

public class TestResult {
    private int numberOfTests;
    private double[] testResults;
    private double averageTime;

    public TestResult(int numberOfTests, double[] testResults, double averageTime) {
        this.numberOfTests = numberOfTests;
        this.testResults = testResults;
        this.averageTime = averageTime;
    }

    public int getNumberOfTests() {
        return numberOfTests;
    }

    public double[] getTestResults() {
        return testResults;
    }

    public double getAverageTime() {
        return averageTime;
    }
}
