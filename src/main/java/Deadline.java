import java.time.LocalDateTime;
import java.time.LocalDate;



public class Deadline extends Task {

    String by;
    LocalDate date;
    LocalDateTime dateTime;
    boolean hasTime;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.hasTime = false;
        String[] split = by.split(" ");
        if (split.length == 1) {
            date = LocalDate.parse(split[0]);
        } else if (split.length == 2) {
            String str = split[0] + "T" + split[1].substring(0,2) + ":" + split[1].substring(2);
            dateTime = LocalDateTime.parse(str);
            this.hasTime = true;
        } else {
            throw new IllegalArgumentException("ERROR! Invalid deadline format.");
        }
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        String deadline;
        if (hasTime) {
            String year = String.valueOf(dateTime.getYear());
            String month = String.valueOf(dateTime.getMonth()).substring(0,3);
            String day = String.valueOf(dateTime.getDayOfMonth());
            String minute;
            if (dateTime.getMinute() < 10) {
                minute = "0" + String.valueOf(dateTime.getMinute());
            } else {
                minute = String.valueOf(dateTime.getMinute());
            }
            String time = String.valueOf(dateTime.getHour()) + minute;
            deadline = day + " " + month + " " + year + " " + time;
        } else {
            String year = String.valueOf(date.getYear());
            String month = String.valueOf(date.getMonth()).substring(0,3);
            String day = String.valueOf(date.getDayOfMonth());
            deadline = day + " " + month + " " + year;
        }
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }


}
