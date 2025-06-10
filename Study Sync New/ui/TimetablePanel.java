package ui;

// Import timetable logic and data model classes
import manager.TimetableManager;
import model.TimetableEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TimetablePanel extends JPanel {

    // This class manages adding/displaying class schedule

    // Reference to shared TimetableManager (passed from main app)
    private final TimetableManager timetableManager;

    // Table model used to update class schedule table
    private final DefaultTableModel tableModel;

    // GUI input components
    private final JComboBox<String> dayComboBox;
    private final JTextField subjectField;

    // Start time dropdowns
    private final JComboBox<String> startHourCombo;
    private final JComboBox<String> startMinuteCombo;
    private final JComboBox<String> startAmPmCombo;

    // End time dropdowns
    private final JComboBox<String> endHourCombo;
    private final JComboBox<String> endMinuteCombo;
    private final JComboBox<String> endAmPmCombo;

    // Constructor — receives the TimetableManager from StudySyncApp
    public TimetablePanel(TimetableManager timetableManager) {
        this.timetableManager = timetableManager;

        // Use a layout with Top (North) + Center (for table)
        setLayout(new BorderLayout());

        // Build dropdown values
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        String[] hours = new String[12];
        for (int i = 1; i <= 12; i++) hours[i - 1] = String.valueOf(i); // "1" to "12"

        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) minutes[i] = String.format("%02d", i); // "00" to "59"

        String[] ampm = { "AM", "PM" };

        // Initialize input fields
        dayComboBox = new JComboBox<>(days);          // Select day
        subjectField = new JTextField(12);            // Enter subject name
        startHourCombo = new JComboBox<>(hours);      // Start hour
        startMinuteCombo = new JComboBox<>(minutes);  // Start minutes
        startAmPmCombo = new JComboBox<>(ampm);       // Start AM/PM
        endHourCombo = new JComboBox<>(hours);        // End hour
        endMinuteCombo = new JComboBox<>(minutes);    // End minutes
        endAmPmCombo = new JComboBox<>(ampm);         // End AM/PM

        // Create Add button and set action
        JButton addButton = new JButton("Add Class");
        addButton.addActionListener(e -> addClass()); // When clicked → call addClass()

        // Row 1: Day + Subject
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Day:"));
        topRow.add(dayComboBox);
        topRow.add(new JLabel("Subject:"));
        topRow.add(subjectField);

        // Row 2: Time Pickers (Start → End) + Add button
        JPanel timeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeRow.add(new JLabel("Time:"));
        timeRow.add(startHourCombo);
        timeRow.add(startMinuteCombo);
        timeRow.add(startAmPmCombo);
        timeRow.add(new JLabel("to"));
        timeRow.add(endHourCombo);
        timeRow.add(endMinuteCombo);
        timeRow.add(endAmPmCombo);
        timeRow.add(addButton);

        // Wrap both rows into one vertical input section
        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BoxLayout(inputSection, BoxLayout.Y_AXIS)); // Stack vertically
        inputSection.add(topRow);
        inputSection.add(timeRow);

        // Add input section to top of panel
        add(inputSection, BorderLayout.NORTH);

        // Create table for showing added classes
        String[] columns = { "Day", "Subject", "Time" }; // Table headers
        tableModel = new DefaultTableModel(columns, 0);  // Empty table model
        JTable timetableTable = new JTable(tableModel);  // Table component
        JScrollPane scrollPane = new JScrollPane(timetableTable); // Add scroll bar

        // Add table to center of panel
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method called when Add button is clicked
    private void addClass() {
        // Read values from dropdowns and input fields
        String day = dayComboBox.getSelectedItem().toString();
        String subject = subjectField.getText().trim();

        // Build time string like: "10:30 AM - 11:45 AM"
        String time = startHourCombo.getSelectedItem() + ":" +
                      startMinuteCombo.getSelectedItem() + " " +
                      startAmPmCombo.getSelectedItem() + " - " +
                      endHourCombo.getSelectedItem() + ":" +
                      endMinuteCombo.getSelectedItem() + " " +
                      endAmPmCombo.getSelectedItem();

        // Validate that fields are not empty
        if (day.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Add entry to timetable manager (logic layer)
        timetableManager.addEntry(day, subject, time);

        // Update GUI table
        refreshTable();

        // Clear input fields for next entry
        subjectField.setText("");
        startHourCombo.setSelectedIndex(0);
        startMinuteCombo.setSelectedIndex(0);
        startAmPmCombo.setSelectedIndex(0);
        endHourCombo.setSelectedIndex(0);
        endMinuteCombo.setSelectedIndex(0);
        endAmPmCombo.setSelectedIndex(0);
    }

    // Refresh the class table with latest data
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing table rows

        // Get map of day → list of TimetableEntry
        Map<String, List<TimetableEntry>> grouped = timetableManager.getGroupedByDay();

        // Loop through each day and entry
        for (String day : grouped.keySet()) {
            for (TimetableEntry entry : grouped.get(day)) {
                // Add row to table
                tableModel.addRow(new Object[] {
                    entry.getDay(),
                    entry.getSubject(),
                    entry.getTime()
                });
            }
        }
    }

    // Expose timetableManager to Dashboard if needed
    public TimetableManager getTimetableManager() {
        return timetableManager;
    }
}

