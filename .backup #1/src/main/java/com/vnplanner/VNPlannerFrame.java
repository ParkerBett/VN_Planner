package com.vnplanner;

import com.vnplanner.io.ProjectIO;
import com.vnplanner.model.CharacterData;
import com.vnplanner.model.Chapter;
import com.vnplanner.model.Project;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class VNPlannerFrame extends JFrame {
    private Project project = new Project();
    private File currentFile = null;

    // Project fields (tab 1)
    private JTextField titleField = new JTextField();
    private JTextField workingTitleField = new JTextField();
    private JTextArea oneSentenceArea = new JTextArea(3,40);
    private JTextArea coreIdeaArea = new JTextArea(5,40);
    private JTextField genreField = new JTextField();
    private JTextArea playerFeelArea = new JTextArea(3,40);

    // Story additional fields
    private JTextArea centralConflictArea = new JTextArea(3,60);
    private JTextArea beginningArea = new JTextArea(3,60);
    private JTextArea middleArea = new JTextArea(3,60);
    private JTextArea revelationsArea = new JTextArea(3,60);
    private JTextArea climaxArea = new JTextArea(3,60);
    private JTextArea endingArea = new JTextArea(3,60);
    private JTextArea whatChangesArea = new JTextArea(3,60);

    // Protagonist fields
    private JTextField protagonistNameField = new JTextField();
    private JTextField protagonistAgeField = new JTextField();
    private JTextArea protagonistPersonalityArea = new JTextArea(3,40);
    private JTextArea protagonistWantArea = new JTextArea(2,40);
    private JTextArea protagonistNeedArea = new JTextArea(2,40);
    private JTextArea protagonistFearArea = new JTextArea(2,40);
    private JTextArea protagonistArcArea = new JTextArea(3,40);

    // Choices & Endings fields
    private JTextField amountOfChoiceField = new JTextField();
    private JTextArea importantChoicesArea = new JTextArea(3,60);
    private JTextArea branchesArea = new JTextArea(3,60);
    private JTextArea differentEndingsArea = new JTextArea(3,60);
    private JTextArea endingRequirementsArea = new JTextArea(3,60);

    // World & Lore fields
    private JTextArea settingArea = new JTextArea(3,60);
    private JTextArea importantLocationsArea = new JTextArea(3,60);
    private JTextArea worldRulesArea = new JTextArea(3,60);
    private JTextArea loreHistoryArea = new JTextArea(3,60);
    private JTextArea secretsArea = new JTextArea(3,60);

    // Presentation fields
    private JTextArea visualStyleArea = new JTextArea(3,60);
    private JTextArea musicAudioArea = new JTextArea(3,60);
    private JTextArea uiPresentationArea = new JTextArea(3,60);
    private JTextArea inspirationsArea = new JTextArea(3,60);

    // Development fields
    private JTextArea engineToolsArea = new JTextArea(3,60);
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

    // Free notes
    private JTextArea freeNotesArea = new JTextArea(10,60);

    public VNPlannerFrame() {
        super("Visual Novel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);

        createMenuBar();
        initTabs();
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
        file.addSeparator();
        file.add(exitItem);

        mb.add(file);
        setJMenuBar(mb);
    }

    private void initTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Project", buildProjectTab());
        tabs.addTab("Story", buildStoryTab());
        tabs.addTab("Protagonist", buildProtagonistTab());
        tabs.addTab("Characters", buildCharactersTab());
        tabs.addTab("Chapters", buildChaptersTab());
        tabs.addTab("Choices & Endings", buildChoicesTab());
        tabs.addTab("World & Lore", buildWorldTab());
        tabs.addTab("Presentation", buildPresentationTab());
        tabs.addTab("Development", buildDevelopmentTab());
        tabs.addTab("Free Notes", buildFreeNotesTab());

        getContentPane().add(tabs, BorderLayout.CENTER);
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
        gbc.gridx = 1; fields.add(genreField, gbc);

        gbc.gridy = y++;
        gbc.gridx = 0; fields.add(new JLabel("Player Feel:"), gbc);
        gbc.gridx = 1; fields.add(new JScrollPane(playerFeelArea), gbc);

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
        gbc.gridy++; grid.add(new JLabel("What Changes:"), gbc);
        gbc.gridy++; grid.add(new JScrollPane(whatChangesArea), gbc);
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
        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(chapterJList), BorderLayout.CENTER);
        JPanel leftButtons = new JPanel();
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");
        leftButtons.add(add); leftButtons.add(remove);
        left.add(leftButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.HORIZONTAL;
        int y=0;
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Title:"), gbc); gbc.gridx=1; right.add(chapTitle, gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Purpose:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapPurpose), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Major Events:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapEvents), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Choices / Interaction:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapChoices), gbc);
        gbc.gridy = y++; gbc.gridx=0; right.add(new JLabel("Ending / Lead Into Next:"), gbc); gbc.gridx=1; right.add(new JScrollPane(chapEndingLead), gbc);
        JButton apply = new JButton("Apply"); gbc.gridy = y++; gbc.gridx=1; right.add(apply, gbc);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.CENTER);

        add.addActionListener(e -> {
            Chapter c = new Chapter();
            c.setTitle("New Chapter");
            chapterListModel.addElement(c);
            project.getChapters().add(c);
            chapterJList.setSelectedIndex(chapterListModel.size()-1);
        });
        remove.addActionListener(e -> {
            int i = chapterJList.getSelectedIndex();
            if (i>=0) { chapterListModel.remove(i); project.getChapters().remove(i); }
        });

        chapterJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chapterJList.addListSelectionListener(e -> {
            Chapter sel = chapterJList.getSelectedValue();
            if (sel!=null) {
                chapTitle.setText(sel.getTitle());
                chapPurpose.setText(sel.getPurpose());
                chapEvents.setText(sel.getMajorEvents());
                chapChoices.setText(sel.getChoices());
                chapEndingLead.setText(sel.getEndingLead());
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

    private JComponent buildChoicesTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.fill = GridBagConstraints.BOTH; gbc.weightx=1.0; gbc.gridx=0; gbc.gridy=0;
        p.add(new JLabel("Amount of Player Choice:"), gbc); gbc.gridy++; p.add(amountOfChoiceField, gbc);
        gbc.gridy++; p.add(new JLabel("Important Choices:"), gbc); gbc.gridy++; p.add(new JScrollPane(importantChoicesArea), gbc);
        gbc.gridy++; p.add(new JLabel("Branches:"), gbc); gbc.gridy++; p.add(new JScrollPane(branchesArea), gbc);
        gbc.gridy++; p.add(new JLabel("Different Endings:"), gbc); gbc.gridy++; p.add(new JScrollPane(differentEndingsArea), gbc);
        gbc.gridy++; p.add(new JLabel("Ending Requirements:"), gbc); gbc.gridy++; p.add(new JScrollPane(endingRequirementsArea), gbc);
        return new JScrollPane(p);
    }

    private JComponent buildWorldTab() {
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
        p.add(new JLabel("Engine / Tools:"), gbc); gbc.gridy++; p.add(new JScrollPane(engineToolsArea), gbc);
        gbc.gridy++; p.add(new JLabel("Must-Have Features:"), gbc); gbc.gridy++; p.add(new JScrollPane(mustHaveArea), gbc);
        gbc.gridy++; p.add(new JLabel("Nice-to-Have:"), gbc); gbc.gridy++; p.add(new JScrollPane(niceToHaveArea), gbc);
        gbc.gridy++; p.add(new JLabel("Scope Limits:"), gbc); gbc.gridy++; p.add(new JScrollPane(scopeLimitsArea), gbc);
        return new JScrollPane(p);
    }
    private JPanel buildFreeNotesTab() { JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(freeNotesArea), BorderLayout.CENTER); return p; }

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
        clearFields();
    }

    private void clearFields() {
        titleField.setText(""); workingTitleField.setText(""); oneSentenceArea.setText(""); coreIdeaArea.setText(""); genreField.setText(""); playerFeelArea.setText(""); freeNotesArea.setText("");
        centralConflictArea.setText(""); beginningArea.setText(""); middleArea.setText(""); revelationsArea.setText(""); climaxArea.setText(""); endingArea.setText(""); whatChangesArea.setText("");
        protagonistNameField.setText(""); protagonistAgeField.setText(""); protagonistPersonalityArea.setText(""); protagonistWantArea.setText(""); protagonistNeedArea.setText(""); protagonistFearArea.setText(""); protagonistArcArea.setText("");
        amountOfChoiceField.setText(""); importantChoicesArea.setText(""); branchesArea.setText(""); differentEndingsArea.setText(""); endingRequirementsArea.setText("");
        settingArea.setText(""); importantLocationsArea.setText(""); worldRulesArea.setText(""); loreHistoryArea.setText(""); secretsArea.setText("");
        visualStyleArea.setText(""); musicAudioArea.setText(""); uiPresentationArea.setText(""); inspirationsArea.setText("");
        engineToolsArea.setText(""); mustHaveArea.setText(""); niceToHaveArea.setText(""); scopeLimitsArea.setText("");
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
        genreField.setText(project.getGenreTone());
        playerFeelArea.setText(project.getPlayerFeel());
        freeNotesArea.setText(project.getFreeNotes());

        centralConflictArea.setText(project.getCentralConflict());
        beginningArea.setText(project.getBeginning());
        middleArea.setText(project.getMiddle());
        revelationsArea.setText(project.getRevelations());
        climaxArea.setText(project.getClimax());
        endingArea.setText(project.getEnding());
        whatChangesArea.setText(project.getWhatChanges());

        protagonistNameField.setText(project.getProtagonistName());
        protagonistAgeField.setText(project.getProtagonistAge());
        protagonistPersonalityArea.setText(project.getProtagonistPersonality());
        protagonistWantArea.setText(project.getProtagonistWant());
        protagonistNeedArea.setText(project.getProtagonistNeed());
        protagonistFearArea.setText(project.getProtagonistFear());
        protagonistArcArea.setText(project.getProtagonistArc());

        amountOfChoiceField.setText(project.getAmountOfChoice());
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

        engineToolsArea.setText(project.getEngineTools());
        mustHaveArea.setText(project.getMustHave());
        niceToHaveArea.setText(project.getNiceToHave());
        scopeLimitsArea.setText(project.getScopeLimits());

        characterListModel.clear();
        for (CharacterData c : project.getCharacters()) characterListModel.addElement(c);
        chapterListModel.clear();
        for (Chapter c : project.getChapters()) chapterListModel.addElement(c);
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

    private void updateProjectFromUI() {
        project.setTitle(titleField.getText());
        project.setWorkingTitle(workingTitleField.getText());
        project.setOneSentencePitch(oneSentenceArea.getText());
        project.setCoreIdea(coreIdeaArea.getText());
        project.setGenreTone(genreField.getText());
        project.setPlayerFeel(playerFeelArea.getText());
        project.setFreeNotes(freeNotesArea.getText());
        // characters and chapters are already bound to project lists
        project.setCentralConflict(centralConflictArea.getText());
        project.setBeginning(beginningArea.getText());
        project.setMiddle(middleArea.getText());
        project.setRevelations(revelationsArea.getText());
        project.setClimax(climaxArea.getText());
        project.setEnding(endingArea.getText());
        project.setWhatChanges(whatChangesArea.getText());

        project.setProtagonistName(protagonistNameField.getText());
        project.setProtagonistAge(protagonistAgeField.getText());
        project.setProtagonistPersonality(protagonistPersonalityArea.getText());
        project.setProtagonistWant(protagonistWantArea.getText());
        project.setProtagonistNeed(protagonistNeedArea.getText());
        project.setProtagonistFear(protagonistFearArea.getText());
        project.setProtagonistArc(protagonistArcArea.getText());

        project.setAmountOfChoice(amountOfChoiceField.getText());
        project.setImportantChoices(importantChoicesArea.getText());
        project.setBranches(branchesArea.getText());
        project.setDifferentEndings(differentEndingsArea.getText());
        project.setEndingRequirements(endingRequirementsArea.getText());

        project.setSetting(settingArea.getText());
        project.setImportantLocations(importantLocationsArea.getText());
        project.setWorldRules(worldRulesArea.getText());
        project.setLoreHistory(loreHistoryArea.getText());
        project.setSecrets(secretsArea.getText());

        project.setVisualStyle(visualStyleArea.getText());
        project.setMusicAudio(musicAudioArea.getText());
        project.setUiPresentation(uiPresentationArea.getText());
        project.setInspirations(inspirationsArea.getText());

        project.setEngineTools(engineToolsArea.getText());
        project.setMustHave(mustHaveArea.getText());
        project.setNiceToHave(niceToHaveArea.getText());
        project.setScopeLimits(scopeLimitsArea.getText());
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
