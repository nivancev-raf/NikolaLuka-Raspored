package io;

import api.FileImportExport;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFileImporter extends FileImportExport {

    @Override
    public void importFile(String path) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Term.class, new JsonDeserializer<Term>() {
            @Override
            public Term deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                JsonObject jsonObject = json.getAsJsonObject();

                Day day = new Day(jsonObject.get("Dan").getAsString());
                Room room = new Room(jsonObject.get("Ucionica").getAsString());

                LocalDate startDate = LocalDate.parse(jsonObject.get("StartDate").getAsString(), dateFormatter);
                Schedule.getInstance().setPeriodPocetak(jsonObject.get("StartDate").getAsString());
                LocalDate endDate = LocalDate.parse(jsonObject.get("EndDate").getAsString(), dateFormatter);
                Schedule.getInstance().setPeriodKraj(jsonObject.get("EndDate").getAsString());
                Period period = new Period(startDate, endDate);

                LocalTime startTime = LocalTime.parse(jsonObject.get("StartTime").getAsString());
                LocalTime endTime = LocalTime.parse(jsonObject.get("EndTime").getAsString());
                Time time = new Time(startTime, endTime);

                Term term = new Term(room, day, time, period);

                for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                    String key = entrySet.getKey();
                    if (!key.equals("Ucionica") && !key.equals("StartDate") && !key.equals("EndDate") && !key.equals("StartTime") && !key.equals("EndTime") && !key.equals("Dan")) {
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

        String jsonContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        Type termType = new TypeToken<List<Term>>(){}.getType();
        List<Term> termEntries = gson.fromJson(jsonContent, termType);

        Schedule.getInstance().getTerms().addAll(termEntries);
        extractHeadersFromJson(jsonContent);

    }

    private void extractHeadersFromJson(String jsonContent) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> jsonList = gson.fromJson(jsonContent, listType);

        if (jsonList != null && !jsonList.isEmpty()) {
            Map<String, Object> firstObject = jsonList.get(0);
            List<String> headers = new ArrayList<>(firstObject.keySet());
//            System.out.println("Headers extracted from JSON: " + headers);

            Schedule.getInstance().getHeaderIndexMap().clear();
            int index = 0;
            for (String header : headers) {
                Schedule.getInstance().getHeaderIndexMap().put(header, index++);
            }
        }
    }
}
