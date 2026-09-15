package com.vnplanner.model;

public class Chapter {
    private String title = "";
    private String purpose = "";
    private String majorEvents = "";
    private String choices = "";
    private String endingLead = "";

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
}
