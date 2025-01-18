package com.project.cwmsgradle;

import javafx.application.Application;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class mainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        System.out.println("JavaFX Version: " + System.getProperty("javafx.runtime.version"));
        System.out.println("Java Version: " + System.getProperty("java.version"));

        try {
            Class.forName("javax.xml.bind.JAXBException");
            System.out.println("JAXB is available on the classpath.");
        } catch (ClassNotFoundException e) {
            System.err.println("JAXB is NOT available on the classpath: " + e.getMessage());
        }


        URL fxmlUrl = mainApp.class.getResource("/views/MainApp-view.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Nie znaleziono pliku FXML: /views/MainApp-view.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);


        Parent root = loader.load();
        primaryStage.setTitle("CWMS-FX");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        launch(args);
    }
}