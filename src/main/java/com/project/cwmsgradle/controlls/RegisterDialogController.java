package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

/**
 * Kontroler odpowiedzialny za rejestrację użytkowników.
 */
public class RegisterDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Obsługuje kliknięcie przycisku rejestracji.
     */
    @FXML
    protected void onSubmitButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtils.showWarningAlert("Błąd rejestracji", "Wszystkie pola muszą być wypełnione.", "Proszę wypełnić wszystkie pola.");
            return;
        }

        if (password.equals(confirmPassword)) {
            if (addUserToDatabase(username, password)) {
                AlertUtils.showInformationAlert("Rejestracja udana", "Konto zostało utworzone.", "Możesz teraz się zalogować.");
            } else {
                AlertUtils.showErrorAlert("Błąd rejestracji", "Użytkownik z taką nazwą już istnieje.", "Proszę wybrać inną nazwę użytkownika.");
            }
        } else {
            AlertUtils.showWarningAlert("Błąd rejestracji", "Hasła nie są identyczne.", "Proszę upewnić się, że hasła są identyczne.");
        }
    }

    /**
     * Dodaje użytkownika do bazy danych.
     * @param username nazwa użytkownika
     * @param password hasło
     * @return true, jeśli użytkownik został pomyślnie dodany, w przeciwnym razie false
     */
    private boolean addUserToDatabase(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            User existingUser = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
            if (existingUser != null) {
                return false;
            }

            // Określa rolę
            String role = "user";
            if ("admin".equals(username) && "admin".equals(password)) {
                User adminUser = (User) session.createQuery("FROM User WHERE role = 'admin'").uniqueResult();
                if (adminUser == null) {
                    role = "admin";
                }
            }

            // Tworzy i zapisuje nowego użytkownika
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(role);
            session.save(newUser);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
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