package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class VehicleAddController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField productionYearField;

    private VehicleMenageController vehicleMenageController;
    private Client client; // Change this line

    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    public void setClient(Client client) { // Change this method
        this.client = client;
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String registrationNumber = registrationNumberField.getText();
        String brand = brandField.getText();
        String model = modelField.getText();
        int productionYear = Integer.parseInt(productionYearField.getText());

        // Retrieve the current user
        currentUsername = AuthenticatedUser.getInstance().getUsername();
        currentUserRole = AuthenticatedUser.getInstance().getRole();

        // Tworzymy nowy obiekt Vehicle, bez id.
        Vehicle newVehicle = new Vehicle(registrationNumber, brand, model, productionYear, client);
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