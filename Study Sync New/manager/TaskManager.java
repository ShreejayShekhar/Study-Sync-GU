package manager;

import model.Task;
import java.util.*;

/**
 * Manages the list of tasks – adding, updating, and retrieving.
 */
public class TaskManager {

    // Stores all tasks
    private final List<Task> taskList;

    // Constructor – initializes empty task list
    public TaskManager() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Adds a new task with given title and assigned days.
     */
    public void addTask(String title, HashSet<String> days) {
        taskList.add(new Task(title, days));
    }

    /**
     * Marks a task as complete for a specific day.
     */
    public boolean markTaskCompleted(int index, String day) {
        if (index >= 0 && index < taskList.size()) {
            taskList.get(index).markCompleted(day);
            return true;
        }
        return false;
    }

    /**
     * Returns all tasks.
     */
    public List<Task> getTasks() {
        return taskList;
    }
}

