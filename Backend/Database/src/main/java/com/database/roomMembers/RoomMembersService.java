package com.database.roomMembers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.rooms.Rooms;

/**
 * This class implements the roomMembers repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Service
public class RoomMembersService {
	@Autowired
	private RoomMembersRepository roomMembersRepository;

	/**
	 * Returns all roomMembers objects.
	 * 
	 * @return
	 */
	public List<RoomMembers> getRoomMembers() {
		return roomMembersRepository.findAll();
	}

	/**
	 * Adds a roomMembers object to the database.
	 * 
	 * @param roomMembers
	 * @return
	 */
	public RoomMembers addRoomMembers(RoomMembers roomMembers) {
		roomMembersRepository.save(roomMembers);
		return roomMembers;
	}

	/**
	 * Returns all rooms all user is a member of.
	 * 
	 * @param userId
	 * @return
	 */
	public List<Rooms> findRoomsByUserId(Long userId) {
		List<RoomMembers> temp = roomMembersRepository.findRoomMembersByUserId(userId);
		List<Rooms> toReturn = new ArrayList<Rooms>();
		for (RoomMembers x : temp) {
			Rooms room = x.getRoom();
			toReturn.add(room);
		}
		return toReturn;
	}

	/**
	 * Returns all roomMembers with a given roomId.
	 * 
	 * @param roomId
	 * @return
	 */
	public List<RoomMembers> findRoomMembersByRoomId(Long roomId) {
		return roomMembersRepository.findRoomMembersByRoomId(roomId);
	}

	/**
	 * Returns one RoomMember with a given userId and roomId.
	 * 
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public RoomMembers findRoomMemberByIds(Long userId, Long roomId) {
		List<RoomMembers> temp = roomMembersRepository.findRoomMembersByUserId(userId);
		for (RoomMembers x : temp) {
			if (x.getRoom().getId().equals(roomId) && x.getUser().getId().equals(userId))
				return x;
		}
		return null;
	}

	/**
	 * Updates the users role.
	 * 
	 * @param userRole
	 * @param userId
	 * @return
	 */
	public void updateUserRole(String userRole, Long userId) {
		roomMembersRepository.updateUserRole(userRole, userId);
	}

	/**
	 * Deletes a roomMembers object from the database.
	 * 
	 * @param roomMembersId
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void deleteById(Long roomMembersId) {
		roomMembersRepository.deleteById(roomMembersId);
	}
}
