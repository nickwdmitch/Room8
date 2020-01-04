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
import com.database.roomList.tasks.subtasks.SubTasks;
import com.database.roomList.tasks.subtasks.SubTasksRepository;
import com.database.roomList.tasks.subtasks.SubTasksService;
import com.database.rooms.Rooms;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubTasksMockitoTests 
{
	@Autowired
	private SubTasksService subTaskService;
	
	@MockBean
	private SubTasksRepository subTasksRepository; 
	
	@Test
	public void getRoomListsTest() 
	{
		Tasks task1 = new Tasks("Contents1", new RoomList(new Rooms("Room1"), "Title1", "Description1"));  
		Tasks task2 = new Tasks("Contents2", new RoomList(new Rooms("Room2"), "Title2", "Description2"));
		
		when(subTasksRepository.findAll()).thenReturn(Stream.of(new SubTasks("Contents1", task1), 
				new SubTasks("Contents2", task2)).collect(Collectors.toList()));
		assertEquals(2, subTaskService.getSubTask().size());
	}
	
	@Test
	public void addRoomListTest()
	{
		Tasks task = new Tasks("Contents1", new RoomList(new Rooms("Room1"), "Title1", "Description1"));
		SubTasks newSubTask = new SubTasks("SubTask1", task);
		when(subTasksRepository.save(newSubTask)).thenReturn(newSubTask); 
		assertEquals(newSubTask, subTaskService.addSubTask(newSubTask));
	}

}
