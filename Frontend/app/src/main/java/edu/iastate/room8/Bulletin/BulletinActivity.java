package edu.iastate.room8.Bulletin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

import edu.iastate.room8.R;
import edu.iastate.room8.utils.Sessions.SessionManager;

/**
 * This class is used for the activity of the bulletin feature. Send important messages to your roommates.
 *
 * @author Paul Degnan
 * @author Jake Vaughn
 */
public class BulletinActivity extends AppCompatActivity {
    /**
     * text view that shows the room's bulletin
     */
    private TextView textView;
    /**
     * text the user inputs to be added to bulletin
     */
    private EditText toAddText;
    //KEEP THIS IN CASE NEED TO USE ARRAY METHODS
//    /**
//     * ArrayList that holds all of bulletin entries
//     */
//    private ArrayList<String> arr;
    //KEEP IN CASE WE NEED
//    /**
//     * mWebSocketClient used for connecting websocket to server.
//     */
//    private WebSocketClient mWebSocketClient;
    /**
     * Button that when clicked will send message
     */
    private Button toAddButton;
    /**
     * session manager used for settings and information for the specific user
     */
    private SessionManager sessionManager;
    /**
     * Another web socket client
     */
    private WebSocketClient cc;

    /**
     * Method that runs on creation
     *
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);

        sessionManager = new SessionManager(this); //session manager used to save this sessions information

        textView = findViewById(R.id.textView);
        toAddButton = findViewById(R.id.buttonForAdd);
        toAddText = findViewById(R.id.messageToAdd);
        //arr = new ArrayList<>();      KEEP IN CASE WE NEED

        setVisibility(); //sets visibility of the buttons

        //when the add button is clicked it will call the to add clicked method
        toAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddClicked();
            }
        });

        webSocketWithBackend();  //does the websocket stuff
    }

    /**
     * Method that sets the visibility of buttons depending on the users permissions in this specific room
     */
    private void setVisibility() {
        if (sessionManager.getPermission().equals("Viewer")) { //session manager has permission of the user in this specific room
            toAddButton.setVisibility(View.INVISIBLE); //sets stuff to invisible for viewers
            toAddText.setVisibility(View.INVISIBLE);
        } else {
            toAddButton.setVisibility(View.VISIBLE); //sets stuff to visible for non-viewers
            toAddText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method that runs when toAdd button is clicked
     */
    private void toAddClicked() {
        String stringToAddText = toAddText.getText().toString();
        if (stringToAddText.equals("")) { //needs this here so users can't add nothing to the database
            Toast.makeText(BulletinActivity.this, "Must input a message to display on the bulletin board", Toast.LENGTH_LONG).show();
        } else {
            try {
                cc.send(toAddText.getText().toString()); //sends message to websocket
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", "Exception in Web Sockets");
            }

        }
        toAddText.setText(""); //sets input of viewer to nothing so they can type a new message
    }


    //KEEP THIS, IMPORTANT
//    /**
//     * Method that will make sure the messages wont go off the screen.
//     * Basically appends here instead of in the parse method
//     */
//    private void appendTextView(){
//        textView.setText("");
//        int numMessagesToShow = 50; //CHANGE THIS NUMBER FOR AMOUNT OF MESSAGES TO SHOW UP
//        int temp = arr.size()-numMessagesToShow;
//        if(temp<0){
//            temp=0;
//        }
//        ArrayList<String> tempArrayList = reverseArrayList(arr);
//        for(int i = temp; i<tempArrayList.size(); i++){
//            textView.append(tempArrayList.get(i));
//        }
//    }
    //KEEP IN CASE WE NEED
//    /**
//     * Method that reverses an ArrayList. Called by appendTextView so that the first message on top of the screen
//     * is the most recent and you can scroll down to older messages.
//     * @param arr1 the ArrayList to reverse
//     * @return a reverse of the ArrayList received
//     */
//    private ArrayList<String> reverseArrayList(ArrayList<String> arr1)
//    {
//        ArrayList<String> reverse = new ArrayList<>();
//        for (int i = arr1.size() - 1; i >= 0; i--) {
//            reverse.add(arr1.get(i));
//        }
//        return reverse;
//    }

    //KEEP IN CASE WE NEED
//    /**
//     * Connects to web sockets for bulletin
//     */
//    private void connectWebSocket() {
//        URI uri;
//        try {
//            uri = new URI("wss://echo.websocket.org");
//        } catch (
//                URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                Log.i("Websocket", "Opened");
//                //mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
//            }
//
//            @Override
//            public void onMessage(String s) {
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView textView = findViewById(R.id.textView);
//                        String temp = textView.getText() + "\n" + message;
//                        textView.setText(temp);
//                    }
//                });
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Log.i("Websocket", "Closed " + s);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.i("Websocket", "Error " + e.getMessage());
//            }
//        };
//        mWebSocketClient.connect();
//    }
//
//    /**
//     * Sends the message to the web socket
//     * @param view view used
//     */
//    public void sendMessage(View view) {
//        EditText editText = findViewById(R.id.messageToAdd);
//        mWebSocketClient.send(sessionManager.getName() + ": " + editText.getText().toString());
//        editText.setText("");
//    }

    /**
     * Web socket that was created to be used with the backend
     */
    private void webSocketWithBackend() {
        Draft[] drafts = {new Draft_6455()};
        String w = "http://coms-309-sb-4.misc.iastate.edu:8080/room";
        w = w + "/" + sessionManager.getName();
        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), drafts[0]) { //this will connect it to the backend
                @Override
                public void onMessage(String message) { //every time a message is received from backend something happens
                    Log.d("", "run() returned: " + message);
                    String s = textView.getText().toString();

                    String messageTemp = message + "\n";
                    String toSet = messageTemp + s; //message is added to beginning of text view
                    textView.setText(toSet); //message is set
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting"); //runs on open, tells us in the log it is opened
                }

                @Override
                public void onClose(int code, String reason, boolean remote) { //runs on closed, tells us in the log it is closed
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                } //sends error to log
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", "URISyntaxException"); //catches exception for bad url
            e.printStackTrace();
        }
        cc.connect(); //connects to backend
    }
}
