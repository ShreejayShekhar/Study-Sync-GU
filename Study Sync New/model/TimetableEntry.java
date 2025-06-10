package model;

/**
 * Represents a single class entry in the timetable.
 */
public class TimetableEntry {
    private String day;
    private String subject;
    private String time; // e.g., "10:00 AM - 11:00 AM"

    // Constructor – sets day, subject, and time for the class
    public TimetableEntry(String day, String subject, String time) {
        this.day = day;
        this.subject = subject;
        this.time = time;
    }

    // Returns the day of the class
    public String getDay() {
        return day;
    }

    // Returns the subject name
    public String getSubject() {
        return subject;
    }

    // Returns the class timing
    public String getTime() {
        return time;
    }

    // Returns a readable string for dashboard or display
    @Override
    public String toString() {
        return subject + " at " + time;
    }
}

