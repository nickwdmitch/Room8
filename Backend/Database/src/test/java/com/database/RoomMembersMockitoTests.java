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

import com.database.roomMembers.RoomMembers;
import com.database.roomMembers.RoomMembersRepository;
import com.database.roomMembers.RoomMembersService;
import com.database.rooms.Rooms;
import com.database.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomMembersMockitoTests  
{
	@Autowired
	private RoomMembersService roomMemberService; 
	
	@MockBean
	private RoomMembersRepository roomMembersRepository;
	
	@Test
	public void getRoomMembersTest() 
	{
		User user1 = new User("Nick", "nickisu@isu", "password");
		User user2 = new User("Nick2", "nickisu2@isu", "password");
		Rooms room1 = new Rooms("Room1");
		Rooms room2 = new Rooms("Room2");
		
		when(roomMembersRepository.findAll()).thenReturn(Stream.of(new RoomMembers(user1, room1, "OWNER"), 
				new RoomMembers(user2, room2, "Owner")).collect(Collectors.toList()));
		assertEquals(2, roomMemberService.getRoomMembers().size());
	}
	
	@Test
	public void addRoomMemberTest()
	{
		User user1 = new User("Nick", "nickisu@isu", "password");
		Rooms room1 = new Rooms("Room1");
		RoomMembers newRoomMember = new RoomMembers(user1, room1, "OWNER");
		when(roomMembersRepository.save(newRoomMember)).thenReturn(newRoomMember); 
		assertEquals(newRoomMember, roomMemberService.addRoomMembers(newRoomMember));
	}
}
