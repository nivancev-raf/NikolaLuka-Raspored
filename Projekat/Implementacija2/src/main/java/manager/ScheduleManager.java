package manager;

import model2.WeeklySchedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {

    private List<WeeklySchedule> weeklySchedules;

    public ScheduleManager() {
        this.weeklySchedules = new ArrayList<>();
    }

    public List<WeeklySchedule> getWeeklySchedules() {
        return weeklySchedules;
    }

    public void setWeeklySchedules(List<WeeklySchedule> weeklySchedules) {
        this.weeklySchedules = weeklySchedules;
    }
}




