package com.project.cwmsgradle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainApp-view.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CWMS-FX");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}