package com.project.cwmsgradle.controlls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuViewController {

    @FXML
    private Button exitButton;

    @FXML
    private Button manageClientsButton;

    @FXML
    private Button manageRepairsButton;

    @FXML
    private Button usersButton;

    @FXML
    private ImageView userPic;

    @FXML
    private Label usernameLabel;

    private String userRole;
    private String username;

    /**
     * Ustawia rolę użytkownika i aktualizuje interfejs użytkownika na podstawie roli.
     * @param role rola użytkownika
     */
    public void setUserRole(String role) {
        this.userRole = role;
        updateUIBasedOnRole();
    }

    /**
     * Ustawia nazwę użytkownika i aktualizuje etykietę.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
        usernameLabel.setText(username);
    }

    /**
     * Aktualizuje interfejs użytkownika na podstawie roli użytkownika.
     */
    private void updateUIBasedOnRole() {
        if ("user".equals(userRole)) {
            usersButton.setVisible(false);
            userPic.setVisible(false);
        } else {
            usersButton.setVisible(true);
            userPic.setVisible(true);
        }
    }

    /**
     * Inicjalizuje kontroler, ustawia widoczność etykiety nazwy użytkownika.
     */
    @FXML
    protected void initialize() {
        usernameLabel.setVisible(true);
    }

    /**
     * Obsługuje kliknięcie przycisku zarządzania klientami, ładuje widok zarządzania klientami.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    public void onManageClientsButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientMenage-view.fxml"));
            Parent root = loader.load();

            ClientMenageController clientController = loader.getController();
            clientController.setMenuViewController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku zarządzania naprawami, ładuje widok zarządzania naprawami.
     */
    @FXML
    protected void onManageRepairsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AppointmentMenage-view.fxml"));
            Parent root = loader.load();

            AppointmentMenageController appointmentController = loader.getController();
            appointmentController.setMenuViewController(this);

            Stage primaryStage = (Stage) manageRepairsButton.getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku zarządzania użytkownikami, ładuje widok zarządzania użytkownikami.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onUsersButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UsersMenage-view.fxml"));
            Parent root = loader.load();

            UsersMenageController usersController = loader.getController();
            usersController.setMenuViewController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button manageVehiclesButton;

    /**
     * Obsługuje kliknięcie przycisku zarządzania pojazdami, ładuje widok zarządzania pojazdami.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onManageVehiclesButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();

            // Pobiera kontroler i przekazuje aktualne informacje o użytkowniku
            VehicleMenageController vehiclesController = loader.getController();
            vehiclesController.setMenuViewController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku wyjścia, ładuje widok główny aplikacji.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onExitButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainApp-view.fxml"));
            Parent root = loader.load();
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca nazwę użytkownika.
     * @return nazwa użytkownika
     */
    public String getUsername() {
        return username;
    }
}