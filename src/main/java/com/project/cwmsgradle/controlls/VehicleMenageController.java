package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
import java.util.List;
import java.util.Optional;

public class VehicleMenageController {

    @FXML
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, String> registrationNumberColumn;

    @FXML
    private TableColumn<Vehicle, String> brandColumn;

    @FXML
    private TableColumn<Vehicle, String> modelColumn;

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
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        loadVehicleData();
        vehicleTableView.setItems(vehicleData);
    }

    public int generateVehicleId() {
        return nextVehicleId++;
    }

    private MenuViewController menuViewController;

    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        if (menuViewController == null) {
            // Handle the case where menuViewController is not set
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Menu View Controller not set");
            alert.setContentText("Please set the Menu View Controller before going back.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set the current user information
            MenuViewController menuController = loader.getController();
            menuController.setUserRole(menuViewController.getUserRole());
            menuController.setUsername(menuViewController.getUsername());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Menu");
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
                deleteVehicleFromDatabase(selectedVehicle);
                refreshTableView();
            }
        }
    }

    public void addVehicleToList(Vehicle vehicle) {
        vehicleData.add(vehicle);
        saveVehicleToDatabase(vehicle);
        refreshTableView();
    }

    public void updateVehicleInList(Vehicle originalVehicle, Vehicle updatedVehicle) {
        int index = vehicleData.indexOf(originalVehicle);
        if (index != -1) {
            vehicleData.set(index, updatedVehicle);
            updateVehicleInDatabase(updatedVehicle);
            refreshTableView();
        }
    }

    private void saveVehicleToDatabase(Vehicle vehicle) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Vehicle.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(vehicle);
            session.getTransaction().commit();
        }
    }

    private void updateVehicleInDatabase(Vehicle vehicle) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Vehicle.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(vehicle);
            session.getTransaction().commit();
        }
    }

    private void deleteVehicleFromDatabase(Vehicle vehicle) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Vehicle.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.delete(vehicle);
            session.getTransaction().commit();
        }
    }

    private void loadVehicleData() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Vehicle.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Vehicle> vehicles = session.createQuery("from Vehicle", Vehicle.class).getResultList();
            vehicleData.addAll(vehicles);
            session.getTransaction().commit();
        }
    }

    private void refreshTableView() {
        vehicleTableView.setItems(null);
        vehicleTableView.setItems(vehicleData);
    }
}