package com.project.cwmsgradle.controlls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Kontroler głównej aplikacji, obsługujący logowanie i rejestrację.
 */
public class mainAppController {

    /**
     * Obsługuje kliknięcie przycisku logowania.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginDialog-view.fxml"));
            Parent root = loader.load();

            // Pobierz scenę z obiektu zdarzenia
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Teraz ustaw nowy root dla istniejącej sceny
            stage.getScene().setRoot(root);
            stage.setTitle("Logowanie");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku rejestracji.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RegisterDialog-view.fxml"));
            Parent root = loader.load();

            // Pobierz scenę z obiektu zdarzenia
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Teraz ustaw nowy root dla istniejącej sceny
            stage.getScene().setRoot(root);
            stage.setTitle("Rejestracja");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}