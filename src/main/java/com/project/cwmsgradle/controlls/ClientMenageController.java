package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> surnameColumn;

    @FXML
    private TableColumn<Client, String> phoneColumn;

    @FXML
    private TableColumn<Client, String> mailColumn;

    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    private int nextClientId = 1;

    private MenuViewController menuViewController;

    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }

    @FXML
    protected void initialize() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClientData();
        clientTableView.setItems(clientData);
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        if (menuViewController == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Menu View Controller not set");
            alert.setContentText("Please set the Menu View Controller before going back.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            MenuViewController menuController = loader.getController();
            menuController.setUserRole(menuViewController.getUserRole());
            menuController.setUsername(menuViewController.getUsername());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddClientButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientAdd-view.fxml"));
            Parent root = loader.load();

            ClientAddController addController = loader.getController();
            addController.setClientMenageController(this);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Dodaj Klienta");
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
                    .map(client -> String.join(",", String.valueOf(client.getClientId()), client.getName(), client.getSurname(), client.getPhone(), client.getEmail()))
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
                if (data.length == 5) {
                    Client client = new Client(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4]);
                    clientData.add(client);
                    nextClientId = Math.max(nextClientId, client.getClientId() + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditClientButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientEdit-view.fxml"));
            Parent root = loader.load();

            ClientEditController editController = loader.getController();
            Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                editController.setClientData(selectedClient);
                editController.setClientMenageController(this);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.setTitle("Edytuj Klienta");
            } else {
                // Handle no client selected case
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Brak wyboru");
                alert.setHeaderText("Nie wybrano klienta");
                alert.setContentText("Proszę wybrać klienta do edycji.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}