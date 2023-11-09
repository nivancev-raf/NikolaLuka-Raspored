package model;
import io.CSVFileImporter;

import java.util.*;

public class Schedule {

    private static Schedule instance;
    private Map<String, Day> days;
    private Map<String, Room> rooms;
    private Map<String, Time> times;
    private List<Term> terms;
    private Map<String, String> additionalData;
    private Map<String, Integer> headerIndexMap;
    private String periodPocetak;
    private String periodKraj;


    private Schedule() {
        initSchedule();
    }

    public static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }

    private void initSchedule(){
        this.days = new HashMap<>();
        this.rooms = new HashMap<>();
        this.times = new HashMap<>();
        this.terms = new ArrayList<>();
        this.additionalData = new HashMap<>();
        this.headerIndexMap = new HashMap<>();
    }

    public List<Term> getTerms() {
        return terms;
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    public static void setInstance(Schedule instance) {
        Schedule.instance = instance;
    }

    public Map<String, Day> getDays() {
        return days;
    }

    public void setDays(Map<String, Day> days) {
        this.days = days;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Map<String, Time> getTimes() {
        return times;
    }

    public Map<String, Integer> getHeaderIndexMap() {
        return headerIndexMap;
    }

    public String getPeriodPocetak() {
        return periodPocetak;
    }

    public void setPeriodPocetak(String periodPocetak) {
        this.periodPocetak = periodPocetak;
    }

    public String getPeriodKraj() {
        return periodKraj;
    }

    public void setPeriodKraj(String periodKraj) {
        this.periodKraj = periodKraj;
    }
}