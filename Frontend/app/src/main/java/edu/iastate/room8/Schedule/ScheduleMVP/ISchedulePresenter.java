package edu.iastate.room8.Schedule.ScheduleMVP;

/**
 * Interface for the schedule presenter
 */
public interface ISchedulePresenter {
    /**
     * Interface for schedule presenter must make it call date parse
     *
     * @return String returned from callDateParser should be the parsed date
     */
    String callDataParser();
}
