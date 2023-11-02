package model;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Time {

    private LocalTime startTime;
    private LocalTime endTime;

    private Map<String, Object> additionalProperties;
    private List<String> termList;

    public Time(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.additionalProperties = new HashMap<>();
        this.termList = new ArrayList<>();
    }

    public String getFullTime(String startTime, String endTime){
        return startTime + "-" + endTime;
    }
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Object getAdditionalProperty(String key) {
        return additionalProperties.get(key);
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }


    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }

}
