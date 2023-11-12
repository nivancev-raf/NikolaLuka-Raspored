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
import java.util.*;

public class CSVFileImporter extends FileImportExport {
    // CSV MANDATORY: Dan, Ucionica, Termin, Period
    private Map<String, Integer> headerIndexMap = Schedule.getInstance().getHeaderIndexMap();
    List<LocalDate> krajnjiDatumi = Schedule.getInstance().getKrajnji();
    List<LocalDate> pocetniDatumi = new ArrayList<>();
    List<LocalDate> poceo = Schedule.getInstance().getPocetni();

    @Override
    public void importFile(String path) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8)))  {
            String line = br.readLine();
            if (line != null) {
                String[] headers = line.split(",");
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

                    pocetniDatumi.add(startPeriod);
                    krajnjiDatumi.add(endPeriod);

                    Day day = new Day(dayValue);
                    Room room = new Room(roomValue);
                    if (!Schedule.getInstance().getRoomList().contains(roomValue)){
                        Schedule.getInstance().getRoomList().add(room);
                    }
                    Time time = new Time(startTime, endTime);
                    Period period = new Period(startPeriod, endPeriod);
                    Term term = new Term(room, day, time, period);
//                    if(i==0) {
//                        Schedule.getInstance().setPeriodPocetak(periodParts[0]);
//                        Schedule.getInstance().setPeriodKraj(periodParts[1]);
//                        i++;
//                    }

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
                pocetniDatumi.sort(Comparator.naturalOrder());
                krajnjiDatumi.sort(Comparator.naturalOrder());
                Schedule.getInstance().setPeriodPocetak(pocetniDatumi.get(0).format(dateFormatter));
                Schedule.getInstance().setPeriodKraj(krajnjiDatumi.get(krajnjiDatumi.size() - 1).format(dateFormatter));
                String date = Schedule.getInstance().getPeriodPocetak();
                LocalDate datum = LocalDate.parse(date,dateFormatter);
                for(LocalDate pocetak: pocetniDatumi){
                    if(pocetak.isAfter(datum)){
                        poceo.add(pocetak);
                    }
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

    public Map<String, Integer> getHeaderIndexMap() {
        return headerIndexMap;
    }
}