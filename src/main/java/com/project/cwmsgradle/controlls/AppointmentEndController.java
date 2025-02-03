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

/**
 * Kontroler odpowiedzialny za zakończenie wizyt.
 */
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

    /**
     * Ustawia wizytę do zakończenia.
     * @param appointment wizyta do zakończenia
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Ustawia TableView z wizytami.
     * @param appointmentsTableView TableView z wizytami
     */
    public void setAppointmentsTableView(TableView<Appointment> appointmentsTableView) {
        this.appointmentsTableView = appointmentsTableView;
    }

    /**
     * Obsługuje kliknięcie przycisku zakończenia wizyty.
     */
    @FXML
    protected void onEndAppointmentButtonClick() {
        if (checkboxEndAppointment.isSelected()) {
            costTextField.setDisable(false);
            updateAppointmentStatusAndCost();
        } else {
            costTextField.setDisable(true);
        }
    }

    /**
     * Aktualizuje status i koszt wizyty.
     */
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

    /**
     * Odświeża TableView z wizytami.
     */
    private void refreshTable() {
        if (appointmentsTableView != null) {
            appointmentsTableView.refresh();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku powrotu.
     */
    @FXML
    protected void onGoBackButtonClick() {
        costTextField.getScene().getWindow().hide();
    }

    /**
     * Ustawia nazwę użytkownika.
     * @param currentUsername nazwa użytkownika
     */
    public void setUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}