package com.database.schedule.events;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersService;
import com.database.rooms.Rooms;
import com.database.rooms.RoomsService;
import com.database.user.User;
import com.database.user.UserService;

/**
 * Events controller for all events endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 */
@RestController
public class EventsController {
	@Autowired
	private EventsService eventService;
	@Autowired
	private RoomMembersService roomMembersService;
	@Autowired
	private RoomsService roomService;
	@Autowired
	private UserService userService;

	/**
	 * Get Events.
	 * 
	 * Gets all of the events of a provided room.
	 * 
	 * @param room
	 * @param user
	 * @return
	 */
	@GetMapping("/getevent/{room}/{user}/{date}/")
	public String getEvent(@PathVariable String room, @PathVariable String user, @PathVariable String date) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);

		List<Events> events = new ArrayList<Events>();
		String response = "";
		for (Events temp : events) {
			if (!temp.getDate().equals(date)) {
				events.remove(temp);
			}
		}

		if (roomMembersService.findRoomMemberByIds(userId, roomId) != null) {
			events = eventService.findEventsByRoomId(roomId);
			response = "\"Response\":\"Success\"";
		} else
			response = "\"Response\":\"No such RoomMembers object for given parameters\"";

		String ret = "{\"Events\":[";
		if (events.isEmpty()) {
			ret += " ";
		}

		for (Events temp : events) {
			ret += "{\"Title\":\"" + temp.getTitle() + "\",\"Description\":\"" + temp.getDescription()
					+ "\",\"Start\":\"" + temp.getStarttime() + "\",\"End\":\"" + temp.getEndtime() + "\",\"User\":\""
					+ temp.getUser().getName() + "\",\"Id\":\"" + temp.getId() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]," + response + "}";
		return ret;
	}

	/**
	 * Create Event.
	 * 
	 * This method takes in the roomId of the room for the room list to be created
	 * and also takes in the userId of the user creating the room. This method is
	 * called whenever a user on the frontend selects the create room option. A
	 * successful response is sent to the frontend if the user is a member of the
	 * room. If the user is not a member of the room then the room list is not
	 * created and a response of "No such RoomMembers object for given parameters"
	 * is sent to the frontend. The frontend is handling user permissions for this
	 * endpoint.
	 * 
	 * @param item
	 * @param room
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/addevent/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String addEvent(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);
		JSONObject body = new JSONObject(item);
		String Title = body.getString("Title");
		String Description = body.getString("Description");
		String Date = body.getString("Date");
		String Start = null;
		String End = null;
		try {
			Start = body.getString("Start");
			End = body.getString("End");
		} catch (JSONException e) {
		}

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null) {
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		}
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have the permissions to take this action\"}";

		Optional<Rooms> tempRoom = roomService.findById(roomId);
		Rooms eventRoom = null;
		Optional<User> tempUser = userService.findById(userId);
		User eventUser = null;

		try {
			eventRoom = tempRoom.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such user exists\"}";
		}

		try {
			eventUser = tempUser.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such user exists\"}";
		}

		if (Start.equals(null) && End.equals(null))
			eventService.addEvent(new Events(eventRoom, Title, Description, Date, eventUser));
		else
			eventService.addEvent(new Events(eventRoom, Title, Description, Date, Start, End, eventUser));
		return "{\"Response\":\"Success\"}";
	}

	/**
	 * Delete Event.
	 * 
	 * Deletes a provided event.
	 * 
	 * @param room
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/deleteevent/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String deleteEvent(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(room);
		Long eventId = Long.valueOf(body.getString("eventId"));

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have permission to complete this action\"}";
		else {
			eventService.deleteById(eventId);
			return "{\"Response\":\"Success\"}";
		}
	}
}
