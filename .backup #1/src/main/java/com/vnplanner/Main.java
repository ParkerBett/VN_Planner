package com.vnplanner;

import javax.swing.SwingUtilities;
import com.vnplanner.VNPlannerFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VNPlannerFrame frame = new VNPlannerFrame();
            frame.setVisible(true);
        });
    }
}
