package com.vnplanner;

import com.vnplanner.io.ProjectIO;
import com.vnplanner.model.Chapter;
import com.vnplanner.model.ChoiceNode;
import com.vnplanner.model.Project;
import com.vnplanner.model.Relationship;
import com.vnplanner.model.Scene;
import com.vnplanner.model.TimelineEntry;
import com.vnplanner.model.WorldLocation;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectIOTest {
    @Test
    public void vnprojRoundTripPreservesNestedPlanningData() throws Exception {
        Project original = sampleProject();
        Path file = Files.createTempFile("vnplanner-", ".vnproj");

        ProjectIO.saveAsVnproj(file.toFile(), original);
        Project loaded = ProjectIO.loadFromVnproj(file.toFile());

        assertProjectData(original, loaded);
    }

    @Test
    public void jsonRoundTripPreservesNestedPlanningData() throws Exception {
        Project original = sampleProject();
        Path file = Files.createTempFile("vnplanner-", ".json");

        ProjectIO.exportJson(file.toFile(), original);
        Project loaded = ProjectIO.importJson(file.toFile());

        assertProjectData(original, loaded);
    }

    private Project sampleProject() {
        Project project = new Project();
        project.setTitle("Round Trip VN");
        project.setEngine("Ren'Py");
        project.setTools("Krita, Audacity");

        Chapter chapter = new Chapter();
        chapter.setTitle("Chapter One");
        chapter.setPurpose("Establish the mystery");
        Scene scene = new Scene();
        scene.setTitle("Opening Scene");
        scene.setLocation("Old Station");
        scene.setChoices("Wait / Leave");
        chapter.getScenes().add(scene);
        project.getChapters().add(chapter);

        WorldLocation location = new WorldLocation();
        location.setName("Old Station");
        location.setType("Transit");
        project.getLocations().add(location);

        ChoiceNode choice = new ChoiceNode("Wait");
        choice.setEnding(true);
        project.getChoices().add(choice);

        Relationship relationship = new Relationship();
        relationship.setFromCharacter("Alice");
        relationship.setToCharacter("Bob");
        relationship.setType("Rivals");
        project.getRelationships().add(relationship);

        TimelineEntry timelineEntry = new TimelineEntry();
        timelineEntry.setLabel("The train arrives");
        timelineEntry.setDate("Day 1");
        project.getTimeline().add(timelineEntry);
        return project;
    }

    private void assertProjectData(Project expected, Project actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getEngine(), actual.getEngine());
        assertEquals(expected.getTools(), actual.getTools());
        assertEquals(1, actual.getChapters().size());
        assertEquals("Chapter One", actual.getChapters().get(0).getTitle());
        assertEquals(1, actual.getChapters().get(0).getScenes().size());
        assertEquals("Old Station", actual.getChapters().get(0).getScenes().get(0).getLocation());
        assertEquals(1, actual.getLocations().size());
        assertEquals("Old Station", actual.getLocations().get(0).getName());
        assertEquals(1, actual.getChoices().size());
        assertTrue(actual.getChoices().get(0).isEnding());
        assertEquals(1, actual.getRelationships().size());
        assertEquals("Rivals", actual.getRelationships().get(0).getType());
        assertEquals(1, actual.getTimeline().size());
        assertEquals("The train arrives", actual.getTimeline().get(0).getLabel());
    }
}
