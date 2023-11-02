package model2;

import model.Term;

import java.util.*;

/*
*
* */

public class WeeklySchedule {

    private List<Term> terms; // lista termina
    private Date startDate;  // pocetni datum
    private Date endDate; // krajnji datum
    private Set<Date> excludedDates; // set datuma koji su iskljuceni


    // korisnik sa konzole treba da unese pocetni datum i krajnji datum
    public WeeklySchedule(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.terms = new ArrayList<>();
        this.excludedDates = new HashSet<>();
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Date> getExcludedDates() {
        return excludedDates;
    }

    public void setExcludedDates(Set<Date> excludedDates) {
        this.excludedDates = excludedDates;
    }
}
