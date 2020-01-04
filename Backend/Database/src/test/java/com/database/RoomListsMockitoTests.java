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

import com.database.roomList.RoomList;
import com.database.roomList.RoomListRepository;
import com.database.roomList.RoomListService;
import com.database.rooms.Rooms;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomListsMockitoTests {
	@Autowired
	private RoomListService roomListService;

	@MockBean
	private RoomListRepository roomListRepository;

	@Test
	public void getRoomListsTest() {
		Rooms room1 = new Rooms("Room1");
		Rooms room2 = new Rooms("Room2");

		when(roomListRepository.findAll()).thenReturn(Stream.of(new RoomList(room1, "First Room", "Description 1"),
				new RoomList(room2, "First Room", "Description 1")).collect(Collectors.toList()));
		assertEquals(2, roomListService.getRoomList().size());
	}

	@Test
	public void addRoomListTest() {
		Rooms room1 = new Rooms("Room1");
		RoomList newRoomList = new RoomList(room1, "First Room", "Description 1");
		when(roomListRepository.save(newRoomList)).thenReturn(newRoomList);
		assertEquals(newRoomList, roomListService.addRoomList(newRoomList));
	}
}
