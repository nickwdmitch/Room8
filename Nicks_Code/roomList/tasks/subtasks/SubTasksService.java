package com.database.roomList.tasks.subtasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.roomList.tasks.subtasks.SubTasks;

/**
 * This class implements the subtask repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Service
public class SubTasksService {
	/**
	 * Holds the subtask repository object.
	 */
	@Autowired
	private SubTasksRepository subtaskRepository;

	/**
	 * Gets all subtasks in the database.
	 * 
	 * @return
	 */
	public List<SubTasks> getSubTask() {
		return subtaskRepository.findAll();
	}

	/**
	 * Adds a subtask to the database.
	 * 
	 * @param subtask
	 * @return
	 */
	public SubTasks addSubTask(SubTasks subtask) {
		return subtaskRepository.save(subtask);
	}

	/**
	 * Gets number of subtasks within the database.
	 * 
	 * @return
	 */
	public Long count() {
		return subtaskRepository.count();
	}

	/**
	 * Returns all subtasks for a provided room.
	 * 
	 * @param taskId
	 * @return
	 */
	public List<SubTasks> findSubTasksByTaskId(Long taskId) {
		return subtaskRepository.findSubTasksByTaskId(taskId);
	}

	/**
	 * Deletes a subtask from the database. Throws IllegalArgumentException if the
	 * subtask does not exist.
	 * 
	 * @param subtask
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean deleteById(Long subtask) throws IllegalArgumentException {
		subtaskRepository.deleteById(subtask);
		return true;
	}

}