package com.project.cwmsgradle.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Klasa narzędziowa do wyświetlania różnych typów alertów.
 */
public class AlertUtils {

    /**
     * Wyświetla alert ostrzegawczy.
     * @param title tytuł alertu
     * @param header nagłówek alertu
     * @param content treść alertu
     */
    public static void showWarningAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    /**
     * Wyświetla alert potwierdzenia.
     * @param title tytuł alertu
     * @param header nagłówek alertu
     * @param content treść alertu
     * @return opcjonalny typ przycisku
     */
    public static Optional<ButtonType> showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        return alert.showAndWait();
    }

    /**
     * Wyświetla alert informacyjny.
     * @param title tytuł alertu
     * @param header nagłówek alertu
     * @param content treść alertu
     */
    public static void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    /**
     * Wyświetla alert błędu.
     * @param title tytuł alertu
     * @param header nagłówek alertu
     * @param content treść alertu
     */
    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(header.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(content.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }

    /**
     * Wyświetla alert błędu dla nieprawidłowych danych logowania.
     */
    public static void showInvalidCredentialsAlert() {
        showErrorAlert("Nieprawidłowe dane", "Logowanie nieudane", "Wprowadzono nieprawidłową nazwę użytkownika lub hasło.");
    }

    /**
     * Wyświetla alert błędu dla istniejącego użytkownika.
     */
    public static void showUserAlreadyExistsAlert() {
        showErrorAlert("Użytkownik istnieje", "Rejestracja nieudana", "Użytkownik o tej nazwie już istnieje.");
    }

    /**
     * Wyświetla alert potwierdzenia usunięcia.
     * @param itemName nazwa elementu do usunięcia
     * @return opcjonalny typ przycisku
     */
    public static Optional<ButtonType> showDeleteConfirmationAlert(String itemName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(new String("Potwierdzenie usunięcia".getBytes(), StandardCharsets.UTF_8));
        alert.setHeaderText(new String(("Czy na pewno chcesz usunąć " + itemName + "?").getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String("Tej operacji nie można cofnąć.".getBytes(), StandardCharsets.UTF_8));
        return alert.showAndWait();
    }
}