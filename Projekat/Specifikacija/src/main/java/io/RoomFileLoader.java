package io;

import api.FileImportExport;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomFileLoader extends FileImportExport {
    private Map<String, Integer> roomHeaderIndexMap = Schedule.getInstance().getRoomHeaderIndexMap();
    private List<Room> rooms = Schedule.getInstance().getRoomList();
    private Set<String> ucionice = Schedule.getInstance().getUcionice();

    @Override
    public void importFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Fajl nije pronađen: " + path);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)))  {

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
                    ucionice.add(roomValue);
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