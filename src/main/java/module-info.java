module com.project.cwmsgradle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.project.cwmsgradle to javafx.fxml;
    exports com.project.cwmsgradle;
    exports com.project.cwmsgradle.controlls;
    opens com.project.cwmsgradle.controlls to javafx.fxml;
}