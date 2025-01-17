package com.project.cwmsgradle.controlls;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class VehiclesEditController {

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandModelField;

    @FXML
    private TextField vehicleTypeField;

    @FXML
    private TextField productionYearField;

    public void setVehicleData(String vehicleData) {
        // Parse and set the vehicle data to the text fields
        // Assuming vehicleData is a comma-separated string
        String[] data = vehicleData.split(",");
        registrationNumberField.setText(data[0]);
        brandModelField.setText(data[1]);
        vehicleTypeField.setText(data[2]);
        productionYearField.setText(data[3]);
    }
}