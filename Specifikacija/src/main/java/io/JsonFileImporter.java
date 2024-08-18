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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFileImporter extends FileImportExport {
// JSON MANDATORY: Ucionica, PocetniDatum, KrajnjiDatum, PocetnoVreme, KrajnjeVreme, Dan


    List<LocalDate> krajnjiDatumi = Schedule.getInstance().getKrajnji();
    List<LocalDate> pocetniDatumi = new ArrayList<>();
    List<LocalDate> poceo = Schedule.getInstance().getPocetni();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public void importFile(String path) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Term.class, new JsonDeserializer<Term>() {
            @Override
            public Term deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                // mandatory
                Day day = new Day(jsonObject.get("Dan").getAsString());
                Room room = new Room(jsonObject.get("Ucionica").getAsString());

                if (!Schedule.getInstance().getRoomList().contains(jsonObject.get("Ucionica").getAsString())){
                    Schedule.getInstance().getRoomList().add(room);
                }

                String periodStr = jsonObject.get("Period").getAsString();
                String[] dateParts = periodStr.split("-");
                LocalDate startDate = LocalDate.parse(dateParts[0].trim(), dateFormatter);
                LocalDate endDate = LocalDate.parse(dateParts[1].trim(), dateFormatter);
                Period period = new Period(startDate, endDate);


                String timeStr = jsonObject.get("Termin").getAsString();
                String[] timeParts = timeStr.split("-");
                LocalTime startTime = LocalTime.parse(timeParts[0].trim());
                LocalTime endTime = LocalTime.parse(timeParts[1].trim());

                pocetniDatumi.add(startDate);
                krajnjiDatumi.add(endDate);
                Time time = new Time(startTime, endTime);

                Term term = new Term(room, day, time, period);

                for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                    String key = entrySet.getKey();
                    if (!key.equals("Ucionica") && !key.equals("Termin") && !key.equals("Period") && !key.equals("Dan")) {
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

            Schedule.getInstance().getHeaderIndexMap().clear();
            int index = 0;
            for (String header : headers) {
                Schedule.getInstance().getHeaderIndexMap().put(header, index++);
            }
        }
    }
}
