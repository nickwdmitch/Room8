package edu.iastate.room8.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.iastate.room8.Bulletin.BulletinActivity;
import edu.iastate.room8.List.MainListActivity;
import edu.iastate.room8.R;
import edu.iastate.room8.Schedule.ScheduleMVP.ScheduleActivity;
import edu.iastate.room8.utils.Sessions.SessionManager;
import edu.iastate.room8.Settings.RoomSettings.RoomSettingsActivity;
import edu.iastate.room8.Settings.UserSettings.UserSettingsActivity;

/**
 * This class is used for the activity of home. Home has buttons to get to each feature or logout
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class HomeActivity extends AppCompatActivity {
    /**
     * Session Manager variable
     */
    private SessionManager sessionManager;
    /**
     * Button that takes you to the settings page
     */
    private Button buttonSettings;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        sessionManager.checkRoom();

        //everything in here being initialized
        Button tempButton = findViewById(R.id.tempButton);
        Button tempButtonBulletin = findViewById(R.id.tempButtonBulletin);
        Button tempButtonSchedule = findViewById(R.id.tempButtonSchedule);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnLeaveRoom = findViewById(R.id.btnLeaveRoom);
        TextView roomIdTextView = findViewById(R.id.RoomIdTextView);
        TextView roomNameTextView = findViewById(R.id.RoomNameTextView);
        buttonSettings = findViewById(R.id.buttonSettings);
        Button btnUserSettings = findViewById(R.id.btnUserSettings);
        String tempRoomIdTextViewToSet = "Room ID: " + sessionManager.getRoomid();
        roomIdTextView.setText(tempRoomIdTextViewToSet);
        String tempRoomNameTextViewSet = "Room Name: " + sessionManager.getRoom();
        roomNameTextView.setText(tempRoomNameTextViewSet);

        setButtonVisibility(); //sets button visibility

        //all of these go to a new activity when clicked on
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, RoomSettingsActivity.class);
                startActivity(i);
            }
        });
        btnUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, UserSettingsActivity.class);
                startActivity(i);
            }
        });
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, MainListActivity.class);
                startActivity(i);
            }
        });
        tempButtonBulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, BulletinActivity.class);
                startActivity(i);
            }
        });
        tempButtonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScheduleActivity.class);
                startActivity(i);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });
        btnLeaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.leaveRoom();
            }
        });
    }

    /**
     * Method that gets button visibility, used for testing mostly
     *
     * @return button visibility
     */
    public int getButtonVisibility() {
        return buttonSettings.getVisibility();
    }

    /**
     * Method that gets permission for the home activity, used for testing mostly
     *
     * @return permission
     */
    public String getPermissionHome() {
        return sessionManager.getPermission();
    }

    /**
     * Method that sets permission for testing
     *
     * @param permission1 permission to set
     */
    public void setPermissionForTesting(String permission1) {
        sessionManager.setPermission(permission1); //sets permissions but used for testing
    }

    /**
     * Method that sets the button visibility
     */
    private void setButtonVisibility() {
        if (sessionManager.getPermission() == null) {
            sessionManager.logout();
        } else {
            if (getPermissionHome().equals("Owner")) { //if the user is an owner they will be able to see the room settings
                buttonSettings.setVisibility(View.VISIBLE);
            } else {
                buttonSettings.setVisibility(View.INVISIBLE);
            }
        }
    }

}
