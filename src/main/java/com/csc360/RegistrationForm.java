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
<<<<<<< HEAD
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

/**
 * Registration form with JavaFX controls and embedded Swing components.
 * Swing JComboBox and JCheckBox are embedded via SwingNode.
 *
=======
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

/**
 * Simple registration form showing JavaFX and Swing working together.
 * 
 * JavaFX provides: Label, TextField, Button, and the main window.
 * Swing provides:  JComboBox, JCheckBox, JRadioButtons, JSlider (via SwingNode).
 * 
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
 * Run with: mvn javafx:run
 */
public class RegistrationForm extends Application {

    // Swing components — shared between EDT and JavaFX threads
    private JComboBox<String> courseBox;
    private JCheckBox agreeBox;
<<<<<<< HEAD
=======
    private JRadioButton fallRadio, springRadio, summerRadio;
    private JSlider ratingSlider;
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2

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

<<<<<<< HEAD
        // 2) Agreement checkbox (JCheckBox)
=======
        // 2) Semester radio buttons (JRadioButton)
        Label semesterLabel = new Label("Semester:");
        SwingNode semesterNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            fallRadio = new JRadioButton("Fall", true);
            springRadio = new JRadioButton("Spring");
            summerRadio = new JRadioButton("Summer");

            // Group them so only one can be selected
            ButtonGroup group = new ButtonGroup();
            group.add(fallRadio);
            group.add(springRadio);
            group.add(summerRadio);

            JPanel panel = new JPanel();
            panel.add(fallRadio);
            panel.add(springRadio);
            panel.add(summerRadio);
            semesterNode.setContent(panel);
        });

        // 3) Rating slider (JSlider 1–10)
        Label sliderLabel = new Label("Rating: 5");
        SwingNode sliderNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            ratingSlider = new JSlider(1, 10, 5);
            ratingSlider.setMajorTickSpacing(1);
            ratingSlider.setPaintTicks(true);
            ratingSlider.setPaintLabels(true);

            // Live-update the JavaFX label when slider moves
            ratingSlider.addChangeListener(e ->
                Platform.runLater(() ->
                    sliderLabel.setText("Rating: " + ratingSlider.getValue())
                )
            );
            sliderNode.setContent(ratingSlider);
        });

        // 4) Agreement checkbox (JCheckBox)
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
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
<<<<<<< HEAD

=======
                int rating     = ratingSlider.getValue();

                String semester = "Fall";
                if (springRadio.isSelected()) semester = "Spring";
                if (summerRadio.isSelected()) semester = "Summer";

                String finalSemester = semester;
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
                Platform.runLater(() ->
                    resultLabel.setText(
                        "Name: " + name
                        + ",  Course: " + course
<<<<<<< HEAD
=======
                        + ",  Semester: " + finalSemester
                        + ",  Rating: " + rating
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
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
<<<<<<< HEAD
=======
            semesterLabel, semesterNode,
            sliderLabel, sliderNode,
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
            checkNode,
            submitBtn,
            resultLabel
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);

        // ==================== Window ====================

        stage.setTitle("Registration Form");
<<<<<<< HEAD
        stage.setScene(new Scene(root, 400, 380));
=======
        stage.setScene(new Scene(root, 400, 480));
>>>>>>> 557094d92ccce8255e10eab1a9440552399964a2
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
