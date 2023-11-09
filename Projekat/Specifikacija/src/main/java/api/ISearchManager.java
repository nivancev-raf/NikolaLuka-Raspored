package api;

import model.SearchCriteria;
import model.Term;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ISearchManager {
    List<Term> searchTermsByCriteria(Map<String, String> criteria); // vraca listu termina na osnovu kriterijuma
    List<Term> search(Map<String, String> criteria);
    String getTermValue(Term term, String header);
    Map<String, List<LocalTime[]>> getFreeSlotsForTeacher(String teacherName, LocalTime workStart, LocalTime workEnd);
    Map<String, List<LocalTime[]>> getOccupiedSlotsForTeacher(String teacherName);
    Map<String, List<LocalTime[]>> getOccupiedSlotsForRoom(String roomName);
    Map<String, List<LocalTime[]>> getFreeSlotsForRoom(String roomName, LocalTime workStart, LocalTime workEnd);

}
