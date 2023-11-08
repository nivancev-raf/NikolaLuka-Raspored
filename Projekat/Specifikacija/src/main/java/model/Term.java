package model;

import api.ITermManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Term implements ITermManager {

    private Day day;
    private Room room;
    private Time time;
    private Period period;
    private List<Term> termList = new ArrayList<>();
    private Map<String, String> additionalProperties;
    private Schedule schedule;

    public Term(Schedule schedule) {
        this.schedule = schedule;
    }

    public Term(Room room, Day day, Time time,Period period) {
        this.room = room;
        this.day = day;
        this.time = time;
        this.period = period;
        this.additionalProperties = new HashMap<>();
    }

    public Term() {

    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Object getAdditionalProperty(String key) {
        return additionalProperties.get(key);
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Day getDay() {
        return day;
    }

    public Room getRoom() {
        return room;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Term{" +
                "day=" + day +
                ", room=" + room +
                ", time=" + time +
                ", additionalProperties=" + additionalProperties + ",period=" + period +
                '}';
    }

    @Override
    public List<Term> getAllTerms() {
        return this.termList;
    }

    @Override
    public void updateTerm(Term termId, Term updatedTerm) {

    }

    @Override
    public void deleteTerm(Term termId) {

    }

    @Override
    public void addAdditionalProperty(String key, Object value) {

    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    @Override
    public Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs,String periodInput) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Day day = new Day(dayInput);
        String[] parts = timeInput.split("-");
        LocalTime startTime = parseTime(parts[0].trim());
        LocalTime endTime = parseTime(parts[1].trim());
        String[] periodParts = periodInput.split("-");
        LocalDate startPeriod = LocalDate.parse(periodParts[0].trim(), dateFormatter);
        LocalDate endPeriod = LocalDate.parse(periodParts[1].trim(), dateFormatter);
        Period period = new Period(startPeriod,endPeriod);
        Time time = new Time(startTime, endTime);
        Room room = new Room(roomInput);

        if (!isTermOccupied(day.getName(), startTime, endTime, room.getName())) {
            Term newTerm = new Term(room, day, time,period);
            if (additionalInputs != null){
                Map<String, String> additionalProperties = new HashMap<>();

                for (Map.Entry<String, String> entry : additionalInputs.entrySet()) {
                    additionalProperties.put(entry.getKey(), entry.getValue());
                }

                newTerm.setAdditionalProperties(additionalProperties);
            }
            return newTerm;
        } else {
            return null; // vraca null ako je termin zauzet
        }
    }



    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        time = time.replaceAll("\"", "");
        if (!time.contains(":")) {
            time += ":00";
        }
        return LocalTime.parse(time, formatter);
    }

    public boolean isTermOccupied(String day, LocalTime startTime, LocalTime endTime, String room) {
        LocalTime start = parseTime(String.valueOf(startTime));
        LocalTime end = parseTime(String.valueOf(endTime));

        for (Term term : schedule.getTerms()) {
            if (term.getDay().getName().equalsIgnoreCase(day.trim()) &&
                    term.getRoom().getName().equalsIgnoreCase(room.trim())) {

                LocalTime termStart = term.getTime().getStartTime();
                LocalTime termEnd = term.getTime().getEndTime();

                if ((start.isAfter(termStart) && start.isBefore(termEnd)) ||
                        (end.isAfter(termStart) && end.isBefore(termEnd)) ||
                        (start.equals(termStart) && end.equals(termEnd))) {
                    return true;
                }
            }
        }
        return false;
    }

}