# Visual Novel Planner Desktop v0.1

Usable Java Swing desktop application for planning Visual Novels on Windows and Linux. It works offline and stores projects locally as portable `.vnproj` files. JSON import/export is also supported.

## Current Status

Desktop v0.1 is complete as a basic planner. It includes the requested planner sections, persistent project files, dynamic Characters and Chapters lists, and a branching Choices & Endings editor.

## Build

Requires Java 11+ and Maven:

```bash
mvn clean package
```

## Run

The Maven Shade plugin creates the executable shaded JAR:

```bash
java -jar target/vn-planner-0.1.0-shaded.jar
```

## Features

- New/Open/Save/Save As (native `.vnproj`)
- Import / Export JSON
- Ten planner sections: Project, Story, Protagonist, Characters, Chapters, Choices & Endings, World & Lore, Presentation, Development, and Free Notes
- Dynamic add/remove for Characters and Chapters
- Choices & Endings mind-map with a protected Start node
- Branch and ending nodes, including protection against children under endings
- Autosave every 30 seconds to the current project or a temporary recovery file when no project has been saved yet
- Startup loading screen using `images/loading_screen.png`
- Application icon using `images/icon.png`

## Project Format

- `.vnproj` is a ZIP archive containing `project.json` (portable across OSes)
- The JSON schema is shared with the Android client
- Newlines, Unicode, quotes, and other normal JSON text are supported

## Notes

- The Story tab intentionally does not display the legacy `What Changes` field, but the field remains in the data model for compatibility with older project files.
- The app has no server, account requirement, browser storage, or GitHub Pages dependency.
