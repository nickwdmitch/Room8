package edu.iastate.room8.RegisterLogin;

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

/**
 * This class is used for the activity Register. You can register for the app here.
 * The server will check if the email or username is already in use. The password will be entered twice and has to be 8 characters at least.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class RegisterActivity extends AppCompatActivity {
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
     * Edit Text with the user input for the new registered users password check
     */
    private EditText passwordEditTextCheck;
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
     * Tag with the current activity
     */
    private String TAG = RegisterActivity.class.getSimpleName();

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initializing variables
        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.userPasswordEditText);
        passwordEditTextCheck = findViewById(R.id.userPasswordCheckEditText);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegisterClicked();
            }
        });
    }

    /**
     * Method that runs when btnRegister is clicked
     */
    private void btnRegisterClicked() {
        userEmailEditTextString = userEmailEditText.getText().toString();
        userNameEditTextString = userNameEditText.getText().toString();
        passwordEditTextString = passwordEditText.getText().toString();
        String passwordCheckTextString = passwordEditTextCheck.getText().toString();

        if (userNameEditTextString.equals("")) { //goes through all possibilities of invalid registration
            Toast.makeText(RegisterActivity.this, "Must input a username!", Toast.LENGTH_SHORT).show();
        } else if (userEmailEditTextString.equals("")) {
            Toast.makeText(RegisterActivity.this, "Must input an email!", Toast.LENGTH_SHORT).show();
        } else if (passwordEditTextString.equals("")) {
            Toast.makeText(RegisterActivity.this, "Must input a password", Toast.LENGTH_SHORT).show();
        } else if (passwordEditTextString.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password must be more than 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!passwordEditTextString.equals(passwordCheckTextString)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            postRequest(); //posts to backend if valid registration
        }
    }

    /**
     * post that creates a new user in the database
     * Sends keys: Name, Email, Password
     * Receives keys: Response which tells whether or not the user could be created
     */
    private void postRequest() {
        String url = "http://coms-309-sb-4.misc.iastate.edu:8080/register";

        Map<String, String> params = new HashMap<>();
        params.put("Name", userNameEditTextString); //name to register
        params.put("Email", userEmailEditTextString); //email to register
        params.put("Password", passwordEditTextString); //password to register

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String success = response.getString("Response");
                            if (success.equals("Success")) { //if success do this
                                finish();
                                Toast.makeText(RegisterActivity.this, "Successfully created account!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username/Email already in use.", Toast.LENGTH_SHORT).show();
                            }
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
            @Override
            public Map<String, String> getHeaders() { //header for json
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() { //parameter for json
                Map<String, String> params = new HashMap<>();
                params.put("Name", userNameEditTextString);
                params.put("Email", userEmailEditTextString);
                params.put("Password", passwordEditTextString);
                return params;
            }
        };
        //These tags will be used to cancel the requests
        String tag_json_obj = "jobj_req";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
