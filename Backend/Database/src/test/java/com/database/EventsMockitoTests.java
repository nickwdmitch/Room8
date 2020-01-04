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
import com.database.schedule.events.Events;
import com.database.schedule.events.EventsRepository;
import com.database.schedule.events.EventsService;
import com.database.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventsMockitoTests {
	@Autowired
	private EventsService eventsService;

	@MockBean
	private EventsRepository eventsRepository;

	@Test
	public void getBulletinTest() {
		Rooms room1 = new Rooms("Room1");
		User user1 = new User("Nick", "nickisu@nick.com", "password123");
		Rooms room2 = new Rooms("Room2");
		User user2 = new User("Thane", "thane@thane.com", "password321");
		when(eventsRepository.findAll()).thenReturn(Stream
				.of(new Events(room1, "Party", "Party @ Nicks", "December 25th", "9:00pm", "3:00am", user1),
						new Events(room2, "Sleep", "Get 8 hours of sleep", "December 26th", "5:00am", "1:00pm", user2))
				.collect(Collectors.toList()));
		assertEquals(2, eventsService.getEvents().size());
	}

	@Test
	public void addRoomListTest() {
		Rooms room1 = new Rooms("Room1");
		User user1 = new User("Nick", "nickisu@nick.com", "password123");
		Events event = new Events(room1, "Party", "Party @ Nicks", "December 25th", "9:00pm", "3:00am", user1);
		when(eventsRepository.save(event)).thenReturn(event);
		assertEquals(event, eventsService.addEvent(event));
	}

}
