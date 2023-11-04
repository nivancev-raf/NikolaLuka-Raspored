package model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule {

    private static Schedule instance;
    private Map<String, Day> days;
    private Map<String, Room> rooms;
    private Map<String, Time> times;
    private List<Term> terms;
    private Map<Term, Map<String, String>> additionalData;
    private Map<String, Integer> headerIndexMap;
    private LocalDate startDate;
    private LocalDate endDate;

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
    

    public void setPeriodVazenja(String period){
        String[] dates = period.split("-");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.startDate = LocalDate.parse(dates[0], formatter);
        this.endDate = LocalDate.parse(dates[1], formatter);
    }

    public List<Term> getTerms() {
        return terms;
    }

    public Map<Term, Map<String, String>> getAdditionalData() {
        return additionalData;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}