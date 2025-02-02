package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.utils.AuthenticatedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentMenageController {
    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();



    public void setMenuViewController(MenuViewController menuViewController) {
    }

    @FXML
    private Label usernameLabelAppointments;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        usernameLabelAppointments.setText(username);
    }

    @FXML
    protected void initialize() {
        usernameLabelAppointments.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set the current user information
            MenuViewController menuController = loader.getController();
            menuController.setUserRole(currentUserRole);
            menuController.setUsername(currentUsername);

            currentUsername = AuthenticatedUser.getInstance().getUsername();
            currentUserRole = AuthenticatedUser.getInstance().getRole();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
