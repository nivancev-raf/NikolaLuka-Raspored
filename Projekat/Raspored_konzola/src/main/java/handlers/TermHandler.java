package handlers;

import api.ITermManager;
import model.Schedule;
import model.Term;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TermHandler {
    private Schedule schedule;
    private ITermManager termManager;

    public TermHandler(Schedule schedule, ITermManager termManager){
        this.schedule = schedule;
        this.termManager = termManager;
    }

    public void addTermCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite dan:");
        String dayInput = scanner.nextLine().trim();

        System.out.println("Unesite vreme (npr. 11:15-13):");
        String timeInput = scanner.nextLine().trim();

        System.out.println("Unesite učionicu:");
        String roomInput = scanner.nextLine().trim();

        System.out.println("Unesite period:");
        String periodInput = scanner.nextLine().trim();


        System.out.println("Da li želite da unesete dodatna polja? (Da/Ne)");
        String answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("Da")) {
            while(true){
                Set<String> availableHeaders = new HashSet<>(schedule.getHeaderIndexMap().keySet());
                availableHeaders.removeAll(Arrays.asList("Dan", "Termin", "Učionica"));
                availableHeaders.removeAll(termManager.getAdditionalProperties().keySet());

                if (availableHeaders.isEmpty()) {
                    System.out.println("Svi headeri su popunjeni.");
                    break;
                }
                System.out.println("Dostupni headeri: " + String.join(", ", availableHeaders));
                System.out.print("Unesite header: ");
                String header = scanner.nextLine().trim();

                if (header.equalsIgnoreCase("kraj")) {
                    break;
                }

                if (!availableHeaders.contains(header)) {
                    System.out.println("Nepostojeći ili već unesen header. Pokušajte ponovo.");
                    continue;
                }
                System.out.print("Unesite vrednost: ");
                String value = scanner.nextLine().trim();
                termManager.getAdditionalProperties().put(header, value);
            }

        }else{
            System.out.println("Dodatna polja nisu uneta.");
        }

        Term newTerm = termManager.addTerm(dayInput, timeInput, roomInput, termManager.getAdditionalProperties(),periodInput);

        if (newTerm != null) {
            System.out.println("Broj termina: PRE " + schedule.getTerms().size());
            schedule.getTerms().add(newTerm);
            System.out.println("Termin je uspešno dodat.");
            System.out.println("Broj termina: POSLE " + schedule.getTerms().size());
        } else {
            System.out.println("Termin je zauzet ili nije validan.");
        }
    }
}
