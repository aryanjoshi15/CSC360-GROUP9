# Registration Form — JavaFX + Swing Demo

![Java Version](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-27-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-red?logo=apache-maven)
![Course](https://img.shields.io/badge/Course-CSC%20360-informational)

A simple desktop registration form built for **CSC 360** that demonstrates JavaFX and Swing components working together in a single window using `SwingNode`.

---

## Overview

This project creates one JavaFX window containing both **JavaFX** and **Swing** UI components. Swing components are embedded inside the JavaFX scene using `SwingNode`, and proper threading is used throughout:

- **Swing components** are created on the Event Dispatch Thread (`SwingUtilities.invokeLater`)
- **JavaFX controls** are updated on the JavaFX Application Thread (`Platform.runLater`)

### Key Features
- **Real-Time Input Filtering**: The student name field automatically blocks numbers and special characters as the user types using a JavaFX `TextFormatter`.
- **Form Validation**: Comprehensive checks ensure all fields are properly completed and mandatory terms are accepted before submission.
- **SGPA & 4.0 Grading**: A Swing `JSlider` allows selecting an SGPA from `0.0` to `4.0` (in `0.1` increments), with real-time conversion to letter grades (`A`, `B+`, `B`, `B-`, `C+`, `C`, `D`, `F`) dynamically displayed on a JavaFX label.
- **Dynamic Visual Feedback**: Instant visual confirmation with contextual color coding for error states (crimson) and success summaries (forest green).

---

## Input Validation & Feedback Rules

The application implements a two-tier validation mechanism to safeguard input integrity:

### 1. Real-Time Input Filtering (JavaFX `TextFormatter`)
The `TextField` for the student's name is attached to a filter that only permits alphabetic characters and whitespace:
```java
nameField.setTextFormatter(new TextFormatter<>(change -> {
    if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
        return change;
    }
    return null; // Rejects keystroke immediately
}));
```
Any attempts to enter numeric digits, punctuation, or special symbols are discarded before reaching the input buffer.

### 2. Submit-Time Validation Checklist
Upon clicking the **Submit** button, the following validations execute sequentially:

| Check | Target Component | Validation Logic | Error Message |
|-------|------------------|------------------|---------------|
| **Name Presence** | `nameField` (JavaFX) | `name.isEmpty()` | `"Please enter your name."` |
| **Digit Guard** | `nameField` (JavaFX) | `name.chars().anyMatch(Character::isDigit)` | `"Name must not contain numbers."` |
| **Character Set** | `nameField` (JavaFX) | `!name.matches("[a-zA-Z\\s]+")` | `"Name can only contain letters and spaces."` |
| **Course Selection** | `courseBox` (Swing) | `course == null || course.isEmpty()` | `"Please select a course."` |
| **Terms Agreement** | `agreeBox` (Swing) | `!agreed` | `"You must agree to the terms before submitting."` |

### 3. Visual Feedback States
- **Validation Failure**: The result label displays in red (`#d32f2f`) with a `❌` indicator and the specific failure reason.
- **Validation Success**: The result label displays in green (`#2e7d32`) with a `✔ Registered successfully!` banner and a consolidated summary of all entered details (Name, Course, Semester, SGPA, and Letter Grade).

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
