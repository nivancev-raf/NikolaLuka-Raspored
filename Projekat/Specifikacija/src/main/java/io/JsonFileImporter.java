package io;

import api.FileImportExport;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.Period;
import model.Room;
import model.ScheduleEntry;
import model.Time;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
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
        builder.registerTypeAdapter(ScheduleEntry.class, new JsonDeserializer<ScheduleEntry>() {
            @Override
            public ScheduleEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                // Create a Room object from the string value
                Room room = new Room(jsonObject.get("Room").getAsString());
                // Create Period objects from the string values
                LocalDate startDate = LocalDate.parse(jsonObject.get("StartDate").getAsString());
                LocalDate endDate = LocalDate.parse(jsonObject.get("EndDate").getAsString());
                Period period = new Period(startDate, endDate);
                // Create Time objects from the string values
                LocalTime startTime = LocalTime.parse(jsonObject.get("StartTime").getAsString());
                LocalTime endTime = LocalTime.parse(jsonObject.get("EndTime").getAsString());
                Time time = new Time(startTime, endTime);

                ScheduleEntry entry = new ScheduleEntry(room, period, time);

                for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                    String key = entrySet.getKey();
                    // Check if the key is not one of the fixed properties
                    if (!key.equals("Room") && !key.equals("StartDate") && !key.equals("EndDate") && !key.equals("StartTime") && !key.equals("EndTime")) {
                        // Add the optional detail to the hashmap
                        entry.addDetail(key, entrySet.getValue().getAsString());
                    }
                }

                return entry;
            }
        });

        Gson gson = builder.create();
        JsonReader reader = new JsonReader(new FileReader(path));
        Type scheduleEntryType = new TypeToken<List<ScheduleEntry>>(){}.getType();
        List<ScheduleEntry> scheduleEntries = gson.fromJson(reader, scheduleEntryType);

        // Process the scheduleEntries as needed

        for (ScheduleEntry entry : scheduleEntries) {
            System.out.println("Room: " + entry.getRoom()); // Assumes Room has a proper toString method
            System.out.println("Period: " + entry.getPeriod()); // Assumes Period has a proper toString method
            System.out.println("Time: " + entry.getTime()); // Assumes Time has a proper toString method
            // Print additional details
            for (Map.Entry<String, String> detail : entry.getAdditionalDetails().entrySet()) {
                System.out.println(detail.getKey() + ": " + detail.getValue());
            }

            // Add a separator between entries for readability
            System.out.println("--------------------------------------------------");
        }
    }
}
