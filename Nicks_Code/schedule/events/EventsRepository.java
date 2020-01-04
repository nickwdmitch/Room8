package com.database.schedule.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface holds the repository for the events entity.
 * 
 * @author Nickolas Mitchell
 */
@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
	/**
	 * Query which returns all events for a given room.
	 * 
	 * @Param room_id
	 * @Return
	 */
	@Query(value = "SELECT * FROM events WHERE room_id = ?1", nativeQuery = true)
	List<Events> findEventsByRoomId(@Param("room_id") Long roomId);
}
