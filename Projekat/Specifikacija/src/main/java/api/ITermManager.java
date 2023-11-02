package api;

import model.Term;

import java.util.List;

public interface ITermManager {

    List<Term> getAllTerms(); // vraca listu svih termina
    void updateTerm(Term termId, Term updatedTerm); // azurira info o terminu
    void deleteTerm(Term termId);
    void addTerm();
    void addAdditionalProperty(String key, Object value);


}
