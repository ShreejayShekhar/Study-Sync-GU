package ui; // This class is inside the 'ui' package

import javax.swing.*; // Swing GUI classes
import java.awt.*;     // For layout (BorderLayout)
import com.formdev.flatlaf.FlatLightLaf; // For modern flat UI style (VisionOS-like)

import manager.TaskManager; // Logic manager for tasks
import manager.TimetableManager; // Logic manager for timetable

public class StudySyncApp extends JFrame { // Main GUI window (inherits JFrame)

    public StudySyncApp() { // Constructor to build the window
        try {
            // Set a modern FlatLaf theme (like iOS / VisionOS)
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf"); // Print error if theme fails
        }

        setTitle("StudySync - Weekly Planner & Timetable Assistant"); // Window title
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window is closed
        setLocationRelativeTo(null); // Center window on screen

        // Step 1: Create shared logic managers
        TaskManager sharedTaskManager = new TaskManager(); // For task handling
        TimetableManager sharedTimetableManager = new TimetableManager(); // For timetable

        // Step 2: Pass shared logic to GUI panels
        TaskPanel taskPanel = new TaskPanel(sharedTaskManager);
        TimetablePanel timetablePanel = new TimetablePanel(sharedTimetableManager);
        DashboardPanel dashboardPanel = new DashboardPanel(sharedTaskManager, sharedTimetableManager);

        // Step 3: Create tab system
        JTabbedPane tabs = new JTabbedPane(); // Like tabs in a browser

        tabs.addTab("Dashboard", dashboardPanel); // Tab for Dashboard
        tabs.addTab("Weekly Tasks", taskPanel);   // Tab for Weekly Tasks
        tabs.addTab("Timetable", timetablePanel); // Tab for Timetable

        // Step 4: Refresh dashboard when user switches to it
        tabs.addChangeListener(e -> {
            int selectedIndex = tabs.getSelectedIndex(); // Which tab is selected
            String selectedTab = tabs.getTitleAt(selectedIndex); // Get tab name

            if (selectedTab.equals("Dashboard")) {
                dashboardPanel.refreshDashboard(); // Refresh data shown on dashboard
            }
        });

        // Step 5: Add tabs to the window
        add(tabs, BorderLayout.CENTER);

        // Step 6: Show window on screen
        setVisible(true);
    }

    // MAIN method — runs when app starts
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudySyncApp()); // Launch GUI safely on UI thread
    }
}






