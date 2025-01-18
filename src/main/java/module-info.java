module com.project.cwmsgradle {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires jakarta.persistence;

    opens com.project.cwmsgradle.entity to javafx.base;
    opens com.project.cwmsgradle.controlls to javafx.fxml;

    exports com.project.cwmsgradle;
    exports com.project.cwmsgradle.entity;
    exports com.project.cwmsgradle.controlls;
    exports com.project.cwmsgradle.utils;
}