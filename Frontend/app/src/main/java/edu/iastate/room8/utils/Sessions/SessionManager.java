package edu.iastate.room8.utils.Sessions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.iastate.room8.RegisterLogin.LoginActivity;
import edu.iastate.room8.JoinRoom.NewUserRoomJoin;

/**
 * Class used for session managing. Holds information like the UserID or Room they are in.
 *
 * @author Jake Vaughn
 */
public class SessionManager implements ISessionManagerInversionPattern {
    /**
     * Shared Preferences
     */
    private SharedPreferences sharedPreferences;
    /**
     * Editor for shared preferences
     */
    private SharedPreferences.Editor editor;
    /**
     * Context, class that session manager is being used in
     */
    public Context context;
    /**
     * Constant RoomsSet Set<String>
     */
    private Set<String> RoomsSet = new HashSet<>();
    /**
     * Constant RoomsEDSet Set<String>
     */
    private Set<String> RoomsIDSet = new HashSet<>();


    /**
     * Session Manager constructor
     *
     * @param context what activity it was constructed in
     */
    public SessionManager(Context context) {
        int PRIVATE_MODE = 0;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /**
     * Creates a new session with everything set to null other than the params.
     *
     * @param name  name of user
     * @param email email of user
     * @param id    id of user
     */

    public void createSession(String name, String email, String id) {

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.putString(ROOM, null);
        editor.putString(ROOMID, null);
        editor.putStringSet(ROOMS, RoomsSet);
        editor.putStringSet(ROOMSID, RoomsIDSet);
        editor.putString(PERMISSION, null);
        editor.apply();
    }

    /**
     * Sets the users name to a new name.
     *
     * @param newName the users new name
     */
    public void setName(String newName) {
        editor.putString(NAME, newName);
        editor.apply();
    }

    /**
     * Sets the users Email to a new email.
     *
     * @param newEmail the users new name
     */
    public void setEmail(String newEmail) {
        editor.putString(EMAIL, newEmail);
        editor.apply();
    }

    /**
     * Adds a room to ROOMS list.
     *
     * @param room room to set for the user
     */
    public void addRoom(String room, String id) {


        RoomsSet = (sharedPreferences.getStringSet(ROOMS, null));
        if(RoomsSet!=null){
            RoomsSet.add(room);
        }
        editor.putStringSet(ROOMS, RoomsSet);

        RoomsIDSet = (sharedPreferences.getStringSet(ROOMSID, null));
        if(RoomsIDSet != null){
            RoomsIDSet.add(id);
        }
        editor.putStringSet(ROOMSID, RoomsIDSet);
        editor.apply();
    }

    /**
     * True if user is in a the specified room false if otherwise
     *
     * @param room is in a room or not
     * @return if user is in a room
     */
    public boolean isRoom(String room) {
        Set<String> set;
        set = (sharedPreferences.getStringSet(ROOMS, null));
        if(set!=null){
            return set.contains(room);
        }else{
            return false;
        }
    }

    /**
     * Sets the current room the user is in.
     *
     * @param room sets the room for the user
     */
    public void setRoom(String room) {
//        if(isRoom(room)){
        editor.putString(ROOM, room);
        editor.apply();
//        }
    }

    /**
     * Sets the current roomid the user is in
     *
     * @param roomid sets the roomid for the user
     */
    public void setRoomid(String roomid) {
        editor.putString(ROOMID, roomid);
        editor.apply();
    }

    /**
     * Sets permission
     *
     * @param permission permission of the user for this specific room
     */
    public void setPermission(String permission) {
        editor.putString(PERMISSION, permission);
        editor.apply();
    }

    /**
     * Returns true if the user is in a room false if otherwise
     *
     * @return returns if the user is in a room
     */
    public boolean isInRoom() {
        return this.getRoom() != null;
    }

    /**
     * Checks Room to see if the user is in a room if not it puts user on NewUserRoomJoin
     */
    public void checkRoom() {
        if (!this.isInRoom()) {
            Intent i = new Intent(context, NewUserRoomJoin.class);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    /**
     * Returns current room if user is in room
     *
     * @return the room the user is in
     */
    public String getRoom() {
        return sharedPreferences.getString(ROOM, null);
    }

    /**
     * Returns current roomid if user is in room
     *
     * @return the room the user is in
     */
    public String getRoomid() {
        return sharedPreferences.getString(ROOMID, null);
    }

    /**
     * Returns all the rooms id the user is a part of.
     *
     * @return all rooms id the user is in
     */
    public Set<String> getRoomsId() {
        return sharedPreferences.getStringSet(ROOMSID, null);
    }

    /**
     * Returns all the rooms the user is a part of.
     *
     * @return all rooms the user is in
     */
    public Set<String> getRooms() {
        return sharedPreferences.getStringSet(ROOMS, null);
    }

    /**
     * Returns the name of the user.
     *
     * @return name of user
     */
    public String getName() {
        return sharedPreferences.getString(NAME, null);
    }

    /**
     * Returns the Email of the user.
     *
     * @return email of user
     */
    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    /**
     * Returns the ID of the user.
     *
     * @return id of user
     */
    public String getID() {
        return sharedPreferences.getString(ID, null);
    }

    /**
     * Gets permission
     *
     * @return permission of the user for this specific room
     */
    public String getPermission() {
        return sharedPreferences.getString(PERMISSION, null);
    }

    /**
     * Returns true if the user is logged in false if otherwise.
     *
     * @return returns if the user is logged in
     */
    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    /**
     * Checks if the user is logged in if they are not logged in go to LoginActivity
     */
    public void checkLogin() {

        if (!this.isLoggin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    /**
     * Returns all of the users Details as a HashMap<"Keyword", "Thing">
     *
     * @return returns user details
     */
    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(ROOM, sharedPreferences.getString(ROOM, null));
        user.put(ROOMID, null);
        user.put(PERMISSION, null);

        return user;
    }

    /**
     * Leaves the current room and switches screen to NewUserRoomJoin.
     */
    public void leaveRoom() {
        editor.putString(ROOM, null);
        editor.putString(ROOMID, null);
        editor.apply();
        Intent i = new Intent(context, NewUserRoomJoin.class);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    /**
     * Removes the specified room from the ROOMS that the user is in
     *
     * @param room is in a room that the user is in.
     */
    public void removeRoom(String room, String id) {
        if (this.isRoom(room)) {
            Set<String> setRoom = this.getRooms();
            Set<String> setid = this.getRoomsId();
            setRoom.remove(room);
            setid.remove(id);

            editor.putStringSet(ROOMS, setRoom);
            editor.putStringSet(ROOMSID, setid);

            editor.apply();
        }
    }

    /**
     * Logs out of the session clearing all of the users data from shared preferences.
     */
    public void logout() {

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((Activity) context).finish();

    }

}