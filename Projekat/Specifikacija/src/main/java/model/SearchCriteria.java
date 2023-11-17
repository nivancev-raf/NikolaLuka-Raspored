package model;

import api.ISearchManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
                    // Provera da li je kraj slobodnog termina pre kraja radnog vremena
                    LocalTime endOfFreeSlot = times[0].isBefore(workEnd) ? times[0] : workEnd;
                    // Dodavanje slobodnog termina samo ako početak nije posle kraja radnog vremena
                    if (!current.isAfter(workEnd)) {
                        dayFreeSlots.add(new LocalTime[]{current, endOfFreeSlot});
                    }
                }
                // Postavljanje trenutnog vremena na kraj zauzetog termina, ali ne posle kraja radnog vremena
                current = times[1].isAfter(current) ? times[1] : current;
                if (current.isAfter(workEnd)) {
                    break;
                }
            }

            // Provera posle poslednjeg zauzetog termina
            if (current.isBefore(workEnd)) {
                dayFreeSlots.add(new LocalTime[]{current, workEnd});
            }

            // Filtriranje slobodnih termina gde je početak jednak kraju
            List<LocalTime[]> filteredDayFreeSlots = dayFreeSlots.stream()
                    .filter(slot -> !slot[0].equals(slot[1]))
                    .collect(Collectors.toList());

            freeSlots.put(day, filteredDayFreeSlots);
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
            String ucionica = term.getRoom().getName().split(" ")[0];
            if (ucionica.equalsIgnoreCase(roomName)) {
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
            String ucionica = term.getRoom().getName().split(" ")[0];
            if (ucionica.equalsIgnoreCase(roomName)) {
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
            List<LocalTime[]> busyTimes = occupiedSlots.get(day);
            // Sortiranje zauzetih termina po početnom vremenu
            busyTimes.sort(Comparator.comparing(o -> o[0]));

            List<LocalTime[]> dayFreeSlots = new ArrayList<>();
            LocalTime current = workStart;

            for (LocalTime[] times : busyTimes) {
                if (current.isBefore(times[0])) {
                    LocalTime startOfFreeSlot = current;
                    LocalTime endOfFreeSlot = times[0];
                    // Provera da li kraj slobodnog termina pada unutar radnog vremena
                    if (endOfFreeSlot.isAfter(workEnd)) {
                        endOfFreeSlot = workEnd;
                    }
                    // Dodavanje slobodnog termina ako početak nije posle kraja radnog vremena
                    if (!startOfFreeSlot.isAfter(workEnd)) {
                        dayFreeSlots.add(new LocalTime[]{startOfFreeSlot, endOfFreeSlot});
                    }
                }
                current = times[1];
                if (current.isAfter(workEnd)) {
                    break;
                }
            }

            // Dodavanje slobodnog termina nakon poslednjeg zauzetog termina, ako postoji
            if (current.isBefore(workEnd)) {
                dayFreeSlots.add(new LocalTime[]{current, workEnd});
            }

            // Filtriranje slobodnih termina gde je početak jednak kraju
            List<LocalTime[]> filteredDayFreeSlots = dayFreeSlots.stream()
                    .filter(slot -> !slot[0].equals(slot[1]))
                    .collect(Collectors.toList());

            freeSlots.put(day, filteredDayFreeSlots);
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

    public DayOfWeek reverseParseDay(String day){
        if(day.equalsIgnoreCase("pon")){
            return DayOfWeek.MONDAY;
        } else if(day.equalsIgnoreCase("uto")){
            return DayOfWeek.TUESDAY;
        }else if(day.equalsIgnoreCase("sre")){
            return DayOfWeek.WEDNESDAY;
        } else if(day.equalsIgnoreCase("čet") || day.equalsIgnoreCase("cet")){
            return DayOfWeek.THURSDAY;
        } else if(day.equalsIgnoreCase("pet")){
            return DayOfWeek.FRIDAY;
        } else if(day.equalsIgnoreCase("sub")){
            return DayOfWeek.SATURDAY;
        }else if(day.equalsIgnoreCase("ned")){
            return DayOfWeek.SUNDAY;
        }
        return null;
    }

    @Override
    public List<Term> searchTermsByCriteria(Map<String, String> criteria) {
        return search(criteria);
    }

}
