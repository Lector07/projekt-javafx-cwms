package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

public class VehicleAddController {


    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();
    //Long clientId = AuthenticatedUser.getInstance().getClientId();

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField productionYearField;

    @FXML
    private ComboBox<Client> clientComboBox;

    private VehicleMenageController vehicleMenageController;

    private SessionFactory sessionFactory;

    public VehicleAddController() {

    }

    @FXML
    public void initialize() {
        // Load clients from the database using Hibernate and populate ComboBox
        populateClientComboBox();
    }

    private void populateClientComboBox() {
        sessionFactory = HibernateUtil.getSessionFactory();
        // Create an ObservableList to hold the clients
        ObservableList<Client> clientList = FXCollections.observableArrayList();

        // Open a Hibernate session
        try {
            // Create a query to retrieve all clients
            Session session = sessionFactory.openSession();
            Query<Client> query = session.createQuery("FROM Client", Client.class);

            List<Client> clients = query.list(); // Execute the query

            // Add the clients to the ObservableList
            clientList.addAll(clients);

            // Set the ObservableList to the ComboBox
            clientComboBox.setItems(clientList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String registrationNumber = registrationNumberField.getText();
        String brand = brandField.getText();
        String model = modelField.getText();
        int productionYear = Integer.parseInt(productionYearField.getText());

        // Get the selected client from the ComboBox
        Client selectedClient = clientComboBox.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Use the selected client to create the new vehicle
            Vehicle newVehicle = new Vehicle(registrationNumber, brand, model, productionYear, selectedClient);
            vehicleMenageController.addVehicleToList(newVehicle);
        } else {
            // Handle the case where no client is selected (e.g., show an error message)
            System.out.println("No client selected");
            // TODO
            // Add error alert!!!!
        }
        navigateToVehicleMenage(event);
    }

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    private void navigateToVehicleMenage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Zarządzanie pojazdami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ComboBox<Client> getClientComboBox() {
        return clientComboBox;
    }

    public void setClientComboBox(ComboBox<Client> clientComboBox) {
        this.clientComboBox = clientComboBox;
    }
}
