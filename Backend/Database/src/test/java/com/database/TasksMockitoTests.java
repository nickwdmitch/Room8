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
import com.database.roomList.tasks.Tasks;
import com.database.roomList.tasks.TasksRepository;
import com.database.roomList.tasks.TasksService;
import com.database.rooms.Rooms;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TasksMockitoTests
{
	@Autowired
	private TasksService taskService;
	
	@MockBean
	private TasksRepository tasksRepository; 
	
	@Test
	public void getRoomListsTest() 
	{
		RoomList list1 = new RoomList(new Rooms("Room1"), "Title1", "Description1");
		RoomList list2 = new RoomList(new Rooms("Room2"), "Title2", "Description2");
		
		when(tasksRepository.findAll()).thenReturn(Stream.of(new Tasks("Contents1", list1), 
				new Tasks("Contents2", list2)).collect(Collectors.toList()));
		assertEquals(2, taskService.getTask().size());
	}
	
	@Test
	public void addRoomListTest()
	{
		RoomList list1 = new RoomList(new Rooms("Room1"), "Title1", "Description1");
		Tasks newTask = new Tasks("Contents1", list1);
		when(tasksRepository.save(newTask)).thenReturn(newTask); 
		assertEquals(newTask, taskService.addTask(newTask));
	}

}
