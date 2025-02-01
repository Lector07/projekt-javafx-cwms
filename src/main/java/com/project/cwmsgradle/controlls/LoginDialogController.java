package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;

public class LoginDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private MenuViewController menuViewController;

    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }


    @FXML
    protected void onSubmitButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticateUser(username, password)) {
            // Pobranie roli
            String role = getUserRole(username);
            // Ustawienie danych w AuthenticatedUser
            AuthenticatedUser authUser = AuthenticatedUser.getInstance();
            authUser.setUsername(username);
            authUser.setRole(role);
            // Ustawianie danych w menu view
            if (menuViewController != null) {
                menuViewController.setUserRole(role);
                menuViewController.setUsername(username);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
                Parent root = loader.load();
                MenuViewController menuController = loader.getController();
                menuController.setUserRole(role);
                menuController.setUsername(username);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.setTitle("Menu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertUtils.showErrorAlert("Błąd logowania", "Nieporawne dane logowania.", "Proszę sprawdzić nazwę użytkownika i hasło.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            User user = (User) session.createQuery("FROM User WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
            return user != null;
        } finally {
            session.close();
        }
    }

    private String getUserRole(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            User user = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
            return user != null ? user.getRole() : null;
        } finally {
            session.close();
        }
    }

    @FXML
    private void onReturnButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainApp-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}