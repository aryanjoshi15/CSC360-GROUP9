package com.csc360.group9.app;

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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainApp extends Application {

    private Label javaFxLabel;
    private JTextField swingTextField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Two-way JavaFX + Swing Communication");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // JavaFX Components
        TextField javaFxTextField = new TextField();
        javaFxTextField.setPromptText("Type something for Swing...");
        
        Button javaFxButton = new Button("Send to Swing");
        
        javaFxLabel = new Label("Waiting for message from Swing...");

        // JavaFX Button Action
        javaFxButton.setOnAction(e -> {
            String text = javaFxTextField.getText();
            // Important: Update Swing components on the Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                if (swingTextField != null) {
                    swingTextField.setText(text);
                }
            });
        });

        // SwingNode setup
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode);

        root.getChildren().addAll(javaFxTextField, javaFxButton, javaFxLabel, swingNode);

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createSwingContent(final SwingNode swingNode) {
        // Important: Create and manipulate Swing components on the EDT
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel();
            
            swingTextField = new JTextField(15);
            JButton swingButton = new JButton("Send to JavaFX");
            
            swingButton.addActionListener(e -> {
                String text = swingTextField.getText();
                // Important: Update JavaFX components on the JavaFX Application Thread
                Platform.runLater(() -> {
                    if (javaFxLabel != null) {
                        javaFxLabel.setText("Message from Swing: " + text);
                    }
                });
            });
            
            panel.add(swingTextField);
            panel.add(swingButton);
            
            swingNode.setContent(panel);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
