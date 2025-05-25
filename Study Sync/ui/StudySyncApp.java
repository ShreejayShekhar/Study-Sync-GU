package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for StudySync.
 * Initializes the GUI with Dashboard, Tasks, and Timetable tabs.
 */
public class StudySyncApp extends JFrame {

    public StudySyncApp() {
        // Set window title and size
        setTitle("StudySync - Weekly Planner & Timetable Assistant");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create tabbed pane for different sections
        JTabbedPane tabs = new JTabbedPane();

        // Create panels
        TaskPanel taskPanel = new TaskPanel();
        TimetablePanel timetablePanel = new TimetablePanel();
        DashboardPanel dashboardPanel = new DashboardPanel(
            taskPanel.getTaskManager(),
            timetablePanel.getTimetableManager()
        );

        // Add panels to tabs
        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Weekly Tasks", taskPanel);
        tabs.addTab("Timetable", timetablePanel);

        // Add tabbed pane to window
        add(tabs, BorderLayout.CENTER);
        setVisible(true); // Show window
    }

    // Program entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudySyncApp());
    }
}





