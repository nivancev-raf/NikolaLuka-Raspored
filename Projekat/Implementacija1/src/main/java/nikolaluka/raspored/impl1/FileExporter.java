package nikolaluka.raspored.impl1;

import api.SpecFileExport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Schedule;
import model.SearchCriteria;
import model.Term;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileExporter extends SpecFileExport {

    private SearchCriteria searchCriteria;
    private Schedule schedule;

    public FileExporter(Schedule schedule) {
        this.schedule = Schedule.getInstance();
        this.searchCriteria = new SearchCriteria(schedule);
    }
    @Override
    public void exportFileTXT(String path){
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

    @Override
    public void exportFileCSV(String path) throws FileNotFoundException {
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

    @Override
    public void exportFileJSON(String path) throws FileNotFoundException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Retrieve all terms from the schedule
        List<Term> terms = schedule.getTerms();

        // Create a list to hold the transformed terms for JSON output
        List<Map<String, Object>> jsonTerms = new ArrayList<>();

        // Transform each Term into a Map that will be converted into JSON
        for (Term term : terms) {
            Map<String, Object> jsonTerm = new HashMap<>();
            DayOfWeek dayOfWeek = searchCriteria.reverseParseDay(term.getDay().getName());
            LocalDate startDate = LocalDate.parse(Schedule.getInstance().getPeriodPocetak().trim(), dateFormatter);
            LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj().trim(), dateFormatter);
            List<String> dates = new ArrayList<>();

            while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                if (startDate.getDayOfWeek() == dayOfWeek && !Schedule.getInstance().getIzuzetiDani().contains(startDate)) {
                    dates.add(startDate.format(dateFormatter));
                }
                startDate = startDate.plusDays(1);
            }

            jsonTerm.put("day", term.getDay().getName());
            jsonTerm.put("dates", dates); // List of specific dates for the term
            jsonTerm.put("startTime", term.getTime().getStartTime().toString());
            jsonTerm.put("endTime", term.getTime().getEndTime().toString());
            jsonTerm.put("room", term.getRoom().getName());
            jsonTerm.put("additionalProperties", term.getAdditionalProperties());

            jsonTerms.add(jsonTerm);
        }

        // Create Gson instance with pretty printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonTerms);

        // Write JSON string to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(json);
        } catch (IOException e) {
            // Handle exception or rethrow as a custom exception
            System.err.println("An error occurred while writing JSON to the file: " + e.getMessage());
            // If you want to rethrow it, you can wrap it into a custom exception and throw
            // throw new CustomExportException("Error while exporting to JSON", e);
        }
    }


}
