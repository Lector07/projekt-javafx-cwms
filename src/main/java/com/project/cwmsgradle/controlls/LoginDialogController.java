package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
            messageLabel.setText("Login successful.");
            if (menuViewController != null) {
                menuViewController.setUserRole(getUserRole(username));
                menuViewController.setUsername(username);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
                Parent root = loader.load();
                MenuViewController menuController = loader.getController();
                menuController.setUserRole(getUserRole(username));
                menuController.setUsername(username);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.setTitle("Menu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password.");
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
}