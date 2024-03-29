package handlers;

import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SearchHandler {
    private Schedule schedule;
    private SearchCriteria searchCriteria;

    public SearchHandler(Schedule schedule, SearchCriteria searchCriteria) {
        this.schedule = schedule;
        this.searchCriteria = searchCriteria;
    }

    public void searchAndPrintTerms(String implAnswer) {
        Scanner scanner = new Scanner(System.in);
        if (implAnswer.equalsIgnoreCase("1")){
            System.out.println("Izaberite opciju:");
            System.out.println("1 - Pretraga po datumu");
            System.out.println("2 - Pretraga po kriterijumima");
            System.out.print("Unesite broj opcije: ");
            String opcija = scanner.nextLine().trim();

            switch (opcija) {
                case "1":
                    searchByDate(scanner);
                    break;
                case "2":
                    searchByCriteria(scanner);
                    break;
                default:
                    System.out.println("Nepostojeća opcija. Pokušajte ponovo.");
                    break;
            }
        }else if (implAnswer.equalsIgnoreCase("2")){
            System.out.println("Izaberite opciju:");
            System.out.println("1 - Pretraga po jednom datumu");
            System.out.println("2 - Pretraga po kriterijumima");
            System.out.println("3 - Pretraga po periodu");
            System.out.print("Unesite broj opcije: ");
            String opcija = scanner.nextLine().trim();

            switch (opcija) {
                case "1":
                    searchByDate(scanner);
                    break;
                case "2":
                    searchByCriteria(scanner);
                    break;
                case "3":
                    searchByPeriod(scanner);
                    break;
                default:
                    System.out.println("Nepostojeća opcija. Pokušajte ponovo.");
                    break;
            }
        }
    }

    private void searchByDate(Scanner scanner) {
        System.out.print("Unesite datum za pretragu (dd.MM.yyyy): ");
        String inputDate = scanner.nextLine().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            for (LocalDate t : schedule.getIzuzetiDani()){
                if (date.isEqual(t)){
                    System.out.println("Termin je izuzetog dana, ne moze se pretraziti");
                    return;
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Neispravan format datuma. Pokušajte ponovo.");
            return;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startPeriod = LocalDate.parse(schedule.getPeriodPocetak().trim(), dateFormatter);
        LocalDate endPeriod = LocalDate.parse(schedule.getPeriodKraj().trim(), dateFormatter);
        if(date.isAfter(startPeriod) && date.isBefore(endPeriod) || date.isEqual(startPeriod) || date.isEqual(endPeriod)) { // uzima i prvi i poslednji dan perioda
//            if(date.isAfter(startPeriod) && date.isBefore(endPeriod)) {
            String dan = searchCriteria.parseDay(date);
            List<Term> results = new ArrayList<>();
            for (Term term : schedule.getTerms()) {
                if (term.getDay().getName().equals(dan)) {
                    results.add(term);
                }
            }
            for (Term term : results) {
                System.out.println(term);
            }
        } else {
            System.out.println("Nije u vazecem periodu uneseni datum");
        }
    }

    private void searchByCriteria(Scanner scanner) {
        Map<String, String> criteria = new HashMap<>();
        Set<String> originalSet = new HashSet<>(schedule.getHeaderIndexMap().keySet());
        Set<String> set = new HashSet<>(originalSet);

        while (true) {
            System.out.println("Dostupni headeri za pretragu: " + String.join(", ", set));
            System.out.print("Unesite header po kojem želite da vršite pretragu: ");
            String header = scanner.nextLine().trim();

            if (!originalSet.contains(header)) {
                System.out.println("Nepostojeći header. Pokušajte ponovo.");
                continue;
            }

            System.out.print("Unesite vrednost za pretragu: ");
            String value = scanner.nextLine().trim();
            criteria.put(header, value);

            System.out.print("Da li želite dodati još kriterijuma? (Da/Ne): ");
            String odgovor = scanner.nextLine().trim();

            if (odgovor.equalsIgnoreCase("Ne")) {
                break;
            }
        }


        List<Term> results = searchCriteria.searchTermsByCriteria(criteria);
        if (results.isEmpty()) {
            System.out.println("Nema rezultata za zadate kriterijume.");
        } else {
            System.out.println("Rezultati pretrage:");
            for (Term term : results) {
                System.out.println(term);
            }
        }
    }
        public void printFreeSlotsForTeacherCLI () {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Unesite naziv profesora: ");
            String teacherName = scanner.nextLine().trim();
            System.out.println("Unesite početak radnog vremena (HH:MM):");
            LocalTime workStart = LocalTime.parse(scanner.nextLine().trim());
            System.out.println("Unesite kraj radnog vremena (HH:MM):");
            LocalTime workEnd = LocalTime.parse(scanner.nextLine().trim());

            Map<String, List<LocalTime[]>> freeSlots = searchCriteria.getFreeSlotsForTeacher(teacherName, workStart, workEnd);
            // Sada ide ispis slobodnih slotova
            for (Map.Entry<String, List<LocalTime[]>> entry : freeSlots.entrySet()) {
                String day = entry.getKey();
                List<LocalTime[]> slots = entry.getValue();
                System.out.println(day.toUpperCase() + ": slobodni termini za " + teacherName + ":");
                for (LocalTime[] times : slots) {
                    System.out.println(" - " + times[0] + " do " + times[1]);
                }
            }
        }

        public void printOccupiedSlotsForTeacherCLI () {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Unesite naziv profesora: ");
            String teacherName = scanner.nextLine().trim();

            Map<String, List<LocalTime[]>> occupiedSlots = searchCriteria.getOccupiedSlotsForTeacher(teacherName);
            // Sada ide ispis slobodnih slotova
            for (Map.Entry<String, List<LocalTime[]>> entry : occupiedSlots.entrySet()) {
                String day = entry.getKey();
                List<LocalTime[]> slots = entry.getValue();
                System.out.println(day.toUpperCase() + ": zauzeti termini za " + teacherName + ":");
                for (LocalTime[] times : slots) {
                    System.out.println(" - " + times[0] + " do " + times[1]);
                }
            }
        }

    public void printFreeSlotsForRoomCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite naziv ucionice: ");
        String roomName = scanner.nextLine().trim();
        System.out.println("Unesite početak radnog vremena (HH:MM):");
        LocalTime workStart = LocalTime.parse(scanner.nextLine().trim());
        System.out.println("Unesite kraj radnog vremena (HH:MM):");
        LocalTime workEnd = LocalTime.parse(scanner.nextLine().trim());

        Map<String, List<LocalTime[]>> freeSlots = searchCriteria.getFreeSlotsForRoom(roomName, workStart, workEnd);
        // Sada ide ispis slobodnih slotova
        for (Map.Entry<String, List<LocalTime[]>> entry : freeSlots.entrySet()) {
            String day = entry.getKey();
            List<LocalTime[]> slots = entry.getValue();
            System.out.println(day.toUpperCase() + ": slobodni termini za " + roomName + ":");
            for (LocalTime[] times : slots) {
                System.out.println(" - " + times[0] + " do " + times[1]);
            }
        }
    }

    public void printfOccupiedSlotsForRoomCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite naziv ucionice: ");
        String roomName = scanner.nextLine().trim();

        Map<String, List<LocalTime[]>> occupiedSlots = searchCriteria.getOccupiedSlotsForRoom(roomName);
        // Sada ide ispis slobodnih slotova
        for (Map.Entry<String, List<LocalTime[]>> entry : occupiedSlots.entrySet()) {
            String day = entry.getKey();
            List<LocalTime[]> slots = entry.getValue();
            System.out.println(day.toUpperCase() + ": zauzeti termini za " + roomName + ":");
            for (LocalTime[] times : slots) {
                System.out.println(" - " + times[0] + " do " + times[1]);
            }
        }
    }


    public void searchByPeriod(Scanner scanner) {
        System.out.print("Unesite početni datum perioda za pretragu (dd.MM.yyyy): ");
        String startDateInput = scanner.nextLine().trim();
        System.out.print("Unesite krajnji datum perioda za pretragu (dd.MM.yyyy): ");
        String endDateInput = scanner.nextLine().trim();

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateInput, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            endDate = LocalDate.parse(endDateInput, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Neispravan format datuma. Pokušajte ponovo.");
            return;
        }

        List<Term> results = new ArrayList<>();
        for (Term term : schedule.getTerms()) {

            LocalDate termStart = term.getPeriod().getStartPeriod();
            LocalDate termEnd = term.getPeriod().getEndPeriod();

            // Provera da li se periodi preklapaju
            if (!startDate.isAfter(termEnd) && !endDate.isBefore(termStart)) {
                results.add(term);
            }
        }

        // Ispis rezultata
        if (results.isEmpty()) {
            System.out.println("Nema termina u zadatom periodu.");
        } else {
            System.out.println("Termini u periodu od " + startDateInput + " do " + endDateInput + ":");
            for (Term term : results) {
                System.out.println(term);
            }
        }
    }

}

