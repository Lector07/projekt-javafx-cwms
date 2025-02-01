package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class VehicleMenageController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();

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
    private TableColumn<Vehicle, String> clientIdColumn;

    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();
    private int nextVehicleId = 1; // Initialize vehicle ID counter

    private SessionFactory sessionFactory;

    @FXML
    private Label usernameLabelVehicle;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        usernameLabelVehicle.setText(username);
    }

    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory(); // Use shared instance
        usernameLabelVehicle.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());

        // Set up table columns
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        clientIdColumn.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            if (vehicle != null && vehicle.getClients() != null) {
                String name = vehicle.getClients().getName();
                String surname = vehicle.getClients().getSurname();  // Assuming 'surname' exists in 'Client'
                return new SimpleObjectProperty<>(name + " " + surname); // Concatenate name and surname
            }
            return new SimpleObjectProperty<>(""); // Return empty string if no client data
        });

        registrationNumberColumn.setCellFactory(column -> new TableCell<Vehicle, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.toUpperCase());
                }
            }
        });

        // Load data and bind it to the table
        loadVehicleData();
        vehicleTableView.setItems(vehicleData);
    }

    public int generateVehicleId() {
        return nextVehicleId++;
    }

    public void setMenuViewController(MenuViewController menuViewController) {

    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set the current user information
            MenuViewController menuController = loader.getController();
            menuController.setUserRole(currentUserRole);
            menuController.setUsername(currentUsername);

            currentUsername = AuthenticatedUser.getInstance().getUsername();
            currentUserRole = AuthenticatedUser.getInstance().getRole();

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
            Optional<ButtonType> result = AlertUtils.showDeleteConfirmationAlert("pojazd o ID: " + selectedVehicle.getVehicleId());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                vehicleData.remove(selectedVehicle);
                deleteVehicleFromDatabase(selectedVehicle);
                refreshTableView();
            }
        }
    }

    public void addVehicleToList(Vehicle vehicle) {
        if (validateVehicleData(vehicle)) {
            vehicleData.add(vehicle);
            saveVehicleToDatabase(vehicle);
            refreshTableView();
        }
    }

    public void updateVehicleInList(Vehicle originalVehicle, Vehicle updatedVehicle) {
        if (validateVehicleData(updatedVehicle)) {
            int index = vehicleData.indexOf(originalVehicle);
            if (index != -1) {
                vehicleData.set(index, updatedVehicle);
                updateVehicleInDatabase(updatedVehicle);
                refreshTableView();
            }
        }
    }

    private boolean validateVehicleData(Vehicle vehicle) {
        if (!isValidBrand(vehicle.getBrand())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy format", "Wpisana wartość musi być z dużej litery i nie może zawierać cyfr.");
            return false;
        }
        if (!isValidModel(vehicle.getModel())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy format", "Wpisana wartość musi być z dużej litery i nie może zawierać cyfr.");
            return false;
        }
        if (!isValidProductionYear(vehicle.getProductionYear())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy rok produkcji", "Rok produkcji musi być liczbą od 1900 roku.");
            return false;
        }
        return true;
    }


    private boolean isValidBrand(String brand) {
        return brand != null && brand.matches("[A-Z][a-zA-Z]*");
    }

    private boolean isValidModel(String model) {
        return model != null && model.matches("[A-Z][a-zA-Z]*");
    }

    private boolean isValidProductionYear(int year) {
        return year >= 1900;
    }

    private void saveVehicleToDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(vehicle);
            session.getTransaction().commit();
        }
    }

    private void updateVehicleInDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(vehicle);
            session.getTransaction().commit();
        }
    }

    private void deleteVehicleFromDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(vehicle);
            session.getTransaction().commit();
        }
    }

    private void loadVehicleData() {
        try (Session session = sessionFactory.openSession()) {
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