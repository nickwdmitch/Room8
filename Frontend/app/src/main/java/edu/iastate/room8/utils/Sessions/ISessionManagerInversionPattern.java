package edu.iastate.room8.utils.Sessions;

import java.util.HashMap;
import java.util.Set;

public interface ISessionManagerInversionPattern {
    /**
     * Constant preference name
     */
    String PREF_NAME = "LOGIN";
    /**
     * Constant login string
     */
    String LOGIN = "IS_LOGIN";
    /**
     * Constant room string
     */
    String ROOM = "ROOM";
    /**
     * Constant roomid string
     */
    String ROOMID = "ROOMID";
    /**
     * Constant rooms string
     */
    String ROOMS = "ROOMS";
    /**
     * Contains the ID of the ROOMS
     */
    String ROOMSID = "ROOMSID";
    /**
     * Constant name string
     */
    String NAME = "NAME";
    /**
     * Constant email string
     */
    String EMAIL = "EMAIL";
    /**
     * Constant ID string
     */
    String ID = "ID";
    /**
     * Constant Permissions String
     */
    String PERMISSION = "PERMISSION";

    /**
     * Creates a new session with everything set to null other than the params.
     *
     * @param name  name of user
     * @param email email of user
     * @param id    id of user
     */
    void createSession(String name, String email, String id);

    /**
     * Sets the users name to a new name.
     *
     * @param newName the users new name
     */
    void setName(String newName);

    /**
     * Sets the users Email to a new email.
     *
     * @param newEmail the users new name
     */
    void setEmail(String newEmail);

    /**
     * Adds a room to ROOMS list.
     *
     * @param room room to set for the user
     */
    void addRoom(String room, String id);

    /**
     * True if user is in a the specified room false if otherwise
     *
     * @param room is in a room or not
     * @return if user is in a room
     */
    boolean isRoom(String room);

    /**
     * Sets the current room the user is in.
     *
     * @param room sets the room for the user
     */
    void setRoom(String room);

    /**
     * Sets the current roomid the user is in
     *
     * @param roomid sets the roomid for the user
     */
    void setRoomid(String roomid);

    /**
     * Sets permission
     *
     * @param permission permission of the user for this specific room
     */
    void setPermission(String permission);

    /**
     * Returns true if the user is in a room false if otherwise
     *
     * @return returns if the user is in a room
     */
    boolean isInRoom();

    /**
     * Checks Room to see if the user is in a room if not it puts user on NewUserRoomJoin
     */
    void checkRoom();

    /**
     * Returns current room if user is in room
     *
     * @return the room the user is in
     */
    String getRoom();

    /**
     * Returns current roomid if user is in room
     *
     * @return the room the user is in
     */
    String getRoomid();

    /**
     * Returns all the rooms id the user is a part of.
     *
     * @return all rooms id the user is in
     */
    Set<String> getRoomsId();

    /**
     * Returns all the rooms the user is a part of.
     *
     * @return all rooms the user is in
     */
    Set<String> getRooms();

    /**
     * Returns the name of the user.
     *
     * @return name of user
     */
    String getName();

    /**
     * Returns the Email of the user.
     *
     * @return email of user
     */
    String getEmail();

    /**
     * Returns the ID of the user.
     *
     * @return id of user
     */
    String getID();

    /**
     * Gets permission
     *
     * @return permission of the user for this specific room
     */
    String getPermission();

    /**
     * Returns true if the user is logged in false if otherwise.
     *
     * @return returns if the user is logged in
     */
    boolean isLoggin();

    /**
     * Checks if the user is logged in if they are not logged in go to LoginActivity
     */
    void checkLogin();

    /**
     * Returns all of the users Details as a HashMap<"Keyword", "Thing">
     *
     * @return returns user details
     */
    HashMap<String, String> getUserDetail();

    /**
     * Leaves the current room and switches screen to NewUserRoomJoin.
     */
    void leaveRoom();

    /**
     * Removes the specified room from the ROOMS that the user is in
     *
     * @param room is in a room that the user is in.
     */
    void removeRoom(String room, String id);

    /**
     * Logs out of the session clearing all of the users data from shared preferences.
     */
    void logout();

}
