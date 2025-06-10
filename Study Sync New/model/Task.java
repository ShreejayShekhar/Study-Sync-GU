package model;

import java.util.HashSet;

/**
 * Represents a single task with assigned and completed days.
 */
public class Task {
    private String title;
    private HashSet<String> assignedDays;
    private HashSet<String> completedDays;

    // Constructor – initializes task with title and assigned days
    public Task(String title, HashSet<String> assignedDays) {
        this.title = title;
        this.assignedDays = assignedDays;
        this.completedDays = new HashSet<>();
    }

    // Returns the task title
    public String getTitle() {
        return title;
    }

    // Returns the days the task is assigned to
    public HashSet<String> getAssignedDays() {
        return assignedDays;
    }

    // Returns the days the task is marked completed
    public HashSet<String> getCompletedDays() {
        return completedDays;
    }

    // Marks task complete for a specific day (if it's assigned)
    public void markCompleted(String day) {
        if (assignedDays.contains(day)) {
            completedDays.add(day);
        }
    }

    // Checks if task is completed for a specific day
    public boolean isCompleted(String day) {
        return completedDays.contains(day);
    }
}

