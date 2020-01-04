package com.database.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface holds the repository for the uses entity.
 * 
 * @author Nickolas Mitchell
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Query which finds a User with the provided userId.
	 */
	@Query("select i from User i where i.id = ?1")
	Optional<User> findById(Long userId);

	/**
	 * Query which updates the username of a provided user.
	 * 
	 * @param userName
	 * @param userId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = "update users u set u.name = ?1 where u.id = ?2", nativeQuery = true)
	void updateUsername(@Param("username") String username, @Param("id") Long userId);

	/**
	 * Query which updates the username of a provided user.
	 * 
	 * @param email
	 * @param userId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = "update users u set u.email = ?1 where u.id = ?2", nativeQuery = true)
	void updateUserEmail(@Param("email") String email, @Param("id") Long userId);

	/**
	 * Query which updates the username of a provided user.
	 * 
	 * @param password
	 * @param userId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = "update users u set u.password = ?1 where u.id = ?2", nativeQuery = true)
	void updateUserPassword(@Param("password") String password, @Param("id") Long userId);
}
