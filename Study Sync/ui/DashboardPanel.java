package ui;

import manager.TaskManager;
import manager.TimetableManager;
import model.Task;
import model.TimetableEntry;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Dashboard panel shows daily task progress and the next class.
 */
public class DashboardPanel extends JPanel {

    private final TaskManager taskManager;
    private final TimetableManager timetableManager;
    private final JLabel taskSummaryLabel;   // Shows task completion status
    private final JLabel nextClassLabel;     // Shows next class info

    public DashboardPanel(TaskManager taskManager, TimetableManager timetableManager) {
        this.taskManager = taskManager;
        this.timetableManager = timetableManager;

        // Layout and spacing
        setLayout(new GridLayout(2, 1, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Initialize labels
        taskSummaryLabel = new JLabel("Task Summary will appear here.");
        taskSummaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(taskSummaryLabel);

        nextClassLabel = new JLabel("Next class will appear here.");
        nextClassLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(nextClassLabel);

        // Load today's data
        refreshDashboard();
    }

    /**
     * Updates the dashboard with today's task progress and timetable.
     */
    public void refreshDashboard() {
        // Get today's day as "Mon", "Tue", etc.
        String today = LocalDate.now().getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        int total = 0;
        int done = 0;

        // Count assigned and completed tasks for today
        for (Task t : taskManager.getTasks()) {
            if (t.getAssignedDays().contains(today)) {
                total++;
                if (t.getCompletedDays().contains(today)) {
                    done++;
                }
            }
        }

        taskSummaryLabel.setText("Today: " + done + " of " + total + " tasks completed.");

        // Get and show today's next class
        Map<String, List<TimetableEntry>> map = timetableManager.getGroupedByDay();
        List<TimetableEntry> todayClasses = map.get(today);

        if (todayClasses == null || todayClasses.isEmpty()) {
            nextClassLabel.setText("No class scheduled today.");
        } else {
            nextClassLabel.setText("Next class: " + todayClasses.get(0).getSubject() +
                    " at " + todayClasses.get(0).getTime());
        }
    }
}
