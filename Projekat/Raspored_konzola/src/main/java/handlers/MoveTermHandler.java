package handlers;

import api.ITermManager;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MoveTermHandler {


    private Schedule schedule;
    private ITermManager termManager;
    private Scanner scanner;

    public MoveTermHandler(Schedule schedule, ITermManager termManager){
        this.schedule = schedule;
        this.termManager = termManager;
        this.scanner = new Scanner(System.in);
    }

    public void editSchedule() {
        Term termToModify = getTermToModify();

        LocalDate splitDate = getSplitDate(termToModify);
        if (splitDate == null) {
            return;
        }

        Term originalTerm = termManager.makeOriginalTerm(termToModify, splitDate);
        Term newTerm = createNewTermWithUserInput(termToModify, splitDate);

        if (termManager.isTermAvailable(newTerm, Schedule.getInstance().getTerms())) {
            termManager.updateScheduleWithNewTerms(termToModify, originalTerm, newTerm);
        } else {
            System.out.println("Nije moguce dodati termin");
        }

        printTermDetails(originalTerm, newTerm);
    }

    private Term getTermToModify() {
        // Korisnik unosi kriterijume za pretragu termina
        //        System.out.println("Unesite ime nastavnika:");
        //        //String teacherName = scanner.nextLine();
        //        System.out.println("Unesite učionicu:");
        //        //String roomName = scanner.nextLine();
        //        System.out.println("Unesite vreme (HH:mm-HH:mm):");
        //        //String timeRange = scanner.nextLine();
        String teacherName = "Kevin Punter";
        String roomName = "Kolarac2";
        String timeRange = "16:00-18:00";

        return termManager.findTermToModify(teacherName, roomName, timeRange);
    }

    private LocalDate getSplitDate(Term termToModify) {
        System.out.println("Unesite datum do kog želite da zadržite originalni termin (dd.MM.yyyy):");
        String splitDateStr = scanner.nextLine();
        LocalDate splitDate = LocalDate.parse(splitDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        if (!termManager.isDateWithinTermPeriod(termToModify, splitDate)) {
            System.out.println("Uneti datum nije unutar perioda termina.");
            return null;
        }
        return splitDate;
    }

    private Term createNewTermWithUserInput(Term termToModify, LocalDate splitDate) {
        System.out.println("Šta želite da promenite za novi termin? 1 - Učionicu, 2 - Vreme, 3 - Oboje");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        Room newRoom = getNewRoom(termToModify, choice);
        Time newTime = getNewTime(termToModify, choice);

        return termManager.makeNewTerm(termToModify, splitDate, newRoom, newTime);
    }

    private Room getNewRoom(Term term, int choice) {
        if (choice == 1 || choice == 3) {
            System.out.println("Unesite novu učionicu:");
            String newRoomName = scanner.nextLine();
            return new Room(newRoomName);
        }
        return term.getRoom();
    }

    private Time getNewTime(Term term, int choice) {
        if (choice == 2 || choice == 3) {
            System.out.println("Unesite novo vreme (HH:mm-HH:mm):");
            String newTimeRange = scanner.nextLine();
            return termManager.splitTime(newTimeRange);
        }
        return term.getTime();
    }

    private void printTermDetails(Term originalTerm, Term newTerm) {
        System.out.println("Stari izmenjeni termin: " + originalTerm);
        System.out.println("Novi dodati termin: " + newTerm);
    }





}
