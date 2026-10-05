/**
 * A registration-form demo that shows JavaFX and Swing components working
 * together in one window.
 *
 * <p>Start with {@link com.csc360.RegistrationForm}.</p>
 *
 * <h2>Who provides what</h2>
 * <table>
 *   <caption>Components by toolkit</caption>
 *   <tr><th>Toolkit</th><th>Components</th></tr>
 *   <tr><td>JavaFX</td>
 *       <td>Stage (window), Label, TextField, Button, VBox layout</td></tr>
 *   <tr><td>Swing (via {@link javafx.embed.swing.SwingNode})</td>
 *       <td>JComboBox, JRadioButton (x3), JSlider, JCheckBox</td></tr>
 * </table>
 *
 * <h2>Threading rules</h2>
 * <p>Each toolkit has its own UI thread, and a component may only be touched
 * on the thread of the toolkit it belongs to.</p>
 * <ul>
 *   <li>Swing components are created, read and changed on the
 *       <b>Event Dispatch Thread (EDT)</b>. To get there from the JavaFX
 *       thread, use {@link javax.swing.SwingUtilities#invokeLater(Runnable)}.</li>
 *   <li>JavaFX controls are created, read and changed on the
 *       <b>JavaFX Application Thread</b>. To get there from the EDT, use
 *       {@link javafx.application.Platform#runLater(Runnable)}.</li>
 * </ul>
 *
 * <h2>Cross-thread flows</h2>
 * <ol>
 *   <li><b>Slider moved:</b> the Swing change listener (EDT) calculates the
 *       SGPA and grade, then updates the JavaFX label through
 *       {@code Platform.runLater}.</li>
 *   <li><b>Submit clicked:</b> the JavaFX thread reads the name, hands off to
 *       the EDT to read the Swing values and validate, then hands back to the
 *       JavaFX thread to show a red error or a green summary.</li>
 * </ol>
 *
 * <h2>Validation order</h2>
 * <ol>
 *   <li>Name is empty</li>
 *   <li>Name contains a digit</li>
 *   <li>Name contains anything other than letters and spaces</li>
 *   <li>No course selected</li>
 *   <li>Terms not accepted</li>
 * </ol>
 * <p>The first failed check is reported; if none fail, a success summary is
 * shown.</p>
 *
 * <h2>Grading scale (4.0)</h2>
 * <table>
 *   <caption>SGPA to letter grade</caption>
 *   <tr><th>SGPA</th><th>Grade</th></tr>
 *   <tr><td>3.7 - 4.0</td><td>A</td></tr>
 *   <tr><td>3.3 - 3.6</td><td>B+</td></tr>
 *   <tr><td>3.0 - 3.2</td><td>B</td></tr>
 *   <tr><td>2.7 - 2.9</td><td>B-</td></tr>
 *   <tr><td>2.3 - 2.6</td><td>C+</td></tr>
 *   <tr><td>2.0 - 2.2</td><td>C</td></tr>
 *   <tr><td>1.0 - 1.9</td><td>D</td></tr>
 *   <tr><td>0.0 - 0.9</td><td>F</td></tr>
 * </table>
 *
 * <p>Run with {@code mvn javafx:run}.</p>
 */
package com.csc360;