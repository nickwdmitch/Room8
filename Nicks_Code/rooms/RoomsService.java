package com.database.rooms;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class implements the rooms repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Service
public class RoomsService {
	@Autowired
	private RoomsRepository roomsRepository;

	private final Logger logger = LoggerFactory.getLogger(RoomsService.class);

	/**
	 * Returns all of the rooms within the database.
	 * 
	 * @return
	 */
	public List<Rooms> getRooms() {
		return roomsRepository.findAll();
	}

	/**
	 * Returns a room given its Id. Returns null if room doesn't exist.
	 * 
	 * @param roomId
	 * @return
	 */
	public Optional<Rooms> findById(Long roomId) {
		Optional<Rooms> room = null;
		try {
			room = roomsRepository.findById(roomId);
			if (room.equals(null))
				throw new NullPointerException();
		} catch (NullPointerException e) {
			logger.info("User does not exist");
		}
		return room;
	}

	/**
	 * Adds a room to the database.
	 * 
	 * @param room
	 * @return
	 */
	public Rooms addRoom(Rooms room) {
		roomsRepository.save(room);
		return room;
	}

	/**
	 * Returns the number of rooms within the database.
	 * 
	 * @return
	 */
	public Long count() {

		return roomsRepository.count();
	}

	/**
	 * Deletes room from database.
	 * 
	 * @param roomId
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void deleteById(Long roomId) {
		roomsRepository.deleteById(roomId);
	}

}
