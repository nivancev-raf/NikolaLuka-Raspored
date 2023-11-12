package model;

import api.ITermManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Term implements ITermManager {

    private Day day;
    private Room room;
    private Time time;
    private Period period;
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


    public Period getPeriod() {
        return period;
    }
    public String getPeriodString() {

        String periodString = LocalDate.parse(period.getStartPeriod().toString()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "-"
                + LocalDate.parse(period.getEndPeriod().toString()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return periodString;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public LocalDate getEndPeriod() {
        return period.getEndPeriod();
    }

    public LocalDate getStartPeriod() {
        return period.getStartPeriod();
    }

    @Override
    public String toString() {
        return "Term{" +
                "day=" + day +
                ", room=" + room + ", kapacitet=" + room.getCapacity() + ", room additional properties=" + room.getAdditional() +
                ", time=" + time +
                ", additionalProperties=" + additionalProperties + ",period=" + period +
                '}';
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public void deleteTerm(String teacherName, String roomName, String time, String day) {
        for(Term term: schedule.getTerms()){
            if(term.getAdditionalProperties().get("Nastavnik").equals(teacherName) && term.getRoom().getName().equals(roomName) && term.getTime().toString().equals(time) &&
                    term.getDay().getName().equals(day)){
                schedule.getTerms().remove(term);
                break;
            }
        }
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

    @Override
    public boolean isTermAvailable(Term newTerm, List<Term> existingTerms) {
        for (Term existingTerm : existingTerms) {
            // Provera da li se vreme i učionica poklapaju
            if (newTerm.getRoom().getName().equals(existingTerm.getRoom().getName()) &&
                    newTerm.getTime().overlaps(existingTerm.getTime()) && newTerm.getDay().equals(existingTerm.getDay())) {
                // Ako se vreme i učionica poklapaju, proveravamo da li se poklapaju i periodi
                if (newTerm.getPeriod().overlaps(existingTerm.getPeriod())) {
                    // Ako se i periodi poklapaju, termin nije dostupan
                    return false;
                }
            }
        }
        // Ako nema poklapanja, termin je dostupan
        return true;
    }

    @Override
    public Term findTermToModify(String teacherName, String roomName, String timeRange) {
        for(Term term: Schedule.getInstance().getTerms()){
            if(term.getAdditionalProperty("Nastavnik").equals(teacherName) && term.getRoom().getName().equals(roomName)
                    && term.getTime().toString().equals(timeRange)){
                return term;
            }
        }
        return null;
    }

    @Override
    public Term makeOriginalTerm(Term termToModify, LocalDate splitDateStr) {
        Period period = new Period(termToModify.getPeriod().getStartPeriod(), splitDateStr);
        Term originalTerm = new Term(termToModify.getRoom(), termToModify.getDay(), termToModify.getTime(), period);
        originalTerm.setAdditionalProperties(termToModify.getAdditionalProperties());
        return originalTerm;
    }

    @Override
    public Term makeNewTerm(Term termToModify, LocalDate splitDate, Room newRoom, Time newTime) {
        Term newTerm = new Term(newRoom, termToModify.getDay(), newTime,
                new Period(splitDate.plusDays(1), termToModify.getPeriod().getEndPeriod()));
        newTerm.setAdditionalProperties(termToModify.getAdditionalProperties());
        for (Term term : Schedule.getInstance().getTerms()) {
            if (term.getRoom().getName().equalsIgnoreCase(newRoom.getName())) {
                newRoom.setCapacity(term.getRoom().getCapacity());
                newRoom.setAdditional(term.getRoom().getAdditional());
                break;
            }
        }

        return newTerm;
    }

    @Override
    public Time splitTime(String timeRange) {
        String[] timeParts = timeRange.split("-");
        LocalTime newStartTime = LocalTime.parse(timeParts[0].trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime newEndTime = LocalTime.parse(timeParts[1].trim(), DateTimeFormatter.ofPattern("HH:mm"));
        return new Time(newStartTime, newEndTime);
    }

    @Override
    public void updateScheduleWithNewTerms(Term oldTerm, Term originalTerm, Term newTerm) {
        List<Term> terms = schedule.getTerms();
        terms.add(originalTerm);
        terms.add(newTerm);
        terms.remove(oldTerm);
    }

    @Override
    public boolean isDateWithinTermPeriod(Term term, LocalDate date) {
        return !(date.isBefore(term.getPeriod().getStartPeriod()) || date.isAfter(term.getPeriod().getEndPeriod()));
    }
    @Override
    public boolean parseIzuzetiDani(String datum) throws DateTimeParseException {
        if (!datum.matches("\\d{2}.\\d{2}.\\d{4}")) {
            return false;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startPeriod = LocalDate.parse(schedule.getPeriodPocetak().trim(), dateFormatter);
        LocalDate endPeriod = LocalDate.parse(schedule.getPeriodKraj().trim(), dateFormatter);
        LocalDate date =  LocalDate.parse(datum,dateFormatter);
        if(date.isAfter(startPeriod) && date.isBefore(endPeriod)){
            Schedule.getInstance().getIzuzetiDani().add(date);
            return true;
        }
        return false;
    }
}