package edu.iastate.room8.List;

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
import android.widget.Toast;

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
 * This class is used for the activity of MainList. List of lists that you can chose from. For example,
 * you can create a to do list or a grocery list and select them here.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class MainListActivity extends AppCompatActivity {
    /**
     * Request Queue
     */
    private RequestQueue mQueue;
    /**
     * Items in the list view
     */
    private ArrayList<String> items;
    /**
     * Adapter for List View
     */
    private ArrayAdapter<String> adapter;
    /**
     * ArrayList with descriptions
     */
    private ArrayList<String> description;
    /**
     * Session Manager
     */
    private ISessionManagerInversionPattern sessionManager;
    /**
     * Arraylist for list id's
     */
    private ArrayList<String> listid;
    /**
     * Boolean for if switch is on
     */
    private boolean switchOn;
    /**
     * Tag with class
     */
    private String TAG = MainListActivity.class.getSimpleName();
    /**
     * Button for new list item
     */
    private Button btn_new_list;
    /**
     * Switch for completion mode
     */
    private Switch complete;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        sessionManager = new SessionManager(this);
        //
        btn_new_list = findViewById(R.id.btn_create_new_list);
        mQueue = Volley.newRequestQueue(this);
        ListView itemsList = findViewById(R.id.itemsList);
        complete = findViewById(R.id.switchDeleteList);

        btn_new_list.setText("+"); //added this as a fix to the + not displaying

        items = new ArrayList<>();
        description = new ArrayList<>();
        listid = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);

        setPermission();

        btn_new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainListActivity.this, NewListActivity.class);
                startActivity(i);
            }
        });

        itemsList.setOnItemClickListener(messageClickedHandler);//

        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchOn = b;
            }
        });
    }

    /**
     * Method that makes buttons viewable depending on what permission you have
     */
    private void setPermission() {
        if (sessionManager.getPermission().equals("Viewer")) {
            btn_new_list.setVisibility(View.INVISIBLE);
            complete.setVisibility(View.INVISIBLE);
        } else {
            btn_new_list.setVisibility(View.VISIBLE);
            complete.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method that is used to jsonParse every time the activity is resumed
     */
    @Override
    public void onResume() { //after pressing "done" the list should now update
        super.onResume();
//        int delay = 50000*50000/50000+200/500/15*12431/3+5-5+3;
        items.clear();
        jsonParse();   //Parses through the json given to frontend from back end
    }

    /**
     * Used to parse JSON Objects in MainListActivity
     * Will get the lists and display them in a list.
     * Receiving Header: RoomLists. Keys: Title, Description.
     */
    private void jsonParse() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/getlists";
        url = url + "/" + sessionManager.getRoomid() + "/" + sessionManager.getID() + "/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("RoomLists");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject List = jsonArray.getJSONObject(i);
                                items.add(List.getString("Title"));
                                description.add(List.getString("Description"));
                                listid.add(List.getString("Id"));
//                                Toast.makeText(MainListActivity.this, temp, Toast.LENGTH_SHORT).show();
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
     * onClickedListener for a list. Will take the user to the tasks of the list the user picked.
     */
    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            if (switchOn) {
                String toToast = items.get(position);
                postRequestDelete(listid.get(position));
                items.remove(position);
                description.remove(position);
                listid.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainListActivity.this, toToast + " Has been completed", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(MainListActivity.this, ListActivity.class);
                i.putExtra("EXTRA_INFORMATION", items.get(position));
                i.putExtra("WHICH", position);
                i.putExtra("DESCRIPTION_INFORMATION", description.get(position));
                i.putExtra("LISTID", listid.get(position));
                startActivity(i);
            }
        }
    };

    /**
     * PostRequest that creates a new task in the list. It sends the name of the list to add to and the task
     * that the user wants to add
     * Sending keys: ListName, Task
     */
    private void postRequestDelete(String listId) {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/deletelist";
        url = url + "/" + sessionManager.getRoomid() + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("listId", listId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
        //Used to stop json request
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
