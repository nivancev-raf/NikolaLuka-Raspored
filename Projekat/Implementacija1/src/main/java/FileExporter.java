import api.SpecFileExport;
import model.Schedule;
import model.SearchCriteria;
import model.Term;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


public  class FileExporter extends SpecFileExport {

    private SearchCriteria searchCriteria;
    private Schedule schedule;

    public FileExporter(Schedule schedule) {
        this.schedule = Schedule.getInstance();
        this.searchCriteria = new SearchCriteria(schedule);
    }

    @Override
    public void exportFile(String path){
        List<Term> terms = schedule.getTerms();

        //try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Pisanje zaglavlja u CSV fajl
            //writer.write("Dan,Period,Termin,Ucionica\n");

            // Pisanje svakog termina kao red u CSV fajlu
                for (Term term : terms) {

                DayOfWeek day = searchCriteria.reverseParseDay(term.getDay().getName());
                LocalDate date = LocalDate.parse(Schedule.getInstance().getPeriodPocetak());
                LocalDate endDate = LocalDate.parse(Schedule.getInstance().getPeriodKraj());

                while(date.isBefore(endDate) || date.isEqual(endDate)){
                    if(date.getDayOfWeek().equals(day)){
                        System.out.println("Dan: " + day + ",datum: " + date + ",termin: " + term.getTime().getStartTime().toString() + "-" +  term.getTime().getEndTime().toString()
                         + ",ucionica: " + term.getRoom().getName());
                    }
                    date = date.plusDays(1);
                }

//                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
//                        term.getDay().getName(),
//                        term.getRoom().getName(),
//                        term.getTime().getStartTime().toString(),
//                        term.getTime().getEndTime().toString(),
//                        term.getSubject(),
//                        term.getProfessor()
//                ));
            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
