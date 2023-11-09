package manager;

import api.ITermManager;
import model.Room;
import model.Schedule;
import model.Term;
import model.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TermManager implements ITermManager {

    private ScheduleManager scheduleManager;

    public TermManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }


    @Override
    public Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs, String period) {
        return null;
    }

    @Override
    public void deleteTerm(String teacherName, String roomName, String time, String day) {

    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return null;
    }

    @Override
    public boolean isTermAvailable(Term newTerm, List<Term> existingTerms) {
        return false;
    }

    @Override
    public Term findTermToModify(String teacherName, String roomName, String timeRange) {
        return null;
    }

    @Override
    public Term makeOriginalTerm(Term termToModify, LocalDate splitDateStr) {
        return null;
    }

    @Override
    public Term makeNewTerm(Term termToModify, LocalDate splitDateStr, Room newRoom, Time newTime) {
        return null;
    }

    @Override
    public Time splitTime(String timeRange) {
        return null;
    }

    @Override
    public void updateScheduleWithNewTerms(Term oldTerm, Term originalTerm, Term newTerm) {

    }

    @Override
    public boolean isDateWithinTermPeriod(Term term, LocalDate date) {
        return false;
    }
}
