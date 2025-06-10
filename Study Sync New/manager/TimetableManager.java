package manager;

import model.TimetableEntry;
import java.util.*;

/**
 * Manages timetable entries – adding and grouping by day.
 */
public class TimetableManager {

    // Stores all timetable entries
    private final List<TimetableEntry> timetable = new ArrayList<>();

    /**
     * Adds a new class to the timetable.
     */
    public void addEntry(String day, String subject, String time) {
        timetable.add(new TimetableEntry(day, subject, time));
    }

    /**
     * Returns entries grouped by day (e.g., Mon → list of classes).
     */
    public Map<String, List<TimetableEntry>> getGroupedByDay() {
        Map<String, List<TimetableEntry>> map = new TreeMap<>();
        for (TimetableEntry entry : timetable) {
            map.computeIfAbsent(entry.getDay(), k -> new ArrayList<>()).add(entry);
        }
        return map;
    }
}
