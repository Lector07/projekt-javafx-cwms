package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.Vehicle;
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
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField productionYearField;

    private Vehicle originalVehicle;
    private VehicleMenageController vehicleMenageController;

    public void setVehicleData(Vehicle vehicle) {
        this.originalVehicle = vehicle;
        registrationNumberField.setText(vehicle.getRegistrationNumber());
        brandField.setText(vehicle.getBrand());
        modelField.setText(vehicle.getModel());
        productionYearField.setText(String.valueOf(vehicle.getProductionYear()));
    }

    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String updatedRegistrationNumber = registrationNumberField.getText();
        String updatedBrand = brandField.getText();
        String updatedModel = modelField.getText();
        int updatedProductionYear = Integer.parseInt(productionYearField.getText());
        Client client = originalVehicle.getClients(); // Retrieve the Client object using getClients
        int vehicleId = originalVehicle.getVehicleId(); // Retrieve the vehicleId from the original vehicle

        // Ensure the client is not null
        if (client == null) {
            // Handle the case where the client is null, e.g., show an error message
            System.out.println("Client cannot be null");
            return;
        }

        Vehicle updatedVehicle = new Vehicle(updatedRegistrationNumber, updatedBrand, updatedModel, updatedProductionYear, client);
        updatedVehicle.setVehicleId(vehicleId); // Set the ID

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