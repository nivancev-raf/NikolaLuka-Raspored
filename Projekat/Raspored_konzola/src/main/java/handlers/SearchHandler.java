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

    public void searchAndPrintTerms() {
        Scanner scanner = new Scanner(System.in);
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
    }

    private void searchByDate(Scanner scanner) {
        System.out.print("Unesite datum za pretragu (dd.MM.yyyy): ");
        String inputDate = scanner.nextLine().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Neispravan format datuma. Pokušajte ponovo.");
            return;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startPeriod = LocalDate.parse(schedule.getPeriodPocetak().trim(), dateFormatter);
        LocalDate endPeriod = LocalDate.parse(schedule.getPeriodKraj().trim(), dateFormatter);
        if(date.isAfter(startPeriod) && date.isBefore(endPeriod)) {
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
        while (true) {
            System.out.println("Dostupni headeri za pretragu: " + String.join(", ", schedule.getHeaderIndexMap().keySet()));
            System.out.print("Unesite header po kojem želite da vršite pretragu: ");
            String header = scanner.nextLine().trim();

            if (!schedule.getHeaderIndexMap().containsKey(header)) {
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
            System.out.println("Unesite početak radnog vremena (HH:MM):");
            LocalTime workStart = LocalTime.parse(scanner.nextLine().trim());
            System.out.println("Unesite kraj radnog vremena (HH:MM):");
            LocalTime workEnd = LocalTime.parse(scanner.nextLine().trim());

            Map<String, List<LocalTime[]>> occupiedSlots = searchCriteria.getOccupiedSlotsForTeacher(teacherName, workStart, workEnd);
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
    }

