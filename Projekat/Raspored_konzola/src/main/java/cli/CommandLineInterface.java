package cli;
import api.ITermManager;
import api.Manager;
import api.SpecFileExport;
import handlers.MoveTermHandler;
import handlers.SearchHandler;
import handlers.TermHandler;
import io.CSVFileImporter;
import io.JsonFileImporter;
import io.RoomFileLoader;
import model.*;
import java.io.FileNotFoundException;
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
    private RoomFileLoader roomFileLoader;
    private SpecFileExport fileExporter;
    private String implAnswer;

    private String room_path;
    public CommandLineInterface() {
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
        this.searchHandler = new SearchHandler(schedule, searchCriteria);
        this.termHandler = new TermHandler(schedule, termManager);
        this.moveTermHandler = new MoveTermHandler(schedule, termManager);
    }

    private String fileTypePath(String s, Scanner scanner) {
        if (s.equalsIgnoreCase("JSON")) {
            jsonFileImporter = new JsonFileImporter();
            System.out.println("Unesite putanju do fajla: (raspored.json)");
            //String filePath = scanner.nextLine();
            String filePath = "raspored.json"; // vratiti scanner kasnije
            return filePath;
        } else if (s.equalsIgnoreCase("CSV")) {
            csvFileImporter = new CSVFileImporter();
            System.out.println("Unesite putanju do fajla: (/raspored.csv)");
            //String filePath = scanner.nextLine();
            String filePath = "/raspored.csv"; // vratiti scanner kasnije
            return filePath;
        } else {
            System.out.println("Pogresan unos");
            return null;
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite tip fajla koji ucitavate: (JSON ili CSV)");
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

        // try cactch za txt
        try {
            roomFileLoader = new RoomFileLoader();
            room_path = "C:\\Users\\User\\Desktop\\Softverske komponente\\clonedProject\\Projekat\\Specifikacija\\src\\main\\resources\\room.txt";
            roomFileLoader.importFile("C:\\Users\\User\\Desktop\\Softverske komponente\\clonedProject\\Projekat\\Specifikacija\\src\\main\\resources\\room.txt");
            System.out.println("Uspesno ucitan txt fajl: " + "/room.txt");
        } catch (Exception e) {
            System.out.println("usao sam ovde");
            return;
        }

        System.out.println();
        System.out.println("Na koji nacin zelite da cuvate fajl?");
        System.out.println("1. Raspored se čuva kao kolekcija konkretnih termina u vremenu i prostoru");
        System.out.println("2. Raspored se čuva na nedeljnom nivou za zadati period");
        implAnswer = scanner.nextLine();
        if (implAnswer.equals("1")) {
//            fileExporter = new FileExporter(Schedule.getInstance());
            try {
                Class.forName("nikolaluka.raspored.impl1.FileExporter");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (implAnswer.equals("2")) {
            try {
                Class.forName("nikolaluka.raspored.impl2.FileExporter2");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Pogresan unos");
            return;
        }

        fileExporter = Manager.getSpecFileExport();



        System.out.println("Unesite izuzete dane u obliku dd.mm.yyyy ili Unesite 'kraj' za prekid");
        while (true) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("kraj")) break;

            if (termManager.parseIzuzetiDani(command)) System.out.println("Uspesno dodat izuzet dan: " + command);
            else System.out.println("Neuspesno dodat izuzet dan (nije u opsegu ili nije dobar format): " + command);          

            if (Schedule.getInstance().getIzuzetiDani().isEmpty())
                System.out.println("Unesite izuzete dane u obliku dd.mm.yyyy ili Unesite 'kraj' za prekid");
            else System.out.println("Da li zelite da dodate jos datuma? Unesite 'kraj' za prekid");

        }


        while (true) {
            showMenu();
            System.out.println("Izaberite opciju (ili 'exit' za izlaz):");
            String command = scanner.nextLine();
            if ("exit".equals(command)) {
                break;
            }
            try {
                executeCommand(command);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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
        System.out.println("10. Stampaj izuzete dane");
        System.out.println("11. Export File");
        System.out.println("12. Dodavanje prostorije sa karakteristikama");
    }

    private void executeCommand(String command) throws FileNotFoundException {
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
                searchHandler.searchAndPrintTerms(implAnswer);
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
            case "10":
                System.out.println("Lista Izuzetih dana: " + Schedule.getInstance().getIzuzetiDani());
                break;
            case "11":
                exportFileCLI();
                break;
            case "12":
                termHandler.AddTermTxt(room_path);
                break;
            default:
                System.out.println("Nepoznata komanda. Molim vas pokušajte ponovo.");
                break;
        }
    }
    private void exportFileCLI() {
        System.out.println("Unesite oblik fajla za export: 1)TXT 2)JSON 3)CSV ---- UNOS: 1,2 ili 3");
        Scanner scanner = new Scanner(System.in);
        String format = scanner.nextLine();

        System.out.println("Unesite putanju gde hocete da sacuvate file: ");
        // Ovde bi korisnik trebao da unese putanju
//        String path = scanner.nextLine();
        String path = "C:\\Users\\User\\Desktop\\Softverske komponente\\clonedProject\\Projekat\\Specifikacija\\src\\main\\resources\\export.csv";

        switch (format.toLowerCase()) {
            case "1":
                exportFile(path, "txt");
                break;
            case "2":
                exportFile(path, "json");
                break;
            case "3":
                exportFile(path, "csv");
                break;
            default:
                System.out.println("Neispravan format. Pokušajte ponovo.");
                break;
        }
    }
    private void exportFile(String path, String format) {
        try {
            if (implAnswer.equalsIgnoreCase("1")) {
                switch (format) {
                    case "txt":
                        fileExporter.exportFileTXT(path);
                        break;
                    case "json":
                        fileExporter.exportFileJSON(path);
                        break;
                    case "csv":
                        fileExporter.exportFileCSV(path);
                        break;
                }
            } else if (implAnswer.equalsIgnoreCase("2")) {
                switch (format) {
                    case "txt":
                        fileExporter.exportFileTXT(path);
                        break;
                    case "json":
                        System.out.println("usao sam");
                        fileExporter.exportFileJSON(path);
                        break;
                    case "csv":
                        fileExporter.exportFileCSV(path);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Navedena putanja nije pronađena: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSchedule() {
        for (Term term : Schedule.getInstance().getTerms()) {
            System.out.println("Day: " + term.getDay().getName());
            System.out.println("Room: " + term.getRoom().getName());
            System.out.println("Capacity: " + term.getRoom().getCapacity());
            System.out.println("Additional: " + term.getRoom().getAdditional());
            System.out.println("Start Time: " + term.getTime().getStartTime());
            System.out.println("End Time: " + term.getTime().getEndTime());
            System.out.println("Additional Data: " + term.getAdditionalProperties());
            System.out.println("Period: " + term.getPeriod());
            System.out.println("-----");
        }
    }
}
