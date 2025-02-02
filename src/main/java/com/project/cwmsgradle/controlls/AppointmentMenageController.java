package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class AppointmentMenageController {
    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();

    Long currentUserId = AuthenticatedUser.getInstance().getUserId();

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment,Long> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment,String> descriptionColumnAppointment;

    @FXML
    private TableColumn<Appointment, LocalDate> dateColumnAppointment;

    @FXML
    private TableColumn<Appointment, Double> costColumnAppointment;

    @FXML
    private TableColumn<Appointment, String> statusColumnAppointment;

    @FXML
    private TableColumn<Appointment, String> clientColumnAppointment;

    @FXML
    private TableColumn<Appointment, String> vehicleColumnAppointment;

    @FXML
    private TableColumn<Appointment, String> mechanicColumnAppointment;

    private ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();

    public SessionFactory sessionFactory;



    /**
     * Ustawia kontroler widoku menu.
     * @param menuViewController kontroler widoku menu
     */
    public void setMenuViewController(MenuViewController menuViewController) {
    }

    @FXML
    private Label usernameLabelAppointments;

    private String username;

    /**
     * Ustawia nazwę użytkownika i aktualizuje etykietę.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
        usernameLabelAppointments.setText(username);
    }
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }


    /**
     * Inicjalizuje kontroler, ustawia widoczność etykiety nazwy użytkownika
     * i ustawia nazwę użytkownika.
     */
    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        usernameLabelAppointments.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());
        setCurrentUserId(AuthenticatedUser.getInstance().getUserId());

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        descriptionColumnAppointment.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumnAppointment.setCellValueFactory(new PropertyValueFactory<>("date"));
        costColumnAppointment.setCellValueFactory(new PropertyValueFactory<>("cost"));
        statusColumnAppointment.setCellValueFactory(new PropertyValueFactory<>("status"));
        clientColumnAppointment.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            if (client != null) {
                return new SimpleStringProperty(client.getName() + " " + client.getSurname());
            } else {
                return new SimpleStringProperty("");
            }
        });
        vehicleColumnAppointment.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue().getVehicle();
            if (vehicle != null) {
                return new SimpleStringProperty(vehicle.getBrand() + " " + vehicle.getModel());
            } else {
                return new SimpleStringProperty("");
            }
        });
        mechanicColumnAppointment.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            if (user != null) {
                return new SimpleStringProperty(user.getUsername());
            } else {
                return new SimpleStringProperty("");
            }
        });

        loadAppointmentsData();
        appointmentsTableView.setItems(appointmentData);
    }

    @FXML
    protected void onAddAppointmentButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AppointmentAdd-view.fxml"));
            Parent root = loader.load();

            // Pobiera kontroler i ustawia aktualne informacje o użytkowniku
            AppointmentAddController appointmentAddController = loader.getController();
            appointmentAddController.setUsername(currentUsername);


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onEditAppointmentButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AppointmentEdit-view.fxml"));
            Parent root = loader.load();

            // Pobiera kontroler i ustawia aktualne informacje o użytkowniku
            AppointmentEditController appointmentEditController = loader.getController();
            appointmentEditController.setUsername(currentUsername);

            Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
            appointmentEditController.setAppointment(selectedAppointment);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void loadAppointmentsData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Appointment> appointment = session.createQuery("SELECT a FROM Appointment a JOIN FETCH a.vehicle JOIN FETCH a.client JOIN FETCH a.user", Appointment.class).getResultList();
            appointmentData.addAll(appointment);
            session.getTransaction().commit();
        }
    }

    public void addAppointmentToList(Appointment appointment) {
        appointmentData.add(appointment);
        saveAppointmentToDatabase(appointment);
        refreshTableView();
    }

    private void saveAppointmentToDatabase(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(appointment);
            session.getTransaction().commit();
        }
    }

    private void refreshTableView() {
        appointmentsTableView.setItems(null);
        appointmentsTableView.setItems(appointmentData);
    }
}