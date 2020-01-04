package com.database.roomList.tasks.subtasks;

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
import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersService;

/**
 * Subtask controller for all subtask endpoints.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@RestController
public class SubTasksController {
	@Autowired
	private SubTasksService subTaskService;
	@Autowired
	private TasksService taskService;
	@Autowired
	private RoomMembersService roomMembersService;

	/**
	 * Get Subtasks.
	 * 
	 * Returns all subtasks for a particular room.
	 * 
	 * @param list
	 * @param task
	 * @return
	 */
	@GetMapping("/getsubtasks/{list}/{task}/")
	public String getSubTasks(@PathVariable String list, @PathVariable String task) {
		Long listId = Long.valueOf(list);
		Long taskId = Long.valueOf(task);

		List<SubTasks> subTasks = new ArrayList<SubTasks>();
		String response = "";

		if (taskService.findTasksByListId(listId).isEmpty())
			response = "\"Response\":\"No such Task for given parameters\"";
		else {
			subTasks = subTaskService.findSubTasksByTaskId(taskId);
			response = "\"Response\":\"Success\"";
		}

		String ret = "{\"SubTaskList\":[";
		if (subTasks.isEmpty()) {
			ret += " ";
		}
		for (SubTasks temp : subTasks) {
			ret += "{\"Contents\":\"" + temp.getContents() + "\",\"Id\":\"" + temp.getId() + "\"},";
		}
		ret = ret.substring(0, ret.length() - 1) + "]," + response + "}";
		return ret;
	}

	/**
	 * Create Subtask.
	 * 
	 * Creates a subtask for the provided room.
	 * 
	 * @param room
	 * @param task
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/addsubtask/{room}/{task}/{user}", consumes = "application/json", produces = "application/json")
	public String addSubTask(@PathVariable("room") String room, @PathVariable("task") String task,
			@PathVariable("user") String user, @RequestBody String item) {
		Long roomId = Long.valueOf(room);
		Long userId = Long.valueOf(user);
		Long taskId = Long.valueOf(task);

		JSONObject body = new JSONObject(item);
		String Contents = body.getString("Contents");

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null) {
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		}
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have the permissions to take this action\"}";

		Optional<Tasks> toAdd = taskService.findById(taskId);
		Tasks toAddTemp = null;

		try {
			toAddTemp = toAdd.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such Task exists\"}";
		}

		subTaskService.addSubTask(new SubTasks(Contents, toAddTemp));
		return "{\"Response\":\"Success\"}";

	}

	/**
	 * Delete Subtask.
	 * 
	 * Deletes a provided subtask for a provided room.
	 * 
	 * @param room
	 * @param user
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/deletesubtask/{room}/{user}", consumes = "application/json", produces = "application/json")
	public String deleteSubTask(@PathVariable("room") String room, @PathVariable("user") String user,
			@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		Long userId = Long.valueOf(user);
		Long roomId = Long.valueOf(room);
		Long subTaskId = Long.valueOf(body.getString("subTaskId"));

		RoomMembers isAdmin = roomMembersService.findRoomMemberByIds(userId, roomId);
		if (isAdmin == null)
			return "{\"Response\":\"No such RoomMembers object for given parameters\"}";
		if (isAdmin.getUserRole().equals("VIEWER"))
			return "{\"Response\":\"User does not have permission to complete this action\"}";
		else {
			subTaskService.deleteById(subTaskId);
			return "{\"Response\":\"Success\"}";
		}
	}

}
