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

/**
 * Kontroler odpowiedzialny za logowanie użytkowników.
 */
public class LoginDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private MenuViewController menuViewController;

    /**
     * Ustawia kontroler menu.
     * @param menuViewController kontroler menu
     */
    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }

    /**
     * Obsługuje kliknięcie przycisku logowania.
     */
    @FXML
    protected void onSubmitButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticateUser(username, password)) {
            User user = getUserByUsername(username);
            if (user != null) {
                // Inicjalizuje instancję AuthenticatedUser
                AuthenticatedUser authUser = AuthenticatedUser.getInstance();
                authUser.setUserId(user.getUserId());
                authUser.setUsername(user.getUsername());
                authUser.setRole(user.getRole());

                // Ustawia dane w widoku menu
                if (menuViewController != null) {
                    menuViewController.setUserRole(user.getRole());
                    menuViewController.setUsername(user.getUsername());
                }

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
                    Parent root = loader.load();
                    MenuViewController menuController = loader.getController();
                    menuController.setUserRole(user.getRole());
                    menuController.setUsername(user.getUsername());
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    stage.setTitle("CWMS-FX");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            AlertUtils.showErrorAlert("Błąd logowania", "Nieporawne dane logowania.", "Proszę sprawdzić nazwę użytkownika i hasło.");
        }
    }

    /**
     * Autoryzuje użytkownika na podstawie nazwy użytkownika i hasła.
     * @param username nazwa użytkownika
     * @param password hasło
     * @return true, jeśli użytkownik został pomyślnie uwierzytelniony, w przeciwnym razie false
     */
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

    /**
     * Pobiera użytkownika na podstawie nazwy użytkownika.
     * @param username nazwa użytkownika
     * @return użytkownik
     */
    private User getUserByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku powrotu.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    private void onReturnButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainApp-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CWMS-FX");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}