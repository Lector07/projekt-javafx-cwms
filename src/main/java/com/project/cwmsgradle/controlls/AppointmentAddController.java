package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler odpowiedzialny za dodawanie wizyt.
 */
public class AppointmentAddController {
    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();
    Long currentUserId = AuthenticatedUser.getInstance().getUserId();
    private SessionFactory sessionFactory;

    @FXML
    private ComboBox<Vehicle> comboboxAppointmentAdd;

    @FXML
    private TextArea textAreaAppointment;

    private UsersMenageController usersMenageController;

    /**
     * Inicjalizuje kontroler, ustawia fabrykę sesji i wypełnia combobox pojazdami.
     */
    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        populateAppointmentComboBox();
        currentUserId = AuthenticatedUser.getInstance().getUserId();
        setCurrentUserId(currentUserId);
        System.out.println("Initialized currentUserId: " + currentUserId); // Linia debugowania
    }

    /**
     * Wypełnia combobox pojazdami z bazy danych.
     */
    private void populateAppointmentComboBox() {
        sessionFactory = HibernateUtil.getSessionFactory();
        ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Vehicle> query = session.createQuery("from Vehicle v left join fetch v.appointments");
            List<Vehicle> vehicles = query.list();
            vehicleList.addAll(vehicles);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd wczytywania pojazdów", "Nie udało się wczytać listy pojazdów");
            return;
        }

        comboboxAppointmentAdd.setItems(vehicleList);
        comboboxAppointmentAdd.setCellFactory(param -> new ListCell<Vehicle>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    setText(vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getClient().getName() + " " + vehicle.getClient().getSurname());
                }
            }
        });

        comboboxAppointmentAdd.setButtonCell(new ListCell<Vehicle>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    setText(vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getClient().getName() + " " + vehicle.getClient().getSurname());
                }
            }
        });
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania wizyty.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onAddAppointmentButtonClick(ActionEvent event) {
        String description = textAreaAppointment.getText();
        Vehicle selectedVehicle = comboboxAppointmentAdd.getSelectionModel().getSelectedItem();
        String status = "Oczekuje";
        double cost = 0.0;
        LocalDate date = LocalDate.now();
        currentUsername = AuthenticatedUser.getInstance().getUsername();

        if (selectedVehicle != null) {
            try (org.hibernate.Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Appointment appointment = new Appointment();
                appointment.setDescription(description);
                appointment.setVehicle(selectedVehicle);
                appointment.setStatus(status);
                appointment.setCost(cost);
                appointment.setDate(date);
                appointment.setClient(selectedVehicle.getClient());

                User user = getUserById(currentUserId);
                if (user == null) {
                    throw new IllegalArgumentException("Nie znaleziono użytkownika o podanym ID: " + currentUserId);
                }
                appointment.setUser(user);

                session.save(appointment);
                session.getTransaction().commit();

                // Odśwież TableView w AppointmentsMenageController
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AppointmentMenage-view.fxml"));
                Parent root = loader.load();
                AppointmentMenageController controller = loader.getController();
                controller.addAppointmentToList(appointment);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Błąd dodawania wizyty", "Wystąpił błąd podczas dodawania nowej wizyty");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Błąd dodawania wizyty", "Nie wybrano pojazdu.");
        }
        navigateToAppointmentMenage(event);
    }

    /**
     * Obsługuje kliknięcie przycisku powrotu.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        navigateToAppointmentMenage(event);
    }

    /**
     * Nawiguje do widoku zarządzania wizytami.
     * @param event zdarzenie kliknięcia przycisku
     */
    private void navigateToAppointmentMenage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AppointmentMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ustawia nazwę użytkownika.
     * @param currentUsername nazwa użytkownika
     */
    public void setUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    /**
     * Ustawia ID bieżącego użytkownika.
     * @param currentUserId ID bieżącego użytkownika
     */
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    /**
     * Zwraca combobox z pojazdami.
     * @return combobox z pojazdami
     */
    public ComboBox<Vehicle> getComboboxAppointmentAdd() {
        return comboboxAppointmentAdd;
    }

    /**
     * Ustawia combobox z pojazdami.
     * @param comboboxAppointmentAdd combobox z pojazdami
     */
    public void setComboboxAppointmentAdd(ComboBox<Vehicle> comboboxAppointmentAdd) {
        this.comboboxAppointmentAdd = comboboxAppointmentAdd;
    }

    /**
     * Zwraca użytkownika na podstawie ID.
     * @param userId ID użytkownika
     * @return użytkownik lub null
     */
    public User getUserById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.createQuery("from User where userId = :userId", User.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

            session.getTransaction().commit();
            return user; // Zwraca obiekt User lub null
        }
    }

    /**
     * Wyświetla alert.
     * @param alertType typ alertu
     * @param title tytuł alertu
     * @param content treść alertu
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}