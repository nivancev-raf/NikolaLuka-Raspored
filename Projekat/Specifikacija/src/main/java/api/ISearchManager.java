package api;

import model.SearchCriteria;
import model.Term;

import java.util.List;
import java.util.Map;

public interface ISearchManager {
    List<Term> searchTermsByCriteria(Map<String, String> criteria); // vraca listu termina na osnovu kriterijuma
    void filterTermsByRoom(SearchCriteria roomName); // vraca listu termina na osnovu imena prostorije
    void filterTermsByDate(SearchCriteria date); // vraca listu termina na osnovu datuma
    void filterTermsByTime(SearchCriteria startTime, SearchCriteria endTime); // vraca listu termina na osnovu vremena

}
