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

    public void setClientData(Client client) {
        this.originalClient = client;
        nameField.setText(client.getName());
        surnameField.setText(client.getSurname());
        phoneField.setText(client.getPhone());
        mailField.setText(client.getEmail());
    }

    public void setClientMenageController(ClientMenageController controller) {
        this.clientMenageController = controller;
    }

    @FXML
    public void onSaveButtonClick(ActionEvent event) {
        String updatedName = nameField.getText();
        String updatedSurname = surnameField.getText();
        String updatedPhone = phoneField.getText();
        String updatedMail = mailField.getText();

        Client updatedClient = new Client(originalClient.getClientId(), updatedName, updatedSurname, updatedPhone, updatedMail);
        clientMenageController.updateClientInList(originalClient, updatedClient);
        navigateToClientMenage(event);
    }

    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        navigateToClientMenage(event);
    }

    @FXML
    public void onGoBackButtonClick(ActionEvent event) {
        navigateToClientMenage(event);
    }

    private void navigateToClientMenage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Zarządzanie klientami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}