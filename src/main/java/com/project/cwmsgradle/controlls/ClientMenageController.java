package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
import java.util.List;
import java.util.Optional;

public class ClientMenageController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();


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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set the current user information
            MenuViewController menuController = loader.getController();
            menuController.setUserRole(AuthenticatedUser.getInstance().getRole());
            menuController.setUsername(AuthenticatedUser.getInstance().getUsername());

            currentUsername = AuthenticatedUser.getInstance().getUsername();
            currentUserRole = AuthenticatedUser.getInstance().getRole();

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
        saveClientToDatabase(client);
    }

    public void updateClientInList(Client originalClient, Client updatedClient) {
        int index = clientData.indexOf(originalClient);
        if (index != -1) {
            clientData.set(index, updatedClient);
            updateClientInDatabase(updatedClient);
        }
    }

    public int generateClientId() {
        return nextClientId++;
    }

    private void saveClientToDatabase(Client client) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
        }
    }

    private void updateClientInDatabase(Client client) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    private void loadClientData() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Client> clients = session.createQuery("from Client", Client.class).getResultList();
            clientData.addAll(clients);
            session.getTransaction().commit();
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
                AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano klienta", "Proszę wybrać klienta do edycji.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}