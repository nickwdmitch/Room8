package com.database.roomList.tasks.subtasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface holds the subtask repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Repository
public interface SubTasksRepository extends JpaRepository<SubTasks, Long> {
	/**
	 * Query which returns all subtasks for a provided room.
	 * 
	 * @param taskId
	 * @return
	 */
	@Query(value = "SELECT * FROM subtasks WHERE task_id = ?1", nativeQuery = true)
	List<SubTasks> findSubTasksByTaskId(@Param("task_id") Long taskId);
}