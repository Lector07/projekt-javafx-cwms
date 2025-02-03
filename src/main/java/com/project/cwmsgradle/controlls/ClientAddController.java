package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * Kontroler odpowiedzialny za dodawanie klientów.
 */
public class ClientAddController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField mailField;

    private ClientMenageController clientMenageController;

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
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();
        String email = mailField.getText(); // Zmieniamy mail na email

        int clientId = clientMenageController.generateClientId();

        // Używamy konstruktora z 4 argumentami
        Client newClient = new Client(name, surname, phone, email);

        newClient.setClientId(clientId); // Ustawiamy clientId za pomocą settera

        clientMenageController.addClientToList(newClient);
        navigateToClientMenage(event);
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