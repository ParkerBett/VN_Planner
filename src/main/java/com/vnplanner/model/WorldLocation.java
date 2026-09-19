package com.vnplanner.model;

public class WorldLocation {
    private String name = "";
    private String type = "";
    private String description = "";
    private String chapterName = "";
    private String sceneName = "";
    private String notes = "";

    public WorldLocation() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getChapterName() { return chapterName; }
    public void setChapterName(String chapterName) { this.chapterName = chapterName; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
