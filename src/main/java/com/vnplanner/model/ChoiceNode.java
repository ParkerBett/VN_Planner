package com.vnplanner.model;

import java.util.ArrayList;
import java.util.List;

public class ChoiceNode {
    private String text = "";
    private boolean ending = false;
    private List<ChoiceNode> children = new ArrayList<>();

    public ChoiceNode() {}

    public ChoiceNode(String text) { this.text = text; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isEnding() { return ending; }
    public void setEnding(boolean ending) { this.ending = ending; }

    public List<ChoiceNode> getChildren() { return children; }
    public void setChildren(List<ChoiceNode> children) { this.children = children; }

    @Override
    public String toString() {
        return text + (ending ? " [Ending]" : "");
    }
}
