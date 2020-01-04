package com.database.rooms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface holds the repository for the rooms entity.
 * 
 * @author Nickolas Mitchell
 */
@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
	/**
	 * Query which returns a room with the provided roomId.
	 */
	@Query("select i from Rooms i where i.id = ?1")
	Optional<Rooms> findById(Long roomId);
}
