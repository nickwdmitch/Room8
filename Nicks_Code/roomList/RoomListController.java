package com.database.roomList;

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

import com.database.roomList.tasks.Tasks;
import com.database.roomList.tasks.TasksService;
import com.database.roomList.tasks.subtasks.SubTasks;
import com.database.roomList.tasks.subtasks.SubTasksService;
import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersService;
import com.database.rooms.Rooms;
import com.database.rooms.RoomsService;

/**
 * RoomList controller for all roomlist endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 *
 */
@RestController
public class RoomListController {
	@Autowired
	private RoomListService roomListService;
	@Autowired
	private RoomMembersService roomMembersService;
	@Autowired
	private SubTasksService subTaskService;
	@Autowired
	private TasksService taskService;
	@Autowired
	private RoomsService roomService;

	/**
	 * Get Lists.
	 * 
	 * Gets all of the lists of a provided room.
	 * 
	 * @param room
	 * @param user
	 * @return
	 */
	@GetMapping("/getlists/{room}/{user}/")
	public String getRoomList(@PathVariable String room, @PathVariable String user) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);

		List<RoomList> lists = new ArrayList<RoomList>();
		String response = "";

		if (roomMembersService.findRoomMemberByIds(userId, roomId) != null) {
			lists = roomListService.findListsByRoomId(roomId);
			response = "\"Response\":\"Success\"";
		} else
			response = "\"Response\":\"No such RoomMembers object for given parameters\"";

		String ret = "{\"RoomLists\":[";
		if (lists.isEmpty()) {
			ret += " ";
		}
		for (RoomList temp : lists) {
			ret += "{\"Title\":\"" + temp.getTitle() + "\",\"Description\":\"" + temp.getDescription() + "\",\"Id\":\""
					+ temp.getId() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]," + response + "}";
		return ret;
	}

	/**
	 * Create RoomList.
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
	@PostMapping(path = "/addlist/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String addRoomList(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);
		JSONObject body = new JSONObject(item);
		String Title = body.getString("Title");
		String Description = body.getString("Description");
		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null) {
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		}
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have the permissions to take this action\"}";

		Optional<Rooms> toAdd = roomService.findById(roomId);
		Rooms toAddTemp = null;

		try {
			toAddTemp = toAdd.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such user exists\"}";
		}

		roomListService.addRoomList(new RoomList(toAddTemp, Title, Description));
		return "{\"Response\":\"Success\"}";
	}

	/**
	 * Delete List.
	 * 
	 * Deletes a provided list.
	 * 
	 * @param room
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/deletelist/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String deletelist(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(room);
		Long listId = Long.valueOf(body.getString("listId"));

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have permission to complete this action\"}";
		else {
			List<Tasks> temp = taskService.findTasksByListId(listId);
			for (Tasks x : temp) {
				List<SubTasks> temp2 = subTaskService.findSubTasksByTaskId(x.getId());
				for (SubTasks y : temp2) {
					subTaskService.deleteById(y.getId());
				}
				taskService.deleteById(x.getId());
			}
			roomListService.deleteById(listId);
			return "{\"Response\":\"Success\"}";
		}
	}

}
