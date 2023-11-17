package nikolaluka.raspored.impl2;
import adapter.LocalDateAdapter;
import adapter.LocalTimeAdapter;
import api.SpecFileExport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Schedule;
import model.SearchCriteria;
import model.Term;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileExporter2 extends SpecFileExport {

    Schedule schedule;
    private SearchCriteria searchCriteria;

    private Set<Date> datumi = new HashSet<>();
    public FileExporter2(Schedule schedule) {
        this.schedule = Schedule.getInstance();
        this.searchCriteria = new SearchCriteria(schedule);
    }

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void exportFileTXT(String path) {

        List<LocalDate> lista1 = Schedule.getInstance().getKrajnji();
        List<LocalDate> lista2 = Schedule.getInstance().getIzuzetiDani();
        List<LocalDate> lista3 = Schedule.getInstance().getPocetni();

        Set<LocalDate> spojeniSet = new HashSet<>();
        spojeniSet.addAll(lista1);
        spojeniSet.addAll(lista2);
        spojeniSet.addAll(lista3);

        List<LocalDate> spojenaLista = new ArrayList<>(spojeniSet);

        spojenaLista.sort(Comparator.naturalOrder());
        System.out.println(spojenaLista);

        // Sortiranje termina po početnom datumu perioda
        Schedule.getInstance().getTerms().sort(Comparator.comparing(term -> LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter)));


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            LocalDate startDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter); // globalni pocetni datum
            LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter); // globalni krajnji datum
            LocalDate currentStartDate = startDate; //
            LocalDate currentEndDate = endDate;

            for (LocalDate excludedDate : spojenaLista) {
                if ((excludedDate.isAfter(startDate) || excludedDate.isEqual(startDate)) && excludedDate.isBefore(endDate)) {
                    if (lista2.contains(excludedDate)) {
                        currentEndDate = excludedDate.minusDays(1);
                    }else{
                        currentEndDate = excludedDate;
                    }
                    if (currentEndDate.isBefore(currentStartDate) || currentEndDate.isEqual(currentStartDate)) {
                        continue;
                    }
                    writePeriod(writer, currentStartDate, currentEndDate);
                    currentStartDate = excludedDate.plusDays(1);
                }
            }
            // Pisanje za poslednji period ako postoji
            if (!currentStartDate.isAfter(endDate)) {
                System.out.println("Usao u poslednji period");
                writePeriod(writer, currentStartDate, endDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportFileCSV(String outputPath) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            List<String> headers = new ArrayList<>(Schedule.getInstance().getHeaderIndexMap().keySet());
            Collections.sort(headers, Comparator.comparingInt(Schedule.getInstance().getHeaderIndexMap()::get));
            String csvHeader = String.join(",", headers) + "\n";
            writer.write(csvHeader);


            for (Term term : Schedule.getInstance().getTerms()) {
                List<String> termDetails = new ArrayList<>();
                for (String header : headers) {
                    String detail;
                    switch (header) {
                        case "Dan":
                            detail = term.getDay().getName();
                            break;
                        case "Termin":
                            detail = term.getTime().getStartTime().toString() + "-" + term.getTime().getEndTime().toString();
                            break;
                        case "Ucionica":
                            detail = term.getRoom().getName();
                            break;
                        case "Period":
                            detail = term.getPeriodString();
                            break;
                        default:
                            detail = term.getAdditionalProperties().getOrDefault(header, "");
                            break;
                    }
                    termDetails.add(detail);
                }
                writer.write(String.join(",", termDetails) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void exportFileJSON(String path) throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();

        List<Term> terms = schedule.getTerms();
        String json = gson.toJson(terms);

        File file = new File(path);
        try {
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    System.err.println("Failed to create new file at the specified path.");
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(json);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing JSON to the file: " + e.getMessage());
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
        LocalDate termStartDate = term.getStartPeriod();
        LocalDate termEndDate = term.getEndPeriod();
        DayOfWeek termDayOfWeek = searchCriteria.reverseParseDay(term.getDay().getName());

        boolean isInPeriod = !termStartDate.isAfter(end) && !termEndDate.isBefore(start);

        boolean isDayInPeriod = false;
        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == termDayOfWeek) {
                isDayInPeriod = true;
                break;
            }
        }

        return isInPeriod && isDayInPeriod;
    }
}