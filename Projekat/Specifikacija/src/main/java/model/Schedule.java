package model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule {

    private static Schedule instance;
    private Map<String, Day> days = new HashMap<>();
    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Time> times = new HashMap<>();
    private List<Term> terms = new ArrayList<>();
    private Map<Term, Map<String, String>> additionalData = new HashMap<>();
    private Map<String, Integer> headerIndexMap;
    Schedule() {
        initialiseSchedule();
    }

    public static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }
    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        time = time.replaceAll("\"", "");
        if (!time.contains(":")) {
            time += ":00";
        }
        return LocalTime.parse(time, formatter);
    }

    private void initialiseSchedule() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/raspored.csv"), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                line = line.replaceAll("[^\\S\\r\\n]+", " ");
                String[] headers = line.split(",");
                headerIndexMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    headerIndexMap.put(headers[i].replace("\"", "").trim(), i);
                }

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String dayValue = values[headerIndexMap.get("Dan")].replaceAll("^\"|\"$", "").trim();
                    String roomValue = values[headerIndexMap.get("Učionica")].replaceAll("^\"|\"$", "").trim();
                    String timeValue = values[headerIndexMap.get("Termin")].replaceAll("^\\s+|\\s+$", "").trim();

                    String[] timeParts = timeValue.split("-");
                    LocalTime startTime = parseTime(timeParts[0].trim());
                    LocalTime endTime = parseTime(timeParts[1].trim());

                    Day day = days.computeIfAbsent(dayValue, Day::new);
                    Room room = rooms.computeIfAbsent(roomValue, Room::new);
                    Time time = new Time(startTime, endTime);
                    Term term = new Term(room, day, time);

                    Map<String, Object> additionalProperties = new HashMap<>();
                    for (Map.Entry<String, Integer> entry : headerIndexMap.entrySet()) {
                        if (!entry.getKey().equals("Dan") && !entry.getKey().equals("Učionica") && !entry.getKey().equals("Termin")) {
                            String additionalValue = values[entry.getValue()].replace("\"", "").trim();
                            additionalProperties.put(entry.getKey(), additionalValue);
                        }
                    }
                    term.setAdditionalProperties(additionalProperties);

                    terms.add(term);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void printSchedule() {
        for (Term term : terms) {
            System.out.println("Day: " + term.getDay().getName());
            System.out.println("Room: " + term.getRoom().getName());
            System.out.println("Start Time: " + term.getTime().getStartTime());
            System.out.println("End Time: " + term.getTime().getEndTime());
            System.out.println("Additional Data: " + term.getAdditionalProperties());
            System.out.println("-----");
        }
    }

    public List<Term> getTerms() {
        return terms;
    }

    public Map<String, Integer> getHeaderIndexMap() {
        return headerIndexMap;
    }

    public Map<Term, Map<String, String>> getAdditionalData() {
        return additionalData;
    }
}