package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonType;

/**
 * Kontroler odpowiedzialny za edycję klientów.
 */
public class ClientEditController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField mailField;

    private Client originalClient;
    private ClientMenageController clientMenageController;

    /**
     * Ustawia dane klienta do edycji.
     * @param client klient do edycji
     */
    public void setClientData(Client client) {
        this.originalClient = client;
        nameField.setText(client.getName());
        surnameField.setText(client.getSurname());
        phoneField.setText(client.getPhone());
        mailField.setText(client.getEmail());
    }

    /**
     * Ustawia kontroler zarządzania klientami.
     * @param controller kontroler zarządzania klientami
     */
    public void setClientMenageController(ClientMenageController controller) {
        this.clientMenageController = controller;
    }

    /**
     * Obsługuje kliknięcie przycisku zapisu klienta.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    public void onSaveButtonClick(ActionEvent event) {
        String updatedName = nameField.getText();
        String updatedSurname = surnameField.getText();
        String updatedPhone = phoneField.getText();
        String updatedEmail = mailField.getText(); // Zmieniam mail na email

        // Użyj konstruktora z 4 argumentami
        Client updatedClient = new Client(updatedName, updatedSurname, updatedPhone, updatedEmail);

        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Potwierdzenie zapisu", "Czy napewno chcesz zapisać zmiany?", "Kliknij OK, aby zapisać zmiany.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedClient.setClientId(originalClient.getClientId()); // Ustawiamy ID
            clientMenageController.updateClientInList(originalClient, updatedClient);
            navigateToClientMenage(event);
        }
    }

    /**
     * Obsługuje kliknięcie przycisku anulowania.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        navigateToClientMenage(event);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}