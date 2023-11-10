package model;

import api.ISearchManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SearchCriteria implements ISearchManager {
    private String roomName;
    private Date date;
    private Date startTime;
    private Date endTime;
    private Schedule schedule;


    public SearchCriteria(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "roomName='" + roomName + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public List<Term> search(Map<String, String> criteria) {
        List<Term> rezultati = new ArrayList<>();
        for (Term term : schedule.getTerms()) {
            boolean match = true;
            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String header = entry.getKey();
                String value = entry.getValue().toLowerCase();
                String termValue = getTermValue(term, header);
                System.out.println("header: " + header + " value: " + value + " termValue: " + termValue);
                if (termValue == null || !termValue.contains(value)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                rezultati.add(term);
            }
        }
        return rezultati;
    }

    @Override
    public String getTermValue(Term term, String header) {
        // JSON MANDATORY: Dan, Ucionica, Termin, Period
        // CSV MANDATORY: Dan, Ucionica, Termin, Period
        switch (header.toLowerCase()) {
            case "dan":
                return term.getDay().getName().toLowerCase();
            case "ucionica":
                return term.getRoom().getName().toLowerCase();
                //csv
            case "termin":
                return term.getTime().toString().toLowerCase();
            case "period":
                return term.getPeriodString().toLowerCase();

            default:
                Object additionalProperty = term.getAdditionalProperty(header);
                return additionalProperty != null ? additionalProperty.toString().toLowerCase() : null;
        }
    }


    @Override
    public Map<String, List<LocalTime[]>> getFreeSlotsForTeacher(String teacherName, LocalTime workStart, LocalTime workEnd) {
        Map<String, List<LocalTime[]>> occupiedSlots = new HashMap<>();
        Map<String, List<LocalTime[]>> freeSlots = new HashMap<>();

        // Pronalaženje svih termina za nastavnika
        for (Term term : schedule.getTerms()) {
            String termTeacher = (String) term.getAdditionalProperty("Nastavnik");
            String termTeacherSurname = termTeacher.split(" ")[0];
            String termTeacherName = termTeacher.split(" ")[1];
            String teacherN = teacherName.split(" ")[0];
            String teacherS = teacherName.split(" ")[1];
            if (termTeacher != null && (termTeacherName.equalsIgnoreCase(teacherN) && termTeacherSurname.equalsIgnoreCase(teacherS)
                    || (termTeacherName.equalsIgnoreCase(teacherS) && termTeacherSurname.equalsIgnoreCase(teacherN)))) {
                String dayName = term.getDay().getName();
                LocalTime startTime = term.getTime().getStartTime();
                LocalTime endTime = term.getTime().getEndTime();

                // Dodavanje zauzetog termina u mapu
                occupiedSlots.computeIfAbsent(dayName, k -> new ArrayList<>()).add(new LocalTime[]{startTime, endTime});
            }
        }

        // Pronalaženje slobodnih termina
        for (String day : occupiedSlots.keySet()) {
            List<LocalTime[]> busyTimes = occupiedSlots.get(day);
            // Sortiranje zauzetih termina po početnom vremenu
            busyTimes.sort(Comparator.comparing(o -> o[0]));

            List<LocalTime[]> dayFreeSlots = new ArrayList<>();
            LocalTime current = workStart;

            for (LocalTime[] times : busyTimes) {
                if (current.isBefore(times[0])) {
                    // Dodavanje slobodnog termina u listu
                    dayFreeSlots.add(new LocalTime[]{current, times[0]});
                }
                current = times[1].isAfter(current) ? times[1] : current;
            }

            // Provera posle poslednjeg zauzetog termina
            if (current.isBefore(workEnd)) {
                dayFreeSlots.add(new LocalTime[]{current, workEnd});
            }

            freeSlots.put(day, dayFreeSlots);
        }

        return freeSlots;
    }

    @Override
    public Map<String, List<LocalTime[]>> getOccupiedSlotsForTeacher(String teacherName) {
        Map<String, List<LocalTime[]>> occupiedSlots = new HashMap<>();

        // Pronalaženje svih termina za nastavnika
        for (Term term : schedule.getTerms()) {
            String termTeacher = (String) term.getAdditionalProperty("Nastavnik");
            String termTeacherSurname = termTeacher.split(" ")[0];
            String termTeacherName = termTeacher.split(" ")[1];
            String teacherN = teacherName.split(" ")[0];
            String teacherS = teacherName.split(" ")[1];
            if (termTeacher != null && (termTeacherName.equalsIgnoreCase(teacherN) && termTeacherSurname.equalsIgnoreCase(teacherS)
                                    || (termTeacherName.equalsIgnoreCase(teacherS) && termTeacherSurname.equalsIgnoreCase(teacherN)))) {
                String dayName = term.getDay().getName();
                LocalTime startTime = term.getTime().getStartTime();
                LocalTime endTime = term.getTime().getEndTime();

                // Dodavanje zauzetog termina u mapu
                occupiedSlots.computeIfAbsent(dayName, k -> new ArrayList<>()).add(new LocalTime[]{startTime, endTime});
            }
        }
        return occupiedSlots;
    }

    @Override
    public Map<String, List<LocalTime[]>> getOccupiedSlotsForRoom(String roomName) {
        Map<String, List<LocalTime[]>> occupiedSlots = new HashMap<>();

        // Pronalaženje svih termina za učionicu
        for (Term term : schedule.getTerms()) {
            if (term.getRoom().getName().equals(roomName)) {
                String dayName = term.getDay().getName();
                LocalTime startTime = term.getTime().getStartTime();
                LocalTime endTime = term.getTime().getEndTime();
                // Dodavanje zauzetog termina u mapu
                occupiedSlots.computeIfAbsent(dayName, k -> new ArrayList<>()).add(new LocalTime[]{startTime, endTime});
            }
        }
        return occupiedSlots;
    }

    @Override
    public Map<String, List<LocalTime[]>> getFreeSlotsForRoom(String roomName, LocalTime workStart, LocalTime workEnd) {
        Map<String, List<LocalTime[]>> occupiedSlots = new HashMap<>();

        // Pronalaženje svih termina za učionicu
        for (Term term : schedule.getTerms()) {
            if (term.getRoom().getName().equals(roomName)) {
                String dayName = term.getDay().getName();
                LocalTime startTime = term.getTime().getStartTime();
                LocalTime endTime = term.getTime().getEndTime();
                // Dodavanje zauzetog termina u mapu
                occupiedSlots.computeIfAbsent(dayName, k -> new ArrayList<>()).add(new LocalTime[]{startTime, endTime});
            }
        }

        // Sada treba izračunati slobodne termine na osnovu zauzetih
        Map<String, List<LocalTime[]>> freeSlots = new HashMap<>();

        for (String day : occupiedSlots.keySet()) {
            List<LocalTime[]> freeTimes = new ArrayList<>();
            LocalTime current = workStart;

            for (LocalTime[] occupied : occupiedSlots.get(day)) {
                if (current.isBefore(occupied[0])) {
                    freeTimes.add(new LocalTime[]{current, occupied[0]});
                }
                current = occupied[1];
            }

            if (current.isBefore(workEnd)) {
                freeTimes.add(new LocalTime[]{current, workEnd});
            }

            freeSlots.put(day, freeTimes);
        }

        return freeSlots;
    }

    public String parseDay (LocalDate date){
        String dan = "";
        if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            dan = "PON";
        } else if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
            dan = "UTO";
        } else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
            dan = "SRE";
        }
        else if(date.getDayOfWeek().equals(DayOfWeek.THURSDAY)){
            dan = "CET";
        }
        else if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            dan = "PET";
        } else if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            dan = "SUB";
        } else if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            dan = "NED";
        }
        return dan;
    }

    @Override
    public List<Term> searchTermsByCriteria(Map<String, String> criteria) {
        return search(criteria);
    }

}
