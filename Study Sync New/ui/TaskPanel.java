package ui;

import model.Task;
import manager.TaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.HashSet;

public class TaskPanel extends JPanel {

    private final TaskManager taskManager; // Logic manager to store and track tasks
    private final DefaultTableModel tableModel; // Table model to populate JTable dynamically
    private final JTable taskTable; // Table that displays tasks
    private final JTextField taskField; // Input field for task title
    private final JCheckBox[] dayCheckboxes; // Array of checkboxes for assigning days

    // Constructor accepts shared TaskManager from main app
    public TaskPanel(TaskManager taskManager) {
        this.taskManager = taskManager;
        setLayout(new BorderLayout()); // Set layout to BorderLayout (NORTH, CENTER, SOUTH)

        // INPUT PANEL (Top Section)
        JPanel inputPanel = new JPanel(new FlowLayout()); // Layout for top section (horizontal)

        taskField = new JTextField(20); // Create text field for task name
        inputPanel.add(new JLabel("Task:")); // Label before the input
        inputPanel.add(taskField); // Add the input box

        // Create checkboxes for selecting days (Mon-Sun)
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        dayCheckboxes = new JCheckBox[7];
        for (int i = 0; i < days.length; i++) {
            dayCheckboxes[i] = new JCheckBox(days[i]); // Create checkbox
            inputPanel.add(dayCheckboxes[i]); // Add to input panel
        }

        // Add Task button - will create a new task
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask()); // When clicked, call addTask()
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH); // Add the input panel to top

        // TASK TABLE SECTION (Center)
        String[] columns = { "Task", "Assigned Days", "Completed Days" };
        tableModel = new DefaultTableModel(columns, 0); // Create table with 3 columns
        taskTable = new JTable(tableModel); // Create table using the model
        JScrollPane scrollPane = new JScrollPane(taskTable); // Make it scrollable
        add(scrollPane, BorderLayout.CENTER); // Add table to the center of panel

        // MARK TASK COMPLETED SECTION (Bottom)
        JPanel completePanel = new JPanel(new FlowLayout());

        JButton markButton = new JButton("Mark Completed"); // Button to mark a task as done

        // When clicked → open a day dropdown and mark the selected task as completed
        markButton.addActionListener(e -> markTaskComplete());

        completePanel.add(new JLabel("Mark Task for Selected Day:"));
        completePanel.add(markButton);

        add(completePanel, BorderLayout.SOUTH); // Add bottom panel to layout
    }

    // Function to add task to taskManager and update table
    private void addTask() {
        String title = taskField.getText().trim(); // Get input from user

        // Validate: Don't allow empty tasks
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task title cannot be empty.");
            return;
        }

        // Create a set of selected days
        HashSet<String> assignedDays = new HashSet<>();
        for (JCheckBox cb : dayCheckboxes) {
            if (cb.isSelected()) {
                assignedDays.add(cb.getText()); // Add checked day to set
            }
        }

        // Validate: At least one day must be selected
        if (assignedDays.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one day.");
            return;
        }

        // Add task to manager and update UI
        taskManager.addTask(title, assignedDays);
        refreshTable(); // Refresh JTable after adding

        // Clear input fields for next task
        taskField.setText("");
        for (JCheckBox cb : dayCheckboxes) cb.setSelected(false);
    }

    // Function to mark a selected task as complete for a day
    private void markTaskComplete() {
        int selectedRow = taskTable.getSelectedRow(); // Get selected row from table

        // If nothing is selected
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a task from the table.");
            return;
        }

        // Day selector popup (combo box)
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        JComboBox<String> daySelector = new JComboBox<>(days);
        int option = JOptionPane.showConfirmDialog(this, daySelector, "Select Day", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedDay = daySelector.getSelectedItem().toString();

            // Try to mark task as completed
            boolean success = taskManager.markTaskCompleted(selectedRow, selectedDay);
            if (success) {
                refreshTable(); // If successful, update table
            } else {
                JOptionPane.showMessageDialog(this, "Day not assigned to this task.");
            }
        }
    }

    // Refreshes the JTable with latest task list from TaskManager
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing table rows
        List<Task> tasks = taskManager.getTasks(); // Get task list
        for (Task t : tasks) {
            tableModel.addRow(new Object[] {
                t.getTitle(),
                String.join(", ", t.getAssignedDays()),
                String.join(", ", t.getCompletedDays())
            });
        }
    }
}


