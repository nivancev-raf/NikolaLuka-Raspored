package api;

import model.Term;

import java.util.List;
import java.util.Map;

public interface ITermManager {

    List<Term> getAllTerms(); // vraca listu svih termina
    void updateTerm(Term termId, Term updatedTerm); // azurira info o terminu
    void deleteTerm(Term termId);

    Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs);
    void addAdditionalProperty(String key, Object value);
    Map<String, String> getAdditionalProperties();


}
