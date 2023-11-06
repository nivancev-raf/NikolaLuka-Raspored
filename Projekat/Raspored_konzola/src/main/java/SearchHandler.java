import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchHandler {
    private Schedule schedule;
    private SearchCriteria searchCriteria;

    public SearchHandler(Schedule schedule, SearchCriteria searchCriteria) {
        this.schedule = schedule;
        this.searchCriteria = searchCriteria;
    }

    void searchAndPrintTerms() {
        Map<String, String> criteria = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

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

    void printFreeSlotsForTeacherCLI() {
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

    void printOccupiedSlotsForTeacherCLI() {
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
