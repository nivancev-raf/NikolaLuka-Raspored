import api.ITermManager;
import io.CSVFileImporter;
import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandLineInterface {
    private Schedule schedule;
    private ITermManager termManager;
    private SearchCriteria searchCriteria;
    public CommandLineInterface() {
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
    }

    public void run() {
        CSVFileImporter csvFileImporter = new CSVFileImporter();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite putanju do fajla: (/raspored.csv)");
        String filePath = scanner.nextLine();
        try {
            csvFileImporter.importFile(filePath);
            System.out.println("Uspesno ucitan fajl: " + filePath);

        } catch (Exception e) {
            System.out.println("FILE: Los unos putanje fajla ili nepostojeci fajl");
            return;
        }
        System.out.println("Unesite period vazenja rasporeda u formatu: dd.MM.yyyy-dd.MM.yyyy   (primer: 01.01.2020-11.01.2020)");
        String periodVazenja = scanner.nextLine();

        try {
            schedule.setPeriodVazenja(periodVazenja);
            System.out.println("Raspored vazi: ");
            System.out.println( "od: " + schedule.getStartDate());
            System.out.println( "do: " + schedule.getEndDate());
        } catch (Exception e) {
            System.out.println("DATUM: Format datuma nije dobar");
            return;
        }


        while (true) {
            showMenu();
            System.out.println("Izaberite opciju (ili 'exit' za izlaz):");
            String command = scanner.nextLine();
            if ("exit".equals(command)) {
                break;
            }
            executeCommand(command);
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("MENI: ");
        System.out.println();
        System.out.println("-------------------------");
        System.out.println("1. Dodaj Termin");
        System.out.println("2. Obrisi Termin");
        System.out.println("3. Pretrazi Termine");

    }

    private void executeCommand(String command) {
        switch (command) {
            case "1":
                // Logika za dodavanje termina
                termManager.addTerm();
                break;
            case "2":
                // Logika za brisanje termina
                break;
            case "3":
                // Logika za pretragu termina
                searchAndPrintTerms();
                break;
            case "4":
                // Logika za pretragu termina
                printSchedule();
                break;
            default:
                System.out.println("Nepoznata komanda. Molim vas pokušajte ponovo.");
                break;
        }
    }


    private void searchAndPrintTerms() {
        Map<String, String> criteria = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        // ovde dodajete logiku za unos kriterijuma
        // ...
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

    public void printSchedule() {
        for (Term term : Schedule.getInstance().getTerms()) {
            System.out.println("Day: " + term.getDay().getName());
            System.out.println("Room: " + term.getRoom().getName());
            System.out.println("Start Time: " + term.getTime().getStartTime());
            System.out.println("End Time: " + term.getTime().getEndTime());
            System.out.println("Additional Data: " + term.getAdditionalProperties());
            System.out.println("-----");
        }
    }

}
