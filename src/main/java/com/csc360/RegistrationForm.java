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
 * A simple registration form that demonstrates JavaFX and Swing working
 * together inside a single window.
 *
 * <p>The two toolkits are combined using {@link SwingNode}, a JavaFX node that
 * can display a Swing component. Each toolkit has its own UI thread, so the
 * code follows two rules:</p>
 *
 * <ul>
 *   <li>Swing components are created, read and changed only on the Swing
 *       <b>Event Dispatch Thread (EDT)</b>, using
 *       {@link SwingUtilities#invokeLater(Runnable)}.</li>
 *   <li>JavaFX controls are created, read and changed only on the
 *       <b>JavaFX Application Thread</b>, using
 *       {@link Platform#runLater(Runnable)} when called from another thread.</li>
 * </ul>
 *
 * <h2>Who provides what</h2>
 * <table>
 *   <caption>Components by toolkit</caption>
 *   <tr><th>Toolkit</th><th>Components</th></tr>
 *   <tr><td>JavaFX</td>
 *       <td>Stage (window), Label, TextField, Button, VBox layout</td></tr>
 *   <tr><td>Swing (via SwingNode)</td>
 *       <td>JComboBox, JRadioButton (x3), JSlider, JCheckBox</td></tr>
 * </table>
 *
 * <h2>Cross-thread flows</h2>
 * <ol>
 *   <li><b>Slider moved:</b> Swing EDT (change listener) to JavaFX thread
 *       (SGPA label update).</li>
 *   <li><b>Submit clicked:</b> JavaFX thread (read name) to Swing EDT (read
 *       Swing values and validate) to JavaFX thread (show result).</li>
 * </ol>
 *
 * <p>Run with {@code mvn javafx:run}, or directly from an IDE.</p>
 *
 * @author CSC 360 - Group 9
 * @version 1.0
 * @since 1.0
 * @see javafx.embed.swing.SwingNode
 * @see javafx.application.Application
 */
public class RegistrationForm {

    /**
     * Creates the launcher class. It holds only the {@code main} method and
     * has no state, so there is normally no reason to instantiate it.
     */
    public RegistrationForm() {
        // Nothing to initialise.
    }

    /**
     * Program entry point. Hands control to the JavaFX runtime, which starts
     * the JavaFX Application Thread and calls {@link App#start(Stage)}.
     *
     * @param args command-line arguments, passed on to JavaFX unchanged
     */
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

    /**
     * The JavaFX application class that builds and shows the registration
     * window.
     *
     * <p>It must be a separate {@code public static} class that extends
     * {@link Application}, so that {@link RegistrationForm#main(String[])}
     * can launch it with {@link Application#launch(Class, String...)}.</p>
     *
     * <p><b>Threading note:</b> the Swing fields below are assigned on the
     * Event Dispatch Thread and must only be read on that thread as well.
     * Every method that reads them says so in its documentation.</p>
     */
    public static class App extends Application {

        /**
         * Creates the application instance. JavaFX calls this constructor
         * itself when {@link Application#launch(Class, String...)} runs, so
         * application code never calls it directly.
         */
        public App() {
            // Nothing to initialise: the UI is built in start(Stage).
        }

        // Swing components. They are created and used on the Swing EDT only.

        /** Swing dropdown for choosing a course (Java, Python or C++). */
        private JComboBox<String> courseBox;

        /** Swing checkbox the user must tick to accept the terms. */
        private JCheckBox agreeBox;

        /** Swing radio button for the Fall semester (selected by default). */
        private JRadioButton fallRadio;

        /** Swing radio button for the Spring semester. */
        private JRadioButton springRadio;

        /** Swing radio button for the Summer semester. */
        private JRadioButton summerRadio;

        /**
         * Swing slider for SGPA. Its integer range is 0 to 40, which is
         * divided by 10 to give a value from 0.0 to 4.0 in steps of 0.1.
         */
        private JSlider sgpaSlider;

        /**
         * Converts an SGPA on a 4.0 scale to a letter grade.
         *
         * <table>
         *   <caption>Grade boundaries</caption>
         *   <tr><th>SGPA</th><th>Grade</th></tr>
         *   <tr><td>3.7 and above</td><td>A</td></tr>
         *   <tr><td>3.3 to below 3.7</td><td>B+</td></tr>
         *   <tr><td>3.0 to below 3.3</td><td>B</td></tr>
         *   <tr><td>2.7 to below 3.0</td><td>B-</td></tr>
         *   <tr><td>2.3 to below 2.7</td><td>C+</td></tr>
         *   <tr><td>2.0 to below 2.3</td><td>C</td></tr>
         *   <tr><td>1.0 to below 2.0</td><td>D</td></tr>
         *   <tr><td>below 1.0</td><td>F</td></tr>
         * </table>
         *
         * @param gpa the SGPA, expected between 0.0 and 4.0
         * @return the matching letter grade, for example {@code "B+"}
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
         * Reads which semester radio button is selected.
         *
         * <p><b>Thread:</b> must be called on the Swing Event Dispatch Thread,
         * because it reads Swing components.</p>
         *
         * @return {@code "Fall"}, {@code "Spring"} or {@code "Summer"};
         *         {@code "Fall"} is the default if neither of the other two
         *         is selected
         */
        private String getSelectedSemester() {
            String semester = "Fall";
            if (springRadio.isSelected()) semester = "Spring";
            if (summerRadio.isSelected()) semester = "Summer";
            return semester;
        }

        /**
         * Checks the submitted form values in order and reports the first
         * problem found.
         *
         * <p>The checks run in this order:</p>
         * <ol>
         *   <li>name is empty</li>
         *   <li>name contains a digit</li>
         *   <li>name contains anything other than letters and spaces</li>
         *   <li>no course is selected</li>
         *   <li>terms are not accepted</li>
         * </ol>
         *
         * <p>This method does not touch any UI component, so it can be called
         * from any thread.</p>
         *
         * @param name   the trimmed text from the name field
         * @param course the selected course, or {@code null} if none
         * @param agreed {@code true} if the terms checkbox is ticked
         * @return the error message for the first failed check, or
         *         {@code null} if every check passed
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
         * Builds the user interface and shows the window. Called by JavaFX on
         * the JavaFX Application Thread.
         *
         * <p>This method does the following:</p>
         * <ol>
         *   <li>Creates the JavaFX controls (title, name field, submit button,
         *       result label).</li>
         *   <li>Creates each Swing component on the EDT and wraps it in a
         *       {@link SwingNode}.</li>
         *   <li>Connects the slider so that moving it updates a JavaFX label
         *       (Swing to JavaFX).</li>
         *   <li>Connects the submit button so that it reads the Swing values,
         *       validates them, and shows the result (JavaFX to Swing and back
         *       to JavaFX).</li>
         *   <li>Adds everything to a {@link VBox} and shows the stage.</li>
         * </ol>
         *
         * <p><b>Validation:</b> see {@link #validate(String, String, boolean)}.
         * The first failed check is shown in red; if all pass, a green summary
         * is shown.</p>
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