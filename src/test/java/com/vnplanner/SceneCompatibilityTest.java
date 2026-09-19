package com.vnplanner;

import com.vnplanner.model.Chapter;
import com.vnplanner.model.Scene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceneCompatibilityTest {
    @Test
    public void chapterShouldDefaultToEmptySceneList() {
        Chapter chapter = new Chapter();
        assertNotNull(chapter.getScenes());
        assertTrue(chapter.getScenes().isEmpty());
    }

    @Test
    public void sceneShouldStoreCorePlanningFields() {
        Scene scene = new Scene();
        scene.setTitle("Scene 4.2");
        scene.setLocation("School Rooftop");
        scene.setCharactersPresent("Alice, Bob");
        scene.setPurpose("Reveal trust fracture");
        scene.setChoices("Trust / Lie");
        scene.setLeadsTo("Chapter 5");

        assertEquals("Scene 4.2", scene.getTitle());
        assertEquals("School Rooftop", scene.getLocation());
        assertEquals("Trust / Lie", scene.getChoices());
        assertEquals("Chapter 5", scene.getLeadsTo());
    }
}
