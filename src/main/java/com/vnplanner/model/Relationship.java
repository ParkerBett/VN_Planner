package com.vnplanner.model;

public class Relationship {
    private String fromCharacter = "";
    private String toCharacter = "";
    private String type = "";
    private String notes = "";

    public Relationship() {}

    public String getFromCharacter() { return fromCharacter; }
    public void setFromCharacter(String fromCharacter) { this.fromCharacter = fromCharacter; }
    public String getToCharacter() { return toCharacter; }
    public void setToCharacter(String toCharacter) { this.toCharacter = toCharacter; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return fromCharacter + " -> " + toCharacter + (type.trim().isEmpty() ? "" : " (" + type + ")");
    }
}
