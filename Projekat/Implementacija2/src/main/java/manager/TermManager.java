package manager;

import api.ITermManager;
import model.Term;

public class TermManager implements ITermManager {

    private ScheduleManager scheduleManager;

    public TermManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Override
    public void getTermById(String s) {

    }

    @Override
    public void getAllTerms() {

    }

    @Override
    public void updateTerm(String s, Term term) {

    }

    @Override
    public void deleteTerm(String s) {

    }

    @Override
    public void addTerm(Term term) {

    }
}
