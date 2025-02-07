package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Kontroler odpowiedzialny za wybór pojazdu.
 */
public class VehicleSelectionController {
    private SessionFactory sessionFactory;
    private AppointmentAddController appointmentAddController;

    @FXML
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, String> brandColumn;

    @FXML
    private TableColumn<Vehicle, String> modelColumn;

    @FXML
    private TableColumn<Vehicle, String> clientColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();
    private FilteredList<Vehicle> filteredData;

    /**
     * Inicjalizuje kontroler, ustawia fabrykę sesji i wypełnia tabelę pojazdami.
     */
    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        clientColumn.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            if (vehicle != null && vehicle.getClient() != null) {
                return new SimpleStringProperty(vehicle.getClient().getName() + " " + vehicle.getClient().getSurname());
            }
            return new SimpleStringProperty("");
        });

        loadVehicleData();
        filteredData = new FilteredList<>(vehicleData, p -> true);
        vehicleTableView.setItems(filteredData);

        vehicleIdColumn.setSortType(TableColumn.SortType.DESCENDING);
        vehicleTableView.getSortOrder().add(vehicleIdColumn);
    }

    /**
     * Laduje dane pojazdów z bazy danych.
     */
    private void loadVehicleData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Vehicle> vehicles = session.createQuery("from Vehicle", Vehicle.class).getResultList();
            vehicleData.addAll(vehicles);
            session.getTransaction().commit();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku wyboru pojazdu.
     */
    @FXML
    protected void onSelectVehicleButtonClick() {
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            appointmentAddController.setSelectedVehicle(selectedVehicle);
            Stage stage = (Stage) vehicleTableView.getScene().getWindow();
            stage.close();
        } else {
            AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano pojazdu", "Proszę wybrać pojazd.");
        }
    }

    /**
     * Obsługuje wpisywanie tekstu w polu wyszukiwania.
     */
    @FXML
    protected void onSearchKeyReleased() {
        String filter = searchField.getText();
        if (filter == null || filter.isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            filteredData.setPredicate(vehicle -> vehicle.getBrand().toLowerCase().contains(filter.toLowerCase()) ||
                    vehicle.getModel().toLowerCase().contains(filter.toLowerCase()) ||
                    (vehicle.getClient() != null && (vehicle.getClient().getName().toLowerCase().contains(filter.toLowerCase()) ||
                            vehicle.getClient().getSurname().toLowerCase().contains(filter.toLowerCase()))));
        }
    }

    public void setAppointmentAddController(AppointmentAddController appointmentAddController) {
        this.appointmentAddController = appointmentAddController;
    }
}