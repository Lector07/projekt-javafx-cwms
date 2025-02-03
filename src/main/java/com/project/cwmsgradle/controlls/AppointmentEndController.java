package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.nio.charset.StandardCharsets;

public class AppointmentEndController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();
    Long currentUserId = AuthenticatedUser.getInstance().getUserId();

    @FXML
    private CheckBox checkboxEndAppointment;

    @FXML
    private TextField costTextField;

    private Appointment appointment;
    private TableView<Appointment> appointmentsTableView;

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setAppointmentsTableView(TableView<Appointment> appointmentsTableView) {
        this.appointmentsTableView = appointmentsTableView;
    }

    @FXML
    protected void onEndAppointmentButtonClick() {
        if (checkboxEndAppointment.isSelected()) {
            costTextField.setDisable(false);
            updateAppointmentStatusAndCost();
        } else {
            costTextField.setDisable(true);
        }
    }

    private void updateAppointmentStatusAndCost() {
        String costText = costTextField.getText();
        if (costText != null && !costText.isEmpty()) {
            try {
                double cost = Double.parseDouble(costText);
                if (cost < 0) {
                    AlertUtils.showErrorAlert("Błąd", "Nieprawidłowy koszt", "Koszt nie może być ujemny.");
                    return;
                }
                appointment.setCost(cost);
                appointment.setStatus(new String("Zakończono".getBytes(), StandardCharsets.UTF_8));

                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.update(appointment);
                    transaction.commit();
                }
                AlertUtils.showInformationAlert("Sukces", "Aktualizacja", "Status zlecenia i koszt zostały zaktualizowane pomyślnie.");
                refreshTable();
            } catch (NumberFormatException e) {
                AlertUtils.showErrorAlert("Błąd", "Nieprawidłowe dane", "Proszę wprowadzić prawidłową liczbę dla kosztu.");
            }
        } else {
            AlertUtils.showErrorAlert("Błąd", "Brak danych", "Proszę wprowadzić koszt.");
        }
    }

    private void refreshTable() {
        if (appointmentsTableView != null) {
            appointmentsTableView.refresh();
        }
    }

    @FXML
    protected void onGoBackButtonClick() {
        costTextField.getScene().getWindow().hide();
    }

    public void setUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}