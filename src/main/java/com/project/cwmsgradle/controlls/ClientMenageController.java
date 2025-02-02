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
    private Label usernameLabelClient;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        usernameLabelClient.setText(username);
    }

    @FXML
    protected void initialize() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usernameLabelClient.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());

        loadClientData();
        clientTableView.setItems(clientData);
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

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
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClientToList(Client client) {
        if (validateClientData(client)) {
            clientData.add(client);
            saveClientToDatabase(client);
        }
    }

    public void updateClientInList(Client originalClient, Client updatedClient) {
        if (validateClientData(updatedClient)) {
            int index = clientData.indexOf(originalClient);
            if (index != -1) {
                clientData.set(index, updatedClient);
                updateClientInDatabase(updatedClient);
            }
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
                stage.setTitle("CWMS-FX");
            } else {
                AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano klienta", "Proszę wybrać klienta do edycji.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteClientButtonClick(ActionEvent event) {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            Optional<ButtonType> result = AlertUtils.showDeleteConfirmationAlert("tego klienta");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clientData.remove(selectedClient);
                deleteClientFromDatabase(selectedClient);
            }
        } else {
            AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano klienta", "Proszę wybrać klienta do usunięcia.");
        }
    }

    private void deleteClientFromDatabase(Client selectedClient) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.delete(selectedClient);
            session.getTransaction().commit();
        }
    }

    private boolean validateClientData(Client client) {
        if (!isValidPhoneNumber(client.getPhone())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy numer telefonu", "Numer telefonu musi mieć 9 cyfr.");
            return false;
        }
        if (!isValidEmail(client.getEmail())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowy email", "Email musi zawierać '@'.");
            return false;
        }
        if (!isValidName(client.getName()) || !isValidName(client.getSurname())) {
            AlertUtils.showWarningAlert("Błąd", "Nieprawidłowe imię lub nazwisko", "Imiona i nazwisko muszą zaczynać się z dużej litery i nie mogą zawierać liczb.");
            return false;
        }
        return true;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{9}");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[A-Z][a-zA-Z]*");
    }
}