package model;

import java.util.HashMap;
import java.util.Map;

public class ScheduleEntry {
    private Room room;
    private Period period;
    private Time time;

    private Map<String, String> additionalDetails;


    public ScheduleEntry(Room room, Period period, Time time) {
        this.room = room;
        this.period = period;
        this.time = time;
        this.additionalDetails = new HashMap<>();
    }


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Map<String, String> getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(Map<String, String> additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public void addDetail(String key, String value) {
        additionalDetails.put(key, value);
    }

    public String getDetail(String key) {
        return additionalDetails.get(key);
    }
}
