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

/**
 * Kontroler odpowiedzialny za zarządzanie klientami.
 */
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

    /**
     * Ustawia kontroler menu.
     * @param menuViewController kontroler menu
     */
    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }

    @FXML
    private Label usernameLabelClient;

    private String username;

    /**
     * Ustawia nazwę użytkownika.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
        usernameLabelClient.setText(username);
    }

    /**
     * Inicjalizuje kontroler, ustawia kolumny tabeli i ładuje dane klientów.
     */
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

    /**
     * Obsługuje kliknięcie przycisku powrotu.
     * @param event zdarzenie kliknięcia przycisku
     */
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

    /**
     * Obsługuje kliknięcie przycisku dodawania klienta.
     * @param event zdarzenie kliknięcia przycisku
     */
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

    /**
     * Dodaje klienta do listy.
     * @param client klient do dodania
     */
    public void addClientToList(Client client) {
        if (validateClientData(client)) {
            clientData.add(client);
            saveClientToDatabase(client);
        }
    }

    /**
     * Aktualizuje klienta na liście.
     * @param originalClient oryginalny klient
     * @param updatedClient zaktualizowany klient
     */
    public void updateClientInList(Client originalClient, Client updatedClient) {
        if (validateClientData(updatedClient)) {
            int index = clientData.indexOf(originalClient);
            if (index != -1) {
                clientData.set(index, updatedClient);
                updateClientInDatabase(updatedClient);
            }
        }
    }

    /**
     * Generuje ID klienta.
     * @return ID klienta
     */
    public int generateClientId() {
        return nextClientId++;
    }

    /**
     * Zapisuje klienta do bazy danych.
     * @param client klient do zapisania
     */
    private void saveClientToDatabase(Client client) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
        }
    }

    /**
     * Aktualizuje klienta w bazie danych.
     * @param client klient do aktualizacji
     */
    private void updateClientInDatabase(Client client) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    /**
     * Ładuje dane klientów z bazy danych.
     */
    private void loadClientData() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Client> clients = session.createQuery("from Client", Client.class).getResultList();
            clientData.addAll(clients);
            session.getTransaction().commit();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku edycji klienta.
     * @param event zdarzenie kliknięcia przycisku
     */
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

    /**
     * Obsługuje kliknięcie przycisku usunięcia klienta.
     * @param event zdarzenie kliknięcia przycisku
     */
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

    /**
     * Usuwa klienta z bazy danych.
     * @param selectedClient klient do usunięcia
     */
    private void deleteClientFromDatabase(Client selectedClient) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Client.class).buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.delete(selectedClient);
            session.getTransaction().commit();
        }
    }

    /**
     * Waliduje dane klienta.
     * @param client klient do walidacji
     * @return true, jeśli dane są poprawne, w przeciwnym razie false
     */
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

    /**
     * Sprawdza, czy numer telefonu jest poprawny.
     * @param phoneNumber numer telefonu do sprawdzenia
     * @return true, jeśli numer telefonu jest poprawny, w przeciwnym razie false
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{9}");
    }

    /**
     * Sprawdza, czy email jest poprawny.
     * @param email email do sprawdzenia
     * @return true, jeśli email jest poprawny, w przeciwnym razie false
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    /**
     * Sprawdza, czy imię lub nazwisko jest poprawne.
     * @param name imię lub nazwisko do sprawdzenia
     * @return true, jeśli imię lub nazwisko jest poprawne, w przeciwnym razie false
     */
    private boolean isValidName(String name) {
        return name != null && name.matches("[A-Z][a-zA-Z]*");
    }
}