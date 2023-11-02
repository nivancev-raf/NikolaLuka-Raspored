import manager.RoomManager;
import manager.SearchManager;
import manager.TermManager;
import manager.ScheduleManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ScheduleManager scheduleManager = new ScheduleManager();
        TermManager termManager = new TermManager(scheduleManager);
        RoomManager roomManager = new RoomManager(scheduleManager);
        SearchManager searchManager = new SearchManager(scheduleManager);



    }
}