package api;

import java.io.FileNotFoundException;

/**
 * Apstraktna klasa koja definise import fajlova.
 */
public abstract class FileImportExport {

    /**
     * Uvozi podatke iz fajla koji se nalazi na odredjenoj putanji.
     *
     * @param path putanja do fajla koji treba uvesti
     * @throws FileNotFoundException ako fajl nije pronađen na specificiranoj putanji
     */
    public abstract void importFile(String path) throws FileNotFoundException;


}
