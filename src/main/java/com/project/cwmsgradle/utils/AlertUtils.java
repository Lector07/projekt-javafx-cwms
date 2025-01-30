package com.project.cwmsgradle.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AlertUtils {

    public static void showWarningAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    public static void showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    public static void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    public static void showInvalidCredentialsAlert() {
        showErrorAlert("Nieprawidłowe dane", "Logowanie nieudane", "Wprowadzono nieprawidłową nazwę użytkownika lub hasło.");
    }

    public static void showUserAlreadyExistsAlert() {
        showErrorAlert("Użytkownik istnieje", "Rejestracja nieudana", "Użytkownik o tej nazwie już istnieje.");
    }

    public static Optional<ButtonType> showDeleteConfirmationAlert(String itemName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(new String("Potwierdzenie usunięcia".getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(("Czy na pewno chcesz usunąć " + itemName + "?").getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String("Tej operacji nie można cofnąć.".getBytes(), StandardCharsets.UTF_8));
        return alert.showAndWait();
    }
}