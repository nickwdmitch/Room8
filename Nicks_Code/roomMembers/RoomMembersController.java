package com.database.roomMembers;

import java.util.ArrayList;
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

import com.database.rooms.Rooms;
import com.database.rooms.RoomsService;
import com.database.user.User;
import com.database.user.UserService;

/**
 * RoomMembers controller for all room members endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 */
@RestController
public class RoomMembersController {
	@Autowired
	private RoomMembersService roomMembersService;
	@Autowired
	private RoomsService roomService;
	@Autowired
	private UserService userService;

	/**
	 * Get RoomMembers.
	 * 
	 * Returns all rooms a user is a member of.
	 * 
	 * @param room
	 * @param user
	 * @return
	 */
	@GetMapping("/getroommembers/{room}/{user}/")
	public String getRoomMembers(@PathVariable String room, @PathVariable String user) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);

		List<RoomMembers> roomMembers = new ArrayList<RoomMembers>();
		String response = "";

		RoomMembers isOwner = roomMembersService.findRoomMemberByIds(userId, roomId);

		if (isOwner == null)
			response = "\"Response\":\"No such RoomMembers object for given parameters\"";
		else if (isOwner.getUserRole().equals("OWNER")) {
			roomMembers = roomMembersService.findRoomMembersByRoomId(roomId);
			response = "\"Response\":\"Success\"";
		} else
			response = "\"Response\":\"Current user does  not have permission to access this information\"";

		String ret = "{\"Users\":[";
		if (roomMembers.isEmpty()) {
			ret += " ";
		}
		for (RoomMembers temp : roomMembers) {
			ret += "{\"Name\":\"" + temp.getUser().getName() + "\",\"Email\":\"" + temp.getUser().getEmail()
					+ "\",\"Role\":\"" + temp.getUserRole() + "\",\"UserId\":\"" + temp.getUser().getId() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]," + response + "}";
		return ret;
	}

	/**
	 * Kick User From Room.
	 * 
	 * This method kicks a particular user from a room. Only the OWNER can kick a
	 * user out of a room. If the user trying to kick another user from the room is
	 * not an OWNER, a response of "User is not an OWNER" is returned. If the user
	 * to be kicked from the room does not exist then a response of "No such
	 * RoomMembers object for given parameters" is returned. Otherwise a success
	 * response is returned.
	 * 
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/room/kick/{user}")
	public String kickUser(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		String RoomId = body.getString("RoomId");
		String toKick = body.getString("UserId");

		RoomMembers toDel = roomMembersService.findRoomMemberByIds(Long.valueOf(toKick), Long.valueOf(RoomId));
		RoomMembers isOwner = roomMembersService.findRoomMemberByIds(Long.valueOf(user), Long.valueOf(RoomId));
		if (toDel == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters (room and user being kicked)\"}";
		else if (isOwner == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters (room and current user)\"}";
		else if (!(isOwner.getRoom().equals(toDel.getRoom()))) // not in the same room
			return "{\"Response\":\"Current user is not in the same room as the user being kicked\"}";
		if (isOwner.getUserRole().equals("OWNER")) {
			roomMembersService.deleteById(toDel.getId());
			return "{\"Response\":\"Success\"}";
		} else
			return "{\"Response\":\"User is not an OWNER\"}";
	}

	/**
	 * Set User Role.
	 * 
	 * Sets the user role. The user role can only be set by the OWNER of the room.
	 * 
	 * @param item
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/room/setrole/{user}")
	public String setRole(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		String RoomId = body.getString("RoomId");
		String setUser = body.getString("UserId");
		String role = body.getString("Role");
		role = role.toUpperCase();

		RoomMembers toSet = roomMembersService.findRoomMemberByIds(Long.valueOf(setUser), Long.valueOf(RoomId));
		RoomMembers isOwner = roomMembersService.findRoomMemberByIds(Long.valueOf(user), Long.valueOf(RoomId));
		if (toSet == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters (room and user being set)\"}";
		else if (isOwner == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters (room and current user)\"}";
		else if (!(isOwner.getRoom().equals(toSet.getRoom()))) // not in the same room
			return "{\"Response\":\"Current user is not in the same room as the user being set\"}";
		else if (isOwner.getUserRole().equals("OWNER")) {
			if (role.equals("VIEWER") || role.equals("ROOMMATE")) {
				roomMembersService.updateUserRole(role, Long.valueOf(setUser));
				return "{\"Response\":\"Success\"}";
			} else
				return "{\"Response\":\"Invalid Role\"}";
		} else
			return "{\"Response\":\"User is not an OWNER\"}";
	}

	/**
	 * Join Room.
	 * 
	 * This method allows a user to join a room. If the room does not exist a
	 * response of "No such room exists" is returned and if the room the user
	 * requested to join does not exist a response of "No such room exists" is
	 * returned. Otherwise a success response is returned and the viewer's
	 * permissions are automatically set to VIEWER. The frontend allows for the
	 * OWNER to give a VIEWER more permission at their discretion.
	 * 
	 * @param item
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/room/join/{user}", consumes = "application/json", produces = "application/json")
	public String joinRoom(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(body.getString("RoomId"));

		Optional<User> admin = userService.findById(userId);
		User adminTemp = null;
		Optional<Rooms> room = roomService.findById(roomId);
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

		RoomMembers roomMember = new RoomMembers(adminTemp, roomTemp, "VIEWER");

		System.out.println("Create room members object " + roomMember);
		roomMembersService.addRoomMembers(roomMember);
		System.out.println("Add room members object to datababse" + roomMember);
		return "{\"Response\":\"Success\"}";
	}

}
