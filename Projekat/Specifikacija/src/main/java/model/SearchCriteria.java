package model;

import api.ISearchManager;

import java.time.LocalTime;
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



    @Override
    public String toString() {
        return "SearchCriteria{" +
                "roomName='" + roomName + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


//    @Override
//    public void searchTermsByCriteria() {
//        Scanner scanner = new Scanner(System.in);
//        Map<String, String> criteria = new HashMap<>();
//
//        while (true) {
//            System.out.println("Dostupni headeri za pretragu: " + String.join(", ", schedule.getHeaderIndexMap().keySet()));
//            System.out.print("Unesite header po kojem želite da vršite pretragu: ");
//            String header = scanner.nextLine().trim();
//
//            if (!schedule.getHeaderIndexMap().containsKey(header)) {
//                System.out.println("Nepostojeći header. Pokušajte ponovo.");
//                continue;
//            }
//
//            System.out.print("Unesite vrednost za pretragu: ");
//            String value = scanner.nextLine().trim();
//            criteria.put(header, value);
//
//            System.out.print("Da li želite dodati još kriterijuma? (Da/Ne): ");
//            String odgovor = scanner.nextLine().trim();
//
//            if (odgovor.equalsIgnoreCase("Ne")) {
//                break;
//            }
//        }
//
//        List<Term> rezultati = search(criteria);
//        if (rezultati.isEmpty()) {
//            System.out.println("Nema rezultata za zadate kriterijume.");
//        } else {
//            System.out.println("Rezultati pretrage:");
//            for (Term term : rezultati) {
//                System.out.println(term);
//            }
//        }
//    }

    private List<Term> search(Map<String, String> criteria) {
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

    private String getTermValue(Term term, String header) {
        switch (header.toLowerCase()) {
            case "dan":
                return term.getDay().getName().toLowerCase();
            case "učionica":
                return term.getRoom().getName().toLowerCase();
            case "termin":
                return term.getTime().toString().toLowerCase();
            default:
                Object additionalProperty = term.getAdditionalProperty(header);
                return additionalProperty != null ? additionalProperty.toString().toLowerCase() : null;
        }
    }

    public void printFreeSlotsForTeacher() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Unesite naziv profesora: ");
        String teacherName = scanner.nextLine().trim();
        // Korisnik unosi radno vreme
        System.out.println("Unesite početak radnog vremena (HH:MM):");
        LocalTime workStart = LocalTime.parse(scanner.nextLine().trim());
        System.out.println("Unesite kraj radnog vremena (HH:MM):");
        LocalTime workEnd = LocalTime.parse(scanner.nextLine().trim());

        // Mapa za praćenje zauzetih termina po danima
        Map<String, List<LocalTime[]>> occupiedSlots = new HashMap<>();

        // Pronalaženje svih termina za nastavnika
        for (Term term : schedule.getTerms()) {
            String termTeacher = (String) term.getAdditionalProperty("Nastavnik");
            if (termTeacher != null && termTeacher.equalsIgnoreCase(teacherName)) {
                String dayName = term.getDay().getName();
                LocalTime startTime = term.getTime().getStartTime();
                LocalTime endTime = term.getTime().getEndTime();

                // Dodavanje zauzetog termina u mapu
                occupiedSlots.computeIfAbsent(dayName, k -> new ArrayList<>()).add(new LocalTime[]{startTime, endTime});
            }
        }

        // Pronalaženje slobodnih termina
        for (Map.Entry<String, List<LocalTime[]>> entry : occupiedSlots.entrySet()) {
            String day = entry.getKey();
            List<LocalTime[]> busyTimes = entry.getValue();

            // Sortiranje zauzetih termina po početnom vremenu
            busyTimes.sort(Comparator.comparing(o -> o[0]));

            // Pronalaženje slobodnih termina
            LocalTime current = workStart;
            System.out.println(day.toUpperCase() + ": slobodni termini za " + teacherName + ":");
            for (LocalTime[] times : busyTimes) {
                if (current.isBefore(times[0])) {
                    // Ispis slobodnog termina
                    System.out.println(" - " + current + " do " + times[0]);
                }
                current = times[1];
            }
            if (current.isBefore(workEnd)) {
                // Ispis slobodnog termina do kraja radnog vremena
                System.out.println(" - " + current + " do " + workEnd);
            }
        }
    }

    @Override
    public List<Term> searchTermsByCriteria(Map<String, String> criteria) {
        return search(criteria);
    }

    @Override
    public void filterTermsByRoom(SearchCriteria roomName) {

    }

    @Override
    public void filterTermsByDate(SearchCriteria date) {

    }

    @Override
    public void filterTermsByTime(SearchCriteria startTime, SearchCriteria endTime) {

    }
}
