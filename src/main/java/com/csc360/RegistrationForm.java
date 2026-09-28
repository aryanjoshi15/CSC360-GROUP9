package com.csc360;

// ====== JavaFX imports ======
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple registration form — JavaFX only (initial skeleton).
 *
 * Run with: mvn javafx:run
 */
public class RegistrationForm extends Application {

    @Override
    public void start(Stage stage) {

        // ==================== JavaFX Components ====================

        // Title
        Label title = new Label("Registration Form");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Name field
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(250);

        // Result label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2e7d32;");

        // Submit button
        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            resultLabel.setText("Name: " + name);
        });

        // ==================== Layout ====================

        VBox root = new VBox(10,
            title,
            nameLabel, nameField,
            submitBtn,
            resultLabel
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);

        // ==================== Window ====================

        stage.setTitle("Registration Form");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
