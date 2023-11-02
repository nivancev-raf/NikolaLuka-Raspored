package model;

import api.ITermManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Term implements ITermManager {

    private Day day;
    private Room room;
    private Time time;
    private List<Term> termList = new ArrayList<>();
    private Map<String, Object> additionalProperties;
    private Schedule schedule;

    public Term(Schedule schedule) {
        this.schedule = schedule;
    }

    public Term(Room room, Day day, Time time) {
        this.room = room;
        this.day = day;
        this.time = time;
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

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
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

    @Override
    public String toString() {
        return "Term{" +
                "day=" + day +
                ", room=" + room +
                ", time=" + time +
                ", additionalProperties=" + additionalProperties +
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

    public Term createTermFromInput() {
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> additionalProperties = new HashMap<>();

        System.out.println("Unesite dan:");
        String dayInput = scanner.nextLine().trim();
        Day day = new Day(dayInput);

        System.out.println("Unesite vreme (npr. 11:15-13):");
        String timeInput = scanner.nextLine().trim();
        String[] parts = timeInput.split("-");
        LocalTime startTime = parseTime(parts[0].trim());
        LocalTime endTime = parseTime(parts[1].trim());
        Time time = new Time(startTime, endTime);

        System.out.println("Unesite učionicu:");
        String roomInput = scanner.nextLine().trim();
        Room room = new Room(roomInput);

        if (!isTermOccupied(day.getName(), startTime, endTime, room.getName())) {
            Term newTerm = new Term(room, day, time);

            System.out.println("Unesite dodatne informacije za termin. Kada završite, unesite 'kraj'.");
            while (true) {
                Set<String> availableHeaders = new HashSet<>(schedule.getHeaderIndexMap().keySet());
                availableHeaders.removeAll(Arrays.asList("Dan", "Termin", "Učionica"));
                availableHeaders.removeAll(additionalProperties.keySet());

                if (availableHeaders.isEmpty()) {
                    System.out.println("Svi headeri su popunjeni.");
                    break;
                }

                System.out.println("Dostupni headeri: " + String.join(", ", availableHeaders));
                System.out.print("Unesite header: ");
                String header = scanner.nextLine().trim();

                if (header.equalsIgnoreCase("kraj")) {
                    break;
                }

                if (!availableHeaders.contains(header)) {
                    System.out.println("Nepostojeći ili već unesen header. Pokušajte ponovo.");
                    continue;
                }

                System.out.print("Unesite vrednost: ");
                String value = scanner.nextLine().trim();
                additionalProperties.put(header, value);
            }

            newTerm.setAdditionalProperties(additionalProperties);
            return newTerm;
        } else {
            System.out.println("Termin je zauzet.");
            return null;
        }
    }

    public void addTerm() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Term newTerm = createTermFromInput();
            if (newTerm != null) {
                schedule.getTerms().add(newTerm);
//                termList.add(newTerm);
                System.out.println("Termin je uspešno dodat.");
                System.out.println(schedule.getTerms().size());
                System.out.println(schedule.getTerms().get(schedule.getTerms().size()-1));
            }

            System.out.print("Da li želite dodati još jedan termin? (Da/Ne): ");
            String odgovor = scanner.nextLine().trim();
            if (odgovor.equalsIgnoreCase("Ne")) {
                schedule.printSchedule();
                break;
            }
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