package manager;

import api.ISearchManager;
import model.SearchCriteria;
import model.Term;

import java.util.List;
import java.util.Map;

public class SearchManager implements ISearchManager {

    private ScheduleManager scheduleManager;

    public SearchManager (ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Override
    public List<Term> searchTermsByCriteria(Map<String, String> criteria) {
        return null;
    }

    @Override
    public void filterTermsByRoom(SearchCriteria roomName) {

    }

    @Override
    public void filterTermsByDate(SearchCriteria date) {

    }

    @Override
    public void filterTermsByTime(SearchCriteria startTime, SearchCriteria endTime) {

    }
}
