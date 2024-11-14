package com.example.fxproject;

import com.example.fxproject.backend.Test;
import com.example.fxproject.misc.TestResult;
import com.example.fxproject.ui.TestTask;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.time.LocalTime;

public class MainController {

    @FXML
    private Label labelTestTitle;
    @FXML private Label labelTestDesc;
    @FXML
    private ProgressBar progressBarCpp;
    @FXML
    private ProgressBar progressBarJava;
    @FXML
    private ProgressBar progressBarPython;
    @FXML private Label statusCpp;
    @FXML private Label statusJava;
    @FXML private Label statusPython;
    @FXML private Button buttonMemoryAlloc;
    @FXML private Button buttonStaticMemory;
    @FXML private Button buttonDynamicMemory;
    @FXML private Button buttonCreateThread;
    @FXML private Button buttonContextSwitch;
    @FXML private Button buttonThreadMigration;
    @FXML private Button buttonStartTest;
    @FXML private Label labelStartCpp;
    @FXML private Label labelStopCpp;
    @FXML private Label labelStartJava;
    @FXML private Label labelStopJava;
    @FXML private Label labelStartPython;
    @FXML private Label labelStopPython;
    @FXML private LineChart graph;
    private int testType = 0;


    private void resetProgressBar(){
        progressBarCpp.setProgress(0.0);
        progressBarJava.setProgress(0.0);
        progressBarPython.setProgress(0.0);
        statusCpp.setText("ASTEAPTA");
        statusJava.setText("ASTEAPTA");
        statusPython.setText("ASTEAPTA");
        labelStartCpp.setText("");
        labelStopCpp.setText("");
        labelStartJava.setText("");
        labelStopJava.setText("");
        labelStartPython.setText("");
        labelStopPython.setText("");
    }
    @FXML protected void onButtonMemoryAllocClick(){
        labelTestTitle.setText("Alocare Memorie");
        labelTestDesc.setText("Testele vor verifica alocarea de memorie.");
        testType = Test.TEST_MEMORY_ALLOC;
        resetProgressBar();
        graph.getData().clear();

    }
    @FXML protected void onbuttonStaticMemoryClick(){
        labelTestTitle.setText("Memorie Statica");
        labelTestDesc.setText("Testele vor verifica timpul de acces a memoriei statice.");
        testType = Test.TEST_STATIC_MEMORY;
        resetProgressBar();
        graph.getData().clear();

    }
    @FXML protected void onbuttonDynamicMemoryClick(){
        labelTestTitle.setText("Memorie Dinamica");
        labelTestDesc.setText("Testele vor verifica timpul de acces a memoriei dinamice.");
        testType = Test.TEST_DYNAMIC_MEMORY;
        resetProgressBar();
        graph.getData().clear();

    }
    @FXML protected void onbuttonCreateThreadClick(){
        labelTestTitle.setText("Creare Thread");
        labelTestDesc.setText("Testele vor verifica crearea de fire de executie.");
        testType = Test.TEST_THREAD_CREATION;
        resetProgressBar();
        graph.getData().clear();

    }
    @FXML protected void onbuttonContextSwitchClick(){
        labelTestTitle.setText("Context Switch");
        labelTestDesc.setText("Testele vor verifica timpul unui context switch.");
        testType = Test.TEST_THREAD_CONTEST_SWITCH;
        resetProgressBar();
        graph.getData().clear();

    }
    @FXML protected void onbuttonThreadMigrationClick(){
        labelTestTitle.setText("Thread Migration");
        labelTestDesc.setText("Testele vor verifica timpul unui thread migration.");
        resetProgressBar();
        graph.getData().clear();

    }
    private void runCppTest(){
        TestTask testTask = new TestTask( 10, testType, 0);
        statusCpp.setText("RULEAZA");
        testTask.messageProperty().addListener((observableValue, s, t1) -> labelStartCpp.setText(t1));
        testTask.progressProperty().addListener(((observableValue, number, t1) -> progressBarCpp.setProgress((double)t1)));
        // Handle the result when the task completes successfully
        testTask.setOnSucceeded(event -> {
            TestResult testResult = testTask.getValue();

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("C++");

            insertDataToGraph(testResult, series);
            labelStopCpp.setText(LocalTime.now().toString());
            statusCpp.setText("FINALIZAT");
            runJavaTest();
        });

        Thread th = new Thread(testTask);
        th.setDaemon(true);
        th.start();
    }
    private void runPythonTest(){
        TestTask testTaskPython = new TestTask( 10, testType, 1);

        testTaskPython.messageProperty().addListener((observableValue, s, t1) -> labelStartPython.setText(t1));
        testTaskPython.progressProperty().addListener(((observableValue, number, t1) -> progressBarPython.setProgress((double)t1)));
        statusPython.setText("RULEAZA");
        testTaskPython.setOnSucceeded(event -> {
            TestResult testResult = testTaskPython.getValue();

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Python");

            insertDataToGraph(testResult, series);
            labelStopPython.setText(LocalTime.now().toString());
            progressBarJava.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progressBarPython.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progressBarCpp.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            statusPython.setText("FINALIZAT");
        });

        Thread th = new Thread(testTaskPython);
        th.setDaemon(true);
        th.start();
    }
    private void runJavaTest(){
        TestTask testTask = new TestTask( 10, testType, 2);

        testTask.messageProperty().addListener((observableValue, s, t1) -> labelStartJava.setText(t1));
        progressBarJava.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        statusJava.setText("RULEAZA");

        testTask.setOnSucceeded(event -> {
            TestResult testResult = testTask.getValue();

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Java");

            insertDataToGraph(testResult, series);
            labelStopJava.setText(LocalTime.now().toString());
            statusJava.setText("FINALIZAT");
            progressBarJava.setProgress(100.0);
            runPythonTest();
        });

        Thread th = new Thread(testTask);
        th.setDaemon(true);
        th.start();
    }

    private void insertDataToGraph(TestResult testResult, XYChart.Series<Number, Number> series) {
        double[] testResults = testResult.getTestResults();
        for (int i = 0; i < testResults.length; i++) {
            series.getData().add(new XYChart.Data<>(i + 1, testResults[i]));
        }
        graph.getData().add(series);
    }

    @FXML protected void onStartButtonClicked() {
        graph.getData().clear();
        runCppTest();
    }

}