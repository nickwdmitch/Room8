package edu.iastate.room8;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import edu.iastate.room8.utils.Sessions.SessionManager;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserLoginMcokitoTest {
    @Mock
    Context mockContext1;
    @Mock
    Context mockContext2;
    @Mock
    SharedPreferences mockPrefs;
    @Mock
    SharedPreferences.Editor mockEditor;

    MockSharedPreference mockSharedPrefs;
    MockSharedPreference.Editor mockPrefsEditor;

    SessionManager sessionManager1;
    SessionManager sessionManager2;
    SessionManager sessionManager3;

    @Before
    public void before() {
        Mockito.when(mockContext1.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        Mockito.when(mockContext1.getSharedPreferences(anyString(), anyInt()).edit()).thenReturn(mockEditor);

        mockSharedPrefs = new MockSharedPreference();
        mockPrefsEditor = mockSharedPrefs.edit();

        Mockito.when(mockContext2.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPrefs);

        sessionManager1 = new SessionManager(mockContext1);
        sessionManager2 = new SessionManager(mockContext2);

        sessionManager3 = new SessionManager(mockContext2);

        sessionManager2.createSession("Jack", "Jack@email.com", "35");
        sessionManager3.addRoom("TestRoom", "5");
        sessionManager3.addRoom("TestRoom2", "6");
        sessionManager3.addRoom("TestRoom3", "7");
        sessionManager3.setRoom("TestRoom");
        sessionManager3.setRoomid("5");
    }

    @Test
    public void createSessionTest2(){
        sessionManager2.createSession("Jack", "Jack@email.com", "35");
        Set<String> set = new HashSet<>();
        assertEquals("Jack", sessionManager2.getUserDetail().get("NAME"));
        assertEquals("Jack@email.com", sessionManager2.getUserDetail().get("EMAIL"));
        assertEquals("35", sessionManager2.getUserDetail().get("ID"));
        assertEquals(null, sessionManager2.getUserDetail().get("ROOM"));
        assertEquals(null, sessionManager2.getUserDetail().get("ROOMID"));
        assertEquals(null, sessionManager2.getUserDetail().get("PERMMISION"));
    }


    @Test
    public void isRoom(){
        assertEquals(true, sessionManager2.isRoom("TestRoom"));
        assertEquals(false, sessionManager2.isRoom("notTestRoom"));
        assertEquals(true, sessionManager2.isRoom("TestRoom2"));


    }
    @Test
    public void getRoomSID() {
        Set<String> set = new HashSet<>();
        set.add("5");
        set.add("6");
        set.add("7");
        assertEquals(set, sessionManager3.getRoomsId());
    }
    @Test
    public void RemoveRoomTest() {
        sessionManager3.removeRoom("TestRoom3", "7");
        Set<String> set = new HashSet<>();
        set.add("TestRoom");
        set.add("TestRoom2");
        assertEquals(set, sessionManager3.getRooms());
    }
    @Test
    public void checkRoom() {
        assertEquals(true, sessionManager3.isInRoom());
        sessionManager2.createSession("1","2","3");
        assertEquals(false, sessionManager2.isInRoom());
        sessionManager2.setRoom("TestRoom2");
        assertEquals(true, sessionManager2.isInRoom());

    }

    @Test
    public void getRoomS() {
        Set<String> set = new HashSet<>();
        set.add("TestRoom");
        set.add("TestRoom2");
        set.add("TestRoom3");
        assertEquals(set, sessionManager3.getRooms());
    }
    @Test
    public void getroomTest(){
        assertEquals("TestRoom", sessionManager3.getRoom());
        assertEquals("5", sessionManager3.getRoomid());
    }

}

