package mas.alan.simulation.utility;

public class TimeConverter {
    
    private TimeConverter() {}

    public static TimeFormat convert(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        int second = Integer.parseInt(time.split(":")[2]);

        return new TimeFormat(hour, minute, second);
    }
}
