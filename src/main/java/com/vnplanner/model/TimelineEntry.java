package com.vnplanner.model;

public class TimelineEntry {
    private String label = "";
    private String date = "";
    private String description = "";
    private String chapter = "";

    public TimelineEntry() {}

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }

    @Override
    public String toString() {
        return (date == null || date.trim().isEmpty() ? "Undated" : date) + " - " + label;
    }
}
