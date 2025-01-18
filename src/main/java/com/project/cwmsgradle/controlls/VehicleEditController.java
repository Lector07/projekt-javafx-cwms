package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.modules.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class VehicleEditController {

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandModelField;

    @FXML
    private TextField vehicleTypeField;

    @FXML
    private TextField productionYearField;

    private Vehicle originalVehicle;
    private VehicleMenageController vehicleMenageController;

    public void setVehicleData(Vehicle vehicle) {
        this.originalVehicle = vehicle;
        registrationNumberField.setText(vehicle.getRegistrationNumber());
        brandModelField.setText(vehicle.getBrandModel());
        vehicleTypeField.setText(vehicle.getVehicleType());
        productionYearField.setText(vehicle.getProductionYear());
    }

    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String updatedRegistrationNumber = registrationNumberField.getText();
        String updatedBrandModel = brandModelField.getText();
        String updatedVehicleType = vehicleTypeField.getText();
        String updatedProductionYear = productionYearField.getText();
        int clientId = originalVehicle.getClientId(); // Retrieve the clientId from the original vehicle
        int vehicleId = originalVehicle.getVehicleId(); // Retrieve the vehicleId from the original vehicle

        Vehicle updatedVehicle = new Vehicle(vehicleId, updatedRegistrationNumber, updatedBrandModel, updatedVehicleType, updatedProductionYear, clientId);
        vehicleMenageController.updateVehicleInList(originalVehicle, updatedVehicle);
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