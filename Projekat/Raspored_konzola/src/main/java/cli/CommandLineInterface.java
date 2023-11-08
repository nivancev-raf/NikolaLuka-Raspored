package cli;

import api.ITermManager;
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
    private JsonFileImporter jsonFileImporter;
    private CSVFileImporter csvFileImporter;
    public CommandLineInterface() {
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
        this.searchHandler = new SearchHandler(schedule, searchCriteria);
        this.termHandler = new TermHandler(schedule, termManager);
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
                deleteTerm();
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
            System.out.println("Period: " + term.getPeriod());
            System.out.println("-----");
        }
    }


    public void editSchedule(){
        Scanner scanner = new Scanner(System.in);
        List<Term> terms = Schedule.getInstance().getTerms();

//        // Korisnik unosi kriterijume za pretragu termina
//        System.out.println("Unesite ime nastavnika:");
//        //String teacherName = scanner.nextLine();
//        System.out.println("Unesite učionicu:");
//        //String roomName = scanner.nextLine();
//        System.out.println("Unesite vreme (HH:mm-HH:mm):");
//        //String timeRange = scanner.nextLine();

        String teacherName = "Brnabic Mateja";
        String roomName = "Rg07 (u)";
        String timeRange = "13:15-15:00";

        Term termToModify = null;
        for(Term term: terms){
            if(term.getAdditionalProperty("Nastavnik").equals(teacherName) && term.getRoom().getName().equals(roomName)
            && term.getTime().toString().equals(timeRange)){
                termToModify = term;
                break;
            }
        }

        System.out.println("Unesite datum do kog želite da zadržite originalni termin (dd.MM.yyyy):");
        String splitDateStr = scanner.nextLine();
        LocalDate splitDate = LocalDate.parse(splitDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        if (splitDate.isBefore(termToModify.getPeriod().getStartPeriod()) || splitDate.isAfter(termToModify.getPeriod().getEndPeriod())) {
            System.out.println("Uneti datum nije unutar perioda termina.");
            return;
        }

        Term originalTerm = new Term(termToModify.getRoom(), termToModify.getDay(), termToModify.getTime(),
                new Period(termToModify.getPeriod().getStartPeriod(), splitDate));
        originalTerm.setAdditionalProperties(termToModify.getAdditionalProperties());

//        System.out.println(originalTerm);

        System.out.println("Šta želite da promenite za novi termin? 1 - Učionicu, 2 - Vreme, 3 - Oboje");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        Room newRoom = termToModify.getRoom();
        Time newTime = termToModify.getTime();

        if (choice == 1 || choice == 3) {
            System.out.println("Unesite novu učionicu:");
            String newRoomName = scanner.nextLine();
            newRoom = new Room(newRoomName);
        }

        LocalTime newStartTime = null;
        LocalTime newEndTime = null;

        if (choice == 2 || choice == 3) {
            System.out.println("Unesite novo vreme (HH:mm-HH:mm):");
            String newTimeRange = scanner.nextLine();
            String[] timeParts = newTimeRange.split("-");
            newStartTime = LocalTime.parse(timeParts[0].trim(), DateTimeFormatter.ofPattern("HH:mm"));
            newEndTime = LocalTime.parse(timeParts[1].trim(), DateTimeFormatter.ofPattern("HH:mm"));
            newTime = new Time(newStartTime, newEndTime);
        }



        // Kreiranje novog termina sa novim vrednostima od unetog datuma
        Term newTerm = new Term(newRoom, termToModify.getDay(), newTime,
                new Period(splitDate.plusDays(1), termToModify.getPeriod().getEndPeriod()));
        newTerm.setAdditionalProperties(termToModify.getAdditionalProperties());

        if(isTermAvailable(newTerm,terms)){
            terms.add(originalTerm);
            terms.add(newTerm);
            terms.remove(termToModify);
        } else {
            System.out.println("Nije moguce dodati termin");
        }

        System.out.println("stari izmenjeni termin: " + originalTerm);
        System.out.println("novi dodati termin: " + newTerm);
    }

    public boolean isTermAvailable(Term newTerm, List<Term> existingTerms) {
        for (Term existingTerm : existingTerms) {
            // Provera da li se vreme i učionica poklapaju
            if (newTerm.getRoom().getName().equals(existingTerm.getRoom().getName()) &&
                    newTerm.getTime().overlaps(existingTerm.getTime()) && newTerm.getDay().equals(existingTerm.getDay())) {
                // Ako se vreme i učionica poklapaju, proveravamo da li se poklapaju i periodi
                if (newTerm.getPeriod().overlaps(existingTerm.getPeriod())) {
                    // Ako se i periodi poklapaju, termin nije dostupan
                    return false;
                }
            }
        }
        // Ako nema poklapanja, termin je dostupan
        return true;
    }

    public void deleteTerm() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite dan:");
        String dayInput = scanner.nextLine().trim();

        System.out.println("Unesite vreme (npr. 11:15-13):");
        String timeInput = scanner.nextLine().trim();

        System.out.println("Unesite učionicu:");
        String roomInput = scanner.nextLine().trim();

        System.out.println("Unesite nastavnika:");
        String teacherInput = scanner.nextLine().trim();

        for(Term term: schedule.getTerms()){
            if(term.getAdditionalProperties().get("Nastavnik").equals(teacherInput) && term.getRoom().getName().equals(roomInput) && term.getTime().toString().equals(timeInput) &&
                    term.getDay().getName().equals(dayInput)){
                schedule.getTerms().remove(term);
                break;
            }
        }
    }

}
