package com.database.roomList;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.rooms.RoomsService;

/**
 * This class implements the roomList repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Service
public class RoomListService {
	/**
	 * Holds the roomList repository.
	 */
	@Autowired
	private RoomListRepository roomListRepository;

	private final Logger logger = LoggerFactory.getLogger(RoomsService.class);

	/**
	 * Gets all roomLists in the database.
	 * 
	 * @return
	 */
	public List<RoomList> getRoomList() {
		return roomListRepository.findAll();
	}

	public Optional<RoomList> findById(Long listId) {
		Optional<RoomList> list = null;
		try {
			list = roomListRepository.findById(listId);
			if (list.equals(null))
				throw new NullPointerException();
		} catch (NullPointerException e) {
			logger.info("User does not exist");
		}
		return list;
	}

	/**
	 * Adds a roomList to the database.
	 * 
	 * @param roomList
	 * @return
	 */
	public RoomList addRoomList(RoomList roomList) {
		return roomListRepository.save(roomList);
	}

	/**
	 * Gets number of roomLists in database.
	 * 
	 * @return
	 */
	public Long count() {
		return roomListRepository.count();
	}

	/**
	 * Deletes a roomList from the database. Throws IllegalArgumentException if the
	 * roomList does not exist.
	 * 
	 * @param roomList
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean deleteById(Long roomList) throws IllegalArgumentException {
		roomListRepository.deleteById(roomList);
		return true;
	}

	/**
	 * Returns all rooms_lists associated with the given roomId.
	 * 
	 * @param roomId
	 * @return
	 */
	public List<RoomList> findListsByRoomId(Long roomId) {
		return roomListRepository.findListByRoomId(roomId);
	}
}
