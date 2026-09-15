# JavaFX + Swing Desktop Application

A desktop application built with **JavaFX** as the primary UI framework, with legacy
**Swing** components embedded inside it using `SwingNode`. The goal is to show that
both toolkits can live in one window and share the same application state.

## Team

| Name | Roll No. |
|------|----------|
| _[Jevis Maniyar](https://github.com/Quack-Duck12)_ | AU2520311 |
| _[Aryan Joshi](https://github.com/aryanjoshi15)_ | AU2500017 |
| _[name]_ | |

## Tech stack

- **Java 21**
- **JavaFX 21**: `javafx-controls`, `javafx-fxml`, `javafx-swing`
- **Swing**: part of the JDK, embedded via `SwingNode`
- **Maven**: build and dependency management tool
- **Git**: version control

## Running it

```bash
git clone --depth=1 https://github.com/aryanjoshi15/CSC360-GROUP9.git
cd CSC360-GROUP9
mvn clean javafx:run
```
Requires JDK 21+ and Maven on the PATH. No separate JavaFX SDK install is needed - Maven
pulls the platform-specific artifacts.

## Project structure

```
javafx-swing-app/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/example/app/
    │   ├── MainApp.java        # entry point, builds the Stage
    │   ├── ui/                 # JavaFX views and layouts
    │   ├── swing/              # Swing panels + SwingNode bridge
    │   ├── controller/         # event handling, wiring
    │   └── model/              # data classes, business logic
    └── resources/com/example/app/
        ├── css/                # stylesheets
        ├── fxml/               # FXML layouts
        └── images/             # icons, assets
```

## How the window is put together

JavaFX uses a three-level hierarchy, and we follow it strictly:

1. **Stage** - the actual OS window. Created once in `MainApp.start()`. Owns the
   title, size, minimum size, icon and close behaviour.
2. **Scene** - the container for everything drawn inside the window. Holds the
   root node and the attached stylesheet.
3. **Scene graph** - the tree of nodes (`BorderPane` → toolbars, panels, controls).
   Swing components enter this tree wrapped in a `SwingNode`.

> **Threading rule:** JavaFX code runs on the JavaFX Application Thread; Swing code
> runs on the Event Dispatch Thread. Always build Swing content inside
> `SwingUtilities.invokeLater(...)` and push updates back with `Platform.runLater(...)`.
> Mixing these up is the single most common bug in this kind of project.

## Development stages

### Stage 1 - Setup (done)
Repository created, Maven project configured, package structure in place, `MainApp`
launches an empty `Stage` with a title and fixed minimum size. Everyone can build
and run the project locally.

### Stage 2 - Layout skeleton
Build the main `BorderPane` shell: menu bar on top, navigation on the left, content
area in the centre, status bar at the bottom. All regions are placeholders. No
business logic yet.

### Stage 3 - JavaFX components
Fill the content area with real JavaFX controls - forms, tables, buttons, dialogs.
Apply the stylesheet so the look is consistent across screens.

### Stage 4 - Swing integration
Create the Swing panels and embed them with `SwingNode`. Verify the threading rule
holds: JavaFX buttons can update Swing panels and the other way round without
freezing the UI.

### Stage 5 - Model and wiring
Add the data classes and connect controllers to them, so both the JavaFX and Swing
sides read from and write to the same state.

### Stage 6 - Polish and testing
Input validation, error dialogs, window icon, keyboard shortcuts, resizing checks.
Test on each member's machine (Windows / Linux / macOS as applicable).

### Stage 7 - Submission
Finalise this README, add screenshots, write the report, and tag the release.