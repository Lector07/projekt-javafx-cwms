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
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Kontroler odpowiedzialny za zarządzanie pojazdami.
 */
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
    private int nextVehicleId = 1; // Inicjalizacja licznika ID pojazdów

    private SessionFactory sessionFactory;

    @FXML
    private Label usernameLabelVehicle;

    private String username;

    /**
     * Ustawia nazwę użytkownika i aktualizuje etykietę.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
        usernameLabelVehicle.setText(username);
    }

    /**
     * Inicjalizuje kontroler, ustawia widoczność etykiety nazwy użytkownika
     * i ustawia nazwę użytkownika.
     */
    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory(); // Użycie współdzielonej instancji
        usernameLabelVehicle.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());

        // Ustawienie kolumn tabeli
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        clientIdColumn.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            if (vehicle != null && vehicle.getClients() != null) {
                String name = vehicle.getClients().getName();
                String surname = vehicle.getClients().getSurname();  // Zakładając, że 'surname' istnieje w 'Client'
                return new SimpleObjectProperty<>(name + " " + surname); // Konkatenacja imienia i nazwiska
            }
            return new SimpleObjectProperty<>(""); // Zwraca pusty string, jeśli brak danych klienta
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

        // Załadowanie danych i powiązanie ich z tabelą
        loadVehicleData();
        vehicleTableView.setItems(vehicleData);
    }

    /**
     * Generuje unikalne ID pojazdu.
     * @return unikalne ID pojazdu
     */
    public int generateVehicleId() {
        return nextVehicleId++;
    }

    public void setMenuViewController(MenuViewController menuViewController) {

    }

    /**
     * Obsługuje kliknięcie przycisku powrotu, ładuje widok menu i ustawia
     * aktualne informacje o użytkowniku.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            // Pobiera kontroler i ustawia aktualne informacje o użytkowniku
            MenuViewController menuController = loader.getController();
            menuController.setUserRole(currentUserRole);
            menuController.setUsername(currentUsername);

            currentUsername = AuthenticatedUser.getInstance().getUsername();
            currentUserRole = AuthenticatedUser.getInstance().getRole();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania pojazdu, ładuje widok dodawania pojazdu.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onAddVehicleButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesAdd-view.fxml"));
            Parent root = loader.load();

            VehicleAddController addController = loader.getController();
            addController.setVehicleMenageController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku edycji pojazdu, ładuje widok edycji pojazdu.
     * @param event zdarzenie kliknięcia przycisku
     */
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
                primaryStage.setTitle("CWMS-FX");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obsługuje kliknięcie przycisku usuwania pojazdu, usuwa wybrany pojazd z bazy danych.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onDeleteVehicleButtonClick(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            Optional<ButtonType> result = AlertUtils.showDeleteConfirmationAlert("pojazd o ID: " + selectedVehicle.getVehicleId());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    deleteVehicleFromDatabase(selectedVehicle);
                    vehicleData.remove(selectedVehicle);
                    refreshTableView();
                } catch (ConstraintViolationException e) {
                    AlertUtils.showErrorAlert("Błąd", "Nie można usunąć pojazdu", "Pojazd jest w naprawie i nie można go usunąć.");
                }
            }
        }
    }

    /**
     * Dodaje pojazd do listy i zapisuje go w bazie danych.
     * @param vehicle pojazd do dodania
     */
    public void addVehicleToList(Vehicle vehicle) {
        if (validateVehicleData(vehicle)) {
            vehicleData.add(vehicle);
            saveVehicleToDatabase(vehicle);
            refreshTableView();
        }
    }

    /**
     * Aktualizuje pojazd na liście i w bazie danych.
     * @param originalVehicle oryginalny pojazd
     * @param updatedVehicle zaktualizowany pojazd
     */
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

    /**
     * Waliduje dane pojazdu.
     * @param vehicle pojazd do walidacji
     * @return true, jeśli dane są poprawne, w przeciwnym razie false
     */
    private boolean validateVehicleData(Vehicle vehicle) {
        if (!isValidBrand(vehicle.getBrand())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy format", "Wpisana wartość musi być z dużej litery i nie może zawierać cyfr.");
            return false;
        }
        if (!isValidModel(vehicle.getModel())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy format", "Wpisana wartość musi być z dużej litery.");
            return false;
        }
        if (!isValidProductionYear(vehicle.getProductionYear())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy rok produkcji", "Rok produkcji musi być liczbą od 1900 roku.");
            return false;
        }
        return true;
    }

    /**
     * Sprawdza, czy marka pojazdu jest poprawna.
     * @param brand marka pojazdu
     * @return true, jeśli marka jest poprawna, w przeciwnym razie false
     */
    private boolean isValidBrand(String brand) {
        return brand != null && brand.matches("[A-Z][a-zA-Z]*");
    }

    /**
     * Sprawdza, czy model pojazdu jest poprawny.
     * @param model model pojazdu
     * @return true, jeśli model jest poprawny, w przeciwnym razie false
     */
    private boolean isValidModel(String model) {
        return model != null && model.matches("[A-Za-z0-9]*");
    }

    /**
     * Sprawdza, czy rok produkcji pojazdu jest poprawny.
     * @param year rok produkcji pojazdu
     * @return true, jeśli rok produkcji jest poprawny, w przeciwnym razie false
     */
    private boolean isValidProductionYear(int year) {
        return year >= 1900;
    }

    /**
     * Zapisuje pojazd w bazie danych.
     * @param vehicle pojazd do zapisania
     */
    private void saveVehicleToDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(vehicle);
            session.getTransaction().commit();
        }
    }

    /**
     * Aktualizuje pojazd w bazie danych.
     * @param vehicle pojazd do aktualizacji
     */
    private void updateVehicleInDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(vehicle);
            session.getTransaction().commit();
        }
    }

    /**
     * Usuwa pojazd z bazy danych.
     * @param vehicle pojazd do usunięcia
     */
    private void deleteVehicleFromDatabase(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(vehicle);
            session.getTransaction().commit();
        }
    }

    /**
     * Ładuje dane pojazdów z bazy danych.
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
     * Odświeża widok tabeli pojazdów.
     */
    private void refreshTableView() {
        vehicleTableView.setItems(null);
        vehicleTableView.setItems(vehicleData);
    }
}