package io;

import api.FileImportExport;
import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CSVFileImporter extends FileImportExport {

    private Map<String, Integer> headerIndexMap;


    @Override
    public void importFile(String path) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8)))  {
            String line = br.readLine();
            if (line != null) {
                String[] headers = line.split(",");
                Map<String, Integer> headerIndexMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    headerIndexMap.put(headers[i].trim().replaceAll("^\"|\"$", ""), i);
                }

                while ((line = br.readLine()) != null) {
                    int i = 0;
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    String dayValue = values[headerIndexMap.get("Dan")].trim().replaceAll("^\"|\"$", "");
                    String roomValue = values[headerIndexMap.get("Učionica")].trim().replaceAll("^\"|\"$", "");
                    String timeValue = values[headerIndexMap.get("Termin")].trim().replaceAll("^\"|\"$", "");
                    String periodValue = values[headerIndexMap.get("Period")].trim().replaceAll("^\"|\"$", "");

                    String[] timeParts = timeValue.split("-");
                    LocalTime startTime = parseTime(timeParts[0].trim());
                    LocalTime endTime = parseTime(timeParts[1].trim());

                    String[] periodParts = periodValue.split("-");
                    LocalDate startPeriod = LocalDate.parse(periodParts[0].trim(), dateFormatter);
                    LocalDate endPeriod = LocalDate.parse(periodParts[1].trim(), dateFormatter);

                    Day day = new Day(dayValue);
                    Room room = new Room(roomValue);
                    Time time = new Time(startTime, endTime);
                    Period period = new Period(startPeriod, endPeriod);
                    Term term = new Term(room, day, time, period);
                    if(i==0) {
                        Schedule.getInstance().setPeriodPocetak(periodParts[0]);
                        Schedule.getInstance().setPeriodKraj(periodParts[1]);
                        i++;
                    }

                    Map<String, String> additionalProperties = new HashMap<>();
                    for (Map.Entry<String, Integer> entry : headerIndexMap.entrySet()) {
                        if (!entry.getKey().equals("Dan") && !entry.getKey().equals("Učionica") && !entry.getKey().equals("Termin") && !entry.getKey().equals("Period")) {
                            String additionalValue = values[entry.getValue()].trim().replaceAll("^\"|\"$", "");
                            additionalProperties.put(entry.getKey(), additionalValue);
                        }
                    }
                    term.setAdditionalProperties(additionalProperties);
                    Schedule.getInstance().getTerms().add(term);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportFile(String path) {

    }

    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        time = time.replaceAll("\"", "");
        if (!time.contains(":")) {
            time += ":00";
        }
        return LocalTime.parse(time, formatter);
    }

}
