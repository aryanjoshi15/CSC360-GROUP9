package com.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    private SwingJavaFX swingApplication;

    public void setSwingApplication(SwingJavaFX application) {
        this.swingApplication = application;
    }

    @FXML
    public void initialize() {
        filterBox.setItems(FXCollections.observableArrayList("All", "Active", "Inactive"));
        filterBox.setValue("All");

        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

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
        if (swingApplication != null)
            swingApplication.updateSwingStatus("JavaFX search executed");
    }

    @FXML
    private void handleAddUser() {
        userTable.getItems().add(new User("New User", "new@example.com", "Active"));
        statusLabel.setText("User added");
        if (swingApplication != null)
            swingApplication.updateSwingStatus("JavaFX added a user");
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
        if (swingApplication != null)
            swingApplication.updateSwingStatus("User deleted");
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
                if (swingApplication != null)
                    swingApplication.updateSwingStatus("JavaFX processing complete");
            });
        });

        worker.setDaemon(true);
        worker.start();
    }
}
