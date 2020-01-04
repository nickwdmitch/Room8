package edu.iastate.room8;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import edu.iastate.room8.Home.HomeActivity;
import edu.iastate.room8.List.SubtaskActivity;
import edu.iastate.room8.RegisterLogin.LoginActivity;
import edu.iastate.room8.Schedule.DayActivity;
import edu.iastate.room8.Schedule.ScheduleMVP.ScheduleActivity;
import edu.iastate.room8.Settings.RoomSettings.RoomSettingsActivity;
import edu.iastate.room8.Schedule.ScheduleMVP.DateParser;
import edu.iastate.room8.Settings.UserSettings.UserSettingsActivity;
import edu.iastate.room8.utils.Sessions.SessionManager;


/**
 * PaulMockitoTest
 * Tests using Mockito for the project
 * @author pndegnan
 */
@RunWith(MockitoJUnitRunner.class)
public class JakeMockitoTest {
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
    /**
     * Used so Mockito can be used in JUNIT tests
     */
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * This is a test to confirm the functionality of the UserSettingsActivity.
     */
    @Test
    public void UserSettingsActivityTest()  {
        // create and configure mock
        UserSettingsActivity test = Mockito.mock(UserSettingsActivity.class);

        JSONObject JSONRequest = new JSONObject();
        when(test.jsonNameRequest()).thenReturn(JSONRequest);

        try {
            JSONRequest.put("ID", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Name", "TestName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Email", "TestEmail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Password", "123Test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertEquals(JSONRequest.getString("ID"), test.jsonNameRequest().getString("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Name"), test.jsonNameRequest().getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Email"), test.jsonNameRequest().getString("Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Password"), test.jsonNameRequest().getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a test to confirm the functionality of the UserSettingsActivity.
     */
    @Test
    public void LoginTest()  {
        // create and configure mock
        LoginActivity test = Mockito.mock(LoginActivity.class);

        JSONObject JSONRequest = new JSONObject();

        when(test.jsonNameRequest()).thenReturn(JSONRequest);

        try {
            JSONRequest.put("ID", "6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Name", "TestName2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Email", "TestEmail2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Password", "123Test2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertEquals(JSONRequest.getString("ID"), test.jsonNameRequest().getString("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Name"), test.jsonNameRequest().getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Email"), test.jsonNameRequest().getString("Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(JSONRequest.getString("Password"), test.jsonNameRequest().getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void UserSettingsSessionTest() {
        //Depends on getUserDetailTest
        sessionManager2.createSession("TestName2", "TestEmail2", "6");
        UserSettingsActivity test = Mockito.mock(UserSettingsActivity.class);
        JSONObject JSONRequest = new JSONObject();
        when(test.jsonNameRequest()).thenReturn(JSONRequest);

        try {
            JSONRequest.put("ID", "6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Name", "TestName2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Email", "TestEmail2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Password", "123Test2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager2.getID(), test.jsonNameRequest().getString("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager2.getName(), test.jsonNameRequest().getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager2.getEmail(), test.jsonNameRequest().getString("Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void GetUserSettingsInfo() {
        Mockito.when(mockPrefs.getString("NAME", null)).thenReturn("Joe");
        Mockito.when(mockPrefs.getString("EMAIL", null)).thenReturn("Joe@email.com");
        Mockito.when(mockPrefs.getString("ID", null)).thenReturn("34");
        Mockito.when(mockPrefs.getString("ROOM", null)).thenReturn("8");

        //Depends on getUserDetailTest
        sessionManager2.createSession("TestName2", "TestEmail2", "6");
        UserSettingsActivity test = Mockito.mock(UserSettingsActivity.class);
        JSONObject JSONRequest = new JSONObject();
        when(test.jsonNameRequest()).thenReturn(JSONRequest);


        assertEquals("Joe", sessionManager1.getUserDetail().get("NAME"));
        assertEquals("Joe@email.com", sessionManager1.getUserDetail().get("EMAIL"));
        assertEquals("34", sessionManager1.getUserDetail().get("ID"));
        assertEquals("8", sessionManager1.getUserDetail().get("ROOM"));

        try {
            JSONRequest.put("ID", "34");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Name", "Joe");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Email", "Joe@email.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONRequest.put("Password", "123Test2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertNotEquals(sessionManager1.getUserDetail().get("ROOM"), test.jsonNameRequest().getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager1.getUserDetail().get("ID"), test.jsonNameRequest().getString("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager1.getUserDetail().get("NAME"), test.jsonNameRequest().getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(sessionManager1.getUserDetail().get("EMAIL"), test.jsonNameRequest().getString("Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
