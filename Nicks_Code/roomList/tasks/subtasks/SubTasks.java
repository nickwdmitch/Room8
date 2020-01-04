package com.database.roomList.tasks.subtasks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.database.roomList.tasks.Tasks;

/**
 * This class implements the subtasks entity. Each task can have multiple
 * subtasks.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Entity
@Table(name = "subtasks")
public class SubTasks {
	/**
	 * A unique Id which is automatically generated for each subtask.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Holds the contents of this subtask.
	 */
	@Column(name = "contents")
	private String contents;

	/**
	 * Signifies if this task has been completed or not.
	 */
	@Column(name = "completed")
	private boolean completed;

	/**
	 * Holds the task this task was created in.
	 */
	@ManyToOne(targetEntity = com.database.roomList.tasks.Tasks.class)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	private Tasks task;

	/**
	 * Default Constructor
	 */
	public SubTasks() {
	}

	/**
	 * Constructor which sets the below parameters.
	 * 
	 * @param task
	 * @param contents
	 * @param users
	 */
	public SubTasks(String contents, Tasks task) {
		this.contents = contents;
		this.task = task;
		this.completed = false;
	}

	/**
	 * Gets the subtask Id.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the subtask's contents.
	 * 
	 * @return
	 */
	protected String getContents() {
		return contents;
	}

	/**
	 * Gets task Id associated with this subtask.
	 * 
	 * @return
	 */
	protected Tasks getTask() {
		return task;
	}

	/**
	 * Sets the subtask Id.
	 * 
	 * @param id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the subtask's contents.
	 * 
	 * @param contents
	 */
	protected void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * Sets the task Id.
	 * 
	 * @param task
	 */
	protected void setListId(Tasks task) {
		this.task = task;
	}

	/**
	 * Gets completed.
	 * 
	 * @return
	 */
	protected boolean getCompleted() {
		return completed;
	}

	/**
	 * Sets Completed.
	 * 
	 * @param completed
	 */
	protected void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Checks if two subtasks are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SubTasks))
			return false;
		SubTasks subtask = (SubTasks) o;
		return this.id == subtask.id && this.contents.equals(subtask.contents) && this.task == subtask.task
				&& this.completed == subtask.completed;
	}

}
