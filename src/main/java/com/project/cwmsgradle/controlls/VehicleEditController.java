package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.Vehicle;
import com.project.cwmsgradle.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;

/**
 * Kontroler odpowiedzialny za edycję pojazdów.
 */
public class VehicleEditController {

    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField productionYearField;

    private Vehicle originalVehicle;
    private VehicleMenageController vehicleMenageController;

    /**
     * Ustawia dane pojazdu w polach tekstowych.
     * @param vehicle pojazd do edycji
     */
    public void setVehicleData(Vehicle vehicle) {
        this.originalVehicle = vehicle;
        registrationNumberField.setText(vehicle.getRegistrationNumber());
        brandField.setText(vehicle.getBrand());
        modelField.setText(vehicle.getModel());
        productionYearField.setText(String.valueOf(vehicle.getProductionYear()));
    }

    /**
     * Ustawia kontroler zarządzania pojazdami.
     * @param controller kontroler zarządzania pojazdami
     */
    public void setVehicleMenageController(VehicleMenageController controller) {
        this.vehicleMenageController = controller;
    }

    /**
     * Obsługuje kliknięcie przycisku zapisu, aktualizuje dane pojazdu.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onSaveButtonClick(ActionEvent event) {
        String updatedRegistrationNumber = registrationNumberField.getText();
        String updatedBrand = brandField.getText();
        String updatedModel = modelField.getText();
        int updatedProductionYear = Integer.parseInt(productionYearField.getText());
        Client client = originalVehicle.getClients(); // Pobiera obiekt Client za pomocą getClients
        int vehicleId = originalVehicle.getVehicleId(); // Pobiera vehicleId z oryginalnego pojazdu

        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Potwierdzenie zapisu", "Czy napewno chcesz zapisać zmiany?", "Kliknij OK, aby zapisać zmiany.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Vehicle updatedVehicle = new Vehicle(updatedRegistrationNumber, updatedBrand, updatedModel, updatedProductionYear, client);
            updatedVehicle.setVehicleId(vehicleId); // Ustawia ID

            vehicleMenageController.updateVehicleInList(originalVehicle, updatedVehicle);
            navigateToVehicleMenage(event);
        }
    }

    /**
     * Obsługuje kliknięcie przycisku anulowania, wraca do widoku zarządzania pojazdami.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    /**
     * Obsługuje kliknięcie przycisku powrotu, wraca do widoku zarządzania pojazdami.
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        navigateToVehicleMenage(event);
    }

    /**
     * Nawiguje do widoku zarządzania pojazdami.
     * @param event zdarzenie kliknięcia przycisku
     */
    private void navigateToVehicleMenage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VehiclesMenage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("CWMS-FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}