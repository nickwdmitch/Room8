package edu.iastate.room8.Schedule.ScheduleMVP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import edu.iastate.room8.R;
import edu.iastate.room8.Schedule.DayActivity;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity Schedule. There is a calender and you can select a day on the calender
 * and see what events exist between you and your roommates and add more by clicking the button for adding new events.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class ScheduleActivity extends AppCompatActivity implements IScheduleActivity {
    /**
     * String holding the date selected
     */
    private String date;
    /**
     * String holding the day selected
     */
    private String day;
    /**
     * String holding the month selected
     */
    private String month;
    /**
     * String holding the year selected
     */
    private String year;
    /**
     * DateParser that will parse the date based on the onClickListener of the calender
     */
    private IDateParserInversionPattern dateParser;
    /**
     * Boolean with whether or not the user has selected anything yet
     */
    boolean clicked;
    /**
     * Session Manager
     */
    ISessionManagerInversionPattern sessionManager;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        //initialize variable
        sessionManager = new SessionManager(this);

        final Button goToScheduleDay = findViewById(R.id.goToScheduleDay);
        final CalendarView calender = findViewById(R.id.calendar);

        clicked = false;
        dateParser = new DateParser(21, 10, 2019);

        goToScheduleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScheduleDayClicked();
            }
        });

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calenderChange(i, i1, i2);
            }
        });
    }

    /**
     * Method that runs whenever the calender date changes
     */
    public void calenderChange(int i, int i1, int i2) {
        dateParser.setDay(i2); //uses date parser to parse date
        dateParser.setMonth(i1);
        dateParser.setYear(i);
        day = dateParser.parseDay();
        month = dateParser.parseMonth();
        year = dateParser.parseYear();
        date = callDateParser();
        clicked = true;
        //setChanged();
        //notifyObservers();
    }

    /**
     * Method that runs whenever goToScheduleDay is clicked
     */
    public void goToScheduleDayClicked() {
        if (!clicked) {
            Toast.makeText(ScheduleActivity.this, "Please select a date first", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(ScheduleActivity.this, DayActivity.class);
            i.putExtra("EXTRA_INFORMATION", date); //sets extra info for next class
            i.putExtra("Day", day);
            i.putExtra("Month", month);
            i.putExtra("Year", year);
            startActivity(i);
        }
    }

    /**
     * Calls the date parser to parse the date that is to be used for the schedule.
     *
     * @return returns the parsed date
     */
    public String callDateParser() {
        SchedulePresenter presenter = new SchedulePresenter(dateParser); //calls the date parser to present data
        return presenter.callDataParser();
    }



}
