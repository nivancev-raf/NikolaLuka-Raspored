package manager;

import api.ITermManager;
import model.Term;

import java.util.List;
import java.util.Map;

public class TermManager implements ITermManager {

    private ScheduleManager scheduleManager;

    public TermManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }


    @Override
    public List<Term> getAllTerms() {
        return null;
    }

    @Override
    public void updateTerm(Term termId, Term updatedTerm) {

    }

    @Override
    public Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs,String period) {
        return null;
    }

    @Override
    public void deleteTerm(String teacherName, String roomName, String time, String day) {

    }

    @Override
    public void addAdditionalProperty(String key, Object value) {

    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return null;
    }
}
