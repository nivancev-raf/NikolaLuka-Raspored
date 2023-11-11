package api;

import java.io.FileNotFoundException;

public abstract class FileImportExport {
    public abstract void importFile(String path) throws FileNotFoundException;

}
