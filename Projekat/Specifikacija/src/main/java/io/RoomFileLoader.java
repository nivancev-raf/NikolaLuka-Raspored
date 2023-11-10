package io;

import api.FileImportExport;
import model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoomFileLoader extends FileImportExport {
    private Map<String, Integer> roomHeaderIndexMap = Schedule.getInstance().getRoomHeaderIndexMap();
    private Set<Room> roomCSV = new HashSet<>(Schedule.getInstance().getRoomList());
    @Override
    public void importFile(String path) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8)))  {

            String line = br.readLine();
            if (line != null) {
                String[] headers = line.split(",");
                for (int i = 0; i < headers.length; i++) {
                    roomHeaderIndexMap.put(headers[i].trim().replaceAll("^\"|\"$", ""), i);
                }
                while ((line = br.readLine()) != null) {
                    int i = 0;
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    String roomValue = values[roomHeaderIndexMap.get("Ucionica")].trim().replaceAll("^\"|\"$", "");
                    String capacityValue = values[roomHeaderIndexMap.get("Kapacitet")].trim().replaceAll("^\"|\"$", "");
                    System.out.println("roomvalue" + roomValue);

                    System.out.println("roomcsv " + roomCSV);
                    for (Room roomcsv : roomCSV) {
                        if (roomcsv.getName().equals(roomValue)) {
                            for (Term terms : Schedule.getInstance().getTerms()) {
                                if (terms.getRoom().getName().equals(roomValue)) {
                                    roomcsv.setCapacity(Integer.parseInt(capacityValue));
                                }
                            }
//                            roomcsv.setCapacity(Integer.parseInt(capacityValue));

                        }
                    }


//                    Day day = new Day(dayValue);
//                    Room room = new Room(roomValue);
//                    Time time = new Time(startTime, endTime);
//                    Period period = new Period(startPeriod, endPeriod);
//                    Term term = new Term(room, day, time, period);
//                    if(i==0) {
//                        Schedule.getInstance().setPeriodPocetak(periodParts[0]);
//                        Schedule.getInstance().setPeriodKraj(periodParts[1]);
//                        i++;
//                    }
//
//                    Map<String, String> additionalProperties = new HashMap<>();
//                    for (Map.Entry<String, Integer> entry : headerIndexMap.entrySet()) {
//                        if (!entry.getKey().equals("Dan") && !entry.getKey().equals("Učionica") && !entry.getKey().equals("Termin") && !entry.getKey().equals("Period")) {
//                            String additionalValue = values[entry.getValue()].trim().replaceAll("^\"|\"$", "");
//                            additionalProperties.put(entry.getKey(), additionalValue);
//                        }
//                    }
//                    term.setAdditionalProperties(additionalProperties);
//                    Schedule.getInstance().getTerms().add(term);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
