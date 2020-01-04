package com.database.roomList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface holds the roomLists repository.
 * 
 * @author Thane Storley, Nickolas Mitchell
 */
@Repository
public interface RoomListRepository extends JpaRepository<RoomList, Long> {
	/**
	 * Query which returns all roomlists for a given room.
	 * 
	 * @param roomId
	 * @return
	 */
	@Query(value = "SELECT * FROM room_lists WHERE id = ?1", nativeQuery = true)
	List<RoomList> findListByRoomId(@Param("id") Long roomId);

}
