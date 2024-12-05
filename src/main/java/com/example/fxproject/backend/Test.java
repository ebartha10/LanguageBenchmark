package com.example.fxproject.backend;

import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Test {
    int TEST_MEMORY_ALLOC = 0;
    int TEST_STATIC_MEMORY = 1;
    int TEST_DYNAMIC_MEMORY = 2;
    int TEST_THREAD_CREATION = 3;
    int TEST_THREAD_CONTEST_SWITCH = 4;
    int TEST_THREAD_MIGRATION = 5;
    default Path getResourcePath(String resource) {
        URL resourceUrl = getClass().getResource(resource);

        if (resourceUrl == null) {
            // Resource not found
            System.err.println("Resource not found: " + resource);
            return null;
        }
        try {
            Path path = Paths.get(resourceUrl.toURI());
            return path;
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    default int parseRuntimeFromOutput(String output) {
        // Extract the runtime result from JMH's output
        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("ns/op")) { // JMH output format
                String[] parts = line.trim().split("\\s+");
                try {
                    // ns/op value is typically the second-to-last column
                    return Integer.parseInt(parts[parts.length - 2]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1; // Return an error code if parsing fails
    }
    /**
     * Runs a test for C++.
     * @return runtime in seconds.
     */
    default double runCpp(String cppFilePath){
        // Construct the executable path by replacing ".cpp" with "" in the same directory
        String executablePath = cppFilePath.toString().replace(".cpp", ".exe");

        // Check if the executable needs to be compiled
        File cppFile = new File(cppFilePath);
        File executable = new File(executablePath);

        if (!executable.exists() || cppFile.lastModified() > executable.lastModified()) {
            System.out.println("Compiling " + cppFilePath + "...");
            try {
                String[] cmd = {"g++", "-g", cppFilePath, "-o", executablePath};
                Process compileProc = Runtime.getRuntime().exec(cmd);
                int compileExitCode = compileProc.waitFor();
                if (compileExitCode != 0) {
                    System.err.println("Compilation failed with exit code: " + compileExitCode);
                    return -1;
                }
            }
            catch (IOException | InterruptedException  e ) {
                System.err.println("Compilation failed! Messaqe: " + e.getMessage());
                return -1;
            }

        }
        ProcessBuilder runProcessBuilder = new ProcessBuilder(executablePath);
        try {
            Process runProcess = runProcessBuilder.start();
            int exitCode = runProcess.waitFor();
            return (double) exitCode / 1000000000;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Runs a test for Java.
     *
     * @return runtime in seconds.
     */
    default double[] runJava(String givenJavaClassPath, int givenIterations){
        try {

            Options opt = new OptionsBuilder()
                    .include(givenJavaClassPath)
                    .forks(1)
                    .warmupIterations(2)
                    .warmupTime(new TimeValue(100, TimeUnit.MILLISECONDS))
                    .measurementIterations(givenIterations)
                    .measurementTime(new TimeValue(100, TimeUnit.MILLISECONDS))
                    .build();

            Collection<RunResult> runResults = new Runner(opt).run();
            // List to store times in seconds for each test iteration
            List<Double> timesInSeconds = new ArrayList<>();
            // Extract and convert times for each result
            for (RunResult runResult : runResults) {
                BenchmarkResult benchmarkResult = runResult.getAggregatedResult();
                for(IterationResult iterationResult : benchmarkResult.getIterationResults()){
                    timesInSeconds.add(iterationResult.getPrimaryResult().getScore() / 1_000_000.0);
                }
            }

            // Convert List to double[] and return
            return timesInSeconds.stream().mapToDouble(Double::doubleValue).toArray();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runs a test for Pythom.
     * @return runtime in seconds.
     */
    default double runPython(String givenPythonFile){
        // ProcessBuilder to execute the Python script
        ProcessBuilder processBuilder = new ProcessBuilder("python", givenPythonFile);
        try {
            // Start the process
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String output = reader.readLine();
            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Check if the process exited successfully
            if (exitCode == 0 && output != null) {
                // Parse the output as a double and return it
                return Double.parseDouble(output) / 1000000000;
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
