# Visual Novel Planner (Java Swing)

Simple desktop Visual Novel planning tool written in Java (Swing). Projects are saved as a `.vnproj` ZIP file containing `project.json`. JSON import/export is also supported.

Build (requires Java 11+ and Maven):

```bash
mvn package
```

Run:

```bash
java -jar target/vn-planner-0.1.0.jar
```

Features:
- New/Open/Save/Save As (native `.vnproj`)
- Import / Export JSON
- Tabs for Project, Story, Protagonist, Characters, Chapters, Free Notes
- Dynamic add/remove for Characters and Chapters

Project format:
- `.vnproj` is a ZIP archive containing `project.json` (portable across OSes)

Notes:
- This is a minimal, maintainable starting point. You can extend fields and UI as needed.
