package nikolaluka.raspored.impl2;

import api.FileImportExport;
import api.SpecFileExport;
import model.Schedule;
import model.SearchCriteria;
import model.Term;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class FileExporter2 extends SpecFileExport {

    Schedule schedule;
    private SearchCriteria searchCriteria;

    public FileExporter2(Schedule schedule) {
        this.schedule = Schedule.getInstance();
        this.searchCriteria = new SearchCriteria(schedule);
    }

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public void exportFile(String path) {
        // Sortiranje izuzetih datuma
        Schedule.getInstance().getIzuzetiDani().sort(Comparator.naturalOrder());

        // Sortiranje termina po početnom datumu perioda
        Schedule.getInstance().getTerms().sort(Comparator.comparing(term -> LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter)));


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            LocalDate startDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
            LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);
            LocalDate currentStartDate = startDate;
            LocalDate currentEndDate = endDate;

            for (LocalDate excludedDate : Schedule.getInstance().getIzuzetiDani()) {
                if ((excludedDate.isAfter(startDate) || excludedDate.isEqual(startDate)) && excludedDate.isBefore(endDate)) {
                    currentEndDate = excludedDate.minusDays(1);
                    writePeriod(writer, currentStartDate, currentEndDate);
                    currentStartDate = excludedDate.plusDays(1);
                }
            }
            // Pisanje za poslednji period ako postoji
            if (!currentStartDate.isAfter(endDate)) {
                writePeriod(writer, currentStartDate, endDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePeriod(BufferedWriter writer, LocalDate start, LocalDate end) throws IOException {
        writer.write("PERIOD OD " + start.format(dateFormatter) + " - PERIOD DO " + end.format(dateFormatter) + "\n\n");
        for (Term term : Schedule.getInstance().getTerms()) {
            if (dayFallsInPeriod(term, start, end) && dayFallsInPeriod(term, start, end)) {
                writer.write(term.getRoom().getName() + ", " + term.getDay().getName() + ", " + term.getAdditionalProperties().get("Predmet") + ", " + term.getTime().getStartTime() + "-" + term.getTime().getEndTime() + "\n");
            }
        }
        writer.write("\n");
    }

    private boolean dayFallsInPeriod(Term term, LocalDate start, LocalDate end) {
        DayOfWeek dayOfWeek = searchCriteria.reverseParseDay(term.getDay().getName());
        LocalDate date = start;
        while (date.isBefore(end.plusDays(1))) { // Uključuje i krajnji datum
            if (date.getDayOfWeek() == dayOfWeek) {
                return true;
            }
            date = date.plusDays(1);
        }
        return false;
    }


}
