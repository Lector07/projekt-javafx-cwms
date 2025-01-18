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

    public void setClientMenageController(ClientMenageController controller) {
        this.clientMenageController = controller;
    }

    @FXML
    public void onSaveButtonClick(ActionEvent event) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();
        String mail = mailField.getText();

        int clientId = clientMenageController.generateClientId();
        int id = 0; // or generate a unique id for the vehicle if needed

        Client newClient = new Client(clientId, id, name, surname, phone, mail);
        clientMenageController.addClientToList(newClient);
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