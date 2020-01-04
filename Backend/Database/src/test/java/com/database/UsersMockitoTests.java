package com.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.database.user.User;
import com.database.user.UserRepository;
import com.database.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersMockitoTests 
{
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository; 
	
	@Test
	public void getUsersTest() 
	{
		when(userRepository.findAll()).thenReturn(Stream.of(new User("Nick", "nickisu@iastate.edu", "password"), 
				new User("Thane", "Thane@iastate.edu", "password")).collect(Collectors.toList()));
		assertEquals(2, userService.getUsers().size());
	}
	
	@Test
	public void addUsersTest()
	{
		User newUser = new User("Jake", "jake@iastate.edu", "password");
		when(userRepository.save(newUser)).thenReturn(newUser); 
		assertEquals(newUser, userService.addUser(newUser));
	}
	

}
