package com.database.rooms;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersService;
import com.database.user.User;
import com.database.user.UserService;

/**
 * Rooms controller for all rooms endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 */
@RestController
public class RoomsController {
	@Autowired
	private RoomsService roomService;
	@Autowired
	private RoomMembersService roomMembersService;
	@Autowired
	private UserService userService;

	/**
	 * Get Rooms.
	 * 
	 * This method returns all of the rooms a particular user is in.
	 * 
	 * @param user
	 * @return
	 */
	@GetMapping("/getrooms/{user}")
	public String getRooms(@PathVariable String user) {
		Long userId = Long.valueOf(user);
		List<Rooms> rooms = roomMembersService.findRoomsByUserId(userId);
		String ret = "{\"Rooms\":[";
		if (rooms.isEmpty()) {
			ret += " ";
		}
		for (Rooms temp : rooms) {
			ret += "{\"Title\":\"" + temp.getTitle() + "\",\"Id\":\"" + temp.getId() + "\",\"Role\":\""
					+ roomMembersService.findRoomMemberByIds(userId, temp.getId()).getUserRole() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]}";
		return ret;
	}

	/**
	 * Create Room.
	 * 
	 * This method creates a room therefore assigning which ever user created this
	 * room is now the owner of the room. If the room does not exist a response of
	 * "No such room exists" is returned or if a user does not exist a response of
	 * "No such user exists" is returned. Otherwise a success response is returned.
	 * 
	 * @param item
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/room/{user}", consumes = "application/json", produces = "application/json")
	public String addRoom(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		String Title = body.getString("Title");
		Rooms toAdd = new Rooms(Title);
		roomService.addRoom(toAdd);

		Optional<User> admin = userService.findById(Long.valueOf(user));
		User adminTemp = null;
		Optional<Rooms> room = roomService.findById(toAdd.getId());
		Rooms roomTemp = null;

		try {
			adminTemp = admin.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such user exists\"}";
		}
		try {
			roomTemp = room.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such room exists\"}";
		}

		RoomMembers roomMember = new RoomMembers(adminTemp, roomTemp, "OWNER");

		System.out.println("Create room members object " + roomMember);
		roomMembersService.addRoomMembers(roomMember);
		System.out.println("Add room members object to datababse" + roomMember);
		return "{\"Response\":\"Success\"}";
	}

	/**
	 * Delete Room.
	 * 
	 * This method deletes a the requested room. Only ADMIN's can delete rooms. If
	 * the user is not an ADMIN a response of "User is not an OWNER" is returned. If
	 * the room does not exist a response of "No such RoomMembers object for given
	 * parameters" is returned. Otherwise a success response is returned and the
	 * room is removed from the database and all users in said room will no longer
	 * have accesss to that room. Also, all of the roomMembers objects with the
	 * target roomId will also be removed from the database.
	 * 
	 * @param item
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/room/delete/{user}")
	public String deleteRoom(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(body.getString("RoomId"));

		RoomMembers isOwner = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isOwner == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		if (isOwner.getUserRole().equals("OWNER")) {
			roomService.deleteById(roomId);
			List<RoomMembers> temp = roomMembersService.findRoomMembersByRoomId(roomId);
			for (RoomMembers x : temp) {
				roomMembersService.deleteById(x.getId());
			}
			return "{\"Response\":\"Success\"}";
		} else
			return "{\"Response\":\"User is not an OWNER\"}";
	}

}
