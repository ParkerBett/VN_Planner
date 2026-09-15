package com.vnplanner;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;
import com.vnplanner.VNPlannerFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            // Slightly larger default font for readability
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("List.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);

            VNPlannerFrame frame = new VNPlannerFrame();
            frame.setVisible(true);
        });
    }
}
