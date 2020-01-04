package com.database.roomMembers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.database.rooms.Rooms;
import com.database.user.User;

/**
 * This class implements the roomMembers entity. The roomMembers entity is
 * simply an extra table to handle the manytomany relationship between rooms and
 * users.
 * 
 * @author Nickolas Mitchell
 */
@Entity
@Table(name = "RoomMembers")
public class RoomMembers {
	/**
	 * A unique Id which is automatically generated for each roomMember.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * Bidirectional Many to Many relationship with users.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * Bidirectional Many to Many relationship with rooms.
	 */
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Rooms room;

	/**
	 * Holds the user role. A user can be an "OWNER" "ROOMMATE" or "VIEWER"
	 */
	@Column(name = "user_role")
	private String userRole;

	/**
	 * Default Constructor.
	 */
	public RoomMembers() {

	}

	/**
	 * Constructor which sets the below fields.
	 * 
	 * @param userId
	 * @param roomId
	 */
	public RoomMembers(User user, Rooms room, String userRole) {
		this.user = user;
		this.room = room;
		this.userRole = userRole;
	}

	/**
	 * Sets the roomMembers Id.
	 * 
	 * @param id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the roomMembers Id.
	 * 
	 * @return
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Gets the users role.
	 * 
	 * @return
	 */
	public String getUserRole() {
		return this.userRole;
	}

	/**
	 * Sets the users role.
	 * 
	 * @param userRole
	 */
	protected void setUserRole(String userRole) {
		if (!userRole.equals("Owner") || !userRole.equals("Roommate"))
			this.userRole = "Viewer";
		else
			this.userRole = userRole;
	}

	/**
	 * Gets the user.
	 * 
	 * @return
	 */
	protected User getUser() {
		return user;
	}

	/**
	 * Sets the user
	 * 
	 * @param user
	 */
	protected void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the room.
	 * 
	 * @return
	 */
	protected Rooms getRoom() {
		return room;
	}

	/**
	 * Sets the room.
	 * 
	 * @param room
	 */
	protected void setRoom(Rooms room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "RoomId: " + room + " userId: " + user;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RoomMembers))
			return false;
		RoomMembers RoomMembers = (RoomMembers) o;
		return this.id == RoomMembers.id && this.userRole.equals(RoomMembers.userRole)
				&& this.user.equals(RoomMembers.user) && this.room.equals(RoomMembers.room);
	}
}
