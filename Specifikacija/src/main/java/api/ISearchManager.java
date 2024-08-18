package api;

import model.SearchCriteria;
import model.Term;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Interfejs koji upravlja pretragom termina.
 */
public interface ISearchManager {

    /**
     * Pretrazuje termine na osnovu zadatih kriterijuma.
     *
     * @param criteria Mapa kriterijuma za pretragu.
     * @return Lista termina koji odgovaraju zadatim kriterijumima.
     */
    List<Term> searchTermsByCriteria(Map<String, String> criteria);

    /**
     * Pretrazuje termine koristeci zadatu mapu kriterijuma.
     *
     * @param criteria Mapa kriterijuma za pretragu.
     * @return Lista termina koji odgovaraju zadatim kriterijumima.
     */
    List<Term> search(Map<String, String> criteria);

    /**
     * Vraca vrednost za odredjeni termin na osnovu zadatog zaglavlja.
     *
     * @param term   Termin za koji se trazi vrednost.
     * @param header Zaglavlje na osnovu kojeg se trazi vrednost.
     * @return Vrednost termina za zadato zaglavlje.
     */
    String getTermValue(Term term, String header);

    /**
     * Vraca mapu slobodnih slotova za nastavnika.
     *
     * @param teacherName Ime nastavnika.
     * @param workStart   Pocetak radnog vremena.
     * @param workEnd     Kraj radnog vremena.
     * @return Mapa slobodnih slotova po danima.
     */
    Map<String, List<LocalTime[]>> getFreeSlotsForTeacher(String teacherName, LocalTime workStart, LocalTime workEnd);

    /**
     * Vraca mapu zauzetih slotova za nastavnika.
     *
     * @param teacherName Ime nastavnika.
     * @return Mapa zauzetih slotova po danima.
     */
    Map<String, List<LocalTime[]>> getOccupiedSlotsForTeacher(String teacherName);

    /**
     * Vraca mapu zauzetih slotova za ucionici.
     *
     * @param roomName Ime ucionice.
     * @return Mapa zauzetih slotova po danima.
     */
    Map<String, List<LocalTime[]>> getOccupiedSlotsForRoom(String roomName);

    /**
     * Vraca mapu slobodnih slotova za ucionici.
     *
     * @param roomName  Ime ucionice.
     * @param workStart Pocetak radnog vremena.
     * @param workEnd   Kraj radnog vremena.
     * @return Mapa slobodnih slotova po danima.
     */
    Map<String, List<LocalTime[]>> getFreeSlotsForRoom(String roomName, LocalTime workStart, LocalTime workEnd);
}
