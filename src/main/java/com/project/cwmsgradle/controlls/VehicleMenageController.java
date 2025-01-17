package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.modules.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleMenageController {

    @FXML
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableColumn<Vehicle, String> registrationNumberColumn;

    @FXML
    private TableColumn<Vehicle, String> brandModelColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> productionYearColumn;

    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        brandModelColumn.setCellValueFactory(new PropertyValueFactory<>("brandModel"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));

        loadVehicleData();
        vehicleTableView.setItems(vehicleData);
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();
            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddVehicleButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesAdd-view.fxml"));
            Parent root = loader.load();

            VehicleAddController addController = loader.getController();
            addController.setVehicleMenageController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Dodaj pojazd");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onEditVehicleButtonClick(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesEdit-view.fxml"));
                Parent root = loader.load();

                VehicleEditController controller = loader.getController();
                controller.setVehicleData(selectedVehicle);
                controller.setVehicleMenageController(this);

                Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                primaryStage.getScene().setRoot(root);
                primaryStage.setTitle("Edytuj pojazd");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onDeleteVehicleButtonClick(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleData.remove(selectedVehicle);
            saveVehicleData();
        }
    }

    public void addVehicleToList(String vehicleDataString) {
        String[] data = vehicleDataString.split(",");
        if (data.length == 4) {
            Vehicle vehicle = new Vehicle(data[0], data[1], data[2], data[3]);
            vehicleData.add(vehicle);
            saveVehicleData();
        }
    }

    public void updateVehicleInList(Vehicle originalVehicle, Vehicle updatedVehicle) {
        int index = vehicleData.indexOf(originalVehicle);
        if (index != -1) {
            vehicleData.set(index, updatedVehicle);
            saveVehicleData();
        }
    }

    private void saveVehicleData() {
        try {
            List<String> vehicleDataStrings = vehicleData.stream()
                    .map(vehicle -> String.join(",", vehicle.getRegistrationNumber(), vehicle.getBrandModel(), vehicle.getVehicleType(), vehicle.getProductionYear()))
                    .collect(Collectors.toList());
            Files.write(Paths.get("vehicleData.txt"), vehicleDataStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadVehicleData() {
        try {
            List<String> vehicleDataStrings = Files.readAllLines(Paths.get("vehicleData.txt"));
            for (String vehicleDataString : vehicleDataStrings) {
                String[] data = vehicleDataString.split(",");
                if (data.length == 4) {
                    Vehicle vehicle = new Vehicle(data[0], data[1], data[2], data[3]);
                    vehicleData.add(vehicle);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}