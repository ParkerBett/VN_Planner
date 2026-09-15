package com.vnplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    // Project section
    private String title = "";
    private String workingTitle = "";
    private String oneSentencePitch = "";
    private String coreIdea = "";
    private String genreTone = "";
    private String playerFeel = "";

    // Story
    private String centralConflict = "";
    private String beginning = "";
    private String middle = "";
    private String revelations = "";
    private String climax = "";
    private String ending = "";
    private String whatChanges = "";

    // Protagonist
    private String protagonistName = "";
    private String protagonistAge = "";
    private String protagonistPersonality = "";
    private String protagonistWant = "";
    private String protagonistNeed = "";
    private String protagonistFear = "";
    private String protagonistArc = "";

    // Characters and Chapters
    private List<CharacterData> characters = new ArrayList<>();
    private List<Chapter> chapters = new ArrayList<>();

    // Choices & Endings
    private String amountOfChoice = "";
    private String importantChoices = "";
    private String branches = "";
    private String differentEndings = "";
    private String endingRequirements = "";

    // World & Lore
    private String setting = "";
    private String importantLocations = "";
    private String worldRules = "";
    private String loreHistory = "";
    private String secrets = "";

    // Presentation
    private String visualStyle = "";
    private String musicAudio = "";
    private String uiPresentation = "";
    private String inspirations = "";

    // Development
    private String engineTools = "";
    private String mustHave = "";
    private String niceToHave = "";
    private String scopeLimits = "";

    // Free notes
    private String freeNotes = "";

    public Project() {}

    // Getters and setters (generated minimally)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getWorkingTitle() { return workingTitle; }
    public void setWorkingTitle(String workingTitle) { this.workingTitle = workingTitle; }
    public String getOneSentencePitch() { return oneSentencePitch; }
    public void setOneSentencePitch(String oneSentencePitch) { this.oneSentencePitch = oneSentencePitch; }
    public String getCoreIdea() { return coreIdea; }
    public void setCoreIdea(String coreIdea) { this.coreIdea = coreIdea; }
    public String getGenreTone() { return genreTone; }
    public void setGenreTone(String genreTone) { this.genreTone = genreTone; }
    public String getPlayerFeel() { return playerFeel; }
    public void setPlayerFeel(String playerFeel) { this.playerFeel = playerFeel; }

    public String getCentralConflict() { return centralConflict; }
    public void setCentralConflict(String centralConflict) { this.centralConflict = centralConflict; }
    public String getBeginning() { return beginning; }
    public void setBeginning(String beginning) { this.beginning = beginning; }
    public String getMiddle() { return middle; }
    public void setMiddle(String middle) { this.middle = middle; }
    public String getRevelations() { return revelations; }
    public void setRevelations(String revelations) { this.revelations = revelations; }
    public String getClimax() { return climax; }
    public void setClimax(String climax) { this.climax = climax; }
    public String getEnding() { return ending; }
    public void setEnding(String ending) { this.ending = ending; }
    public String getWhatChanges() { return whatChanges; }
    public void setWhatChanges(String whatChanges) { this.whatChanges = whatChanges; }

    public String getProtagonistName() { return protagonistName; }
    public void setProtagonistName(String protagonistName) { this.protagonistName = protagonistName; }
    public String getProtagonistAge() { return protagonistAge; }
    public void setProtagonistAge(String protagonistAge) { this.protagonistAge = protagonistAge; }
    public String getProtagonistPersonality() { return protagonistPersonality; }
    public void setProtagonistPersonality(String protagonistPersonality) { this.protagonistPersonality = protagonistPersonality; }
    public String getProtagonistWant() { return protagonistWant; }
    public void setProtagonistWant(String protagonistWant) { this.protagonistWant = protagonistWant; }
    public String getProtagonistNeed() { return protagonistNeed; }
    public void setProtagonistNeed(String protagonistNeed) { this.protagonistNeed = protagonistNeed; }
    public String getProtagonistFear() { return protagonistFear; }
    public void setProtagonistFear(String protagonistFear) { this.protagonistFear = protagonistFear; }
    public String getProtagonistArc() { return protagonistArc; }
    public void setProtagonistArc(String protagonistArc) { this.protagonistArc = protagonistArc; }

    public List<CharacterData> getCharacters() { return characters; }
    public void setCharacters(List<CharacterData> characters) { this.characters = characters; }
    public List<Chapter> getChapters() { return chapters; }
    public void setChapters(List<Chapter> chapters) { this.chapters = chapters; }

    public String getAmountOfChoice() { return amountOfChoice; }
    public void setAmountOfChoice(String amountOfChoice) { this.amountOfChoice = amountOfChoice; }
    public String getImportantChoices() { return importantChoices; }
    public void setImportantChoices(String importantChoices) { this.importantChoices = importantChoices; }
    public String getBranches() { return branches; }
    public void setBranches(String branches) { this.branches = branches; }
    public String getDifferentEndings() { return differentEndings; }
    public void setDifferentEndings(String differentEndings) { this.differentEndings = differentEndings; }
    public String getEndingRequirements() { return endingRequirements; }
    public void setEndingRequirements(String endingRequirements) { this.endingRequirements = endingRequirements; }

    public String getSetting() { return setting; }
    public void setSetting(String setting) { this.setting = setting; }
    public String getImportantLocations() { return importantLocations; }
    public void setImportantLocations(String importantLocations) { this.importantLocations = importantLocations; }
    public String getWorldRules() { return worldRules; }
    public void setWorldRules(String worldRules) { this.worldRules = worldRules; }
    public String getLoreHistory() { return loreHistory; }
    public void setLoreHistory(String loreHistory) { this.loreHistory = loreHistory; }
    public String getSecrets() { return secrets; }
    public void setSecrets(String secrets) { this.secrets = secrets; }

    public String getVisualStyle() { return visualStyle; }
    public void setVisualStyle(String visualStyle) { this.visualStyle = visualStyle; }
    public String getMusicAudio() { return musicAudio; }
    public void setMusicAudio(String musicAudio) { this.musicAudio = musicAudio; }
    public String getUiPresentation() { return uiPresentation; }
    public void setUiPresentation(String uiPresentation) { this.uiPresentation = uiPresentation; }
    public String getInspirations() { return inspirations; }
    public void setInspirations(String inspirations) { this.inspirations = inspirations; }

    public String getEngineTools() { return engineTools; }
    public void setEngineTools(String engineTools) { this.engineTools = engineTools; }
    public String getMustHave() { return mustHave; }
    public void setMustHave(String mustHave) { this.mustHave = mustHave; }
    public String getNiceToHave() { return niceToHave; }
    public void setNiceToHave(String niceToHave) { this.niceToHave = niceToHave; }
    public String getScopeLimits() { return scopeLimits; }
    public void setScopeLimits(String scopeLimits) { this.scopeLimits = scopeLimits; }

    public String getFreeNotes() { return freeNotes; }
    public void setFreeNotes(String freeNotes) { this.freeNotes = freeNotes; }
}
