package cli;

import api.ITermManager;
import handlers.MoveTermHandler;
import handlers.SearchHandler;
import handlers.TermHandler;
import io.CSVFileImporter;
import io.JsonFileImporter;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommandLineInterface {
    private Schedule schedule;
    private ITermManager termManager;
    private SearchCriteria searchCriteria;
    private SearchHandler searchHandler;
    private TermHandler termHandler;
    private MoveTermHandler moveTermHandler;
    private JsonFileImporter jsonFileImporter;
    private CSVFileImporter csvFileImporter;
    public CommandLineInterface() {
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
        this.searchHandler = new SearchHandler(schedule, searchCriteria);
        this.termHandler = new TermHandler(schedule, termManager);
        this.moveTermHandler = new MoveTermHandler(schedule, termManager);
    }

    private String fileTypePath(String s, Scanner scanner){
        if (s.equalsIgnoreCase("JSON")){
            jsonFileImporter = new JsonFileImporter();
            System.out.println("Unesite putanju do fajla: (raspored.json)");
            //String filePath = scanner.nextLine();
            String filePath = "raspored.json"; // vratiti scanner kasnije
            return filePath;
        }else if (s.equalsIgnoreCase("CSV")){
            csvFileImporter = new CSVFileImporter();
            System.out.println("Unesite putanju do fajla: (/raspored.csv)");
            //String filePath = scanner.nextLine();
            String filePath = "/raspored.csv"; // vratiti scanner kasnije
            return filePath;
        }else{
            System.out.println("Pogresan unos");
            return null;
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite tip fajla: (JSON ili CSV)");
        String fileType = scanner.nextLine();
        String filePath = fileTypePath(fileType, scanner);
        if (filePath == null) return;

        try {
            if (filePath.contains(".json")) jsonFileImporter.importFile(filePath);
            else if (filePath.contains(".csv")) csvFileImporter.importFile(filePath);
            else {
                System.out.println("FILE: Los unos putanje fajla ili nepostojeci fajl");
                return;
            }
            System.out.println("Uspesno ucitan fajl: " + filePath);
        } catch (Exception e) {
            System.out.println(e);
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
        System.out.println("6. Pretrazi slobodne termine za ucionice");
        System.out.println("7. Izlistaj zauzete termine za ucionice");
        System.out.println("8. Premestanje termina");
        System.out.println("9. Stampanje celog rasporeda");

    }

    private void executeCommand(String command) {
        switch (command) {
            case "1":
                // Logika za dodavanje termina
                termHandler.addTermCLI();
                break;
            case "2":
                // Logika za brisanje termina
                termHandler.deleteTerm();
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
                searchHandler.printFreeSlotsForRoomCLI();
                break;
            case "7":
                searchHandler.printfOccupiedSlotsForRoomCLI();
                break;
            case "8":
                moveTermHandler.editSchedule();
                break;
            case "9":
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
            System.out.println("Period: " + term.getPeriod());
            System.out.println("-----");
        }
    }
}
