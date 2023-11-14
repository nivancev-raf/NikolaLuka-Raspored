package api;

import java.io.FileNotFoundException;

public abstract class SpecFileExport {

    public abstract void exportFileTXT(String path) throws FileNotFoundException;
    public abstract void exportFileCSV(String path) throws FileNotFoundException;
    public abstract void exportFileJSON(String path) throws FileNotFoundException;


}
