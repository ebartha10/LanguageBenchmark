module com.example.fxproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jmh.core;

    opens com.example.fxproject to javafx.fxml;
    exports com.example.fxproject;
    exports com.example.fxproject.javaTests.jmh_generated;
}