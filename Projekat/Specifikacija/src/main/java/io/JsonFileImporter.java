package io;

import api.FileImportExport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileImporter extends FileImportExport {

    @Override
    public void importFile(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
//        Review data = gson.fromJson(reader, Review.class);

    }

}
