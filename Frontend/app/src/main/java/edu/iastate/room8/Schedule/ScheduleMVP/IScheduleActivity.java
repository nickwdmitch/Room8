package edu.iastate.room8.Schedule.ScheduleMVP;

/**
 * Interface for schedule activity
 */
public interface IScheduleActivity {

    /**
     * Changes the calender's day, month and year
     *
     * @param i  Year
     * @param i1 Month
     * @param i2 Day
     */
    void calenderChange(int i, int i1, int i2);

    /**
     * Goes to the schedule for that day for the specific room
     */
    void goToScheduleDayClicked();
}
