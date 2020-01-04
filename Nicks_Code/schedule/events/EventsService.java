package com.database.schedule.events;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class implements the events repository.
 * 
 * @author Nickolas Mitchell
 */
@Service
public class EventsService {
	@Autowired
	private EventsRepository eventsRepository;

	/**
	 * Returns all events within the database.
	 * 
	 * @return
	 */
	public List<Events> getEvents() {
		return eventsRepository.findAll();
	}

	/**
	 * Adds an event to the database.
	 * 
	 * @param event
	 * @return
	 */
	public Events addEvent(Events event) {
		return eventsRepository.save(event);
	}

	/**
	 * Returns the number of events within the database.
	 * 
	 * @return
	 */
	public Long count() {

		return eventsRepository.count();
	}

	/**
	 * Deletes an event from the database. Throws IllegalArgumentException if the
	 * event does not exist.
	 * 
	 * @param eventId
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean deleteById(Long eventId) throws IllegalArgumentException {
		eventsRepository.deleteById(eventId);
		return true;
	}

	/**
	 * Returns all events associated with the roomId.
	 * 
	 * @param roomId
	 * @return
	 */
	public List<Events> findEventsByRoomId(Long roomId) {
		return eventsRepository.findEventsByRoomId(roomId);
	}
}
