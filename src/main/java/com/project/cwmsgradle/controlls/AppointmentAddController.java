package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Kontroler odpowiedzialny za dodawanie wizyt.
 */
public class AppointmentAddController {
    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();
    Long currentUserId = AuthenticatedUser.getInstance().getUserId();
    private SessionFactory sessionFactory;

    @FXML
    private TextArea textAreaAppointment;
    @FXML
    private Label selectedVehicleLabel;

    private Vehicle selectedVehicle;

    /**
     * Inicjalizuje kontroler, ustawia fabrykę sesji i wypełnia combobox pojazdami.
     */
    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        currentUserId = AuthenticatedUser.getInstance().getUserId();
        setCurrentUserId(currentUserId);
        System.out.println("Initialized currentUserId: " + currentUserId); // Linia debugowania
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania wizyty.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void AddAppointmentButtonClick(ActionEvent event) {
        String description = textAreaAppointment.getText();
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
                AlertUtils.showErrorAlert("Błąd dodawania wizyty", "Wystąpił błąd podczas dodawania nowej wizyty", e.getMessage());
            }
        } else {
            AlertUtils.showWarningAlert("Błąd dodawania wizyty", "Nie wybrano pojazdu.", "Proszę wybrać pojazd.");
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

    @FXML
    protected void onSelectVehicleButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehicleSelection-view.fxml"));
            Parent root = loader.load();

            VehicleSelectionController vehicleSelectionController = loader.getController();
            vehicleSelectionController.setAppointmentAddController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Wybierz pojazd");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
        selectedVehicleLabel.setText(vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getClient().getName() + " " + vehicle.getClient().getSurname());
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
     * Zwraca użytkownika na podstawie ID.
     * @param userId ID użytkownika
     * @return użytkownik lub null
     */
    public User getUserById(Long userId) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.createQuery("from User where userId = :userId", User.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

            session.getTransaction().commit();
            return user; // Zwraca obiekt User lub null
        }
    }
}