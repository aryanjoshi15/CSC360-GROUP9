# Registration Form — JavaFX + Swing Demo

A simple desktop registration form built for **CSC 360** that demonstrates JavaFX and Swing components working together in a single window using `SwingNode`.

---

## Overview

This project creates one JavaFX window containing both **JavaFX** and **Swing** UI components. Swing components are embedded inside the JavaFX scene using `SwingNode`, and proper threading is used throughout:

- **Swing components** are created on the Event Dispatch Thread (`SwingUtilities.invokeLater`)
- **JavaFX controls** are updated on the JavaFX Application Thread (`Platform.runLater`)

### Key Features
- **Form Validation**: Submitting only proceeds if all fields are filled (Name provided, Course picked) and the user has agreed to the terms.
- **SGPA & 4.0 Grading**: A Swing `JSlider` allows selecting an SGPA from `0.0` to `4.0` (in `0.1` increments), with real-time conversion to letter grades (`A`, `B+`, `B`, `B-`, `C+`, `C`, `D`, `F`) dynamically displayed on a JavaFX label.

---

## UI Components

| Component              | Toolkit    | Purpose                                              |
|------------------------|------------|------------------------------------------------------|
| `Label`                | JavaFX     | Title — "Registration Form"                          |
| `TextField`            | JavaFX     | Text input for the student's name (required)         |
| `JComboBox`            | Swing      | Dropdown to pick a course (Java, Python, C++)        |
| `JRadioButton` (×3)    | Swing      | Radio buttons to select a semester (Fall/Spring/Summer)|
| `JSlider`              | Swing      | SGPA slider (`0.0` – `4.0`) with grade evaluation    |
| `JCheckBox`            | Swing      | Checkbox — "I agree to the terms" (required)         |
| `Button`               | JavaFX     | Submit button (validates and displays output)        |
| `Label`                | JavaFX     | Result label (shows error or success summary)        |

---

## Project Structure

```
CSC360-GROUP9/
├── pom.xml                                        # Maven build config
├── README.md                                      # Documentation
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

### Option 1: Terminal
```bash
mvn javafx:run
```

### Option 2: IDE "Run / Debug"
You can directly click **Run** or **Debug** in your IDE (IntelliJ IDEA, VS Code, Antigravity IDE, NetBeans) thanks to the built-in launcher pattern and `exec-maven-plugin` configuration.

---

## Grading Scale (Out of 4.0)

| SGPA Range  | Letter Grade |
|-------------|--------------|
| 3.7 – 4.0   | **A**        |
| 3.3 – 3.6   | **B+**       |
| 3.0 – 3.2   | **B**        |
| 2.7 – 2.9   | **B-**       |
| 2.3 – 2.6   | **C+**       |
| 2.0 – 2.2   | **C**        |
| 1.0 – 1.9   | **D**        |
| 0.0 – 0.9   | **F**        |

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
│                                 │     SGPA, agreement
└─────────────────────────────────┘          │
                                             ▼
                                   Platform.runLater()
                                             │
                                             ▼
                                   JavaFX result label updated
                                   (validates inputs & shows status)
```

---

## Authors

**CSC 360 — Group 9**
