package com.csc360.group9.app;

import com.csc360.group9.app.controller.MainController;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;

public class MainApp {
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel windowSizeLabel;
    private JFXPanel fxPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.createSwingGUI();
            app.createJavaFXGUI();
        });
    }

    private void createSwingGUI() {
        frame = new JFrame("Swing + JavaFX + FXML");
        frame.setSize(1100, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        viewMenu.add(refreshItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new java.awt.Color(35, 38, 45));

        JLabel title = new JLabel("  My Application");
        title.setForeground(java.awt.Color.WHITE);
        title.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));
        title.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.add(title, BorderLayout.NORTH);

        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));

        JButton dashboardButton = createSidebarButton("Dashboard");
        JButton usersButton = createSidebarButton("Users");
        JButton settingsButton = createSidebarButton("Settings");
        JButton aboutButton = createSidebarButton("About");

        navigation.add(dashboardButton);
        navigation.add(usersButton);
        navigation.add(settingsButton);
        navigation.add(aboutButton);
        sidebar.add(navigation, BorderLayout.CENTER);

        dashboardButton.addActionListener(e -> updateStatus("Dashboard selected"));
        usersButton.addActionListener(e -> updateStatus("Users selected"));
        settingsButton.addActionListener(e -> updateStatus("Settings selected"));
        aboutButton.addActionListener(e ->
            JOptionPane.showMessageDialog(frame, "Swing + JavaFX + FXML\nCSC360", "About",
                    JOptionPane.INFORMATION_MESSAGE));

        frame.add(sidebar, BorderLayout.WEST);

        fxPanel = new JFXPanel();
        frame.add(fxPanel, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(new java.awt.Color(240, 240, 240));

        statusLabel = new JLabel("Ready");
        windowSizeLabel = new JLabel();

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(windowSizeLabel, BorderLayout.EAST);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { updateWindowSize(); }
            @Override public void componentMoved(ComponentEvent e) { updateStatus("Window moved"); }
            @Override public void componentShown(ComponentEvent e) { updateStatus("Window shown"); }
            @Override public void componentHidden(ComponentEvent e) { updateStatus("Window hidden"); }
        });

        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowGainedFocus(WindowEvent e) { updateStatus("Window gained focus"); }
            @Override public void windowLostFocus(WindowEvent e) { updateStatus("Window lost focus"); }
        });

        newItem.addActionListener(e -> updateStatus("New selected"));
        refreshItem.addActionListener(e -> updateStatus("Refresh selected"));
        aboutItem.addActionListener(e ->
            JOptionPane.showMessageDialog(frame, "Swing + JavaFX + FXML\nVersion 1.0",
                    "About", JOptionPane.INFORMATION_MESSAGE));
        exitItem.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
        updateWindowSize();
    }

    private void createJavaFXGUI() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        MainApp.class.getResource("/com/csc360/group9/app/fxml/main.fxml"));

                Parent root = loader.load();

                Scene scene = new Scene(root);

                scene.getStylesheets().add(
                        MainApp.class.getResource("/com/csc360/group9/app/css/style.css").toExternalForm());

                fxPanel.setScene(scene);

                MainController controller = loader.getController();
                controller.setMainApplication(this);

            } catch (java.io.IOException e) {
                java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, "Failed to load FXML", e);
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(frame,
                            "Failed to load FXML:\n" + e.getMessage(),
                            "FXML Error", JOptionPane.ERROR_MESSAGE));
            }
        });
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFocusPainted(false);
        button.setForeground(java.awt.Color.WHITE);
        button.setBackground(new java.awt.Color(50, 54, 63));
        button.setBorder(new EmptyBorder(10, 20, 10, 10));
        return button;
    }

    public void updateStatus(String message) {
        if (statusLabel != null) statusLabel.setText(message);
    }

    public void updateSwingStatus(String message) {
        SwingUtilities.invokeLater(() -> updateStatus(message));
    }

    private void updateWindowSize() {
        if (frame != null && windowSizeLabel != null) {
            windowSizeLabel.setText(frame.getWidth() + " × " + frame.getHeight());
        }
    }
}
