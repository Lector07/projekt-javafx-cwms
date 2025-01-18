package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleMenageController {

    @FXML
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, String> registrationNumberColumn;

    @FXML
    private TableColumn<Vehicle, String> brandModelColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> productionYearColumn;

    @FXML
    private TableColumn<Vehicle, Integer> clientIdColumn;

    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();
    private int nextVehicleId = 1; // Initialize vehicle ID counter

    @FXML
    protected void initialize() {
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        brandModelColumn.setCellValueFactory(new PropertyValueFactory<>("brandModel"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        loadVehicleData();
        vehicleTableView.setItems(vehicleData);
    }

    public int generateVehicleId() {
        return nextVehicleId++;
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
            addController.setClientId(getSelectedClientId()); // Pass the selected client ID

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Dodaj pojazd");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSelectedClientId() {
        // Implement logic to get the selected client ID
        return 1; // Placeholder value
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText("Czy na pewno chcesz usunąć ten pojazd?");
            alert.setContentText("ID Pojazdu: " + selectedVehicle.getVehicleId() + "\nID Klienta: " + selectedVehicle.getClientId());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                vehicleData.remove(selectedVehicle);
                saveVehicleData();
                refreshTableView();
            }
        }
    }

    public void addVehicleToList(Vehicle vehicle) {
        vehicleData.add(vehicle);
        saveVehicleData();
        refreshTableView();
    }

    public void updateVehicleInList(Vehicle originalVehicle, Vehicle updatedVehicle) {
        int index = vehicleData.indexOf(originalVehicle);
        if (index != -1) {
            vehicleData.set(index, updatedVehicle);
            saveVehicleData();
            refreshTableView();
        }
    }

    private void saveVehicleData() {
        try {
            List<String> vehicleDataStrings = vehicleData.stream()
                    .map(vehicle -> String.join(",", String.valueOf(vehicle.getVehicleId()), vehicle.getRegistrationNumber(), vehicle.getBrandModel(), vehicle.getVehicleType(), vehicle.getProductionYear(), String.valueOf(vehicle.getClientId())))
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
                if (data.length == 6) {
                    int vehicleId = Integer.parseInt(data[0]);
                    int clientId = Integer.parseInt(data[5]);
                    Vehicle vehicle = new Vehicle(vehicleId, data[1], data[2], data[3], data[4], clientId);
                    vehicleData.add(vehicle);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTableView() {
        vehicleTableView.setItems(null);
        vehicleTableView.setItems(vehicleData);
    }
}