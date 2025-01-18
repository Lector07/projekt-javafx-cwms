package com.project.cwmsgradle.controlls;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class MenuViewController {

    @FXML
    private Button exitButton;

    @FXML
    private Button manageClientsButton;

    @FXML
    private Button manageRepairsButton;

    @FXML
    public void onManageClientsButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientMenage-view.fxml"));
            Parent root = loader.load();
            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Zarządzanie klientami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onManageRepairsButtonClick() {
        // Handle manage repairs button click
    }

    @FXML
    private Button manageVehiclesButton;

    @FXML
    protected void onManageVehiclesButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();
            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Zarządzanie pojazdami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onExitButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainApp-view.fxml")); // Użyj właściwej ścieżki do swojej strony głównej
            Parent root = loader.load();

            // Pobierz scenę z obiektu zdarzenia
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Ustawiamy nową scenę dla istniejącego okna
            stage.getScene().setRoot(root);
            stage.setTitle("Strona Główna");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}