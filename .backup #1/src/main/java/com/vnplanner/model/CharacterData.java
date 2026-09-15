package com.vnplanner.model;

public class CharacterData {
    private String name = "";
    private String role = "";
    private String personality = "";
    private String want = "";
    private String fear = "";
    private String relationToProtagonist = "";
    private String arc = "";

    public CharacterData() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }
    public String getWant() { return want; }
    public void setWant(String want) { this.want = want; }
    public String getFear() { return fear; }
    public void setFear(String fear) { this.fear = fear; }
    public String getRelationToProtagonist() { return relationToProtagonist; }
    public void setRelationToProtagonist(String relationToProtagonist) { this.relationToProtagonist = relationToProtagonist; }
    public String getArc() { return arc; }
    public void setArc(String arc) { this.arc = arc; }
}
