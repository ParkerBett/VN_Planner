# Visual Novel Planner Desktop v0.1

Usable Java Swing desktop application for planning Visual Novels on Windows and Linux. It works offline and stores projects locally as portable `.vnproj` files. JSON import/export is also supported.

## Current Status

Desktop v0.1 is a usable planner with persistent project files, dynamic planning editors, project search, a statistics dashboard, and manual backups.

## Build

Requires Java 11+ and Maven:

```bash
mvn clean package
```

On Windows PowerShell, run Maven from the project directory:

```powershell
Set-Location "C:\Projects\Java\VN_Planner"
mvn test
```

## Run

The Maven Shade plugin creates the executable shaded JAR:

```bash
java -jar target/vn-planner-0.1.0-shaded.jar
```

On Windows, if `java` reports a missing `jvm.cfg` under an old JDK path, use the installed JDK explicitly:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
& "$env:JAVA_HOME\bin\java.exe" -jar target\vn-planner-0.1.0-shaded.jar
```

## Features

- New/Open/Save/Save As (native `.vnproj`)
- Import / Export JSON
- Dashboard with counts for characters, chapters, scenes, locations, and choice nodes
- Project-wide search across project text, characters, chapters, scenes, and locations
- Character relationship editor and timeline editor
- Ten starter templates: Blank, Linear, Branching, Multi-Route, Mystery, Horror, Romance, Adventure, Dramatic / Character, and Experimental
- Separate Chapters and Scenes editors with linked chapter selection
- Locations editor with scene location assignment
- Choice Mind-Map with a protected Start node
- Dynamic add/remove for Characters and Chapters
- Branch and ending nodes, including protection against children under endings
- Manual timestamped backups of saved `.vnproj` files
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
