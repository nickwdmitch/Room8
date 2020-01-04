package edu.iastate.room8.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import edu.iastate.room8.JoinRoom.NewUserRoomJoin;
import edu.iastate.room8.R;
import edu.iastate.room8.app.AppController;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity of login. If you don't have a login you can press the register button.
 * Use email and password to login.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * User input for email
     */
    private EditText userEmailEditText;
    /**
     * User input for password
     */
    private EditText passwordEditText;
    /**
     * Button that will send the email and password to be checked for logging in
     */
    private Button loginbtn;
    /**
     * Integer with amount of login attempts they get
     */
    private int loginAttemps = 5;
    /**
     * Text View that shows the amount of login attempts left
     */
    private TextView loginAttempsTextView;
    /**
     * Tag with current activity name
     */
    private String TAG = LoginActivity.class.getSimpleName();
    /**
     * Session Manager
     */
    private ISessionManagerInversionPattern sessionManager;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginbtn = findViewById(R.id.loginbtn);
        Button signUpBtn = findViewById(R.id.signUpBtn);
        loginAttempsTextView = findViewById(R.id.loginAttempsTextView);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequest(); //can change to validate(userEmailEditText.getText().toString(), passwordEditText.getText().toString()); for testing
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    //KEEP FOR TESTING PURPOSES
//    /**
//     * Validation method to see if the email and password correspond to a user in the database. (Old method for testing)
//     * @param userName_Email email the user typed in
//     * @param userPassword password the user typed in
//     */
//    private void validate(String userName_Email, String userPassword){
//        String getUser = "";
//        String getEmail = "";
//        String getPassword = "";
//        String id = "whatever we want the id to be";
//        String getRoom = "not null";  //Set this to equal if user is in a room. If it is null the user isn't in a room
//
//        //This if matches the (username or email) and password with those on the database
//        if((userName_Email.equals(getUser) || userName_Email.equals(getEmail)) && (userPassword.equals(getPassword))){
//            sessionManager.createSession("TestUser", "TestEmail", "TestPassword");  //Creates a new session where the user is logged in
//
//                Intent i = new Intent(LoginActivity.this, NewUserRoomJoin.class);
//                startActivity(i);
//
//
//        }else{
//
//            loginAttemps--;
//            String toSetText = "Incorrect User Name or Password" + "\n" +
//                    "Login Attemps Left: " + loginAttemps
//                    + "\n" + userName_Email + " " + userPassword;
//            loginAttempsTextView.setText(toSetText);
//
//            if (loginAttemps == 0){
//                loginbtn.setEnabled(false);
//
//            }
//        }
//    }

    /**
     * Validation method to see if the email and password correspond to a user in the database.
     *
     * @param success   Whether or not the login attempt was successful
     * @param userID    ID of the user that logged in
     * @param userEmail email of the user that logged in
     * @param userName  username of the user that logged in
     */
    private void validate2(String success, String userID, String userEmail, String userName) {
        if (success.equals("Success")) {

            Intent i = new Intent(LoginActivity.this, NewUserRoomJoin.class);
            sessionManager.createSession(userName, userEmail, userID);  //Creates a new session where the user is logged in
            startActivity(i);

        } else {
            Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show(); //can only make 5 errors otherwise locks you out
            loginAttemps--;
            String toSetText = "Incorrect User Name or Password" + "\n" +
                    "Login Attemps Left: " + loginAttemps
                    + "\n";
            loginAttempsTextView.setText(toSetText);

            if (loginAttemps == 0) {
                loginbtn.setEnabled(false);

            }
        }
    }

    /**
     * PostRequest used to see if the email and password are in the server.
     * If the email and password correspond to a user in the database the server will
     * return a "Success" and the user will be brought the the rooms activity
     */
    private void postRequest() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/login";

        Map<String, String> params = new HashMap<>();
        params.put("Email", userEmailEditText.getText().toString()); //email to send to backend
        params.put("Password", passwordEditText.getText().toString()); //password to send to backend

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response"); //response from the login to show if logged in successful
                            String userID = response.getString("UserId");
                            String userEmail = userEmailEditText.getText().toString();
                            String userName = response.getString("Username");
                            Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            validate2(success, userID, userEmail, userName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                Map<String, String> params = new HashMap<>();
                params.put("Email", userEmailEditText.getText().toString());
                params.put("Password", passwordEditText.getText().toString());
                return params;
            }
        };
        // This tag will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public JSONObject jsonNameRequest() {
        return null;
    } //testing use
}
