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
        Set<DayOfWeek> daysInPeriod = new HashSet<>();
        LocalDate currentStartDate = null;
        LocalDate currentEndDate = null;

        // Provera pojavljivanja dana u periodu
        for (Term term : Schedule.getInstance().getTerms()) {
            LocalDate startDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
            LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);
            DayOfWeek dayOfWeek = searchCriteria.reverseParseDay(term.getDay().getName());

            while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                if (startDate.getDayOfWeek().equals(dayOfWeek)) {
                    daysInPeriod.add(dayOfWeek);
                    break;
                }
                startDate = startDate.plusDays(1);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Term term : Schedule.getInstance().getTerms()) {
                LocalDate startDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
                LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);
                DayOfWeek dayOfWeek = searchCriteria.reverseParseDay(term.getDay().getName());

                // Provera izuzetih dana
                for (LocalDate excludedDate : Schedule.getInstance().getIzuzetiDani()) {
                    if ((excludedDate.isAfter(startDate) || excludedDate.isEqual(startDate)) && excludedDate.isBefore(endDate)) {
                        // Ako je došlo do promene perioda zbog izuzetog dana
                        if (currentStartDate == null || currentEndDate == null || !startDate.isEqual(currentStartDate) || !excludedDate.minusDays(1).isEqual(currentEndDate)) {
                            if (currentStartDate != null && currentEndDate != null) {
                                // Zapisivanje prethodnog perioda
                                writePeriod(writer, currentStartDate, currentEndDate);
                            }
                            // Ažuriranje trenutnog perioda
                            currentStartDate = startDate;
                            currentEndDate = excludedDate.minusDays(1);
                        }
                        // Ažuriranje početnog datuma za sledeći period
                        startDate = excludedDate.plusDays(1);
                    }
                }

                // Provera da li se dan pojavljuje u periodu
                if (daysInPeriod.contains(dayOfWeek)) {
                    // Ako je došlo do promene perioda
                    if (currentStartDate == null || currentEndDate == null || !startDate.isEqual(currentStartDate) || !endDate.isEqual(currentEndDate)) {
                        // Zapisivanje prethodnog perioda
                        if (currentStartDate != null && currentEndDate != null) {
                            writePeriod(writer, currentStartDate, currentEndDate);
                        }
                        // Ažuriranje trenutnog perioda
                        currentStartDate = startDate;
                        currentEndDate = endDate;
                    }
                }
            }

            // Zapisivanje poslednjeg perioda
            if (currentStartDate != null && currentEndDate != null) {
                writePeriod(writer, currentStartDate, currentEndDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePeriod(BufferedWriter writer, LocalDate start, LocalDate end) throws IOException {
        writer.write("PERIOD OD " + start.format(dateFormatter) + " - PERIOD DO " + end.format(dateFormatter) + "\n\n");
        for (Term term : Schedule.getInstance().getTerms()) {
            if (termFallsInPeriod(term, start, end)) {
                writer.write(term.getRoom().getName() + ", " + term.getDay().getName() + ", " + term.getAdditionalProperties().get("Predmet") + ", " + term.getTime().getStartTime() + "-" + term.getTime().getEndTime() + "\n");
            }
        }
        writer.write("\n");
    }

    private boolean termFallsInPeriod(Term term, LocalDate start, LocalDate end) {
        LocalDate termStartDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
        LocalDate termEndDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);
        return !termStartDate.isAfter(end) && !termEndDate.isBefore(start);
    }

}
