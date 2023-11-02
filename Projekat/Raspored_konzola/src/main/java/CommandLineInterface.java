import api.ITermManager;
import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.util.Scanner;

public class CommandLineInterface {
    private Schedule schedule;
    private ITermManager termManager;
    private SearchCriteria searchCriteria;
    public CommandLineInterface() {
        // Učitajte implementaciju rasporeda
        this.schedule = Schedule.getInstance();
        this.termManager = new Term(schedule);
        this.searchCriteria = new SearchCriteria(schedule);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
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
        // Dodajte ostale opcije po potrebi
    }

    private void executeCommand(String command) {
        switch (command) {
            case "1":
                // Logika za dodavanje termina
                termManager.addTerm();
                break;
            case "2":
                // Logika za brisanje termina
                // termManager.deleteTerm(???); // Trebaće vam neki način da identifikujete koji termin želite obrisati
                break;
            case "3":
                // Logika za pretragu termina
                searchCriteria.searchTermsByCriteria();
                break;
            default:
                System.out.println("Nepoznata komanda. Molim vas pokušajte ponovo.");
                break;
        }
    }
}
