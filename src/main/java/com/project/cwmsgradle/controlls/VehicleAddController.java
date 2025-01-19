package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class VehicleAddController {

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField productionYearField;

    private VehicleMenageController vehicleMenageController;
    private int clientId;

    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String registrationNumber = registrationNumberField.getText();
        String brand = brandField.getText();
        String model = modelField.getText();
        int productionYear = Integer.parseInt(productionYearField.getText()); // Convert to int

        int vehicleId = vehicleMenageController.generateVehicleId(); // Generate vehicle ID
        Vehicle newVehicle = new Vehicle(vehicleId, registrationNumber, brand, model, productionYear, clientId);
        vehicleMenageController.addVehicleToList(newVehicle);
        navigateToVehicleMenage(event);
    }

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    private void navigateToVehicleMenage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Zarządzanie pojazdami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}