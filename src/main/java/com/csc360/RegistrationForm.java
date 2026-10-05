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
 * Registration form demo: a JavaFX window with Swing components embedded
 * through {@link SwingNode}.
 *
 * <p>For the full overview, threading rules and grade table, see the
 * {@linkplain com.csc360 package documentation}.</p>
 *
 * @author CSC 360 - Group 9
 * @version 1.0
 */
public class RegistrationForm {

    /** Creates the launcher class; it only holds {@link #main(String[])}. */
    public RegistrationForm() {
        // Nothing to initialise.
    }

    /**
     * Program entry point. Launches the JavaFX application.
     *
     * @param args command-line arguments, passed on to JavaFX
     */
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

    /**
     * JavaFX application that builds and shows the registration window.
     * The Swing fields are created and read on the Swing EDT only.
     */
    public static class App extends Application {

        /** Creates the application; JavaFX calls this itself on launch. */
        public App() {
            // Nothing to initialise: the UI is built in start(Stage).
        }

        // Swing components. They are created and used on the Swing EDT only.

        /** Swing dropdown for choosing a course. */
        private JComboBox<String> courseBox;

        /** Swing checkbox for accepting the terms. */
        private JCheckBox agreeBox;

        /** Swing radio button for the Fall semester (default). */
        private JRadioButton fallRadio;

        /** Swing radio button for the Spring semester. */
        private JRadioButton springRadio;

        /** Swing radio button for the Summer semester. */
        private JRadioButton summerRadio;

        /** Swing slider for SGPA; range 0 to 40, divided by 10 for 0.0 to 4.0. */
        private JSlider sgpaSlider;

        /**
         * Converts an SGPA (4.0 scale) to a letter grade.
         *
         * @param gpa SGPA between 0.0 and 4.0
         * @return letter grade, for example {@code "B+"}
         */
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

        /**
         * Returns the selected semester. Call on the Swing EDT.
         *
         * @return {@code "Fall"}, {@code "Spring"} or {@code "Summer"}
         */
        private String getSelectedSemester() {
            String semester = "Fall";
            if (springRadio.isSelected()) semester = "Spring";
            if (summerRadio.isSelected()) semester = "Summer";
            return semester;
        }

        /**
         * Validates the form values and returns the first error found.
         * Does not touch the UI, so it is safe on any thread.
         *
         * @param name   trimmed text from the name field
         * @param course selected course, or {@code null} if none
         * @param agreed whether the terms checkbox is ticked
         * @return an error message, or {@code null} if every check passes
         */
        private String validate(String name, String course, boolean agreed) {
            if (name.isEmpty()) {
                return "Please enter your name.";
            } else if (name.chars().anyMatch(Character::isDigit)) {
                return "Name must not contain numbers.";
            } else if (!name.matches("[a-zA-Z\\s]+")) {
                return "Name can only contain letters and spaces.";
            } else if (course == null || course.isEmpty()) {
                return "Please select a course.";
            } else if (!agreed) {
                return "You must agree to the terms before submitting.";
            }
            return null;
        }

        /**
         * Builds the UI and shows the window. Runs on the JavaFX Application
         * Thread.
         *
         * @param stage the primary window supplied by JavaFX
         */
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

            // ==================== Swing Components ====================

            // 1) Course dropdown (JComboBox)
            // The SwingNode is a JavaFX object, so it is created here on the
            // JavaFX thread. The JComboBox is a Swing object, so it is created
            // on the EDT and then placed inside the node.
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

                // Live-update the JavaFX label as slider moves and repaint to prevent visual artifacts.
                // This listener runs on the EDT. It may read the slider, but it must
                // use Platform.runLater to change the JavaFX label.
                sgpaSlider.addChangeListener(e -> {
                    double gpa = sgpaSlider.getValue() / 10.0;
                    String grade = getGrade(gpa);
                    Platform.runLater(() ->
                        sgpaLabel.setText(String.format("SGPA: %.1f / 4.0 (Grade: %s)", gpa, grade))
                    );
                    SwingUtilities.invokeLater(() -> sgpaSlider.repaint());
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

            // Submit flow: JavaFX thread (read name) -> Swing EDT (read Swing
            // values and validate) -> JavaFX thread (show the result).
            Button submitBtn = new Button("Submit");
            submitBtn.setOnAction(e -> {
                // Read JavaFX name field
                String name = nameField.getText().trim();

                // Read Swing values on the EDT
                SwingUtilities.invokeLater(() -> {
                    String course   = (String) courseBox.getSelectedItem();
                    boolean agreed  = agreeBox.isSelected();
                    double gpa      = sgpaSlider.getValue() / 10.0;
                    String grade    = getGrade(gpa);
                    String semester = getSelectedSemester();

                    // Validation: null means every check passed
                    String error = validate(name, course, agreed);

                    // Update JavaFX UI (back on the JavaFX thread)
                    Platform.runLater(() -> {
                        if (error != null) {
                            // Show error in red
                            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #d32f2f;");
                            resultLabel.setText("❌ " + error);
                        } else {
                            // Show success summary in green
                            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2e7d32;");
                            resultLabel.setText(
                                "✔ Registered successfully!\n"
                                + "Name: " + name
                                + ", Course: " + course
                                + ", Semester: " + semester
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