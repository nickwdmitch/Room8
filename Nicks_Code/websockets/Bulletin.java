package com.database.websockets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bulletin. This class provides the entity implementation for bulletin. The
 * bulletin is only meant to hold the data from the websocket.
 * 
 * @author Nickolas Mitchell
 */
@Entity
@Table(name = "bulletin")
public class Bulletin {

	/**
	 * Unique Id of bulletin.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Username of the user in the websocket.
	 */
	@Column(name = "username")
	private String username;

	/**
	 * Message the user sends in the websocket.
	 */
	@Column(name = "message")
	private String message;

	/**
	 * Default Constructor.
	 */
	public Bulletin() {
	}

	/**
	 * Main constructor which sets the username and message.
	 * 
	 * @param username
	 * @param message
	 */
	public Bulletin(String username, String message) {
		this.username = username;
		this.message = message;
	}

	/**
	 * Returns the Id.
	 * 
	 * @return
	 */
	protected Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the username.
	 * 
	 * @return
	 */
	protected String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 */
	protected void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the message.
	 * 
	 * @return
	 */
	protected String getMessage() {
		return message;
	}

	/**
	 * Sets the message
	 * 
	 * @param message
	 */
	protected void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Bulletin))
			return false;
		Bulletin bulletin = (Bulletin) o;
		return this.id == bulletin.id && this.username.equals(bulletin.username)
				&& this.message.equals(bulletin.message);
	}

}
