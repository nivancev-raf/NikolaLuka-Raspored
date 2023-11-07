package cli;

import api.ITermManager;
import handlers.SearchHandler;
import handlers.TermHandler;
import io.CSVFileImporter;
import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.util.*;

public class CommandLineInterface {
    private Schedule schedule;
    private ITermManager termManager;
    private SearchCriteria searchCriteria;
    private SearchHandler searchHandler;
    private TermHandler termHandler;
    public CommandLineInterface() {
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
        this.searchHandler = new SearchHandler(schedule, searchCriteria);
        this.termHandler = new TermHandler(schedule, termManager);
    }

    public void run() {
        CSVFileImporter csvFileImporter = new CSVFileImporter();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite putanju do fajla: (/raspored.csv)");
        //String filePath = scanner.nextLine();
        String filePath = "/raspored.csv"; // vratiti scanner kasnije
        try {
            csvFileImporter.importFile(filePath);
            System.out.println("Uspesno ucitan fajl: " + filePath);

        } catch (Exception e) {
            System.out.println("FILE: Los unos putanje fajla ili nepostojeci fajl");
            return;
        }
        System.out.println("Unesite period vazenja rasporeda u formatu: dd.MM.yyyy-dd.MM.yyyy   (primer: 01.01.2020-11.01.2020)");
//        String periodVazenja = scanner.nextLine();
        String periodVazenja = "01.10.2020-11.10.2020"; // vratiti scanner kasnije
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
        System.out.println("-------------------------");
        System.out.println();
        System.out.println("MENI: ");
        System.out.println();
        System.out.println("-------------------------");
        System.out.println("1. Dodaj Termin");
        System.out.println("2. Obrisi Termin");
        System.out.println("3. Pretrazi Termine");
        System.out.println("4. Pretrazi Slobone Termine za Nastavnika");
        System.out.println("5. Izlistaj zauzete termine za Nastavnika");
        System.out.println("6. Premestanje termina");
        System.out.println("7. Stampanje celog rasporeda");

    }

    private void executeCommand(String command) {
        switch (command) {
            case "1":
                // Logika za dodavanje termina
                termHandler.addTermCLI();
                break;
            case "2":
                // Logika za brisanje termina
                break;
            case "3":
                // Logika za pretragu termina
                searchHandler.searchAndPrintTerms();
                break;
            case "4":
                // Logika za pretragu termina slobodnih za nastavnika
                searchHandler.printFreeSlotsForTeacherCLI();
                break;
            case "5":
                // Logika za pretragu zauzetih termina za nastavnika
                searchHandler.printOccupiedSlotsForTeacherCLI();
                break;
            case "6":
                editSchedule();
                break;
            case "7":
                // printaj ceo termin
                printSchedule();
                break;
            default:
                System.out.println("Nepoznata komanda. Molim vas pokušajte ponovo.");
                break;
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


    public void editSchedule(){

    }

}
