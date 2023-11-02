
import model.Schedule;
import model.SearchCriteria;
import model.Term;

public class Main {
    public static void main(String[] args) {
        Schedule schedule = Schedule.getInstance();
        System.setProperty("file.encoding", "UTF-8");
        SearchCriteria searchCriteria = new SearchCriteria(schedule);
        Term term = new Term(schedule);
        term.addTerm();
        //searchCriteria.searchTermsByCriteria();
    }
}