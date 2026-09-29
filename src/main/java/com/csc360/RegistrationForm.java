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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// ====== Swing imports ======
import java.util.Hashtable;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

/**
 * Simple registration form showing JavaFX and Swing working together.
 * 
 * JavaFX provides: Label, TextField, Button, and the main window.
 * Swing provides:  JComboBox, JCheckBox, JRadioButtons, JSlider for SGPA (via SwingNode).
 * 
 * Run with: mvn javafx:run or directly via IDE Run/Debug.
 */
public class RegistrationForm {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

    public static class App extends Application {

        // Swing components — shared between EDT and JavaFX threads
        private JComboBox<String> courseBox;
        private JCheckBox agreeBox;
        private JRadioButton fallRadio, springRadio, summerRadio;
        private JSlider sgpaSlider;

        // Convert SGPA (out of 4.0) to letter grade
        private String getGrade(double gpa) {
            if (gpa >= 3.7) return "A";
            if (gpa >= 3.3) return "B+";
            if (gpa >= 3.0) return "B";
            if (gpa >= 2.7) return "B-";
            if (gpa >= 2.3) return "C+";
            if (gpa >= 2.0) return "C";
            if (gpa >= 1.0) return "D";
            return "F";
        }

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
            nameField.setMaxWidth(260);

            // Real-time input filter: allow only letters and spaces (block numbers & special characters)
            nameField.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                    return change;
                }
                return null;
            }));

            // ==================== Swing Components ====================

            // 1) Course dropdown (JComboBox)
            Label courseLabel = new Label("Course:");
            SwingNode courseNode = new SwingNode();
            SwingUtilities.invokeLater(() -> {
                courseBox = new JComboBox<>(new String[]{"Java", "Python", "C++"});
                courseNode.setContent(courseBox);
            });

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

            // 3) SGPA Slider (JSlider 0.0 - 4.0, values 0-40 step 1 = 0.1)
            Label sgpaLabel = new Label("SGPA: 3.5 / 4.0 (Grade: A)");
            SwingNode sgpaNode = new SwingNode();
            SwingUtilities.invokeLater(() -> {
                sgpaSlider = new JSlider(0, 40, 35);
                sgpaSlider.setMajorTickSpacing(10);
                sgpaSlider.setMinorTickSpacing(5);
                sgpaSlider.setPaintTicks(true);
                sgpaSlider.setPaintLabels(true);

                // Custom labels: 0.0, 1.0, 2.0, 3.0, 4.0
                Hashtable<Integer, JLabel> labels = new Hashtable<>();
                labels.put(0, new JLabel("0.0"));
                labels.put(10, new JLabel("1.0"));
                labels.put(20, new JLabel("2.0"));
                labels.put(30, new JLabel("3.0"));
                labels.put(40, new JLabel("4.0"));
                sgpaSlider.setLabelTable(labels);

                // Live-update the JavaFX label as slider moves
                sgpaSlider.addChangeListener(e -> {
                    double gpa = sgpaSlider.getValue() / 10.0;
                    String grade = getGrade(gpa);
                    Platform.runLater(() ->
                        sgpaLabel.setText(String.format("SGPA: %.1f / 4.0 (Grade: %s)", gpa, grade))
                    );
                });
                sgpaNode.setContent(sgpaSlider);
            });

            // 4) Agreement checkbox (JCheckBox)
            SwingNode checkNode = new SwingNode();
            SwingUtilities.invokeLater(() -> {
                agreeBox = new JCheckBox("I agree to the terms");
                checkNode.setContent(agreeBox);
            });

            // ==================== JavaFX: Submit & Validation ====================

            Label resultLabel = new Label();
            resultLabel.setStyle("-fx-font-size: 13px;");
            resultLabel.setWrapText(true);

            Button submitBtn = new Button("Submit");
            submitBtn.setOnAction(e -> {
                // Read JavaFX name field
                String name = nameField.getText().trim();

                // Read Swing values on the EDT
                SwingUtilities.invokeLater(() -> {
                    String course  = (String) courseBox.getSelectedItem();
                    boolean agreed = agreeBox.isSelected();
                    double gpa     = sgpaSlider.getValue() / 10.0;
                    String grade   = getGrade(gpa);

                    String semester = "Fall";
                    if (springRadio.isSelected()) semester = "Spring";
                    if (summerRadio.isSelected()) semester = "Summer";

                    // Validation checks: all fields must be filled and terms agreed
                    String error = null;
                    if (name.isEmpty()) {
                        error = "Please enter your name.";
                    } else if (!name.matches("[a-zA-Z\\s]+")) {
                        error = "Name can only contain letters and spaces.";
                    } else if (course == null || course.isEmpty()) {
                        error = "Please select a course.";
                    } else if (!agreed) {
                        error = "You must agree to the terms before submitting.";
                    }

                    String finalError = error;
                    String finalSemester = semester;

                    // Update JavaFX UI
                    Platform.runLater(() -> {
                        if (finalError != null) {
                            // Show error in red
                            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #d32f2f;");
                            resultLabel.setText("❌ " + finalError);
                        } else {
                            // Show success summary in green
                            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2e7d32;");
                            resultLabel.setText(
                                "✔ Registered successfully!\n"
                                + "Name: " + name
                                + ", Course: " + course
                                + ", Semester: " + finalSemester
                                + ", SGPA: " + String.format("%.1f/4.0", gpa) + " (" + grade + ")"
                            );
                        }
                    });
                });
            });

            // ==================== Layout ====================

            VBox root = new VBox(10,
                title,
                nameLabel, nameField,
                courseLabel, courseNode,
                semesterLabel, semesterNode,
                sgpaLabel, sgpaNode,
                checkNode,
                submitBtn,
                resultLabel
            );
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER_LEFT);

            // ==================== Window ====================

            stage.setTitle("Registration Form");
            stage.setScene(new Scene(root, 420, 520));
            stage.show();
        }
    }
}
