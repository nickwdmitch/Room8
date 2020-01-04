package edu.iastate.room8.JoinRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.iastate.room8.Home.HomeActivity;
import edu.iastate.room8.R;
import edu.iastate.room8.app.AppController;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity NewUserRoomJoin. You can create a new room which you can access in this.
 * You can join a new room too with the room ID. You can access any of the rooms you have already joined too.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class NewUserRoomJoin extends AppCompatActivity {
    /**
     * User input for ID of room to join
     */
    private EditText joinRoomEditText;
    /**
     * User input for the name of the new room they want to create
     */
    private EditText newRoomCreateEditText;
    /**
     * Tag with the activity currently in
     */
    private String TAG = NewUserRoomJoin.class.getSimpleName();
    /**
     * Session Manager
     */
    private ISessionManagerInversionPattern sessionManager;
    /**
     * These tags will be used to cancel the requests
     */
    private String tag_json_obj = "jobj_req";

    /**
     * ArrayList with items for List View
     */
    private ArrayList<String> items;
    /**
     * Adapter for List View
     */
    private ArrayAdapter<String> adapter;
    /**
     * ids of the rooms parsed
     */
    private ArrayList<String> ids;
    /**
     * Holds permissions for users
     */
    private ArrayList<String> permissions;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_room_join);
        //initialize variables on creation
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Button newRoomCreate = findViewById(R.id.NewRoomCreate);
        newRoomCreateEditText = findViewById(R.id.RoomNameCreate);
        Button joinRoom = findViewById(R.id.RoomJoin);
        joinRoomEditText = findViewById(R.id.roomIdEditText);
        ListView list = findViewById(R.id.RoomList);
        Button logout = findViewById(R.id.logoutButton);
        Button updateButton = findViewById(R.id.buttonUpdateRooms);

        items = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);

        ids = new ArrayList<>();

        permissions = new ArrayList<>();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });

        newRoomCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRoomCreateClicked();
            }
        });

        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequestJoin();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        list.setOnItemClickListener(messageClickedHandler);
    }

    /**
     * list onClickListener. Goes to homeActivity and sets the room of the user to the correct room.
     */
    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            sessionManager.setRoom(items.get(position)); //based on what the position of the list is clicked, will get that item and will
            sessionManager.setRoomid(ids.get(position)); //set it to the session manager to be used for the rest of the classes
            sessionManager.setPermission(permissions.get(position));
            Intent i = new Intent(NewUserRoomJoin.this, HomeActivity.class);
            startActivity(i);
        }
    };

    /**
     * Method that runs when newRoomCreate button is clicked
     */
    private void newRoomCreateClicked() {
        if (newRoomCreateEditText.getText().toString().equals("")) { //makes sure user has to have a room name
            Toast.makeText(NewUserRoomJoin.this, "Must input a room name!", Toast.LENGTH_SHORT).show();
        } else {
            postRequestCreate(); //tells backend to create room
            items.clear(); //the clears here are so it can be updated by the update button and not be shown twice
            ids.clear();
            permissions.clear();
        }
    }

    /**
     * Used to parse JSON Objects in NewUserRoomJoin
     * Will get the rooms the user has joined and display them in a list
     * Receives Header: Rooms. Keys: Title, ID
     */
    private void jsonParse() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/getrooms";
        url = url + "/" + sessionManager.getID();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("Rooms");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject List = jsonArray.getJSONObject(i);
                                items.add(List.getString("Title"));
                                ids.add(List.getString("Id"));
                                String role = List.getString("Role");
                                if (role.equals("OWNER")) { //takes what the backend has permissions called and converts them to what we have them called
                                    permissions.add("Owner");
                                } else if (role.equals("ROOMMATE")) {
                                    permissions.add("Editor");
                                } else {
                                    permissions.add("Viewer");
                                }
                                sessionManager.addRoom(List.getString("Title"), List.getString("Id")); //adds rooms to session manager
                            }
                            adapter.notifyDataSetChanged(); //notifies adapter to update list view

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
        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * post that creates a new room in the database.
     * Sends Keys: Title
     */
    private void postRequestCreate() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/room";
        url = url + "/" + sessionManager.getID();
        Map<String, String> params = new HashMap<>();
        params.put("Title", newRoomCreateEditText.getText().toString()); //this is sent to backend, whatever user input is

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, //this is the main line in post requests, posts to backend
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
            public Map<String, String> getHeaders() { //not used but needed
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() { //not used but needed
                Map<String, String> params = new HashMap<>();
                params.put("User", getIntent().getStringExtra("USER_ID"));
                params.put("CreateRoom", "Yes");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * post that lets the user join a new room
     * Sends Keys: Title, RoomId
     */
    private void postRequestJoin() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/room/join";
        url = url + "/" + sessionManager.getID();

        Map<String, String> params = new HashMap<>();
        params.put("RoomId", joinRoomEditText.getText().toString()); //sends room id to backend

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, //posts to backend
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            if (success.equals("Success")) { //if it was a success then clear everything so it can be updated
                                items.clear();
                                ids.clear();
                                permissions.clear();
                            } else {
                                Toast.makeText(NewUserRoomJoin.this, "Room does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //used for errors on json object
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() { //used for headers for json object
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() { //used for parameters for json object
                Map<String, String> params = new HashMap<>();
                params.put("User", getIntent().getStringExtra("USER_ID"));
                params.put("CreateRoom", "Yes");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
