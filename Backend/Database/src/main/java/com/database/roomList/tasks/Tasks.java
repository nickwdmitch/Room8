package com.database.roomList.tasks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.database.roomList.RoomList;

/**
 * This class implements the tasks entity. Each roomList can have multiple
 * tasks.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Entity
@Table(name = "tasks")
public class Tasks {
	/**
	 * A unique Id which is automatically generated for each task.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Holds the contents of this task.
	 */
	@Column(name = "contents")
	private String contents;

	/**
	 * Signifies if this task has been completed or not.
	 */
	@Column(name = "completed")
	private boolean completed;

	/**
	 * Many to One relationship with roomLists.
	 */
	@ManyToOne(targetEntity = com.database.roomList.RoomList.class)
	@JoinColumn(name = "list_id", referencedColumnName = "id")
	private RoomList list;

	/**
	 * Default Constructor
	 */
	public Tasks() {
	}

	/**
	 * Constructor
	 * 
	 * @param list
	 * @param contents
	 */
	public Tasks(String contents, RoomList list) {
		this.contents = contents;
		this.list = list;
		this.completed = false;
	}

	/**
	 * Gets the task Id.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the task's contents.
	 * 
	 * @return
	 */
	protected String getContents() {
		return contents;
	}

	/**
	 * Gets list Id associated with this task.
	 * 
	 * @return
	 */
	protected RoomList getRoomList() {
		return list;
	}

	/**
	 * Sets the task Id.
	 * 
	 * @param id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the task's contents.
	 * 
	 * @param contents
	 */
	protected void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * Sets the List Id.
	 * 
	 * @param listId
	 */
	protected void setList(RoomList list) {
		this.list = list;
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
	 * Checks if two tasks are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Tasks))
			return false;
		Tasks task = (Tasks) o;
		return this.id == task.id && this.contents.equals(task.contents) && this.list == task.list
				&& this.completed == task.completed;
	}
}