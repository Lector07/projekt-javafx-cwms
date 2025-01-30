
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


    public void setUserRole(String role) {
        this.userRole = role;
        updateUIBasedOnRole();
    }

    public void setUsername(String username) {
        this.username = username;
        usernameLabel.setText(username);
    }

    private void updateUIBasedOnRole() {
        if ("user".equals(userRole)) {
            usersButton.setVisible(false);
            userPic.setVisible(false);
        } else {
            usersButton.setVisible(true);
            userPic.setVisible(true);
        }
    }

    @FXML
    protected void initialize() {
        usernameLabel.setVisible(true);
    }

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

    @FXML
    protected void onManageRepairsButtonClick() {
        // Handle manage repairs button click
    }

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

    @FXML
    protected void onManageVehiclesButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current user information
            VehicleMenageController vehiclesController = loader.getController();
            vehiclesController.setMenuViewController(this);

            Stage primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public String getUsername() {
        return username;
    }
}