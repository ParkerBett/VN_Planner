package com.vnplanner;

import com.vnplanner.io.ProjectIO;
import com.vnplanner.model.CharacterData;
import com.vnplanner.model.Chapter;
import com.vnplanner.model.Project;
import com.vnplanner.model.Relationship;
import com.vnplanner.model.Scene;
import com.vnplanner.model.TimelineEntry;
import com.vnplanner.model.WorldLocation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VNPlannerFrame extends JFrame {
    private Project project = new Project();
    private File currentFile = null;
    private JTabbedPane tabs;

    // Project fields (tab 1)
    private JTextField titleField = new JTextField();
    private JTextField workingTitleField = new JTextField();
    private JTextArea oneSentenceArea = new JTextArea(3,40);
    private JTextArea coreIdeaArea = new JTextArea(5,40);
    private JComboBox<String> genreCombo;
    private JTextArea playerFeelArea = new JTextArea(3,40);

    // Story additional fields
    private JTextArea centralConflictArea = new JTextArea(3,60);
    private JTextArea beginningArea = new JTextArea(3,60);
    private JTextArea middleArea = new JTextArea(3,60);
    private JTextArea revelationsArea = new JTextArea(3,60);
    private JTextArea climaxArea = new JTextArea(3,60);
    private JTextArea endingArea = new JTextArea(3,60);
    // (removed whatChangesArea — that section was removed from Story tab)

    // Protagonist fields
    private JTextField protagonistNameField = new JTextField();
    private JTextField protagonistAgeField = new JTextField();
    private JTextArea protagonistPersonalityArea = new JTextArea(3,40);
    private JTextArea protagonistWantArea = new JTextArea(2,40);
    private JTextArea protagonistNeedArea = new JTextArea(2,40);
    private JTextArea protagonistFearArea = new JTextArea(2,40);
    private JTextArea protagonistArcArea = new JTextArea(3,40);

    // Choices & Endings fields
    private JComboBox<String> amountOfChoiceCombo;
    private JTextArea importantChoicesArea = new JTextArea(3,60);
    private JTextArea branchesArea = new JTextArea(3,60);
    private JTextArea differentEndingsArea = new JTextArea(3,60);
    private JTextArea endingRequirementsArea = new JTextArea(3,60);

    // Choice mind-map tree
    private javax.swing.tree.DefaultMutableTreeNode choiceRootNode;
    private javax.swing.tree.DefaultTreeModel choiceTreeModel;
    private JTree choiceTree;
    private javax.swing.tree.DefaultMutableTreeNode startNode;

    // World & Lore fields
    private JTextArea settingArea = new JTextArea(3,60);
    private JTextArea importantLocationsArea = new JTextArea(3,60);
    private JTextArea worldRulesArea = new JTextArea(3,60);
    private JTextArea loreHistoryArea = new JTextArea(3,60);
    private JTextArea secretsArea = new JTextArea(3,60);

    // World locations
    private DefaultListModel<WorldLocation> locationListModel = new DefaultListModel<>();
    private JList<WorldLocation> locationJList = new JList<>(locationListModel);
    private JTextField locationNameField = new JTextField();
    private JTextField locationTypeField = new JTextField();
    private JTextArea locationDescriptionArea = new JTextArea(3,30);
    private JComboBox<String> locationChapterCombo = new JComboBox<>();
    private JComboBox<String> locationSceneCombo = new JComboBox<>();
    private JTextArea locationNotesArea = new JTextArea(3,30);

    // Presentation fields
    private JTextArea visualStyleArea = new JTextArea(3,60);
    private JTextArea musicAudioArea = new JTextArea(3,60);
    private JTextArea uiPresentationArea = new JTextArea(3,60);
    private JTextArea inspirationsArea = new JTextArea(3,60);

    // Development fields
    private JComboBox<String> engineCombo = new JComboBox<>(new String[]{
        "",
        "Ren'Py",
        "Unity",
        "Godot",
        "GameMaker",
        "RPG Maker",
        "Twine",
        "Visual Novel Maker",
        "KiriKiri",
        "Adventure Game Studio",
        "Custom Engine",
        "Other"
    });
    private JTextArea toolsArea = new JTextArea(3,60);
    private JTextArea mustHaveArea = new JTextArea(3,60);
    private JTextArea niceToHaveArea = new JTextArea(3,60);
    private JTextArea scopeLimitsArea = new JTextArea(3,60);

    // Characters
    private DefaultListModel<CharacterData> characterListModel = new DefaultListModel<>();
    private JList<CharacterData> characterJList = new JList<>(characterListModel);
    private JTextField charName = new JTextField();
    private JTextField charRole = new JTextField();
    private JTextArea charPersonality = new JTextArea(3,30);
    private JTextArea charWant = new JTextArea(2,30);
    private JTextArea charFear = new JTextArea(2,30);
    private JTextArea charRelation = new JTextArea(2,30);
    private JTextArea charArc = new JTextArea(3,30);

    // Chapters
    private DefaultListModel<Chapter> chapterListModel = new DefaultListModel<>();
    private JList<Chapter> chapterJList = new JList<>(chapterListModel);
    private JTextField chapTitle = new JTextField();
    private JTextArea chapPurpose = new JTextArea(3,30);
    private JTextArea chapEvents = new JTextArea(4,30);
    private JTextArea chapChoices = new JTextArea(3,30);
    private JTextArea chapEndingLead = new JTextArea(2,30);

    // Scenes inside chapters
    private DefaultListModel<Scene> sceneListModel = new DefaultListModel<>();
    private JList<Scene> sceneJList = new JList<>(sceneListModel);
    private JComboBox<Chapter> sceneChapterCombo = new JComboBox<>();
    private JTextField sceneTitleField = new JTextField();
    private JComboBox<String> sceneLocationCombo = new JComboBox<>();
    private JTextArea sceneCharactersField = new JTextArea(2,30);
    private JTextArea scenePurposeField = new JTextArea(2,30);
    private JTextArea sceneEventsField = new JTextArea(3,30);
    private JTextArea sceneDialogueField = new JTextArea(3,30);
    private JTextArea sceneChoicesField = new JTextArea(2,30);
    private JTextArea sceneLeadsToField = new JTextArea(2,30);
    private JTextArea sceneNotesField = new JTextArea(3,30);

    // Free notes
    private JTextArea freeNotesArea = new JTextArea(10,60);
    private JTextField searchField = new JTextField();
    private JTextArea searchResultsArea = new JTextArea(16, 70);
    private DefaultListModel<SearchResult> searchResultsModel = new DefaultListModel<>();
    private JList<SearchResult> searchResultsList = new JList<>(searchResultsModel);
    private JLabel dashboardStatsLabel = new JLabel();
    private DefaultListModel<Relationship> relationshipListModel = new DefaultListModel<>();
    private JList<Relationship> relationshipJList = new JList<>(relationshipListModel);
    private JTextField relationshipFromField = new JTextField();
    private JTextField relationshipToField = new JTextField();
    private JTextField relationshipTypeField = new JTextField();
    private JTextArea relationshipNotesArea = new JTextArea(3, 30);
    private DefaultListModel<TimelineEntry> timelineListModel = new DefaultListModel<>();
    private JList<TimelineEntry> timelineJList = new JList<>(timelineListModel);
    private JTree timelineTree;
    private JTextField timelineLabelField = new JTextField();
    private JTextField timelineDateField = new JTextField();
    private JTextField timelineChapterField = new JTextField();
    private JTextArea timelineDescriptionArea = new JTextArea(3, 30);
    // Autosave timer
    private javax.swing.Timer autosaveTimer;

    private static class SearchResult {
        private final String label;
        private final Runnable action;

        SearchResult(String label, Runnable action) {
            this.label = label;
            this.action = action;
        }

        void open() {
            if (action != null) action.run();
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public VNPlannerFrame() {
        super("Visual Novel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);

        createMenuBar();
        // Initialize styled components
        initStyledComponents();
        initTabs();
        // start autosave timer (30 seconds)
        autosaveTimer = new javax.swing.Timer(30_000, e -> runAutosave());
        autosaveTimer.setRepeats(true);
        autosaveTimer.start();
        // stop timer on close
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (autosaveTimer != null) autosaveTimer.stop();
            }
        });
    }

    private void runAutosave() {
        // perform a silent autosave: if there's a currentFile, save to it; otherwise save to temp
        try {
            updateProjectFromUI();
            if (currentFile != null) {
                ProjectIO.saveAsVnproj(currentFile, project);
                System.out.println("[Autosave] Saved to " + currentFile.getAbsolutePath());
            } else {
                String tmp = System.getProperty("java.io.tmpdir");
                File f = new File(tmp, "vnplanner_autosave.vnproj");
                ProjectIO.saveAsVnproj(f, project);
                System.out.println("[Autosave] Saved to temp " + f.getAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("[Autosave] failed: " + ex.getMessage());
        }
    }

    private void initStyledComponents() {
        String[] genres = new String[]{"Other","Romance","Mystery","Fantasy","Sci-Fi","Horror","Slice of Life","Drama","Comedy","Thriller"};
        genreCombo = new JComboBox<>(genres);
        genreCombo.setEditable(true);

        String[] amounts = new String[]{"Low","Medium","High","Custom"};
        amountOfChoiceCombo = new JComboBox<>(amounts);
        amountOfChoiceCombo.setEditable(true);

        characterJList.setPreferredSize(new Dimension(220, 400));
        characterJList.setFixedCellWidth(220);
        chapterJList.setPreferredSize(new Dimension(220, 400));
        chapterJList.setFixedCellWidth(220);
        locationJList.setPreferredSize(new Dimension(220, 400));
        locationJList.setFixedCellWidth(220);
        sceneJList.setPreferredSize(new Dimension(220, 400));
        sceneJList.setFixedCellWidth(220);
        sceneChapterCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value == null) {
                label.setText("Select a chapter");
            } else {
                label.setText(value.toString());
            }
            if (isSelected) label.setBackground(list.getSelectionBackground());
            label.setOpaque(true);
            return label;
        });
    }

    private void createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New Project");
        newItem.addActionListener(e -> newProject());
        JMenuItem openItem = new JMenuItem("Open Project");
        openItem.addActionListener(e -> openProject());
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveProject(false));
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> saveProject(true));
        JMenuItem importJson = new JMenuItem("Import JSON");
        importJson.addActionListener(e -> importJson());
        JMenuItem exportJson = new JMenuItem("Export JSON");
        exportJson.addActionListener(e -> exportJson());
        JMenuItem backupItem = new JMenuItem("Create Backup");
        backupItem.addActionListener(e -> backupProject());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> dispose());

        file.add(newItem);
        file.add(openItem);
        file.addSeparator();
        file.add(saveItem);
        file.add(saveAsItem);
        file.addSeparator();
        file.add(importJson);
        file.add(exportJson);
        file.add(backupItem);
        file.addSeparator();
        file.add(exitItem);

        mb.add(file);
        setJMenuBar(mb);
    }

    private void applyTemplate(String template) {
        newProject();
        titleField.setText(template);
        amountOfChoiceCombo.setSelectedItem("Medium");

        switch (template) {
            case "Linear VN":
                genreCombo.setSelectedItem("Drama");
                oneSentenceArea.setText("One focused story moves from its opening promise to a single resolution.");
                centralConflictArea.setText("The protagonist must overcome one central obstacle before the final ending.");
                addTemplateChapter("Chapter 1", "Introduce the premise and establish the main conflict.");
                addTemplateChapter("Chapter 2", "Escalate the conflict and develop the consequences.");
                addTemplateChapter("Chapter 3", "Resolve the conflict and lead into the ending.");
                break;
            case "Branching VN":
                genreCombo.setSelectedItem("Drama");
                oneSentenceArea.setText("Important choices split the story into paths with different consequences.");
                importantChoicesArea.setText("Choice points, consequences, flags, and branch rejoining.");
                branchesArea.setText("Route A and Route B");
                addTemplateChoice("Choice A", false);
                addTemplateChoice("Choice B", false);
                break;
            case "Multi-Route VN":
                genreCombo.setSelectedItem("Romance");
                oneSentenceArea.setText("A common route opens into separate character-focused stories and endings.");
                branchesArea.setText("Common Route -> Route A / Route B / Route C");
                addTemplateCharacter("Love Interest A", "Route character");
                addTemplateCharacter("Love Interest B", "Route character");
                addTemplateCharacter("Love Interest C", "Route character");
                break;
            case "Mystery VN":
                genreCombo.setSelectedItem("Mystery");
                oneSentenceArea.setText("An investigation uncovers secrets, clues, and a final explanation.");
                centralConflictArea.setText("Central mystery:\nSuspects:\nEvidence:\nClues:\nRed herrings:\nFinal explanation:");
                revelationsArea.setText("Revelations and secrets to uncover.");
                break;
            case "Horror VN":
                genreCombo.setSelectedItem("Horror");
                oneSentenceArea.setText("The protagonist faces an escalating threat where survival choices carry a cost.");
                centralConflictArea.setText("Threat:\nRules:\nFear progression:\nMajor scares:\nSurvival choices:");
                endingRequirementsArea.setText("Bad endings\nTrue ending");
                break;
            case "Romance VN":
                genreCombo.setSelectedItem("Romance");
                oneSentenceArea.setText("Relationships grow through choices, conflicts, routes, and emotional resolution.");
                importantChoicesArea.setText("Romantic choices and relationship progression.");
                branchesArea.setText("Character routes and route endings.");
                break;
            case "Adventure VN":
                genreCombo.setSelectedItem("Fantasy");
                oneSentenceArea.setText("A journey through unfamiliar places reveals discoveries and a larger world.");
                importantLocationsArea.setText("Locations:\nJourney:\nDiscoveries:\nWorld lore:\nRoute progression:");
                break;
            case "Dramatic / Character VN":
                genreCombo.setSelectedItem("Drama");
                oneSentenceArea.setText("Character goals and internal conflicts build toward emotional turning points.");
                protagonistWantArea.setText("External goal:");
                protagonistNeedArea.setText("Internal need:");
                protagonistArcArea.setText("Character arc:\nTurning points:\nResolution:");
                break;
            case "Experimental VN":
                genreCombo.setSelectedItem("Other");
                oneSentenceArea.setText("A flexible visual novel built around an unusual narrative structure or mechanic.");
                coreIdeaArea.setText("Core concept:\nStructure:\nNarrative gimmick:\nPlayer interaction:\nUnusual mechanics:\nPerspective:\nRules the VN follows:");
                break;
            case "Blank VN":
            default:
                titleField.setText("Blank VN");
                break;
        }
        populateChoicesFromProject();
        updateProjectFromUI();
        refreshSceneChapterOptions();
        refreshDashboard();
        refreshTimelineTree();
        if (tabs != null) tabs.setSelectedIndex(2);
    }

    private void addTemplateChapter(String title, String purpose) {
        Chapter chapter = new Chapter();
        chapter.setTitle(title);
        chapter.setPurpose(purpose);
        project.getChapters().add(chapter);
        chapterListModel.addElement(chapter);
    }

    private void addTemplateCharacter(String name, String role) {
        CharacterData character = new CharacterData();
        character.setName(name);
        character.setRole(role);
        project.getCharacters().add(character);
        characterListModel.addElement(character);
    }

    private void addTemplateChoice(String text, boolean ending) {
        com.vnplanner.model.ChoiceNode choice = new com.vnplanner.model.ChoiceNode(text);
        choice.setEnding(ending);
        project.getChoices().add(choice);
    }

        

    private void initTabs() {
        tabs = new JTabbedPane();
        tabs.addTab("Dashboard", buildDashboardTab());
        tabs.addTab("Search", buildSearchTab());
        tabs.addTab("Project", buildProjectTab());
        tabs.addTab("Story", buildStoryTab());
        tabs.addTab("Protagonist", buildProtagonistTab());
        tabs.addTab("Characters", buildCharactersTab());
        tabs.addTab("Relationships", buildRelationshipsTab());
        tabs.addTab("Chapters", buildChaptersTab());
        tabs.addTab("Scenes", buildScenesTab());
        tabs.addTab("Timeline", buildTimelineTab());
        tabs.addTab("Choice Mind-Map", buildChoicesTab());
        tabs.addTab("Locations", buildWorldTab());
        tabs.addTab("Lore", buildLoreTab());
        tabs.addTab("Presentation", buildPresentationTab());
        tabs.addTab("Development", buildDevelopmentTab());
        tabs.addTab("Free Notes", buildFreeNotesTab());

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildRelationshipsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(relationshipJList), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");
        buttons.add(add); buttons.add(remove);
        left.add(buttons, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(240, 0));
        left.setMinimumSize(new Dimension(240, 0));

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("From Character:"), gbc); gbc.gridx = 1; right.add(relationshipFromField, gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("To Character:"), gbc); gbc.gridx = 1; right.add(relationshipToField, gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Relationship:"), gbc); gbc.gridx = 1; right.add(relationshipTypeField, gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Notes:"), gbc); gbc.gridx = 1; right.add(new JScrollPane(relationshipNotesArea), gbc);
        JButton apply = new JButton("Apply");
        gbc.gridy = y; gbc.gridx = 1; right.add(apply, gbc);
        panel.add(left, BorderLayout.WEST); panel.add(new JScrollPane(right), BorderLayout.CENTER);

        add.addActionListener(e -> {
            Relationship relationship = new Relationship();
            relationship.setFromCharacter("New Character");
            relationshipListModel.addElement(relationship);
            project.getRelationships().add(relationship);
            relationshipJList.setSelectedIndex(relationshipListModel.size() - 1);
        });
        remove.addActionListener(e -> {
            int index = relationshipJList.getSelectedIndex();
            if (index >= 0) { relationshipListModel.remove(index); project.getRelationships().remove(index); }
        });
        relationshipJList.addListSelectionListener(e -> {
            Relationship relationship = relationshipJList.getSelectedValue();
            if (relationship != null) {
                relationshipFromField.setText(relationship.getFromCharacter());
                relationshipToField.setText(relationship.getToCharacter());
                relationshipTypeField.setText(relationship.getType());
                relationshipNotesArea.setText(relationship.getNotes());
            }
        });
        apply.addActionListener(e -> {
            int index = relationshipJList.getSelectedIndex();
            if (index >= 0) {
                Relationship relationship = relationshipListModel.get(index);
                relationship.setFromCharacter(relationshipFromField.getText());
                relationship.setToCharacter(relationshipToField.getText());
                relationship.setType(relationshipTypeField.getText());
                relationship.setNotes(relationshipNotesArea.getText());
                relationshipJList.repaint();
            }
        });
        return panel;
    }

    private JPanel buildTimelineTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        timelineTree = new JTree();
        timelineTree.setRootVisible(true);
        timelineTree.setShowsRootHandles(true);
        panel.add(new JScrollPane(timelineTree), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Story order: chapters and their linked scenes"));
        JButton refresh = new JButton("Refresh Timeline");
        refresh.addActionListener(e -> refreshTimelineTree());
        controls.add(refresh);
        panel.add(controls, BorderLayout.SOUTH);
        refreshTimelineTree();
        return panel;
    }

    private void refreshTimelineTree() {
        if (timelineTree == null) return;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                project.getTitle() == null || project.getTitle().trim().isEmpty()
                        ? "Visual Novel Timeline" : project.getTitle());
        int chapterNumber = 1;
        for (Chapter chapter : project.getChapters()) {
            String chapterTitle = chapter.getTitle() == null || chapter.getTitle().trim().isEmpty()
                    ? "Untitled Chapter" : chapter.getTitle();
            DefaultMutableTreeNode chapterNode = new DefaultMutableTreeNode(
                    "Chapter " + chapterNumber + ": " + chapterTitle);
            if (chapter.getScenes() == null || chapter.getScenes().isEmpty()) {
                chapterNode.add(new DefaultMutableTreeNode("No scenes yet"));
            } else {
                int sceneNumber = 1;
                for (Scene scene : chapter.getScenes()) {
                    String sceneTitle = scene.getTitle() == null || scene.getTitle().trim().isEmpty()
                            ? "Untitled Scene" : scene.getTitle();
                    String location = scene.getLocation() == null || scene.getLocation().trim().isEmpty()
                            ? "" : " @ " + scene.getLocation();
                    chapterNode.add(new DefaultMutableTreeNode(
                            "Scene " + chapterNumber + "." + sceneNumber + ": " + sceneTitle + location));
                    sceneNumber++;
                }
            }
            root.add(chapterNode);
            chapterNumber++;
        }
        if (project.getChapters().isEmpty()) {
            root.add(new DefaultMutableTreeNode("Add chapters to build the timeline"));
        }
        timelineTree.setModel(new DefaultTreeModel(root));
        for (int i = 0; i < timelineTree.getRowCount(); i++) timelineTree.expandRow(i);
    }

    private JComponent buildDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel heading = new JLabel("Project Dashboard");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 20f));
        panel.add(heading, BorderLayout.NORTH);

        dashboardStatsLabel.setVerticalAlignment(SwingConstants.TOP);
        panel.add(dashboardStatsLabel, BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh Statistics");
        refresh.addActionListener(e -> refreshDashboard());
        panel.add(refresh, BorderLayout.SOUTH);
        refreshDashboard();
        return panel;
    }

    private JComponent buildSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel controls = new JPanel(new BorderLayout(8, 0));
        controls.add(new JLabel("Search:"), BorderLayout.WEST);
        controls.add(searchField, BorderLayout.CENTER);
        JButton search = new JButton("Find");
        search.addActionListener(e -> runProjectSearch());
        controls.add(search, BorderLayout.EAST);
        searchField.addActionListener(e -> runProjectSearch());
        panel.add(controls, BorderLayout.NORTH);
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SearchResult result = searchResultsList.getSelectedValue();
                if (result != null) result.open();
            }
        });
        panel.add(new JScrollPane(searchResultsList), BorderLayout.CENTER);
        return panel;
    }

    private void refreshDashboard() {
        int sceneCount = 0;
        for (Chapter chapter : project.getChapters()) {
            if (chapter.getScenes() != null) sceneCount += chapter.getScenes().size();
        }
        int choiceCount = countChoiceNodes(project.getChoices());
        String title = project.getTitle() == null || project.getTitle().trim().isEmpty()
                ? "Untitled project" : project.getTitle();
        dashboardStatsLabel.setText("<html><h2>" + escapeHtml(title) + "</h2>"
                + "Characters: " + project.getCharacters().size() + "<br>"
                + "Relationships: " + project.getRelationships().size() + "<br>"
                + "Chapters: " + project.getChapters().size() + "<br>"
                + "Scenes: " + sceneCount + "<br>"
                + "Timeline items: " + (project.getChapters().size() + sceneCount) + "<br>"
                + "Locations: " + project.getLocations().size() + "<br>"
                + "Choice nodes: " + choiceCount + "<br>"
                + "Engine: " + escapeHtml(project.getEngine()) + "</html>");
    }

    private int countChoiceNodes(java.util.List<com.vnplanner.model.ChoiceNode> nodes) {
        int count = 0;
        if (nodes != null) {
            for (com.vnplanner.model.ChoiceNode node : nodes) {
                count++;
                count += countChoiceNodes(node.getChildren());
            }
        }
        return count;
    }

    private String escapeHtml(String value) {
        if (value == null || value.trim().isEmpty()) return "Not set";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void runProjectSearch() {
        String query = searchField.getText().trim().toLowerCase();
        searchResultsModel.clear();
        if (query.isEmpty()) {
            searchResultsModel.addElement(new SearchResult("Enter a search term.", null));
            return;
        }
        addSearchResult(query, "Project title", project.getTitle(), () -> selectTab(2));
        addSearchResult(query, "Working title", project.getWorkingTitle(), () -> selectTab(2));
        addSearchResult(query, "Core idea", project.getCoreIdea(), () -> selectTab(2));
        addSearchResult(query, "Story", project.getBeginning() + " " + project.getMiddle() + " "
                + project.getRevelations() + " " + project.getClimax() + " " + project.getEnding(),
                () -> selectTab(3));
        addSearchResult(query, "Free notes", project.getFreeNotes(), () -> selectTab(15));
        for (int i = 0; i < project.getCharacters().size(); i++) {
            final int index = i;
            CharacterData character = project.getCharacters().get(i);
            addSearchResult(query, "Character: " + character.getName(), character.getName() + " "
                    + character.getRole() + " " + character.getPersonality() + " " + character.getArc(),
                    () -> { selectTab(5); characterJList.setSelectedIndex(index); });
        }
        for (int i = 0; i < project.getRelationships().size(); i++) {
            final int index = i;
            Relationship relationship = project.getRelationships().get(i);
            addSearchResult(query, "Relationship: " + relationship, relationship.getFromCharacter() + " "
                + relationship.getToCharacter() + " " + relationship.getType() + " " + relationship.getNotes(),
                () -> { selectTab(6); relationshipJList.setSelectedIndex(index); });
        }
        for (TimelineEntry entry : project.getTimeline()) {
            addSearchResult(query, "Timeline: " + entry.getLabel(), entry.getLabel() + " "
                + entry.getDate() + " " + entry.getChapter() + " " + entry.getDescription(),
                () -> selectTab(9));
        }
        for (int chapterIndex = 0; chapterIndex < project.getChapters().size(); chapterIndex++) {
            final int selectedChapterIndex = chapterIndex;
            Chapter chapter = project.getChapters().get(chapterIndex);
            addSearchResult(query, "Chapter: " + chapter.getTitle(), chapter.getTitle() + " "
                    + chapter.getPurpose() + " " + chapter.getMajorEvents() + " " + chapter.getChoices(),
                    () -> { selectTab(7); chapterJList.setSelectedIndex(selectedChapterIndex); });
            if (chapter.getScenes() != null) {
                for (int sceneIndex = 0; sceneIndex < chapter.getScenes().size(); sceneIndex++) {
                    final int selectedSceneIndex = sceneIndex;
                    Scene scene = chapter.getScenes().get(sceneIndex);
                    addSearchResult(query, "Scene: " + scene.getTitle(), scene.getTitle() + " "
                            + scene.getLocation() + " " + scene.getPurpose() + " " + scene.getEvents() + " "
                            + scene.getDialogueNotes() + " " + scene.getNotes(),
                            () -> {
                                selectTab(8);
                                sceneChapterCombo.setSelectedIndex(selectedChapterIndex);
                                sceneJList.setSelectedIndex(selectedSceneIndex);
                            });
                }
            }
        }
        for (int i = 0; i < project.getLocations().size(); i++) {
            final int index = i;
            WorldLocation location = project.getLocations().get(i);
            addSearchResult(query, "Location: " + location.getName(), location.getName() + " "
                    + location.getType() + " " + location.getDescription() + " " + location.getNotes(),
                    () -> { selectTab(11); locationJList.setSelectedIndex(index); });
        }
        if (searchResultsModel.isEmpty()) {
            searchResultsModel.addElement(new SearchResult("No matches found.", null));
        }
    }

    private void addSearchResult(String query, String label, String value, Runnable action) {
        if (value != null && value.toLowerCase().contains(query)) {
            searchResultsModel.addElement(new SearchResult(label, action));
        }
    }

    private void selectTab(int index) {
        if (tabs != null) tabs.setSelectedIndex(index);
    }

    private JPanel buildProjectTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        gbc.gridy = y++;
        gbc.gridx = 0; fields.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; fields.add(titleField, gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; gbc.weightx = 0; fields.add(new JLabel("Working Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; fields.add(workingTitleField, gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; gbc.weightx = 0; fields.add(new JLabel("One-sentence Pitch:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; fields.add(new JScrollPane(oneSentenceArea), gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; fields.add(new JLabel("Core Idea:"), gbc);
        gbc.gridx = 1; fields.add(new JScrollPane(coreIdeaArea), gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; fields.add(new JLabel("Genre / Tone:"), gbc);
        gbc.gridx = 1; fields.add(genreCombo, gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; fields.add(new JLabel("Player Feel:"), gbc);
        gbc.gridx = 1; fields.add(new JScrollPane(playerFeelArea), gbc);
        fields.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        p.add(fields, BorderLayout.NORTH);
        return p;
    }

    private JPanel buildStoryTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.gridx = 0; gbc.gridy = 0;
        grid.add(new JLabel("Central Conflict:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(centralConflictArea), gbc);
        gbc.gridy++; grid.add(new JLabel("Beginning:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(beginningArea), gbc);
        gbc.gridy++; grid.add(new JLabel("Middle:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(middleArea), gbc);
        gbc.gridy++; grid.add(new JLabel("Revelations:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(revelationsArea), gbc);
        gbc.gridy++; grid.add(new JLabel("Climax:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(climaxArea), gbc);
        gbc.gridy++; grid.add(new JLabel("Ending:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(endingArea), gbc);
        p.add(new JScrollPane(grid), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildProtagonistTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y=0;
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Name:"), gbc); gbc.gridx=1; p.add(protagonistNameField, gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Age:"), gbc); gbc.gridx=1; p.add(protagonistAgeField, gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Personality:"), gbc); gbc.gridx=1; p.add(new JScrollPane(protagonistPersonalityArea), gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Want:"), gbc); gbc.gridx=1; p.add(new JScrollPane(protagonistWantArea), gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Need:"), gbc); gbc.gridx=1; p.add(new JScrollPane(protagonistNeedArea), gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Fear:"), gbc); gbc.gridx=1; p.add(new JScrollPane(protagonistFearArea), gbc);
        gbc.gridy = y++; gbc.gridx=0; p.add(new JLabel("Character Arc:"), gbc); gbc.gridx=1; p.add(new JScrollPane(protagonistArcArea), gbc);
        return p;
    }

    private JPanel buildCharactersTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(characterJList), BorderLayout.CENTER);
        JPanel leftButtons = new JPanel();
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");
        leftButtons.add(add); leftButtons.add(remove);
        left.add(leftButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y=0;
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Name:"), gbc); gbc.gridx=1; right.add(charName, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Role:"), gbc); gbc.gridx=1; right.add(charRole, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Personality:"), gbc); gbc.gridx=1; right.add(new JScrollPane(charPersonality), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Want:"), gbc); gbc.gridx=1; right.add(new JScrollPane(charWant), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Fear:"), gbc); gbc.gridx=1; right.add(new JScrollPane(charFear), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Relation to Protagonist:"), gbc); gbc.gridx=1; right.add(new JScrollPane(charRelation), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Arc:"), gbc); gbc.gridx=1; right.add(new JScrollPane(charArc), gbc);
        JButton apply = new JButton("Apply"); gbc.gridy = y++; gbc.gridx=1; right.add(apply, gbc);

        left.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        left.setPreferredSize(new Dimension(240, 0));
        left.setMinimumSize(new Dimension(240, 0));
        right.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.CENTER);

        add.addActionListener(e -> {
            CharacterData c = new CharacterData();
            c.setName("New Character");
            characterListModel.addElement(c);
            project.getCharacters().add(c);
            characterJList.setSelectedIndex(characterListModel.size()-1);
        });

        remove.addActionListener(e -> {
            int i = characterJList.getSelectedIndex();
            if (i>=0) {
                characterListModel.remove(i);
                project.getCharacters().remove(i);
            }
        });

        characterJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        characterJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                CharacterData sel = characterJList.getSelectedValue();
                if (sel != null) {
                    charName.setText(sel.getName());
                    charRole.setText(sel.getRole());
                    charPersonality.setText(sel.getPersonality());
                    charWant.setText(sel.getWant());
                    charFear.setText(sel.getFear());
                    charRelation.setText(sel.getRelationToProtagonist());
                    charArc.setText(sel.getArc());
                }
            }
        });

        apply.addActionListener(e -> {
            int i = characterJList.getSelectedIndex();
            if (i>=0) {
                CharacterData sel = characterListModel.get(i);
                sel.setName(charName.getText());
                sel.setRole(charRole.getText());
                sel.setPersonality(charPersonality.getText());
                sel.setWant(charWant.getText());
                sel.setFear(charFear.getText());
                sel.setRelationToProtagonist(charRelation.getText());
                sel.setArc(charArc.getText());
                characterJList.repaint();
            }
        });

        characterJList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getName());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            l.setOpaque(true);
            return l;
        });

        return p;
    }

    private JPanel buildChaptersTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel chapterPanel = new JPanel(new BorderLayout());
        chapterPanel.add(new JScrollPane(chapterJList), BorderLayout.CENTER);
        JPanel chapterButtons = new JPanel();
        JButton addChapter = new JButton("Add");
        JButton removeChapter = new JButton("Remove");
        chapterButtons.add(addChapter); chapterButtons.add(removeChapter);
        chapterPanel.add(chapterButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y=0;
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Title:"), gbc); gbc.gridx=1; right.add(chapTitle, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Purpose:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapPurpose), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Major Events:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapEvents), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Choices / Interaction:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapChoices), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Ending / Lead Into Next:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapEndingLead), gbc);
        JButton apply = new JButton("Apply Chapter");
        gbc.gridy = y++; gbc.gridx=0; gbc.gridwidth = 2; right.add(apply, gbc);

        chapterPanel.setPreferredSize(new Dimension(240, 0));
        chapterPanel.setMinimumSize(new Dimension(240, 0));
        p.add(chapterPanel, BorderLayout.WEST);
        p.add(new JScrollPane(right), BorderLayout.CENTER);

        addChapter.addActionListener(e -> {
            Chapter c = new Chapter();
            c.setTitle("New Chapter");
            chapterListModel.addElement(c);
            project.getChapters().add(c);
            chapterJList.setSelectedIndex(chapterListModel.size()-1);
            refreshSceneChapterOptions();
            refreshTimelineTree();
        });
        removeChapter.addActionListener(e -> {
            int i = chapterJList.getSelectedIndex();
            if (i>=0) {
                chapterListModel.remove(i);
                project.getChapters().remove(i);
                refreshSceneChapterOptions();
                refreshTimelineTree();
            }
        });

        chapterJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chapterJList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Chapter sel = chapterJList.getSelectedValue();
            if (sel != null) {
                chapTitle.setText(sel.getTitle());
                chapPurpose.setText(sel.getPurpose());
                chapEvents.setText(sel.getMajorEvents());
                chapChoices.setText(sel.getChoices());
                chapEndingLead.setText(sel.getEndingLead());
                if (sceneChapterCombo != null) {
                    sceneChapterCombo.setSelectedItem(sel);
                }
            }
        });

        apply.addActionListener(e -> {
            int i = chapterJList.getSelectedIndex();
            if (i>=0) {
                Chapter sel = chapterListModel.get(i);
                sel.setTitle(chapTitle.getText());
                sel.setPurpose(chapPurpose.getText());
                sel.setMajorEvents(chapEvents.getText());
                sel.setChoices(chapChoices.getText());
                sel.setEndingLead(chapEndingLead.getText());
                chapterJList.repaint();
                refreshTimelineTree();
            }
        });

        chapterJList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getTitle());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            l.setOpaque(true);
            return l;
        });

        return p;
    }

    private JPanel buildScenesTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Chapter:"));
        top.add(sceneChapterCombo);
        p.add(top, BorderLayout.NORTH);

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(sceneJList), BorderLayout.CENTER);
        JPanel sceneButtons = new JPanel();
        JButton addScene = new JButton("Add Scene");
        JButton removeScene = new JButton("Remove Scene");
        sceneButtons.add(addScene); sceneButtons.add(removeScene);
        left.add(sceneButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y=0;
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Scene Title:"), gbc); gbc.gridx=1; right.add(sceneTitleField, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Location:"), gbc); gbc.gridx=1; right.add(sceneLocationCombo, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Characters Present:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneCharactersField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Purpose:"), gbc); gbc.gridx=1; right.add(new JScrollPane(scenePurposeField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Events:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneEventsField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Dialogue / Story Notes:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneDialogueField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Choices:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneChoicesField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Leads To:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneLeadsToField), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Optional Notes:"), gbc); gbc.gridx=1; right.add(new JScrollPane(sceneNotesField), gbc);
        JButton applyScene = new JButton("Apply Scene");
        gbc.gridy = y++; gbc.gridx=0; gbc.gridwidth = 2; right.add(applyScene, gbc);

        left.setPreferredSize(new Dimension(240, 0));
        left.setMinimumSize(new Dimension(240, 0));
        p.add(left, BorderLayout.WEST);
        p.add(new JScrollPane(right), BorderLayout.CENTER);

        sceneChapterCombo.addActionListener(e -> {
            Chapter selectedChapter = (Chapter) sceneChapterCombo.getSelectedItem();
            sceneListModel.clear();
            if (selectedChapter != null) {
                if (selectedChapter.getScenes() != null) {
                    for (Scene scene : selectedChapter.getScenes()) sceneListModel.addElement(scene);
                }
                if (!sceneListModel.isEmpty()) sceneJList.setSelectedIndex(0);
                else clearSceneEditor();
            } else {
                clearSceneEditor();
            }
        });

        addScene.addActionListener(e -> {
            Chapter selectedChapter = (Chapter) sceneChapterCombo.getSelectedItem();
            if (selectedChapter == null) {
                JOptionPane.showMessageDialog(this, "Select a chapter before adding a scene.");
                return;
            }
            Scene scene = new Scene();
            scene.setTitle("New Scene");
            if (selectedChapter.getScenes() == null) {
                selectedChapter.setScenes(new java.util.ArrayList<>());
            }
            selectedChapter.getScenes().add(scene);
            sceneListModel.addElement(scene);
            sceneJList.setSelectedIndex(sceneListModel.size() - 1);
            refreshTimelineTree();
        });

        removeScene.addActionListener(e -> {
            Chapter selectedChapter = (Chapter) sceneChapterCombo.getSelectedItem();
            int i = sceneJList.getSelectedIndex();
            if (selectedChapter != null && i >= 0) {
                selectedChapter.getScenes().remove(i);
                sceneListModel.remove(i);
                if (sceneListModel.isEmpty()) clearSceneEditor();
                refreshTimelineTree();
            }
        });

        sceneJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sceneJList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Scene sel = sceneJList.getSelectedValue();
            if (sel != null) {
                sceneTitleField.setText(sel.getTitle());
                populateSceneLocationCombo();
                sceneLocationCombo.setSelectedItem(sel.getLocation());
                sceneCharactersField.setText(sel.getCharactersPresent());
                scenePurposeField.setText(sel.getPurpose());
                sceneEventsField.setText(sel.getEvents());
                sceneDialogueField.setText(sel.getDialogueNotes());
                sceneChoicesField.setText(sel.getChoices());
                sceneLeadsToField.setText(sel.getLeadsTo());
                sceneNotesField.setText(sel.getNotes());
            }
        });

        applyScene.addActionListener(e -> {
            int i = sceneJList.getSelectedIndex();
            if (i >= 0) {
                Scene sel = sceneListModel.get(i);
                sel.setTitle(sceneTitleField.getText());
                sel.setLocation((String) sceneLocationCombo.getSelectedItem());
                sel.setCharactersPresent(sceneCharactersField.getText());
                sel.setPurpose(scenePurposeField.getText());
                sel.setEvents(sceneEventsField.getText());
                sel.setDialogueNotes(sceneDialogueField.getText());
                sel.setChoices(sceneChoicesField.getText());
                sel.setLeadsTo(sceneLeadsToField.getText());
                sel.setNotes(sceneNotesField.getText());
                sceneJList.repaint();
                refreshTimelineTree();
            }
        });

        sceneJList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.toSummary());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            l.setOpaque(true);
            return l;
        });

        refreshSceneChapterOptions();
        return p;
    }

    private void clearSceneEditor() {
        sceneTitleField.setText("");
        populateSceneLocationCombo();
        sceneCharactersField.setText("");
        scenePurposeField.setText("");
        sceneEventsField.setText("");
        sceneDialogueField.setText("");
        sceneChoicesField.setText("");
        sceneLeadsToField.setText("");
        sceneNotesField.setText("");
    }

    private void populateSceneLocationCombo() {
        sceneLocationCombo.removeAllItems();
        sceneLocationCombo.addItem("");
        for (int i = 0; i < locationListModel.size(); i++) {
            WorldLocation loc = locationListModel.getElementAt(i);
            if (loc != null && loc.getName() != null && !loc.getName().trim().isEmpty()) {
                sceneLocationCombo.addItem(loc.getName());
            }
        }
    }

    private JComponent buildChoicesTab() {
        JPanel p = new JPanel(new BorderLayout());

        choiceRootNode = new javax.swing.tree.DefaultMutableTreeNode("Choices");
        startNode = new javax.swing.tree.DefaultMutableTreeNode(new com.vnplanner.model.ChoiceNode("Start"));
        choiceRootNode.add(startNode);
        choiceTreeModel = new javax.swing.tree.DefaultTreeModel(choiceRootNode);
        choiceTree = new JTree(choiceTreeModel);
        choiceTree.setRootVisible(false);
        choiceTree.setShowsRootHandles(true);
        JScrollPane treeScroll = new JScrollPane(choiceTree);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        JButton addBranch = new JButton("Add Branch");
        JButton addEnding = new JButton("Add Ending");
        JButton toggleEnding = new JButton("Toggle Ending");
        JButton rename = new JButton("Rename");
        JButton remove = new JButton("Remove");
        controls.add(addBranch); controls.add(addEnding); controls.add(toggleEnding); controls.add(rename); controls.add(remove);

        topPanel.add(new JLabel("Choice Mind-Map"), BorderLayout.NORTH);
        topPanel.add(controls, BorderLayout.SOUTH);

        p.add(topPanel, BorderLayout.NORTH);
        // place the tree in a center-left position with a slight right offset and larger size
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST; // left-center vertically
        gbc.insets = new Insets(0, 30, 0, 0); // shift slightly right from left edge
        treeScroll.setPreferredSize(new Dimension(600, 420));
        centerPanel.add(treeScroll, gbc);
        p.add(centerPanel, BorderLayout.CENTER);

        // Actions
        addBranch.addActionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) choiceTree.getLastSelectedPathComponent();
            if (sel == null || sel == choiceRootNode) sel = startNode;
            // prevent adding under an ending
            Object su = sel.getUserObject();
            if (su instanceof com.vnplanner.model.ChoiceNode && ((com.vnplanner.model.ChoiceNode) su).isEnding()) {
                JOptionPane.showMessageDialog(this, "Cannot add children to an ending node. Select a non-ending node.");
                return;
            }
            String text = JOptionPane.showInputDialog(this, "Branch text:", "New Branch");
            if (text != null) {
                com.vnplanner.model.ChoiceNode cn = new com.vnplanner.model.ChoiceNode(text);
                javax.swing.tree.DefaultMutableTreeNode node = new javax.swing.tree.DefaultMutableTreeNode(cn);
                sel.add(node);
                choiceTreeModel.reload(sel);
                // expand and center start node
                SwingUtilities.invokeLater(() -> {
                    expandAll(choiceTree, new javax.swing.tree.TreePath(choiceRootNode.getPath()));
                    centerNode(startNode);
                });
            }
        });

        addEnding.addActionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) choiceTree.getLastSelectedPathComponent();
            if (sel == null || sel == choiceRootNode) sel = startNode;
            // prevent adding ending under an ending
            Object su2 = sel.getUserObject();
            if (su2 instanceof com.vnplanner.model.ChoiceNode && ((com.vnplanner.model.ChoiceNode) su2).isEnding()) {
                JOptionPane.showMessageDialog(this, "Cannot add children to an ending node. Select a non-ending node.");
                return;
            }
            String text = JOptionPane.showInputDialog(this, "Ending text:", "New Ending");
            if (text != null) {
                com.vnplanner.model.ChoiceNode cn = new com.vnplanner.model.ChoiceNode(text);
                cn.setEnding(true);
                javax.swing.tree.DefaultMutableTreeNode node = new javax.swing.tree.DefaultMutableTreeNode(cn);
                sel.add(node);
                choiceTreeModel.reload(sel);
                SwingUtilities.invokeLater(() -> {
                    expandAll(choiceTree, new javax.swing.tree.TreePath(choiceRootNode.getPath()));
                    centerNode(startNode);
                });
            }
        });

        rename.addActionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) choiceTree.getLastSelectedPathComponent();
            if (sel == null || sel == choiceRootNode || sel == startNode) return;
            Object u = sel.getUserObject();
            String old = u==null?"":u.toString();
            String text = JOptionPane.showInputDialog(this, "Rename:", old);
            if (text != null) {
                if (u instanceof com.vnplanner.model.ChoiceNode) {
                    ((com.vnplanner.model.ChoiceNode) u).setText(text);
                } else {
                    sel.setUserObject(text);
                }
                choiceTreeModel.nodeChanged(sel);
            }
        });

        remove.addActionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) choiceTree.getLastSelectedPathComponent();
            if (sel == null || sel == choiceRootNode || sel == startNode) return;
            javax.swing.tree.DefaultMutableTreeNode parent = (javax.swing.tree.DefaultMutableTreeNode) sel.getParent();
            if (parent != null) {
                sel.removeFromParent();
                choiceTreeModel.reload(parent);
                SwingUtilities.invokeLater(() -> {
                    expandAll(choiceTree, new javax.swing.tree.TreePath(choiceRootNode.getPath()));
                    centerNode(startNode);
                });
            }
        });

        toggleEnding.addActionListener(e -> {
            javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) choiceTree.getLastSelectedPathComponent();
            if (sel == null || sel == choiceRootNode || sel == startNode) return;
            Object u = sel.getUserObject();
            if (u instanceof com.vnplanner.model.ChoiceNode) {
                com.vnplanner.model.ChoiceNode cn = (com.vnplanner.model.ChoiceNode) u;
                boolean wantToSetEnding = !cn.isEnding();
                if (wantToSetEnding && sel.getChildCount() > 0) {
                    JOptionPane.showMessageDialog(this, "Cannot mark as ending while node has children. Remove children first.");
                    return;
                }
                cn.setEnding(wantToSetEnding);
                choiceTreeModel.nodeChanged(sel);
                SwingUtilities.invokeLater(() -> {
                    expandAll(choiceTree, new javax.swing.tree.TreePath(choiceRootNode.getPath()));
                    centerNode(startNode);
                });
            }
        });

        // custom renderer to prefix node labels and mark endings/start
        choiceTree.setCellRenderer((tree, value, sel, expanded, leaf, row, hasFocus) -> {
            javax.swing.tree.DefaultTreeCellRenderer r = new javax.swing.tree.DefaultTreeCellRenderer();
            r.setOpaque(true);
            Object u = ((javax.swing.tree.DefaultMutableTreeNode) value).getUserObject();
            if (value == startNode) {
                // show start node text without prefix
                String t = "";
                if (u instanceof com.vnplanner.model.ChoiceNode) t = ((com.vnplanner.model.ChoiceNode) u).getText();
                r.setText(t);
                r.setIcon(r.getLeafIcon());
            } else if (u instanceof com.vnplanner.model.ChoiceNode) {
                com.vnplanner.model.ChoiceNode cn = (com.vnplanner.model.ChoiceNode) u;
                String prefix = cn.isEnding() ? "Ending: " : "Branch: ";
                r.setText(prefix + cn.getText());
                r.setIcon(cn.isEnding() ? r.getLeafIcon() : r.getOpenIcon());
            } else {
                r.setText(value.toString());
            }
            return r;
        });

        choiceTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    javax.swing.tree.TreePath tp = choiceTree.getPathForLocation(e.getX(), e.getY());
                    if (tp != null) {
                        javax.swing.tree.DefaultMutableTreeNode sel = (javax.swing.tree.DefaultMutableTreeNode) tp.getLastPathComponent();
                        if (sel != null && sel != choiceRootNode && sel != startNode) {
                            Object u = sel.getUserObject();
                            String old = u==null?"":u.toString();
                            String text = JOptionPane.showInputDialog(VNPlannerFrame.this, "Edit:", old);
                            if (text != null) {
                                if (u instanceof com.vnplanner.model.ChoiceNode) ((com.vnplanner.model.ChoiceNode) u).setText(text);
                                else sel.setUserObject(text);
                                choiceTreeModel.nodeChanged(sel);
                            }
                        }
                    }
                }
            }
        });

        return p;
    }

    private JComponent buildWorldTab() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        locationJList.setFixedCellWidth(220);
        left.add(new JScrollPane(locationJList), BorderLayout.CENTER);
        JPanel leftButtons = new JPanel();
        JButton addLocation = new JButton("Add");
        JButton removeLocation = new JButton("Remove");
        leftButtons.add(addLocation); leftButtons.add(removeLocation);
        left.add(leftButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Name:"), gbc); gbc.gridx = 1; right.add(locationNameField, gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Type:"), gbc); gbc.gridx = 1; right.add(locationTypeField, gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Description:"), gbc); gbc.gridx = 1; right.add(new JScrollPane(locationDescriptionArea), gbc);
        gbc.gridy = y++; gbc.gridx = 0; right.add(new JLabel("Notes:"), gbc); gbc.gridx = 1; right.add(new JScrollPane(locationNotesArea), gbc);
        JButton applyLocation = new JButton("Apply");
        gbc.gridy = y++; gbc.gridx = 1; right.add(applyLocation, gbc);

        p.add(left, BorderLayout.WEST);
        p.add(new JScrollPane(right), BorderLayout.CENTER);

        addLocation.addActionListener(e -> {
            WorldLocation loc = new WorldLocation();
            loc.setName("New Location");
            locationListModel.addElement(loc);
            project.getLocations().add(loc);
            locationJList.setSelectedIndex(locationListModel.size() - 1);
        });

        removeLocation.addActionListener(e -> {
            int index = locationJList.getSelectedIndex();
            if (index >= 0) {
                project.getLocations().remove(index);
                locationListModel.remove(index);
            }
        });

        locationJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        locationJList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            WorldLocation selected = locationJList.getSelectedValue();
            if (selected == null) {
                clearLocationEditor();
                return;
            }
            locationNameField.setText(selected.getName());
            locationTypeField.setText(selected.getType());
            locationDescriptionArea.setText(selected.getDescription());
            locationNotesArea.setText(selected.getNotes());
        });

        applyLocation.addActionListener(e -> {
            int index = locationJList.getSelectedIndex();
            if (index < 0) return;
            WorldLocation selected = locationListModel.get(index);
            selected.setName(locationNameField.getText());
            selected.setType(locationTypeField.getText());
            selected.setDescription(locationDescriptionArea.getText());
            selected.setNotes(locationNotesArea.getText());
            locationJList.repaint();
        });

        locationJList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.getName());
            if (isSelected) label.setBackground(list.getSelectionBackground());
            label.setOpaque(true);
            return label;
        });

        refreshLocationDropdowns();
        return p;
    }

    private JComponent buildLoreTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.gridx=0; gbc.gridy=0;
        p.add(new JLabel("Setting:"), gbc); gbc.gridy++; p.add(new JScrollPane(settingArea), gbc);
        gbc.gridy++; p.add(new JLabel("Important Locations:"), gbc); gbc.gridy++; p.add(new JScrollPane(importantLocationsArea), gbc);
        gbc.gridy++; p.add(new JLabel("World Rules:"), gbc); gbc.gridy++; p.add(new JScrollPane(worldRulesArea), gbc);
        gbc.gridy++; p.add(new JLabel("Lore / History:"), gbc); gbc.gridy++; p.add(new JScrollPane(loreHistoryArea), gbc);
        gbc.gridy++; p.add(new JLabel("Secrets:"), gbc); gbc.gridy++; p.add(new JScrollPane(secretsArea), gbc);
        return new JScrollPane(p);
    }

    private JComponent buildPresentationTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.gridx=0; gbc.gridy=0;
        p.add(new JLabel("Visual Style:"), gbc); gbc.gridy++; p.add(new JScrollPane(visualStyleArea), gbc);
        gbc.gridy++; p.add(new JLabel("Music / Audio:"), gbc); gbc.gridy++; p.add(new JScrollPane(musicAudioArea), gbc);
        gbc.gridy++; p.add(new JLabel("UI / Presentation:"), gbc); gbc.gridy++; p.add(new JScrollPane(uiPresentationArea), gbc);
        gbc.gridy++; p.add(new JLabel("Inspirations:"), gbc); gbc.gridy++; p.add(new JScrollPane(inspirationsArea), gbc);
        return new JScrollPane(p);
    }

    private JComponent buildDevelopmentTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.gridx=0; gbc.gridy=0;
        p.add(new JLabel("Engine:"), gbc); gbc.gridy++; p.add(engineCombo, gbc);
        gbc.gridy++; p.add(new JLabel("Tools:"), gbc); gbc.gridy++; p.add(new JScrollPane(toolsArea), gbc);
        gbc.gridy++; p.add(new JLabel("Must-Have Features:"), gbc); gbc.gridy++; p.add(new JScrollPane(mustHaveArea), gbc);
        gbc.gridy++; p.add(new JLabel("Nice-to-Have:"), gbc); gbc.gridy++; p.add(new JScrollPane(niceToHaveArea), gbc);
        gbc.gridy++; p.add(new JLabel("Scope Limits:"), gbc); gbc.gridy++; p.add(new JScrollPane(scopeLimitsArea), gbc);
        return new JScrollPane(p);
    }
    private JPanel buildFreeNotesTab() { JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(freeNotesArea), BorderLayout.CENTER); return p; }

    private void refreshSceneChapterOptions() {
        Chapter selected = (Chapter) sceneChapterCombo.getSelectedItem();
        sceneChapterCombo.removeAllItems();
        for (Chapter chapter : project.getChapters()) {
            sceneChapterCombo.addItem(chapter);
        }
        if (selected != null && project.getChapters().contains(selected)) {
            sceneChapterCombo.setSelectedItem(selected);
        } else if (sceneChapterCombo.getItemCount() > 0) {
            sceneChapterCombo.setSelectedIndex(0);
        }
        if (sceneChapterCombo.getItemCount() == 0) {
            clearSceneEditor();
        }
    }

    private void refreshLocationDropdowns() {
        populateSceneLocationCombo();
    }

    private void clearLocationEditor() {
        locationNameField.setText("");
        locationTypeField.setText("");
        locationDescriptionArea.setText("");
        locationNotesArea.setText("");
    }

    private java.util.List<WorldLocation> getLocationListFromModel() {
        java.util.List<WorldLocation> result = new java.util.ArrayList<>();
        for (int i = 0; i < locationListModel.size(); i++) {
            result.add(locationListModel.getElementAt(i));
        }
        return result;
    }

    private JPanel labeledTextArea(String label) {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea a = new JTextArea(3,60);
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(new JScrollPane(a), BorderLayout.CENTER);
        return p;
    }

    private void newProject() {
        project = new Project();
        currentFile = null;
        characterListModel.clear();
        chapterListModel.clear();
        relationshipListModel.clear();
        timelineListModel.clear();
        clearFields();
        refreshTimelineTree();
    }

    private void clearFields() {
        titleField.setText(""); workingTitleField.setText(""); oneSentenceArea.setText(""); coreIdeaArea.setText(""); genreCombo.setSelectedItem("Other"); playerFeelArea.setText(""); freeNotesArea.setText("");
        centralConflictArea.setText(""); beginningArea.setText(""); middleArea.setText(""); revelationsArea.setText(""); climaxArea.setText(""); endingArea.setText("");
        protagonistNameField.setText(""); protagonistAgeField.setText(""); protagonistPersonalityArea.setText(""); protagonistWantArea.setText(""); protagonistNeedArea.setText(""); protagonistFearArea.setText(""); protagonistArcArea.setText("");
        amountOfChoiceCombo.setSelectedItem("Medium"); importantChoicesArea.setText(""); branchesArea.setText(""); differentEndingsArea.setText(""); endingRequirementsArea.setText("");
        // reset choice tree with a Start node
        choiceRootNode = new javax.swing.tree.DefaultMutableTreeNode("Choices");
        startNode = new javax.swing.tree.DefaultMutableTreeNode(new com.vnplanner.model.ChoiceNode("Start"));
        choiceRootNode.add(startNode);
        choiceTreeModel = new javax.swing.tree.DefaultTreeModel(choiceRootNode);
        if (choiceTree != null) choiceTree.setModel(choiceTreeModel);
        settingArea.setText(""); importantLocationsArea.setText(""); worldRulesArea.setText(""); loreHistoryArea.setText(""); secretsArea.setText("");
        locationListModel.clear();
        clearLocationEditor();
        refreshLocationDropdowns();
        sceneChapterCombo.removeAllItems();
        sceneListModel.clear();
        clearSceneEditor();
        visualStyleArea.setText(""); musicAudioArea.setText(""); uiPresentationArea.setText(""); inspirationsArea.setText("");
        engineCombo.setSelectedItem("");
        toolsArea.setText("");
        mustHaveArea.setText(""); niceToHaveArea.setText(""); scopeLimitsArea.setText("");
    }

    private void openProject() {
        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(this);
        if (r==JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                if (f.getName().toLowerCase().endsWith(".vnproj")) {
                    project = ProjectIO.loadFromVnproj(f);
                } else if (f.getName().toLowerCase().endsWith(".json")) {
                    project = ProjectIO.importJson(f);
                } else {
                    JOptionPane.showMessageDialog(this, "Unsupported file type.");
                    return;
                }
                currentFile = f;
                populateUIFromProject();
                JOptionPane.showMessageDialog(this, "Project loaded.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to open: " + ex.getMessage());
            }
        }
    }

    private void populateUIFromProject() {
        titleField.setText(project.getTitle());
        workingTitleField.setText(project.getWorkingTitle());
        oneSentenceArea.setText(project.getOneSentencePitch());
        coreIdeaArea.setText(project.getCoreIdea());
        genreCombo.setSelectedItem(project.getGenreTone());
        playerFeelArea.setText(project.getPlayerFeel());
        freeNotesArea.setText(project.getFreeNotes());

        centralConflictArea.setText(project.getCentralConflict());
        beginningArea.setText(project.getBeginning());
        middleArea.setText(project.getMiddle());
        revelationsArea.setText(project.getRevelations());
        climaxArea.setText(project.getClimax());
        endingArea.setText(project.getEnding());

        protagonistNameField.setText(project.getProtagonistName());
        protagonistAgeField.setText(project.getProtagonistAge());
        protagonistPersonalityArea.setText(project.getProtagonistPersonality());
        protagonistWantArea.setText(project.getProtagonistWant());
        protagonistNeedArea.setText(project.getProtagonistNeed());
        protagonistFearArea.setText(project.getProtagonistFear());
        protagonistArcArea.setText(project.getProtagonistArc());

        // Populate choices tree
        populateChoicesFromProject();

        amountOfChoiceCombo.setSelectedItem(project.getAmountOfChoice());
        importantChoicesArea.setText(project.getImportantChoices());
        branchesArea.setText(project.getBranches());
        differentEndingsArea.setText(project.getDifferentEndings());
        endingRequirementsArea.setText(project.getEndingRequirements());

        settingArea.setText(project.getSetting());
        importantLocationsArea.setText(project.getImportantLocations());
        worldRulesArea.setText(project.getWorldRules());
        loreHistoryArea.setText(project.getLoreHistory());
        secretsArea.setText(project.getSecrets());

        visualStyleArea.setText(project.getVisualStyle());
        musicAudioArea.setText(project.getMusicAudio());
        uiPresentationArea.setText(project.getUiPresentation());
        inspirationsArea.setText(project.getInspirations());

        engineCombo.setSelectedItem(project.getEngine() == null ? "" : project.getEngine());
        toolsArea.setText(project.getTools() == null ? "" : project.getTools());
        mustHaveArea.setText(project.getMustHave());
        niceToHaveArea.setText(project.getNiceToHave());
        scopeLimitsArea.setText(project.getScopeLimits());

        characterListModel.clear();
        for (CharacterData c : project.getCharacters()) characterListModel.addElement(c);
        relationshipListModel.clear();
        for (Relationship relationship : project.getRelationships()) relationshipListModel.addElement(relationship);
        timelineListModel.clear();
        for (TimelineEntry entry : project.getTimeline()) timelineListModel.addElement(entry);
        chapterListModel.clear();
        for (Chapter c : project.getChapters()) chapterListModel.addElement(c);
        refreshSceneChapterOptions();
        locationListModel.clear();
        for (WorldLocation loc : project.getLocations()) locationListModel.addElement(loc);
        refreshLocationDropdowns();
        refreshTimelineTree();
    }

    private void saveProject(boolean saveAs) {
        if (saveAs || currentFile==null) {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("New Visual Novel.vnproj"));
            int r = fc.showSaveDialog(this);
            if (r!=JFileChooser.APPROVE_OPTION) return;
            currentFile = fc.getSelectedFile();
            if (!currentFile.getName().toLowerCase().endsWith(".vnproj")) {
                currentFile = new File(currentFile.getAbsolutePath() + ".vnproj");
            }
        }

        updateProjectFromUI();

        try {
            ProjectIO.saveAsVnproj(currentFile, project);
            JOptionPane.showMessageDialog(this, "Saved to " + currentFile.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage());
        }
    }

    private void backupProject() {
        if (currentFile == null || !currentFile.exists()) {
            JOptionPane.showMessageDialog(this, "Save the project before creating a backup.");
            return;
        }
        updateProjectFromUI();
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            String baseName = currentFile.getName();
            int extension = baseName.lastIndexOf('.');
            if (extension > 0) baseName = baseName.substring(0, extension);
            File backup = new File(currentFile.getParentFile(), baseName + "-backup-" + timestamp + ".vnproj");
            Files.copy(currentFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(this, "Backup created at " + backup.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to create backup: " + ex.getMessage());
        }
    }

    private void updateProjectFromUI() {
        project.setTitle(titleField.getText());
        project.setWorkingTitle(workingTitleField.getText());
        project.setOneSentencePitch(oneSentenceArea.getText());
        project.setCoreIdea(coreIdeaArea.getText());
        Object gi = genreCombo.getSelectedItem();
        project.setGenreTone(gi==null?"":gi.toString());
        project.setPlayerFeel(playerFeelArea.getText());
        project.setFreeNotes(freeNotesArea.getText());
        // characters and chapters are already bound to project lists
        project.setCentralConflict(centralConflictArea.getText());
        project.setBeginning(beginningArea.getText());
        project.setMiddle(middleArea.getText());
        project.setRevelations(revelationsArea.getText());
        project.setClimax(climaxArea.getText());
        project.setEnding(endingArea.getText());

        project.setProtagonistName(protagonistNameField.getText());
        project.setProtagonistAge(protagonistAgeField.getText());
        project.setProtagonistPersonality(protagonistPersonalityArea.getText());
        project.setProtagonistWant(protagonistWantArea.getText());
        project.setProtagonistNeed(protagonistNeedArea.getText());
        project.setProtagonistFear(protagonistFearArea.getText());
        project.setProtagonistArc(protagonistArcArea.getText());

        Object ai = amountOfChoiceCombo.getSelectedItem();
        project.setAmountOfChoice(ai==null?"":ai.toString());
        project.setImportantChoices(importantChoicesArea.getText());
        project.setBranches(branchesArea.getText());
        project.setDifferentEndings(differentEndingsArea.getText());
        project.setEndingRequirements(endingRequirementsArea.getText());

        project.setSetting(settingArea.getText());
        project.setImportantLocations(importantLocationsArea.getText());
        project.setWorldRules(worldRulesArea.getText());
        project.setLoreHistory(loreHistoryArea.getText());
        project.setSecrets(secretsArea.getText());
        project.setLocations(getLocationListFromModel());

        project.setVisualStyle(visualStyleArea.getText());
        project.setMusicAudio(musicAudioArea.getText());
        project.setUiPresentation(uiPresentationArea.getText());
        project.setInspirations(inspirationsArea.getText());

        project.setEngine((String) engineCombo.getSelectedItem());
        project.setTools(toolsArea.getText());
        project.setMustHave(mustHaveArea.getText());
        project.setNiceToHave(niceToHaveArea.getText());
        project.setScopeLimits(scopeLimitsArea.getText());
        // Save choices from tree into project
        updateChoicesToProject();
    }

    private void importJson() {
        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(this);
        if (r==JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Project p = ProjectIO.importJson(f);
                project = p; currentFile = null; populateUIFromProject();
                JOptionPane.showMessageDialog(this, "Imported JSON project.");
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "Failed import: " + ex.getMessage()); }
        }
    }

    // Choices tree helpers
    private javax.swing.tree.DefaultMutableTreeNode toTreeNode(com.vnplanner.model.ChoiceNode cn) {
        javax.swing.tree.DefaultMutableTreeNode node = new javax.swing.tree.DefaultMutableTreeNode(cn);
        for (com.vnplanner.model.ChoiceNode child : cn.getChildren()) node.add(toTreeNode(child));
        return node;
    }

    private com.vnplanner.model.ChoiceNode toChoiceNode(javax.swing.tree.DefaultMutableTreeNode node) {
        Object u = node.getUserObject();
        com.vnplanner.model.ChoiceNode cn;
        if (u instanceof com.vnplanner.model.ChoiceNode) {
            com.vnplanner.model.ChoiceNode src = (com.vnplanner.model.ChoiceNode) u;
            cn = new com.vnplanner.model.ChoiceNode(src.getText());
            cn.setEnding(src.isEnding());
        } else {
            cn = new com.vnplanner.model.ChoiceNode(u==null?"":u.toString());
        }
        for (int i=0;i<node.getChildCount();i++) {
            javax.swing.tree.DefaultMutableTreeNode ch = (javax.swing.tree.DefaultMutableTreeNode) node.getChildAt(i);
            cn.getChildren().add(toChoiceNode(ch));
        }
        return cn;
    }

    private void populateChoicesFromProject() {
        choiceRootNode = new javax.swing.tree.DefaultMutableTreeNode("Choices");
        startNode = new javax.swing.tree.DefaultMutableTreeNode(new com.vnplanner.model.ChoiceNode("Start"));
        choiceRootNode.add(startNode);
        if (project.getChoices() != null) {
            for (com.vnplanner.model.ChoiceNode cn : project.getChoices()) startNode.add(toTreeNode(cn));
        }
        choiceTreeModel = new javax.swing.tree.DefaultTreeModel(choiceRootNode);
        if (choiceTree != null) {
            choiceTree.setModel(choiceTreeModel);
            SwingUtilities.invokeLater(() -> {
                expandAll(choiceTree, new javax.swing.tree.TreePath(choiceRootNode.getPath()));
                centerNode(startNode);
            });
        }
    }

    private void updateChoicesToProject() {
        java.util.List<com.vnplanner.model.ChoiceNode> list = new java.util.ArrayList<>();
        if (startNode != null) {
            for (int i=0;i<startNode.getChildCount();i++) {
                javax.swing.tree.DefaultMutableTreeNode ch = (javax.swing.tree.DefaultMutableTreeNode) startNode.getChildAt(i);
                list.add(toChoiceNode(ch));
            }
        }
        project.setChoices(list);
    }

    // expand all nodes under a given parent
    private void expandAll(JTree tree, javax.swing.tree.TreePath parent) {
        Object node = parent.getLastPathComponent();
        javax.swing.tree.TreeNode tn = (javax.swing.tree.TreeNode) node;
        if (tn.getChildCount() >= 0) {
            for (int i = 0; i < tn.getChildCount(); i++) {
                javax.swing.tree.TreeNode n = tn.getChildAt(i);
                javax.swing.tree.TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path);
            }
        }
        tree.expandPath(parent);
    }

    // center a given tree node in the tree's viewport
    private void centerNode(javax.swing.tree.DefaultMutableTreeNode node) {
        if (choiceTree == null || node == null) return;
        javax.swing.tree.TreePath path = new javax.swing.tree.TreePath(node.getPath());
        Rectangle nodeBounds = choiceTree.getPathBounds(path);
        if (nodeBounds == null) return;
        java.awt.Component c = SwingUtilities.getAncestorOfClass(JViewport.class, choiceTree);
        if (!(c instanceof JViewport)) return;
        JViewport vp = (JViewport) c;
        Dimension extentSize = vp.getExtentSize();
        Point viewPos = vp.getViewPosition();
        int px = nodeBounds.x - (extentSize.width / 2) + (nodeBounds.width / 2);
        int py = nodeBounds.y - (extentSize.height / 2) + (nodeBounds.height / 2);
        if (px < 0) px = 0;
        if (py < 0) py = 0;
        vp.setViewPosition(new Point(px, py));
    }

    private void exportJson() {
        JFileChooser fc = new JFileChooser();
        int r = fc.showSaveDialog(this);
        if (r==JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".json")) f = new File(f.getAbsolutePath()+".json");
            updateProjectFromUI();
            try { ProjectIO.exportJson(f, project); JOptionPane.showMessageDialog(this, "Exported JSON."); }
            catch (IOException ex) { JOptionPane.showMessageDialog(this, "Failed export: " + ex.getMessage()); }
        }
    }
}
