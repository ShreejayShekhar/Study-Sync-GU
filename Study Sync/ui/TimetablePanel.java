package ui;

import manager.TimetableManager;
import model.TimetableEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * TimetablePanel – GUI tab to add and view class timetable entries.
 */
public class TimetablePanel extends JPanel {

    private final TimetableManager timetableManager = new TimetableManager();
    private final DefaultTableModel tableModel;

    private final JComboBox<String> dayComboBox;
    private final JTextField subjectField;

    // Dropdowns for selecting start time
    private final JComboBox<String> startHourCombo;
    private final JComboBox<String> startMinuteCombo;
    private final JComboBox<String> startAmPmCombo;

    // Dropdowns for selecting end time
    private final JComboBox<String> endHourCombo;
    private final JComboBox<String> endMinuteCombo;
    private final JComboBox<String> endAmPmCombo;

    public TimetablePanel() {
        setLayout(new BorderLayout());

        // === Top input panel ===
        JPanel inputPanel = new JPanel(new FlowLayout());

        // Day selector
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        dayComboBox = new JComboBox<>(days);
        inputPanel.add(new JLabel("Day:"));
        inputPanel.add(dayComboBox);

        // Subject field
        subjectField = new JTextField(12);
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);

        // Time label
        inputPanel.add(new JLabel("Time:"));

        // Time dropdowns
        String[] hours = new String[12];
        for (int i = 1; i <= 12; i++) hours[i - 1] = String.valueOf(i);

        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) minutes[i] = String.format("%02d", i);

        String[] ampm = { "AM", "PM" };

        // Start time
        startHourCombo = new JComboBox<>(hours);
        startMinuteCombo = new JComboBox<>(minutes);
        startAmPmCombo = new JComboBox<>(ampm);
        inputPanel.add(startHourCombo);
        inputPanel.add(startMinuteCombo);
        inputPanel.add(startAmPmCombo);

        inputPanel.add(new JLabel("to"));

        // End time
        endHourCombo = new JComboBox<>(hours);
        endMinuteCombo = new JComboBox<>(minutes);
        endAmPmCombo = new JComboBox<>(ampm);
        inputPanel.add(endHourCombo);
        inputPanel.add(endMinuteCombo);
        inputPanel.add(endAmPmCombo);

        // Add class button
        JButton addButton = new JButton("Add Class");
        addButton.addActionListener(e -> addClass());
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // === Table to show timetable entries ===
        String[] columns = { "Day", "Subject", "Time" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable timetableTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds a new class entry to the timetable.
     */
    private void addClass() {
        String day = dayComboBox.getSelectedItem().toString();
        String subject = subjectField.getText().trim();

        // Build time string from selected dropdowns
        String time = startHourCombo.getSelectedItem() + ":" +
                      startMinuteCombo.getSelectedItem() + " " +
                      startAmPmCombo.getSelectedItem() + " - " +
                      endHourCombo.getSelectedItem() + ":" +
                      endMinuteCombo.getSelectedItem() + " " +
                      endAmPmCombo.getSelectedItem();

        if (day.isEmpty() || subject.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        timetableManager.addEntry(day, subject, time);
        refreshTable();

        // Clear form
        subjectField.setText("");
        startHourCombo.setSelectedIndex(0);
        startMinuteCombo.setSelectedIndex(0);
        startAmPmCombo.setSelectedIndex(0);
        endHourCombo.setSelectedIndex(0);
        endMinuteCombo.setSelectedIndex(0);
        endAmPmCombo.setSelectedIndex(0);
    }

    /**
     * Updates the timetable table with current entries.
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // clear table
        Map<String, List<TimetableEntry>> grouped = timetableManager.getGroupedByDay();

        for (String day : grouped.keySet()) {
            for (TimetableEntry entry : grouped.get(day)) {
                tableModel.addRow(new Object[] {
                        entry.getDay(),
                        entry.getSubject(),
                        entry.getTime()
                });
            }
        }
    }

    // Expose manager to other panels (like Dashboard)
    public TimetableManager getTimetableManager() {
        return timetableManager;
    }
}
