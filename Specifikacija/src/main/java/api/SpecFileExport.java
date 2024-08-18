package api;

import java.io.FileNotFoundException;

/**
 * Apstraktna klasa koja definise metode za izvoz specifikacija fajlova u razlicitim formatima.
 */
public abstract class SpecFileExport {

    /**
     * Izvozi podatke u TXT formatu na specificiranu putanju.
     *
     * @param path - putanja do lokacije gde ce se sacuvati TXT fajl
     * @throws FileNotFoundException ako nije moguce pronaci ili kreirati fajl na specificiranoj putanji
     */
    public abstract void exportFileTXT(String path) throws FileNotFoundException;

    /**
     * Izvozi podatke u CSV formatu na specificiranu putanju.
     *
     * @param path putanja do lokacije gde ce se sacuvati CSV fajl
     * @throws FileNotFoundException ako nije moguce pronaci ili kreirati fajl na specificiranoj putanji
     */
    public abstract void exportFileCSV(String path) throws FileNotFoundException;

    /**
     * Izvozi podatke u JSON formatu na specificiranu putanju.
     *
     * @param path putanja do lokacije gde ce se sacuvati JSON fajl
     * @throws FileNotFoundException ako nije moguce pronaci ili kreirati fajl na specificiranoj putanji
     */
    public abstract void exportFileJSON(String path) throws FileNotFoundException;

}
