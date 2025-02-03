package com.project.cwmsgradle;

import javafx.application.Application;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Główna klasa aplikacji JavaFX.
 */
public class mainApp extends Application {

    /**
     * Metoda start inicjalizująca główną scenę aplikacji.
     * @param primaryStage główna scena
     * @throws IOException w przypadku błędów wejścia/wyjścia
     */
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

        InputStream iconStream = getClass().getResourceAsStream("/icons/icon.png");
        if (iconStream == null) {
            throw new IOException("Nie znaleziono pliku ikony: /icons/icon.png");
        }
        Image icon = new Image(iconStream);
        primaryStage.getIcons().add(icon);

        Parent root = loader.load();
        primaryStage.setTitle("CWMS-FX");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();
    }

    /**
     * Główna metoda uruchamiająca aplikację.
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        launch(args);
    }
}