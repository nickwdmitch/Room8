package edu.iastate.room8.Schedule.ScheduleMVP;

import java.util.Observable;
import java.util.Observer;

/**
 * Class that is used to parse the date for the ScheduleActivity
 *
 * @author Paul Degnan
 */
public class DateParser implements IDateParserInversionPattern, Observer {
    /**
     * integer that holds the day
     */
    private int day;
    /**
     * integer that holds the month
     */
    private int month;
    /**
     * integer that holds the year
     */
    private int year;

    /**
     * Constructor for DateParser
     *
     * @param i  day
     * @param i1 month
     * @param i2 year
     */
    public DateParser(int i, int i1, int i2) {
        day = i;
        month = i1 - 1;
        year = i2;
    }

    /**
     * sets the day
     *
     * @param i number of day
     */
    public void setDay(int i) {
        day = i;
    }

    /**
     * sets the month
     *
     * @param i number of month
     */
    public void setMonth(int i) {
        month = i;
    }

    /**
     * Sets the year
     *
     * @param i number of year
     */
    public void setYear(int i) {
        year = i;
    }

    /**
     * Parses the date
     *
     * @return the date that has been parsed
     */
    public String parseDate() {
        return (month + 1) + "/" + day + "/" + year;

    }

    /**
     * Parses the day
     *
     * @return the day parsed
     */
    public String parseDay() {
        return day + "";
    }

    /**
     * Parses the month
     *
     * @return the month parsed
     */
    public String parseMonth() {
        return (month + 1) + "";

    }

    /**
     * Parses the year
     *
     * @return the year parsed
     */
    public String parseYear() {
        return year + "";

    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof SchedulePresenter){
            parseDate();
        }
    }
}
