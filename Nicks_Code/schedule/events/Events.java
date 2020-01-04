package com.database.schedule.events;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.database.rooms.Rooms;
import com.database.user.User;

/**
 * This class provides the implementation for the Events entity. The schedule
 * entity has a OneToMany relationship with events.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Entity
@Table(name = "events")
public class Events {
	/**
	 * A unique id which is generated for each event.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Title of this event.
	 */
	@Column(name = "title")
	private String title;

	/**
	 * Description of this event.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * Date of the event
	 */
	@Column(name = "date")
	private String date;

	/**
	 * Time at which this event starts.
	 */
	@Column(name = "starttime")
	private String starttime;

	/**
	 * Time at which this event ends.
	 */
	@Column(name = "endtime")
	private String endtime;

	/**
	 * Assigns each event to a user. A user can have multiple events.
	 */
	@ManyToOne(targetEntity = com.database.user.User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	/**
	 * Many to One relationship with Rooms. Holds the schedule this event was
	 * created in. Holds the schedule this event was created in.
	 */
	@ManyToOne(targetEntity = com.database.rooms.Rooms.class)
	@JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "fk_eventroom_id")) //
	private Rooms room;

	/**
	 * Default constructor.
	 */
	public Events() {
	}

	/**
	 * Constructor which sets the room, title, description, start/end time, and user
	 * all corresponding to this event.
	 * 
	 * @param contents
	 * @param userId
	 */
	public Events(Rooms room, String title, String description, String date, String start, String end, User user) {
		this.room = room;
		this.title = title;
		this.description = description;
		this.date = date;
		this.starttime = start;
		this.endtime = end;
		this.user = user;
	}

	/**
	 * Constructor which only sets the room, title, description, and user. This
	 * constructor is helpful if we decide to allow the user the option to provided
	 * a start and end time.
	 * 
	 * @param contents
	 * @param user
	 * @param schedule
	 */
	public Events(Rooms room, String title, String description, String date, User user) {
		this.room = room;
		this.title = title;
		this.description = description;
		this.date = date;
		this.user = user;
		starttime = null;
		endtime = null;
	}

	/**
	 * Returns event id.
	 * 
	 * @return
	 */
	protected Long getId() {
		return id;
	}

	/**
	 * Sets event id.
	 * 
	 * @param id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns event title.
	 * 
	 * @return
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * Sets event title.
	 * 
	 * @param title
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns event description.
	 * 
	 * @return
	 */
	protected String getDescription() {
		return description;
	}

	/**
	 * Sets event description.
	 * 
	 * @param description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns event date.
	 * 
	 * @return
	 */
	protected String getDate() {
		return date;
	}

	/**
	 * Sets event date.
	 * 
	 * @param date
	 */
	protected void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns event start time.
	 * 
	 * @return
	 */
	protected String getStarttime() {
		return starttime;
	}

	/**
	 * Sets events start time.
	 * 
	 * @param starttime
	 */
	protected void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * Returns event end time.
	 * 
	 * @return
	 */
	protected String getEndtime() {
		return endtime;
	}

	/**
	 * Sets events end time.
	 * 
	 * @param endtime
	 */
	protected void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * Returns user associated with this event.
	 * 
	 * @return
	 */
	protected User getUser() {
		return user;
	}

	/**
	 * Sets user associated with this event.
	 * 
	 * @param user
	 */
	protected void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns room this event is associated with.
	 * 
	 * @return
	 */
	protected Rooms getRoom() {
		return room;
	}

	/**
	 * Sets room this event is associated with.
	 * 
	 * @param room
	 */
	protected void setRoom(Rooms room) {
		this.room = room;
	}

	/**
	 * If no starttime or endtime are specified, then the returned string looks
	 * like: "User: 'username' 'title' 'description'"
	 * 
	 * If a startime and endtime are specified, then the returned string looks like:
	 * "User: 'username' 'title' 'description' 'starttime' 'endtime'"
	 */
	@Override
	public String toString() {
		if (starttime.equals(null) || endtime.equals(null))
			return "User: " + user.getName() + "\n" + title + " \n" + description;
		return "User: " + user.getName() + "\n" + title + " \n" + description + "\n" + starttime + "\n" + endtime;
	}

	/**
	 * Checks if two events are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Events))
			return false;
		Events event = (Events) o;
		return this.id == event.id && this.title.equals(event.title) && this.description.equals(event.description)
				&& this.starttime.equals(event.starttime) && this.endtime.equals(event.endtime)
				&& this.room.equals(event.room) && this.user.equals(event.user);
	}

}
