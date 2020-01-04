package com.database.roomList.tasks;

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

import com.database.roomList.RoomList;
import com.database.roomList.RoomListService;
import com.database.roomList.tasks.subtasks.SubTasks;
import com.database.roomList.tasks.subtasks.SubTasksService;
import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersService;

/**
 * Task Controller for all task endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 */
@RestController
public class TasksController {
	@Autowired
	private TasksService taskService;
	@Autowired
	private RoomListService roomListService;
	@Autowired
	private RoomMembersService roomMembersService;
	@Autowired
	private SubTasksService subTaskService;

	/**
	 * Get Tasks.
	 * 
	 * Gets all of the tasks for a provided room.
	 * 
	 * @param room
	 * @param list
	 * @return
	 */
	@GetMapping("/gettasks/{room}/{list}/")
	public String gettasks(@PathVariable String room, @PathVariable String list) {
		Long roomId = Long.valueOf(room);
		Long listId = Long.valueOf(list);

		List<Tasks> tasks = new ArrayList<Tasks>();
		String response = "";

		if (roomListService.findListsByRoomId(roomId).isEmpty())
			response = "\"Response\":\"No such List for given parameters\"";
		else {
			tasks = taskService.findTasksByListId(listId);
			response = "\"Response\":\"Success\"";
		}

		String ret = "{\"TaskList\":[";
		if (tasks.isEmpty()) {
			ret += " ";
		}
		for (Tasks temp : tasks) {
			ret += "{\"Contents\":\"" + temp.getContents() + "\",\"Id\":\"" + temp.getId() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]," + response + "}";
		return ret;
	}

	/**
	 * Create Task.
	 * 
	 * Creates a task for a provided room.
	 * 
	 * @param room
	 * @param list
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/addtask/{room}/{list}/{user}", consumes = "application/json", produces = "application/json")
	public String addTask(@PathVariable("room") String room, @PathVariable("list") String list,
			@PathVariable("user") String user, @RequestBody String item) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);
		Long listId = Long.valueOf(list);
		JSONObject body = new JSONObject(item);
		String Contents = body.getString("Contents");

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null) {
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		}
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have the permissions to take this action\"}";

		Optional<RoomList> toAdd = roomListService.findById(listId);
		RoomList toAddTemp = null;

		try {
			toAddTemp = toAdd.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such List exists\"}";
		}

		taskService.addTask(new Tasks(Contents, toAddTemp));
		return "{\"Response\":\"Success\"}";

	}

	/**
	 * Delete Task.
	 * 
	 * Deletes a provided task.
	 * 
	 * @param room
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/deletetask/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String deleteTask(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(room);
		Long taskId = Long.valueOf(body.getString("taskId"));

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have permission to complete this action\"}";
		else {
			List<SubTasks> temp = subTaskService.findSubTasksByTaskId(taskId);
			for (SubTasks x : temp) {
				subTaskService.deleteById(x.getId());
			}
			taskService.deleteById(taskId);
			return "{\"Response\":\"Success\"}";
		}
	}

}
