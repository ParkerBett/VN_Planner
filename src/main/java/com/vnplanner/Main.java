package com.vnplanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
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

            Runnable launchFrame = () -> {
                VNPlannerFrame frame = new VNPlannerFrame();
                // try to set window icon from images/icon.png
                try {
                    BufferedImage icon = null;
                    File icf = new File("images/icon.png");
                    if (icf.exists()) icon = ImageIO.read(icf);
                    else {
                        URL u = Main.class.getResource("/images/icon.png");
                        if (u != null) icon = ImageIO.read(u);
                    }
                    if (icon != null) frame.setIconImage(icon);
                } catch (Exception ignored) {}
                frame.setVisible(true);
            };

            // try to load splash image (images/loading_screen.png) and show for at least 3s with progress bar
            try {
                BufferedImage logo = null;
                File lf = new File("images/loading_screen.png");
                if (lf.exists()) logo = ImageIO.read(lf);
                else {
                    URL u = Main.class.getResource("/images/loading_screen.png");
                    if (u != null) logo = ImageIO.read(u);
                }
                if (logo != null) {
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                        int sw = Math.max(200, screen.width / 6); // one-sixth width (min 200)
                        int sh = Math.max(200, screen.height / 6); // one-sixth height (min 200)

                    JWindow splash = new JWindow();
                    splash.setSize(sw, sh);
                    splash.setLayout(new BorderLayout());

                    // progress bar at bottom
                    JProgressBar pb = new JProgressBar(0, 100);
                    pb.setStringPainted(false);
                    pb.setPreferredSize(new Dimension(sw, 24));

                    // scale logo to fit available area (leave room for progress bar)
                    int availW = sw - 20;
                    int availH = sh - pb.getPreferredSize().height - 20;
                    int imgW = logo.getWidth();
                    int imgH = logo.getHeight();
                    double scale = Math.min(availW / (double) imgW, availH / (double) imgH);
                    if (scale <= 0) scale = 1.0;
                    int newW = (int) Math.max(1, Math.round(imgW * scale));
                    int newH = (int) Math.max(1, Math.round(imgH * scale));
                    Image scaled = logo.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imgLabel.setVerticalAlignment(SwingConstants.CENTER);

                    splash.getContentPane().add(imgLabel, BorderLayout.CENTER);
                    splash.getContentPane().add(pb, BorderLayout.SOUTH);
                    splash.setLocationRelativeTo(null);
                    splash.setVisible(true);

                    // Timer to update progress over 3 seconds; ensure launch happens on EDT
                    final long start = System.currentTimeMillis();
                    final int duration = 3000; // ms
                    javax.swing.Timer timer = new javax.swing.Timer(40, null);
                    timer.addActionListener(evt -> {
                        long now = System.currentTimeMillis();
                        double frac = (now - start) / (double) duration;
                        if (frac > 1.0) frac = 1.0;
                        pb.setValue((int) Math.round(frac * 100));
                        if (frac >= 1.0) {
                            timer.stop();
                            splash.dispose();
                            SwingUtilities.invokeLater(launchFrame);
                        }
                    });
                    timer.setInitialDelay(0);
                    timer.start();
                } else {
                    SwingUtilities.invokeLater(launchFrame);
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(launchFrame);
            }
        });
    }
}
