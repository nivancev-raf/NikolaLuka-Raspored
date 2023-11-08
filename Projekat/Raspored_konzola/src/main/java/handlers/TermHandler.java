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
        Set<String> originalSet = new HashSet<>(schedule.getHeaderIndexMap().keySet());
        Set<String> set = new HashSet<>(originalSet); // Kopirajte originalni skup

        if (answer.equalsIgnoreCase("Da")) {
            while (true) {
                Set<String> tempSet = new HashSet<>(set); // Napravite kopiju skupa

                tempSet.removeAll(Arrays.asList("Dan", "Termin", "Učionica", "Period"));

                if (tempSet.isEmpty()) {
                    System.out.println("Svi headeri su popunjeni.");
                    break;
                }
                System.out.println("Dostupni headeri: " + String.join(", ", tempSet));
                System.out.print("Unesite header: ");
                String header = scanner.nextLine().trim();

                if (header.equalsIgnoreCase("kraj")) {
                    break;
                }

                if (!tempSet.contains(header)) {
                    System.out.println("Nepostojeći ili već unesen header. Pokušajte ponovo.");
                    continue;
                }
                System.out.print("Unesite vrednost: ");
                String value = scanner.nextLine().trim();
                Schedule.getInstance().getAdditionalData().put(header, value);
                set.remove(header); // Izbacite element iz izvornog skupa
            }
        }
        else{
            System.out.println("Dodatna polja nisu uneta.");
        }

        Term newTerm = termManager.addTerm(dayInput, timeInput, roomInput, Schedule.getInstance().getAdditionalData(), periodInput);

        if (newTerm != null) {
            System.out.println("Broj termina: PRE " + schedule.getTerms().size());
            schedule.getTerms().add(newTerm);
            System.out.println("Termin je uspešno dodat.");
            System.out.println("Broj termina: POSLE " + schedule.getTerms().size());
        } else {
            System.out.println("Termin je zauzet ili nije validan.");
        }
    }

    public void deleteTerm(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite dan:");
        String dayInput = scanner.nextLine().trim();

        System.out.println("Unesite vreme (npr. 11:15-13):");
        String timeInput = scanner.nextLine().trim();

        System.out.println("Unesite učionicu:");
        String roomInput = scanner.nextLine().trim();

        System.out.println("Unesite nastavnika:");
        String teacherInput = scanner.nextLine().trim();

        termManager.deleteTerm(teacherInput,roomInput,timeInput,dayInput);

    }
}
