package edu.iastate.room8.Schedule.ScheduleMVP;

public interface IDateParserInversionPattern {
    /**
     * sets the day
     *
     * @param i number of day
     */
    void setDay(int i);

    /**
     * sets the month
     *
     * @param i number of month
     */
    void setMonth(int i);

    /**
     * Sets the year
     *
     * @param i number of year
     */
    void setYear(int i);

    /**
     * Parses the day
     *
     * @return the day parsed
     */
    String parseDay();

    /**
     * Parses the month
     *
     * @return the month parsed
     */
    String parseMonth();

    /**
     * Parses the year
     *
     * @return the year parsed
     */
    String parseYear();

    /**
     * Parses the date
     *
     * @return the date that has been parsed
     */
    String parseDate();
}
