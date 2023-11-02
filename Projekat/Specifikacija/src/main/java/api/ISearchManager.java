package api;

import model.SearchCriteria;

public interface ISearchManager {
    void searchTermsByCriteria(); // vraca listu termina na osnovu kriterijuma
    void filterTermsByRoom(SearchCriteria roomName); // vraca listu termina na osnovu imena prostorije
    void filterTermsByDate(SearchCriteria date); // vraca listu termina na osnovu datuma
    void filterTermsByTime(SearchCriteria startTime, SearchCriteria endTime); // vraca listu termina na osnovu vremena

}
