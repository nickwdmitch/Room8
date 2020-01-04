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

import com.database.rooms.Rooms;
import com.database.rooms.RoomsRepository;
import com.database.rooms.RoomsService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomMockitoTests 
{
	@Autowired
	private RoomsService roomService;
	@MockBean
	private RoomsRepository roomRepository; 
	
	@Test
	public void getRoomsTest() 
	{
		when(roomRepository.findAll()).thenReturn(Stream.of(new Rooms("Room1"), 
				new Rooms("Room2")).collect(Collectors.toList()));
		assertEquals(2, roomService.getRooms().size());
	}
	
	@Test
	public void addRoomTest()
	{
		Rooms newRoom = new Rooms("Room3");
		when(roomRepository.save(newRoom)).thenReturn(newRoom); 
		assertEquals(newRoom, roomService.addRoom(newRoom));
	}
}
