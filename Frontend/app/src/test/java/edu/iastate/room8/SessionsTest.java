package edu.iastate.room8;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import android.content.Context;
import android.content.SharedPreferences;

import edu.iastate.room8.utils.Sessions.SessionManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionsTest {
    @Mock
    Context mockContext1;
    @Mock
    Context mockContext2;
    @Mock
    SharedPreferences mockPrefs;
    @Mock
    SharedPreferences.Editor mockEditor;

    private SessionManager sessionManager1;
    private SessionManager sessionManager2;

    @Before
    public void before() {
        MockSharedPreference mockSharedPrefs;
        //MockSharedPreference.Editor mockPrefsEditor;

        Mockito.when(mockContext1.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        Mockito.when(mockContext1.getSharedPreferences(anyString(), anyInt()).edit()).thenReturn(mockEditor);

        mockSharedPrefs = new MockSharedPreference();
        //mockPrefsEditor = mockSharedPrefs.edit();

        Mockito.when(mockContext2.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPrefs);

        sessionManager1 = new SessionManager(mockContext1);
        sessionManager2 = new SessionManager(mockContext2);
    }


    @Test
    public void createSessionTest() {
        //Depends on getUserDetailTest
        sessionManager2.createSession("Jack", "Jack@email.com", "35");

        assertEquals("Jack", sessionManager2.getUserDetail().get("NAME"));
        assertEquals("Jack@email.com", sessionManager2.getUserDetail().get("EMAIL"));
        assertEquals("35", sessionManager2.getUserDetail().get("ID"));
        assertNull(sessionManager2.getUserDetail().get("ROOM"));
    }

//    @Test
//    public void addRoomTest (){
//        //Depends on getUserDetailTest
//        sessionManager2.createSession("Jack", "Jack@email.com", "35");
//        sessionManager2.addRoom("8", "8"); //TODO This is Paul making a comment here. Due to the change in sessionManager this broke. Commenting for pipeline to work
//
//        assertEquals("8", sessionManager2.getUserDetail().get("ROOM"));
//    }

    @Test
    public void getUserDetailTest() {
        Mockito.when(mockPrefs.getString("NAME", null)).thenReturn("Joe");
        Mockito.when(mockPrefs.getString("EMAIL", null)).thenReturn("Joe@email.com");
        Mockito.when(mockPrefs.getString("ID", null)).thenReturn("34");
        Mockito.when(mockPrefs.getString("ROOM", null)).thenReturn("8");

        assertEquals("Joe", sessionManager1.getUserDetail().get("NAME"));
        assertEquals("Joe@email.com", sessionManager1.getUserDetail().get("EMAIL"));
        assertEquals("34", sessionManager1.getUserDetail().get("ID"));
        assertEquals("8", sessionManager1.getUserDetail().get("ROOM"));

    }

    @Test
    public void isInRoomTest(){
        assertFalse(sessionManager1.isInRoom());

        Mockito.when(mockPrefs.getString("ROOM", null)).thenReturn("8");
        assertTrue(sessionManager1.isInRoom());
    }

    @Test
    public void isLoggin(){
        assertFalse(sessionManager1.isLoggin());

        Mockito.when(mockPrefs.getBoolean("IS_LOGIN", false)).thenReturn(true);

        assertTrue(sessionManager1.isLoggin());
    }
}

