package edu.iastate.room8.Schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.iastate.room8.R;
import edu.iastate.room8.app.AppController;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity NewSchedule. You can create a new event in the schedule for the day on your rooms events page.
 * The new event added will be viewable in DayActivity.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class NewScheduleActivity extends AppCompatActivity {
    /**
     * Edit Text with the user input for the start time
     */
    private EditText startTime;
    /**
     * Edit Text with the user input for the end time
     */
    private EditText endTime;
    /**
     * Edit Text with the user input for the event name
     */
    private EditText eventName;
    /**
     * Edit Text with the user input for the event description
     */
    private EditText eventDescription;
    /**
     * String that holds the start time
     */
    private String startTimeString;
    /**
     * String that holds the end time
     */
    private String endTimeString;
    /**
     * String that holds the event name
     */
    private String eventNameString;
    /**
     * String that holds the event description
     */
    private String eventDescriptionString;
    /**
     * String that golds the date
     */
    private String date;
    /**
     * Session Manager
     */
    private ISessionManagerInversionPattern sessionManager;
    /**
     * Holds if switch is on for start
     */
    private boolean switchOnStart;
    /**
     * Holds if switch is on for end
     */
    private boolean switchOnEnd;
    /**
     * Tag with the current activity
     */
    private String TAG = NewScheduleActivity.class.getSimpleName();

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        //initialize all variables
        sessionManager = new SessionManager(this);

        final Button addNewEventButton = findViewById(R.id.addNewEventButton);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        Switch switchStart = findViewById(R.id.switchAmPmStart);
        Switch switchEnd = findViewById(R.id.switchAmPmEnd);
        final Button finish = findViewById(R.id.finishFromNewSchedule);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switchStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchOnStart = b;
            }
        });

        switchEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchOnEnd = b;
            }
        });

        date = getIntent().getStringExtra("DATE"); //get stuff from last activity

        addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewEventButtonClicked();
            }
        });
    }

    /**
     * Method that runs whenever addNewEventButton is clicked
     */
    private void addNewEventButtonClicked() {
        startTimeString = startTime.getText().toString(); //when new event button is clicked is settings strings and then going to post
        endTimeString = endTime.getText().toString();
        eventNameString = eventName.getText().toString();
        eventDescriptionString = eventDescription.getText().toString();

        if(startTimeString.contains(":") && endTimeString.contains(":")){
            addAmPm();
            postRequest();
        }else{
            Toast.makeText(this, "Please input the correct format", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that adds am or pm depending on the switch state
     */
    private void addAmPm(){
        if(switchOnStart){
            startTimeString = startTimeString + "pm";
        }else{
            startTimeString = startTimeString + "am";
        }
        if(switchOnEnd){
            endTimeString = endTimeString + "pm";
        }else{
            endTimeString = endTimeString + "am";
        }
    }

    /**
     * PostRequest that creates a new event on a day specified by the user.
     * Sends Keys: EventName, StartTime, EndTime, EventDescription, date.
     */
    private void postRequest() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/addevent";
        url = url + "/" + sessionManager.getRoomid() + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("Title", eventNameString);
        params.put("Start", startTimeString); //post takes strings and sends to backend for new event
        params.put("End", endTimeString);
        params.put("Description", eventDescriptionString);
        String tempDate = date.replaceAll("/", ":");
        params.put("Date", tempDate);

        JSONObject toPost = new JSONObject(params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, toPost,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { //runs on response
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //runs on error
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() { //used for json headers
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() { //used for json parameters
                Map<String, String> params = new HashMap<>();
                params.put("EventName", eventNameString);
                params.put("StartTime", startTimeString);
                params.put("EndTime", endTimeString);
                params.put("EventDescription", eventDescriptionString);
                params.put("Date", date);
                return params;
            }
        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
