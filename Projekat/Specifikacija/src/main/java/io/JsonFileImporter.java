package io;

import api.FileImportExport;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class JsonFileImporter extends FileImportExport {

    @Override
    public void importFile(String path) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        // Register custom deserializers for Room, Period, and Time if necessary
        builder.registerTypeAdapter(Term.class, new JsonDeserializer<Term>() {
            @Override
            public Term deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                JsonObject jsonObject = json.getAsJsonObject();

                Day day = new Day(jsonObject.get("Day").getAsString());
                // Create a Room object from the string value
                Room room = new Room(jsonObject.get("Room").getAsString());
                // Create Period objects from the string values
                LocalDate startDate = LocalDate.parse(jsonObject.get("StartDate").getAsString(), dateFormatter);
                Schedule.getInstance().setPeriodPocetak(jsonObject.get("StartDate").getAsString());
                LocalDate endDate = LocalDate.parse(jsonObject.get("EndDate").getAsString(), dateFormatter);
                Schedule.getInstance().setPeriodKraj(jsonObject.get("EndDate").getAsString());
                Period period = new Period(startDate, endDate);
                // Create Time objects from the string values
                LocalTime startTime = LocalTime.parse(jsonObject.get("StartTime").getAsString());
                LocalTime endTime = LocalTime.parse(jsonObject.get("EndTime").getAsString());
                Time time = new Time(startTime, endTime);

                Term term = new Term(room, day, time, period);


                for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                    String key = entrySet.getKey();
                    // Check if the key is not one of the fixed properties
                    if (!key.equals("Room") && !key.equals("StartDate") && !key.equals("EndDate") && !key.equals("StartTime") && !key.equals("EndTime") && !key.equals("Day")) {
                        // Add the optional detail to the hashmap
                        term.getAdditionalProperties().put(key, entrySet.getValue().getAsString());
                    }
                }

                return term;
            }
        });

        Gson gson = builder.create();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found at path: " + path);
        }
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));


//        Type scheduleEntryType = new TypeToken<List<ScheduleEntry>>(){}.getType();
//        List<ScheduleEntry> scheduleEntries = gson.fromJson(reader, scheduleEntryType);
        Type termType = new TypeToken<List<Term>>(){}.getType();
        List<Term> termEntries = gson.fromJson(reader, termType);
        Schedule.getInstance().getTerms().addAll(termEntries);


//        for (Term entry : termEntries) {
//            System.out.println("Room: " + entry.getRoom()); // Assumes Room has a proper toString method
//            System.out.println("Period: " + entry.getPeriod()); // Assumes Period has a proper toString method
//            System.out.println("Time: " + entry.getTime()); // Assumes Time has a proper toString method
//            System.out.println("Day: " + entry.getDay()); // Assumes Day has a proper toString method
//
//
//
//            // Print additional details
//            for (Map.Entry<String, String> detail : entry.getAdditionalProperties().entrySet()) {
//                System.out.println(detail.getKey() + ": " + detail.getValue());
//            }
//
//            // Add a separator between entries for readability
//            System.out.println("--------------------------------------------------");
//        }
//        System.out.println(Schedule.getInstance().getTerms());
    }
}
