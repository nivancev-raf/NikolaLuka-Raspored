package api;

import java.io.FileNotFoundException;

public abstract class FileImportExport {
    public void exportFile(String path) {
        // Common export functionality, if applicable
    }

    public abstract void importFile(String path) throws FileNotFoundException;

}
