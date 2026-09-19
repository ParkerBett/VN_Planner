package com.vnplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Chapter {
    private String title = "";
    private String purpose = "";
    private String majorEvents = "";
    private String choices = "";
    private String endingLead = "";
    private List<Scene> scenes = new ArrayList<>();

    public Chapter() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getMajorEvents() { return majorEvents; }
    public void setMajorEvents(String majorEvents) { this.majorEvents = majorEvents; }
    public String getChoices() { return choices; }
    public void setChoices(String choices) { this.choices = choices; }
    public String getEndingLead() { return endingLead; }
    public void setEndingLead(String endingLead) { this.endingLead = endingLead; }
    public List<Scene> getScenes() { return scenes; }
    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes == null ? new ArrayList<>() : scenes;
    }

    @Override
    public String toString() {
        return title == null || title.trim().isEmpty() ? "Untitled Chapter" : title;
    }
}
