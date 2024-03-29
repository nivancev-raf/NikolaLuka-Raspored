package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Period {

    private LocalDate startPeriod;
    private LocalDate endPeriod;




    public Period(LocalDate startPeriod, LocalDate endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(LocalDate startPeriod) {
        this.startPeriod = startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
    }

    public boolean overlaps(Period other) {
        return !(
                this.endPeriod.isBefore(other.startPeriod) ||
                        this.startPeriod.isAfter(other.endPeriod)
        );
    }

    @Override
    public String toString() {
        return startPeriod + ":" + endPeriod;
    }
}