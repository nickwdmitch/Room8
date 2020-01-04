package edu.iastate.room8.Schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.iastate.room8.R;
import edu.iastate.room8.app.AppController;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity of the specific day chosen from the schedule.
 * Can see what is happening on the day for everyone in your room.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class DayActivity extends AppCompatActivity {
    /**
     * string for the date
     */
    private String dateString;
    /**
     * request queue
     */
    private RequestQueue mQueue;
    /**
     * ArrayList with the information for the events
     */
    private ArrayList<String> items;
    /**
     * ArrayList with event names
     */
    private ArrayList<String> eventNames;
    /**
     * adapter for list view
     */
    private ArrayAdapter<String> adapter;
    /**
     * Button that when clicked adds the scheduled item
     */
    private Button buttonAddScheduleItem;
    /**
     * Session manager
     */
    private ISessionManagerInversionPattern sessionManager;
    /**
     * Event id arraylist
     */
    private ArrayList<String> eventId;
    /**
     * Arraylist for the descriptions
     */
    private ArrayList<String> descriptions;
    /**
     * Array list that holds start and end times
     */
    private ArrayList<String> startEnd;
    /**
     * Used to see if switch is on
     */
    private boolean switchOn;
    /**
     * Tag with class
     */
    private String TAG = DayActivity.class.getSimpleName();

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        //initializes variables
        sessionManager = new SessionManager(this);

        TextView date = findViewById(R.id.date);
        buttonAddScheduleItem = findViewById(R.id.buttonAddScheduledItem);
        ListView listView = findViewById(R.id.scheduleListView);
        Switch deleteModeDay = findViewById(R.id.deleteModeDay);

        mQueue = Volley.newRequestQueue(this);

        setPermissions();

        date.setText(getIntent().getStringExtra("EXTRA_INFORMATION")); //gets extra info from last activity
        dateString = date.getText().toString();
        eventNames = new ArrayList<>();
        items = new ArrayList<>();
        eventId = new ArrayList<>();
        descriptions = new ArrayList<>();
        startEnd = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        buttonAddScheduleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAddScheduleItemClicked();
            }
        });

        deleteModeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchOn=b;
            }
        });

        listView.setOnItemClickListener(messageClickedHandler);
    }

    /**
     * Method that is used to jsonParse every time the activity is resumed
     */
    @Override
    public void onResume() { //after pressing "done" the list should now update
        super.onResume();
        items.clear();
        eventNames.clear();
        eventId.clear();
        descriptions.clear();
        jsonParse();   //Parses through the json given to frontend from back end
    }

    /**
     * Method that runs whenever buttonAddScheduleItem is clicked
     */
    private void buttonAddScheduleItemClicked() {
        Intent i = new Intent(DayActivity.this, NewScheduleActivity.class);
        i.putExtra("DATE", dateString); //date info for next class
        startActivity(i);
    }

    /**
     * Method that that sets button visibility based on permission of user
     */
    private void setPermissions() {
        if (sessionManager.getPermission().equals("Viewer")) { //sets button visibility
            buttonAddScheduleItem.setVisibility(View.INVISIBLE);
        } else {
            buttonAddScheduleItem.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Used for testing mockito like they do in the tutorial
     *
     * @return JSONObject to be used
     */
    public JSONObject jsonGetSchedule() {
        return null;
    } //testing use

    /**
     * Used to parse JSON Objects in DayActivity
     * Will get the events for the day selected by the User and display them in a list
     * Receives: Header: Schedule. Keys: StartTime. EndTime. EventName. User.
     */
    private void jsonParse() {
        String dateTemp = dateString.replaceAll("/", ":");

        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/getevent";
        url = url + "/" + sessionManager.getRoomid() + "/" + sessionManager.getID() + "/" + dateTemp + "/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Events");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject List = jsonArray.getJSONObject(i);
                                String start = List.getString("Start"); //gets all info for all events for room
                                String end = List.getString("End");
                                String eventName = List.getString("Title");
                                String user = List.getString("User");
                                items.add(user + ": " + eventName + "\t" + start + " - " + end);
                                eventId.add(List.getString("Id"));
                                descriptions.add(List.getString("Description"));
                                eventNames.add(eventName);
                                startEnd.add(start + "-" + end);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    /**
     * An onClickListener for a list. Used to look at the description of an event.
     * Will open up a new activity when any event is clicked on.
     */
    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            if(switchOn){
                String tempForDelete = eventId.get(position);
                eventNames.remove(position);
                items.remove(position);
                eventId.remove(position);
                descriptions.remove(position);
                startEnd.remove(position);
                adapter.notifyDataSetChanged();
                postRequestDelete(tempForDelete);
            }else{
                Intent i = new Intent(DayActivity.this, ScheduleDescriptionActivity.class);
                i.putExtra("EXTRA_INFORMATION", eventNames.get(position)); //sets extra info for next activity
//            i.putExtra("EVENTID", eventId.get(position));
                i.putExtra("DESCRIPTION", descriptions.get(position));
                i.putExtra("DATE", dateString);
                i.putExtra("NAME", sessionManager.getName());
                i.putExtra("STARTEND", startEnd.get(position));
                startActivity(i);
            }
        }
    };

    /**
     * PostRequest that creates a new event on a day specified by the user.
     * Sends Keys: EventName, StartTime, EndTime, EventDescription, date.
     */
    private void postRequestDelete(final String eventId) {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/deleteevent";
        url = url + "/" + sessionManager.getRoomid() + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("eventId", eventId);

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
                params.put("EventId", eventId);
                return params;
            }
        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
