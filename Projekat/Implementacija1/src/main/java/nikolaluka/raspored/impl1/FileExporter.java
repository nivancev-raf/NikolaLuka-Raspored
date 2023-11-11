package nikolaluka.raspored.impl1;

import api.SpecFileExport;
import model.Schedule;
import model.SearchCriteria;
import model.Term;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class FileExporter extends SpecFileExport {

    private SearchCriteria searchCriteria;
    private Schedule schedule;

    public FileExporter(Schedule schedule) {
        this.schedule = Schedule.getInstance();
        this.searchCriteria = new SearchCriteria(schedule);
    }
    @Override
    public void exportFile(String path){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Pisanje zaglavlja u CSV fajl
            writer.write("Dan,Period,Termin,Ucionica\n");

            // Pisanje svakog termina kao red u CSV fajlu
            for (Term term : schedule.getTerms()) {
                DayOfWeek day = searchCriteria.reverseParseDay(term.getDay().getName());
                LocalDate date = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
                LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);

                while (date.isBefore(endDate) || date.isEqual(endDate)) {
                    if (date.getDayOfWeek().equals(day) && !Schedule.getInstance().getIzuzetiDani().contains(date)) {
                        writer.write(String.format("%s,%s,%s-%s,%s\n",
                                term.getDay().getName(),
                                date.format(dateFormatter),
                                term.getTime().getStartTime().toString(),
                                term.getTime().getEndTime().toString(),
                                term.getRoom().getName()
                        ));
                    }
                    date = date.plusDays(1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
