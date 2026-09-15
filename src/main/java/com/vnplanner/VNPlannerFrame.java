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
    // Autosave timer
    private javax.swing.Timer autosaveTimer;

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
        chapterJList.setPreferredSize(new Dimension(220, 400));
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

        project.setVisualStyle(visualStyleArea.getText());
        project.setMusicAudio(musicAudioArea.getText());
        project.setUiPresentation(uiPresentationArea.getText());
        project.setInspirations(inspirationsArea.getText());

        project.setEngineTools(engineToolsArea.getText());
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
