package ui;

import model.Task;
import manager.TaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.HashSet;

/**
 * TaskPanel – GUI tab for adding tasks and tracking weekly progress.
 */
public class TaskPanel extends JPanel {

    private final TaskManager taskManager = new TaskManager();
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final JTextField taskField;
    private final JCheckBox[] dayCheckboxes;

    public TaskPanel() {
        setLayout(new BorderLayout());

        // === Input panel for adding tasks ===
        JPanel inputPanel = new JPanel(new FlowLayout());
        taskField = new JTextField(20);
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);

        // === Checkboxes for assigning task to specific days ===
        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        dayCheckboxes = new JCheckBox[7];
        for (int i = 0; i < days.length; i++) {
            dayCheckboxes[i] = new JCheckBox(days[i]);
            inputPanel.add(dayCheckboxes[i]);
        }

        // === Button to add the task ===
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        inputPanel.add(addButton);
        add(inputPanel, BorderLayout.NORTH);

        // === Table to display all tasks ===
        String[] columns = { "Task", "Assigned Days", "Completed Days" };
        tableModel = new DefaultTableModel(columns, 0);
        taskTable = new JTable(tableModel);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // === Panel and button to mark task as completed ===
        JPanel completePanel = new JPanel(new FlowLayout());
        JButton markButton = new JButton("Mark Completed");
        completePanel.add(new JLabel("Mark Task for Day:"));
        completePanel.add(markButton);
        markButton.addActionListener(e -> markTaskComplete());
        add(completePanel, BorderLayout.SOUTH);
    }

    /**
     * Adds a new task based on input and selected days.
     */
    private void addTask() {
        String title = taskField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task title cannot be empty.");
            return;
        }

        HashSet<String> assignedDays = new HashSet<>();
        for (JCheckBox cb : dayCheckboxes) {
            if (cb.isSelected()) {
                assignedDays.add(cb.getText());
            }
        }

        if (assignedDays.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one day.");
            return;
        }

        taskManager.addTask(title, assignedDays);
        refreshTable();
        taskField.setText(""); // clear input
        for (JCheckBox cb : dayCheckboxes)
            cb.setSelected(false); // uncheck days
    }

    /**
     * Marks the selected task as complete for a selected day.
     */
    private void markTaskComplete() {
        int selectedRow = taskTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a task from the table.");
            return;
        }

        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        JComboBox<String> daySelector = new JComboBox<>(days);
        int option = JOptionPane.showConfirmDialog(this, daySelector, "Select Day", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedDay = daySelector.getSelectedItem().toString();
            boolean success = taskManager.markTaskCompleted(selectedRow, selectedDay);
            if (success) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Day not assigned to this task.");
            }
        }
    }

    /**
     * Updates the task table to reflect current task list.
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // clear table
        List<Task> tasks = taskManager.getTasks();
        for (Task t : tasks) {
            tableModel.addRow(new Object[] {
                    t.getTitle(),
                    String.join(", ", t.getAssignedDays()),
                    String.join(", ", t.getCompletedDays())
            });
        }
    }

    // Getter for sharing taskManager with other panels
    public TaskManager getTaskManager() {
        return taskManager;
    }
}
