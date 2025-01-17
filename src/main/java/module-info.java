module com.project.cwmsgradle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.project.cwmsgradle.modules to javafx.base;
    opens com.project.cwmsgradle.controlls to javafx.fxml;

    exports com.project.cwmsgradle;
    exports com.project.cwmsgradle.modules;
    exports com.project.cwmsgradle.controlls;
}