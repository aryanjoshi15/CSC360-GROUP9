# Registration Form — JavaFX + Swing Demo

A simple desktop registration form built for **CSC 360** that demonstrates JavaFX and Swing components working together in a single window using `SwingNode`.

---

## Overview

This project creates one JavaFX window containing both **JavaFX** and **Swing** UI components. Swing components are embedded inside the JavaFX scene using `SwingNode`, and proper threading is used throughout:

- **Swing components** are created on the Event Dispatch Thread (`SwingUtilities.invokeLater`)
- **JavaFX controls** are updated on the JavaFX Application Thread (`Platform.runLater`)

When the user fills in the form and clicks **Submit**, all values (from both toolkits) are collected and displayed in a summary label.

---

## UI Components

| Component              | Toolkit    | Purpose                                      |
|------------------------|------------|----------------------------------------------|
| `Label`                | JavaFX     | Title — "Registration Form"                  |
| `TextField`            | JavaFX     | Text input for the student's name            |
| `JComboBox`            | Swing      | Dropdown to pick a course (Java, Python, C++)|
| `JRadioButton` (×3)    | Swing      | Radio buttons to select a semester           |
| `JSlider`              | Swing      | Slider (1–10) to rate experience             |
| `JCheckBox`            | Swing      | Checkbox — "I agree to the terms"            |
| `Button`               | JavaFX     | Submit button to display the summary         |
| `Label`                | JavaFX     | Result label showing collected form data     |

---

## Project Structure

```
CSC360-GROUP9/
├── pom.xml                                        # Maven build config
└── src/
    └── main/
        └── java/
            └── com/
                └── csc360/
                    └── RegistrationForm.java      # Single-file application
```

Everything lives in one file — no CSS, no FXML, no extra windows.

---

## Prerequisites

- **Java 17+** (tested with Java 26)
- **Maven 3.8+**

---

## How to Run

```bash
mvn javafx:run
```

Maven will download the required JavaFX libraries automatically on first run.

---

## Dependencies

Defined in `pom.xml`:

| Dependency             | Version | Purpose                           |
|------------------------|---------|-----------------------------------|
| `javafx-controls`      | 21.0.2  | Core JavaFX UI controls           |
| `javafx-swing`         | 21.0.2  | `SwingNode` for embedding Swing   |

---

## Threading Model

```
┌─────────────────────────────────┐
│   JavaFX Application Thread     │
│                                 │
│  • TextField, Button, Labels    │
│  • Button click handler starts  │──── reads name ────┐
│                                 │                    │
└─────────────────────────────────┘                    │
                                                       ▼
┌─────────────────────────────────┐     SwingUtilities.invokeLater()
│   Swing EDT (Event Dispatch     │
│          Thread)                │
│                                 │
│  • JComboBox, JCheckBox         │
│  • JRadioButtons, JSlider       │──── reads course, semester,
│                                 │     rating, agreed
└─────────────────────────────────┘          │
                                             ▼
                                   Platform.runLater()
                                             │
                                             ▼
                                   JavaFX result label updated
```

---

## Authors

**CSC 360 — Group 9**
