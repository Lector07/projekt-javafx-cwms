package com.project.cwmsgradle.controlls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class VehicleAddController {

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandModelField;

    @FXML
    private TextField vehicleTypeField;

    @FXML
    private TextField productionYearField;

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        // Save vehicle data logic here
        System.out.println("Registration number: " + registrationNumberField.getText());
        System.out.println("Brand model: " + brandModelField.getText());
        System.out.println("Vehicle type: " + vehicleTypeField.getText());
        System.out.println("Production year: " + productionYearField.getText());
        // Close the form
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        // Close the form without saving
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();

            // Pobierz scenę z obiektu zdarzenia
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Ustawiamy nową scenę dla istniejącego okna
            stage.getScene().setRoot(root);
            stage.setTitle("Zarządzanie pojazdami");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}