package api;

import model.Room;
import model.Term;
import model.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ITermManager {

    Term addTerm(String dayInput, String timeInput, String roomInput, Map<String, String> additionalInputs,String period);
    void deleteTerm(String teacherName, String roomName, String time, String day);
    Map<String, String> getAdditionalProperties();
    boolean isTermAvailable(Term newTerm, List<Term> existingTerms);
    Term findTermToModify(String teacherName, String roomName, String timeRange);
    Term makeOriginalTerm(Term termToModify, LocalDate splitDateStr);
    Term makeNewTerm(Term termToModify, LocalDate splitDateStr, Room newRoom, Time newTime);
    Time splitTime(String timeRange);
    void updateScheduleWithNewTerms(Term oldTerm, Term originalTerm, Term newTerm);
    boolean isDateWithinTermPeriod(Term term, LocalDate date);

}
