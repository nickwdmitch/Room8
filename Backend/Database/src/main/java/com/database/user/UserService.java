package com.database.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the users repository.
 * 
 * @author Nickolas Mitchell
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * Returns all users within the database.
	 * 
	 * @return
	 */
	public List<User> getUsers() {
		logger.info("Returning all users in database");
		return userRepository.findAll();
	}

	/**
	 * Returns a specific user by searching for their id. If the user is not found a
	 * null user object is returned.
	 * 
	 * @param userId
	 * @return
	 */
	public Optional<User> findById(Long userId) {
		Optional<User> user = null;
		try {
			user = userRepository.findById(userId);
			if (user.equals(null))
				throw new NullPointerException();
		} catch (NullPointerException e) {
			logger.info("User does not exist");
		}
		return user;
	}

	/**
	 * Adds a user to the database.
	 * 
	 * @param user
	 * @return
	 */
	public User addUser(User user) {
		userRepository.save(user);
		logger.info("Added: " + user.getName() + " to the database");
		return user;
	}

	/**
	 * Returns number of users within the database.
	 * 
	 * @return
	 */
	public Long count() {
		logger.info("Users in database: " + userRepository.count());
		return userRepository.count();
	}

	/**
	 * Deletes user from the database.
	 * 
	 * @param userId
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void deleteById(Long userId) {
		logger.info("Deleted " + findById(userId) + " from the database");
		userRepository.deleteById(userId);
	}

	/**
	 * Updates username given the userId.
	 * 
	 * @param username
	 * @param userId
	 */
	void updateUsername(@Param("username") String username, @Param("id") Long userId)
	{
		userRepository.updateUsername(username, userId); 
	}

	/**
	 * Updates user email given the userId.
	 * 
	 * @param email
	 * @param userId
	 */
	void updateUserEmail(@Param("email") String email, @Param("id") Long userId)
	{
		userRepository.updateUserEmail(email, userId); 	
	}

	/**
	 * Updates user password given the userId.
	 * 
	 * @param password
	 * @param userId
	 */
	void updateUserPassword(@Param("password") String password, @Param("id") Long userId)
	{
		userRepository.updateUserPassword(password, userId); 
	}
}