package com.vnplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private String title = "";
    private String location = "";
    private String charactersPresent = "";
    private String purpose = "";
    private String events = "";
    private String dialogueNotes = "";
    private String choices = "";
    private String conditions = "";
    private String leadsTo = "";
    private String notes = "";
    private List<String> tags = new ArrayList<>();

    public Scene() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCharactersPresent() { return charactersPresent; }
    public void setCharactersPresent(String charactersPresent) { this.charactersPresent = charactersPresent; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getEvents() { return events; }
    public void setEvents(String events) { this.events = events; }
    public String getDialogueNotes() { return dialogueNotes; }
    public void setDialogueNotes(String dialogueNotes) { this.dialogueNotes = dialogueNotes; }
    public String getChoices() { return choices; }
    public void setChoices(String choices) { this.choices = choices; }
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    public String getLeadsTo() { return leadsTo; }
    public void setLeadsTo(String leadsTo) { this.leadsTo = leadsTo; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags == null ? new ArrayList<>() : tags; }

    public String toSummary() {
        if (title == null || title.trim().isEmpty()) {
            return "New Scene";
        }
        return title;
    }
}