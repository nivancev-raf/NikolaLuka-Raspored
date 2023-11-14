package io;

import api.FileImportExport;
import model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RoomFileLoader extends FileImportExport {
    private Map<String, Integer> roomHeaderIndexMap = Schedule.getInstance().getRoomHeaderIndexMap();
    private List<Room> rooms = Schedule.getInstance().getRoomList();

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

                    for (Term term : Schedule.getInstance().getTerms()) {
                        if (term.getRoom().getName().equals(roomValue)) {
                            rooms.add(term.getRoom());
                            term.getRoom().setCapacity(Integer.parseInt(capacityValue));
                        }
                    }

                    Map<String, String> additionalProperties = new HashMap<>();
                    for (Map.Entry<String, Integer> entry : roomHeaderIndexMap.entrySet()) {
                        if (!entry.getKey().equals("Ucionica") && !entry.getKey().equals("Kapacitet")) {
                            String additionalValue = values[entry.getValue()].trim().replaceAll("^\"|\"$", "");
                            additionalProperties.put(entry.getKey(), additionalValue);
                        }
                    }

                    // Ažuriranje dodatnih informacija za svaku učionicu
                    for (Term term : Schedule.getInstance().getTerms()) {
                        if (term.getRoom().getName().equals(roomValue)) {
                            term.getRoom().setAdditional(additionalProperties);
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}