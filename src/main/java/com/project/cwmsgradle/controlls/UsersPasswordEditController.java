package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class UsersPasswordEditController {

    @FXML
    private TextField usersEditTextField;

    @FXML
    private TextField confirmPasswordField;

    private UsersMenageController usersMenageController;
    private User user;

    public void setUsersMenageController(UsersMenageController controller) {
        this.usersMenageController = controller;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    protected void initialize() {
        usersEditTextField.setPromptText("Wprowadź nowe hasło");
        confirmPasswordField.setPromptText("Potwierdź nowe hasło");
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String newPassword = usersEditTextField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtils.showWarningAlert("Błąd", "Pole hasła jest puste", "Proszę wprowadzić i potwierdzić nowe hasło.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertUtils.showWarningAlert("Błąd", "Hasła nie są zgodne", "Nowe hasło i potwierdzenie hasła muszą być takie same.");
            return;
        }

        if (newPassword.equals(user.getPassword())) {
            AlertUtils.showWarningAlert("Błąd", "To samo hasło", "Nowe hasło nie może być takie samo jak poprzednie.");
            return;
        }

        if (updateUserPassword(user, newPassword)) {
            AlertUtils.showInformationAlert("Sukces", "Hasło zostało zmienione", "Hasło użytkownika zostało pomyślnie zmienione.");
            usersMenageController.refreshUserTable();
            closeWindow();
        } else {
            AlertUtils.showErrorAlert("Błąd", "Zmiana hasła nie powiodła się", "Wystąpił błąd podczas zmiany hasła.");
        }
    }

    private boolean updateUserPassword(User user, String newPassword) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            user.setPassword(newPassword);
            session.update(user);
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

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) usersEditTextField.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UsersMenage-view.fxml"));
            Parent root = loader.load();
            Button source = (Button) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}