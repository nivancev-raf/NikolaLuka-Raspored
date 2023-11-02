package manager;

import api.ISearchManager;
import model.SearchCriteria;

public class SearchManager implements ISearchManager {

    private ScheduleManager scheduleManager;

    public SearchManager (ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Override
    public void searchTermsByCriteria(SearchCriteria searchCriteria) {

    }

    @Override
    public void filterTermsByRoom(String s) {

    }

    @Override
    public void filterTermsByDate(String s) {

    }

    @Override
    public void filterTermsByTime(String s, String s1) {

    }
}
