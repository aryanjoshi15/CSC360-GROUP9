package com.csc360.group9.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MainApp extends Application {

    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minimal JavaFX + Swing Demo");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        statusLabel = new Label("Click a button below.");
        
        Button javaFxButton = new Button("I am a JavaFX Button");
        javaFxButton.setOnAction(e -> statusLabel.setText("JavaFX Button clicked!"));

        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode);

        root.getChildren().addAll(statusLabel, javaFxButton, swingNode);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            JButton swingButton = new JButton("I am a Swing JButton");
            swingButton.addActionListener(e -> {
                // When clicked in the Swing EDT thread, update the JavaFX label 
                // back on the JavaFX Application Thread.
                Platform.runLater(() -> statusLabel.setText("Swing Button clicked!"));
            });
            swingNode.setContent(swingButton);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
