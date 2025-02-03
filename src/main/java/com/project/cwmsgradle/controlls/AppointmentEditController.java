package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Kontroler odpowiedzialny za edycję wizyt.
 */
public class AppointmentEditController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();

    @FXML
    private ComboBox<String> comboboxAppointmentEdit;

    @FXML
    private TextArea textAreaAppointment;

    private Appointment appointment;

    /**
     * Inicjalizuje kontroler, wypełnia combobox statusami wizyt.
     */
    @FXML
    public void initialize() {
        comboboxAppointmentEdit.getItems().addAll(
                new String("W naprawie".getBytes(), StandardCharsets.UTF_8),
                new String("Oczekuje na części".getBytes(), StandardCharsets.UTF_8),
                new String("Naprawiony".getBytes(), StandardCharsets.UTF_8)
        );
    }

    /**
     * Ustawia wizytę do edycji.
     * @param appointment wizyta do edycji
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        comboboxAppointmentEdit.setValue(appointment.getStatus());
        textAreaAppointment.setText(appointment.getDescription());
    }

    /**
     * Obsługuje kliknięcie przycisku edycji wizyty.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onEditButtonClick(ActionEvent event) {
        String updatedDescription = textAreaAppointment.getText();
        String updatedStatus = comboboxAppointmentEdit.getValue();

        appointment.setDescription(updatedDescription);
        appointment.setStatus(updatedStatus);

        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Potwierdzenie edycji", "Czy na pewno chcesz edytować?", "Tej operacji nie można cofnąć.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.update(appointment);
                transaction.commit();
            }
        }
    }

    /**
     * Obsługuje kliknięcie przycisku powrotu.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    public void onGoBackButtonClick(ActionEvent event) {
        navigateToClientMenage(event);
    }

    /**
     * Nawiguje do widoku zarządzania klientami.
     * @param event zdarzenie kliknięcia przycisku
     */
    private void navigateToClientMenage(ActionEvent event) {
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
}