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


//    @Override
//    public void importFile(String path) {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
//
//            String line = br.readLine();
//            if (line != null) {
//                line = line.replaceAll("[^\\S\\r\\n]+", " ");
//                String[] headers = line.split(",");
//                headerIndexMap = Schedule.getInstance().getHeaderIndexMap();
//                for (int i = 0; i < headers.length; i++) {
//                    headerIndexMap.put(headers[i].replace("\"", "").trim(), i);
//                }
//
//                while ((line = br.readLine()) != null) {
//                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                    String dayValue = values[headerIndexMap.get("Dan")].replaceAll("^\"|\"$", "").trim();
//                    String roomValue = values[headerIndexMap.get("Učionica")].replaceAll("^\"|\"$", "").trim();
//                    String timeValue = values[headerIndexMap.get("Termin")].replaceAll("^\\s+|\\s+$", "").trim();
//                    String periodValue = values[headerIndexMap.get("Period")].replaceAll("^\\s+|\\s+$", "").trim();
//
//                    String[] timeParts = timeValue.split("-");
//                    LocalTime startTime = parseTime(timeParts[0].trim());
//                    LocalTime endTime = parseTime(timeParts[1].trim());
//
//                    String[] periodParts = periodValue.split("-");
//                    LocalDate startPeriod = LocalDate.parse(periodParts[0].trim());
//                    LocalDate endPeriod = LocalDate.parse(periodParts[1].trim());
//                    System.out.println("start" + startPeriod);
//                    System.out.println("end" + endPeriod);
//
//                    Day day = Schedule.getInstance().getDays().computeIfAbsent(dayValue, Day::new);
//                    Room room = Schedule.getInstance().getRooms().computeIfAbsent(roomValue, Room::new);
//                    Time time = new Time(startTime, endTime);
//                    Period period = new Period(startPeriod,endPeriod);
//                    Term term = new Term(room, day, time,period);
//
//                    Map<String, String> additionalProperties = new HashMap<>();
//                    for (Map.Entry<String, Integer> entry : headerIndexMap.entrySet()) {
//                        if (!entry.getKey().equals("Dan") && !entry.getKey().equals("Učionica") && !entry.getKey().equals("Termin")) {
//                            String additionalValue = values[entry.getValue()].replace("\"", "").trim();
//                            additionalProperties.put(entry.getKey(), additionalValue);
//                        }
//                    }
//                    term.setAdditionalProperties(additionalProperties);
//
//                    Schedule.getInstance().getTerms().add(term);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
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
//                    Schedule.getInstance().getPeriodpocetak().add(startPeriod);
//                    Schedule.getInstance().getPeriodKraj().add(endPeriod);


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



    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        time = time.replaceAll("\"", "");
        if (!time.contains(":")) {
            time += ":00";
        }
        return LocalTime.parse(time, formatter);
    }

}
