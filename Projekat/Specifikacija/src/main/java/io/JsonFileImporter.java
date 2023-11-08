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

        Type termType = new TypeToken<List<Term>>(){}.getType();
        List<Term> termEntries = gson.fromJson(reader, termType);
//        System.out.println("Term entries: " + termEntries);
//        if (!termEntries.isEmpty()) {
//            // Pretpostavimo da svi Term objekti imaju iste ključeve
//            JsonObject exampleObject = (JsonObject) gson.toJsonTree(termEntries.get(0));
//            int index = 0;
//            for (Map.Entry<String, JsonElement> entry : exampleObject.entrySet()) {
//                System.out.println("Header: " + entry.getKey());
//                Schedule.getInstance().getHeaderIndexMap().put(entry.getKey(), index++);
//            }
//        }
//        System.out.println("Header index map: " + Schedule.getInstance().getHeaderIndexMap());

        Schedule.getInstance().getTerms().addAll(termEntries);

    }
}
