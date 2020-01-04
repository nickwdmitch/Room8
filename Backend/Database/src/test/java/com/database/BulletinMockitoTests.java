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

import com.database.websockets.Bulletin;
import com.database.websockets.BulletinRepository;
import com.database.websockets.BulletinService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BulletinMockitoTests {
	@Autowired
	private BulletinService bulletinService;

	@MockBean
	private BulletinRepository bulletinRepository;

	@Test
	public void getBulletinTest() {
		when(bulletinRepository.findAll())
				.thenReturn(Stream.of(new Bulletin("Nick", "Hello websocket"), new Bulletin("Thane", "Hello Nick!"))
						.collect(Collectors.toList()));
		assertEquals(2, bulletinService.getBulletin().size());
	}

	@Test
	public void addRoomListTest() {
		Bulletin bulletin = new Bulletin("Nick", "Hello websocket");
		when(bulletinRepository.save(bulletin)).thenReturn(bulletin);
		assertEquals(bulletin, bulletinService.addBulletin(bulletin));
	}

}
