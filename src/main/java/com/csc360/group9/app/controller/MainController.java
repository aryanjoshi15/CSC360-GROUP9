package com.csc360.group9.app.controller;

import com.csc360.group9.app.MainApp;
import com.csc360.group9.app.model.User;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    private MainApp mainApplication;

    public void setMainApplication(MainApp application) {
        this.mainApplication = application;
    }

    @FXML
    public void initialize() {
        filterBox.setItems(FXCollections.observableArrayList("All", "Active", "Inactive"));
        filterBox.setValue("All");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        userTable.setItems(FXCollections.observableArrayList(
                new User("Alice", "alice@example.com", "Active"),
                new User("Bob", "bob@example.com", "Active"),
                new User("Charlie", "charlie@example.com", "Inactive"),
                new User("David", "david@example.com", "Active")
        ));
    }

    @FXML
    private void handleSearch() {
        statusLabel.setText("Searching for: " + searchField.getText());
        if (mainApplication != null)
            mainApplication.updateSwingStatus("JavaFX search executed");
    }

    @FXML
    private void handleAddUser() {
        userTable.getItems().add(new User("New User", "new@example.com", "Active"));
        statusLabel.setText("User added");
        if (mainApplication != null)
            mainApplication.updateSwingStatus("JavaFX added a user");
    }

    @FXML
    private void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select a user first");
            return;
        }

        userTable.getItems().remove(selected);
        statusLabel.setText("User deleted");
        if (mainApplication != null)
            mainApplication.updateSwingStatus("User deleted");
    }

    @FXML
    private void handleProcess() {
        progressBar.setProgress(0);
        statusLabel.setText("Processing...");

        Thread worker = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                final int value = i;

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Platform.runLater(() -> progressBar.setProgress(value / 100.0));
            }

            Platform.runLater(() -> {
                statusLabel.setText("Processing complete");
                if (mainApplication != null)
                    mainApplication.updateSwingStatus("JavaFX processing complete");
            });
        });

        worker.setDaemon(true);
        worker.start();
    }
}
