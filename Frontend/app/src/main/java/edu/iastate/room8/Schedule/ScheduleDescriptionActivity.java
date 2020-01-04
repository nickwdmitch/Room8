package edu.iastate.room8.Schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import edu.iastate.room8.R;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity of ScheduleDescription. You will be able to see the description of the created event.
 * This includes the time frame, description, event name, and who created the event.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class ScheduleDescriptionActivity extends AppCompatActivity {
    /**
     * Text View with the event name
     */
    private TextView eventName;
    /**
     * Text View with the person who created the event
     */
    private TextView person;
    /**
     * Text View with the description of the event
     */
    private TextView description;
    /**
     * Text View with the time frame of the event
     */
    private TextView startEnd;
    /**
     * Date to show
     */
    private TextView date;
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
        setContentView(R.layout.activity_schedule_description);
        //initialized variables
        sessionManager = new SessionManager(this);
        eventName = findViewById(R.id.eventNameDescription);
        person = findViewById(R.id.personTextView);
        description = findViewById(R.id.descriptionTextView);
        startEnd = findViewById(R.id.startEndTextView);
        date = findViewById(R.id.dateDescriptionActivity);

        setDescription();
    }


    private void setDescription(){
        eventName.setText(getIntent().getStringExtra("EXTRA_INFORMATION"));
        person.setText(getIntent().getStringExtra("NAME"));
        description.setText(getIntent().getStringExtra("DESCRIPTION"));
        startEnd.setText(getIntent().getStringExtra("STARTEND"));
        date.setText(getIntent().getStringExtra("DATE"));

    }
}
