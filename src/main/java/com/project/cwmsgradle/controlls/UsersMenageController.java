package com.project.cwmsgradle.controlls;

import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.utils.AlertUtils;
import com.project.cwmsgradle.utils.AuthenticatedUser;
import com.project.cwmsgradle.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UsersMenageController {

    String currentUsername = AuthenticatedUser.getInstance().getUsername();
    String currentUserRole = AuthenticatedUser.getInstance().getRole();

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, Long> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, LocalDateTime> createdAtColumn;

    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private MenuViewController menuViewController;

    private SessionFactory sessionFactory;

    @FXML
    private Label usernameLabelUsers;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        usernameLabelUsers.setText(username);
    }

    public void setMenuViewController(MenuViewController menuViewController) {
        this.menuViewController = menuViewController;
    }

    @FXML
    protected void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        usernameLabelUsers.setVisible(true);
        setUsername(AuthenticatedUser.getInstance().getUsername());

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadUsersData();
        usersTableView.setItems(usersData);
    }

    private void loadUsersData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).getResultList();
            usersData.addAll(users);
            session.getTransaction().commit();
        }
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu-view.fxml"));
            Parent root = loader.load();

            MenuViewController menuController = loader.getController();
            menuController.setUserRole(currentUserRole);
            menuController.setUsername(currentUsername);

            currentUsername = AuthenticatedUser.getInstance().getUsername();
            currentUserRole = AuthenticatedUser.getInstance().getRole();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Menu");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteUserButtonClick(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if ("admin".equals(selectedUser.getUsername())) {
                AlertUtils.showWarningAlert("Nie można usunąć", "Usunięcie nieudane", "Nie można usunąć użytkownika.");
                return;
            }

            Optional<ButtonType> result = AlertUtils.showDeleteConfirmationAlert(selectedUser.getUsername());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                usersData.remove(selectedUser);
                deleteUserFromDatabase(selectedUser);
            }
        } else {
            AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano użytkownika", "Proszę wybrać użytkownika do usunięcia.");
        }
    }

    @FXML
    protected void onEditPasswordButtonClick(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UsersPasswordEdit-view.fxml"));
                Parent root = loader.load();

                UsersPasswordEditController passwordEditController = loader.getController();
                passwordEditController.setUsersMenageController(this);
                passwordEditController.setUser(selectedUser);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Zmiana hasła");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertUtils.showWarningAlert("Brak wyboru", "Nie wybrano użytkownika", "Proszę wybrać użytkownika do zmiany hasła.");
        }
    }

    private void deleteUserFromDatabase(User selectedUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(selectedUser);
            session.getTransaction().commit();
        }
    }

    public void refreshUserTable() {
        usersData.clear();
        loadUsersData();
    }
}