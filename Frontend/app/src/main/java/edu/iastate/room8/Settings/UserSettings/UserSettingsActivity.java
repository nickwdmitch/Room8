package edu.iastate.room8.Settings.UserSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.iastate.room8.R;
import edu.iastate.room8.app.AppController;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * Class for users settings
 */
public class UserSettingsActivity extends AppCompatActivity {
    /**
     * Edit Text with the user input for the new registered users username
     */
    private EditText userNameEditText;
    /**
     * Edit Text with the user input for the new registered users email
     */
    private EditText userEmailEditText;
    /**
     * Edit Text with the user input for the new registered users password
     */
    private EditText passwordEditText;
    /**
     * String with the user input for the new registered users username
     */
    private String userNameEditTextString;
    /**
     * String with the user input for the new registered users email
     */
    private String userEmailEditTextString;
    /**
     * String with the user input for the new registered users password
     */
    private String passwordEditTextString;
    /**
     * Current password
     */
    private EditText currentPassword;
    /**
     * Tag with the current activity
     */
    private String TAG = UserSettingsActivity.class.getSimpleName();
    /**
     * Session Manager variable
     */
    public SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sessionManager = new SessionManager(this);
        //initializing variables
        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.userPasswordEditText);
        Button btnChangeName = findViewById(R.id.btnChangeName);
        Button btnChangeEamil = findViewById(R.id.btnChangeEmail);
        Button btnChangePass = findViewById(R.id.btnChangePass);
        Button btnlogout = findViewById(R.id.btnLogout);
        Button changeAll = findViewById(R.id.changeAll);
        currentPassword = findViewById(R.id.editTextCurrentPassword);

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameEditTextString = userNameEditText.getText().toString();
                sessionManager.setName(userEmailEditTextString);
                postRequestName();
            }
        });


        btnChangeEamil.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmailEditTextString = userEmailEditText.getText().toString();
                sessionManager.setEmail(userEmailEditText.getText().toString());
                postRequestEmail();
            }
        }));

        btnChangePass.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view){
                passwordEditTextString = passwordEditText.getText().toString();
                postRequestPass();
            }
        }));

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        changeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequestAll();
            }
        });
    }
    
    private void postRequestName() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/updateuser";
        url = url + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("Name", userNameEditTextString); //name to register
//        params.put("userId", sessionManager.getID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            Toast.makeText(UserSettingsActivity.this, success, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //on error for json
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public JSONObject jsonNameRequest() {
        return null;
    } //testing use
    
    private void postRequestEmail() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/updateuser";
        url = url + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("Email", userEmailEditTextString); //email to register
//        params.put("userId", sessionManager.getID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            Toast.makeText(UserSettingsActivity.this, success, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //on error for json
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    
    private void postRequestPass() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/updateuser";
        url = url + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        params.put("Password", passwordEditTextString); //password to register
        params.put("CurrentPassword", currentPassword.getText().toString());


//        params.put("userId", sessionManager.getID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            Toast.makeText(UserSettingsActivity.this, success, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //on error for json
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void postRequestAll() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/updateuser";
        url = url + "/" + sessionManager.getID() + "/";

        Map<String, String> params = new HashMap<>();
        if(!passwordEditText.getText().toString().equals("")){
            params.put("Password", passwordEditText.getText().toString()); //password to register
        }
        if(!currentPassword.getText().toString().equals("")){
            params.put("CurrentPassword", currentPassword.getText().toString());
        }
        if(!userEmailEditText.getText().toString().equals("")){
            params.put("Email", userEmailEditText.getText().toString()); //email to register
        }
        if(!userNameEditText.getText().toString().equals("")){
            params.put("Name", userNameEditText.getText().toString()); //name to register
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            Toast.makeText(UserSettingsActivity.this, success, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //on error for json
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
