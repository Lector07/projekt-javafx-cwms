package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMenageController {

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, Integer> clientIdColumn;

    @FXML
    private TableColumn<Client, Integer> idVehicleColumn;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> surnameColumn;

    @FXML
    private TableColumn<Client, String> phoneColumn;

    @FXML
    private TableColumn<Client, String> mailColumn;

    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    private int nextClientId = 1;

    @FXML
    protected void initialize() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        idVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));

        loadClientData();
        clientTableView.setItems(clientData);
    }

    @FXML
    protected void onAddClientButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientAdd-view.fxml"));
            Parent root = loader.load();

            ClientAddController addController = loader.getController();
            addController.setClientMenageController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Dodaj klienta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onEditClientButtonClick(ActionEvent event) {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientEdit-view.fxml"));
                Parent root = loader.load();

                ClientEditController controller = loader.getController();
                controller.setClientData(selectedClient);
                controller.setClientMenageController(this);

                Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                primaryStage.getScene().setRoot(root);
                primaryStage.setTitle("Edytuj klienta");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDeleteClientButtonClick() {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć klienta?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    clientData.remove(selectedClient);
                    saveClientData();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie wybrano klienta do usunięcia.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();
            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClientToList(Client client) {
        clientData.add(client);
        saveClientData();
    }

    public void updateClientInList(Client originalClient, Client updatedClient) {
        int index = clientData.indexOf(originalClient);
        if (index != -1) {
            clientData.set(index, updatedClient);
            saveClientData();
        }
    }

    public int generateClientId() {
        return nextClientId++;
    }

    private void saveClientData() {
        try {
            List<String> clientDataStrings = clientData.stream()
                    .map(client -> String.join(",", String.valueOf(client.getClientId()), String.valueOf(client.getId()), client.getName(), client.getSurname(), client.getPhone(), client.getMail()))
                    .collect(Collectors.toList());
            Files.write(Paths.get("clientData.txt"), clientDataStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClientData() {
        try {
            List<String> clientDataStrings = Files.readAllLines(Paths.get("clientData.txt"));
            for (String clientDataString : clientDataStrings) {
                String[] data = clientDataString.split(",");
                if (data.length == 6) {
                    Client client = new Client(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3], data[4], data[5]);
                    clientData.add(client);
                    nextClientId = Math.max(nextClientId, client.getClientId() + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}