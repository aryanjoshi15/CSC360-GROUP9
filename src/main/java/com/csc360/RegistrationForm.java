package com.csc360;

// ====== JavaFX imports ======
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// ====== Swing imports ======
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

/**
 * Registration form with JavaFX controls and embedded Swing components.
 * Swing JComboBox and JCheckBox are embedded via SwingNode.
 *
 * Run with: mvn javafx:run
 */
public class RegistrationForm extends Application {

    // Swing components — shared between EDT and JavaFX threads
    private JComboBox<String> courseBox;
    private JCheckBox agreeBox;

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

        // ==================== Swing Components ====================

        // 1) Course dropdown (JComboBox)
        Label courseLabel = new Label("Course:");
        SwingNode courseNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            courseBox = new JComboBox<>(new String[]{"Java", "Python", "C++"});
            courseNode.setContent(courseBox);
        });

        // 2) Agreement checkbox (JCheckBox)
        SwingNode checkNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            agreeBox = new JCheckBox("I agree to the terms");
            checkNode.setContent(agreeBox);
        });

        // ==================== JavaFX: Submit & Result ====================

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2e7d32;");
        resultLabel.setWrapText(true);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e -> {
            // Read the JavaFX text field (already on FX thread)
            String name = nameField.getText().trim();

            // Read Swing values on the EDT, then show result on FX thread
            SwingUtilities.invokeLater(() -> {
                String course  = (String) courseBox.getSelectedItem();
                boolean agreed = agreeBox.isSelected();

                Platform.runLater(() ->
                    resultLabel.setText(
                        "Name: " + name
                        + ",  Course: " + course
                        + ",  Agreed: " + (agreed ? "Yes" : "No")
                    )
                );
            });
        });

        // ==================== Layout ====================

        VBox root = new VBox(10,
            title,
            nameLabel, nameField,
            courseLabel, courseNode,
            checkNode,
            submitBtn,
            resultLabel
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);

        // ==================== Window ====================

        stage.setTitle("Registration Form");
        stage.setScene(new Scene(root, 400, 380));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
