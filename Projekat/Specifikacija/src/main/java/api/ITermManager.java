package api;

import model.Room;
import model.Term;
import model.Time;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Interfejs koji definise upravljanje terminima unutar rasporeda.
 */
public interface ITermManager {

    /**
     * Dodaje termin u raspored.
     *
     * @param dayInput          Dan u nedelji za termin.
     * @param timeInput         Vremenski opseg (pocetak i kraj) za termin.
     * @param roomInput         Identifikator ucionice za termin.
     * @param additionalInputs  Mapa dodatnih svojstava povezanih sa terminom.
     * @param period            Period tokom kojeg je termin vazeci.
     * @return                  Novo kreirana instanca termina, ili null ako termin nije mogao biti dodat.
     */
    Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs, String period);

    /**
     * Brise termin iz rasporeda na osnovu prosledjenih parametara.
     *
     * @param teacherName Ime nastavnika povezanog sa terminom.
     * @param roomName    Ime ucionice povezane sa terminom.
     * @param time        Vremenski opseg termina.
     * @param day         Dan u nedelji za termin.
     */
    void deleteTerm(String teacherName, String roomName, String time, String day);

    /**
     * Dobavlja mapu dodatnih svojstava povezanih sa terminom.
     *
     * @return Mapa dodatnih svojstava.
     */
    Map<String, String> getAdditionalProperties();

    /**
     * Proverava da li je novi termin dostupan za dodavanje u raspored bez konflikta.
     *
     * @param newTerm        Nova instanca termina koju treba proveriti.
     * @param existingTerms  Lista postojecih instanci termina protiv kojih se vrsi provera.
     * @return               True ako novi termin ne konfliktira sa postojecim terminima, false inace.
     */
    boolean isTermAvailable(Term newTerm, List<Term> existingTerms);

    /**
     * Pronalazi termin koji treba izmeniti na osnovu prosledjenih parametara.
     *
     * @param teacherName Ime nastavnika povezanog sa terminom.
     * @param roomName    Ime ucionice povezane sa terminom.
     * @param timeRange   Vremenski opseg termina.
     * @return            Instanca termina koja odgovara kriterijumima, ili null ako nije pronadjen.
     */
    Term findTermToModify(String teacherName, String roomName, String timeRange);

    /**
     * Kreira novu instancu termina koja predstavlja originalni termin do odredjenog datuma deljenja.
     *
     * @param termToModify Termin koji treba podeliti.
     * @param splitDateStr Datum na koji originalni termin zavrsava.
     * @return             Originalna instanca termina sa prilagodjenim krajnjim datumom.
     */
    Term makeOriginalTerm(Term termToModify, LocalDate splitDateStr);

    /**
     * Kreira novu instancu termina koja predstavlja termin pocvsi od dana nakon odredjenog datuma deljenja.
     *
     * @param termToModify Termin koji treba podeliti.
     * @param splitDateStr Datum sa kojeg novi termin pocinje.
     * @param newRoom      Ucionica za novi termin.
     * @param newTime      Vreme za novi termin.
     * @return             Nova instanca termina pocvsi od datuma deljenja.
     */
    Term makeNewTerm(Term termToModify, LocalDate splitDateStr, Room newRoom, Time newTime);

    /**
     * Deli vremenski opseg na objekat klase Time.
     *
     * @param timeRange Vremenski opseg koji treba podeliti.
     * @return          Objekat klase Time koji predstavlja pocetno i krajnje vreme.
     */
    Time splitTime(String timeRange);

    /**
     * Azurira raspored dodavanjem novih termina i uklanjanjem starog termina.
     *
     * @param oldTerm       Stari termin koji treba ukloniti.
     * @param originalTerm  Originalni termin koji treba dodati.
     * @param newTerm       Novi termin koji treba dodati.
     */
    void updateScheduleWithNewTerms(Term oldTerm, Term originalTerm, Term newTerm);

    /**
     * Proverava da li odredjeni datum pada unutar perioda termina.
     *
     * @param term Termin ciji se period proverava.
     * @param date Datum koji treba proveriti.
     * @return     True ako datum pripada periodu termina, false inace.
     */
    boolean isDateWithinTermPeriod(Term term, LocalDate date);

    /**
     * Parsira datum i dodaje ga u listu izuzetih dana ako datum pripada periodu termina.
     *
     * @param datum Datum koji treba parsirati i dodati.
     * @return      True ako je datum uspesno dodat u listu izuzetih dana, false ako je izvan perioda termina.
     * @throws DateTimeParseException ako niz datuma nije u ispravnom formatu.
     */
    boolean parseIzuzetiDani(String datum) throws DateTimeParseException;

    /**
     * Dodaje informacije o terminu iz tekstualne datoteke u raspored.
     *
     * @param room_path          Putanja do tekstualne datoteke sa informacijama o ucionici.
     * @param kapacitet          Kapacitet ucionice.
     * @param ucionica           Identifikator ucionice.
     * @param additionalProperties Mapa dodatnih svojstava za ucionicu.
     */
    void addTermTxt(String room_path, int kapacitet, String ucionica, Map<String, String> additionalProperties);
}
