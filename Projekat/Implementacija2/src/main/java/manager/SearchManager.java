package manager;

import api.ISearchManager;
import model.SearchCriteria;
import model.Term;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class SearchManager implements ISearchManager {


    @Override
    public List<Term> searchTermsByCriteria(Map<String, String> criteria) {
        return null;
    }

    @Override
    public List<Term> search(Map<String, String> criteria) {
        return null;
    }

    @Override
    public String getTermValue(Term term, String header) {
        return null;
    }

    @Override
    public Map<String, List<LocalTime[]>> getFreeSlotsForTeacher(String teacherName, LocalTime workStart, LocalTime workEnd) {
        return null;
    }

    @Override
    public Map<String, List<LocalTime[]>> getOccupiedSlotsForTeacher(String teacherName) {
        return null;
    }

    @Override
    public Map<String, List<LocalTime[]>> getOccupiedSlotsForRoom(String roomName) {
        return null;
    }

    @Override
    public Map<String, List<LocalTime[]>> getFreeSlotsForRoom(String roomName, LocalTime workStart, LocalTime workEnd) {
        return null;
    }
}
