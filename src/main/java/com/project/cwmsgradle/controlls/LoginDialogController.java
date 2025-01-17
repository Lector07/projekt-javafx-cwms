package com.project.cwmsgradle.controlls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onSubmitButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();
            Stage primaryStage = (Stage) usernameField.getScene().getWindow();
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle("Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}