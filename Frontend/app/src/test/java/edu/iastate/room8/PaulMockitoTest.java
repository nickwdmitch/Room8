package edu.iastate.room8;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.iastate.room8.Home.HomeActivity;
import edu.iastate.room8.List.SubtaskActivity;
import edu.iastate.room8.Schedule.DayActivity;
import edu.iastate.room8.Schedule.ScheduleMVP.IDateParserInversionPattern;
import edu.iastate.room8.Schedule.ScheduleMVP.ISchedulePresenter;
import edu.iastate.room8.Schedule.ScheduleMVP.ScheduleActivity;
import edu.iastate.room8.Schedule.ScheduleMVP.SchedulePresenter;
import edu.iastate.room8.Settings.RoomSettings.RoomSettingsActivity;
import edu.iastate.room8.Schedule.ScheduleMVP.DateParser;
import edu.iastate.room8.utils.Sessions.ISessionManagerInversionPattern;
import edu.iastate.room8.utils.Sessions.SessionManager;


/**
 * PaulMockitoTest
 * Tests using Mockito for the project
 * @author pndegnan
 */
public class PaulMockitoTest {
    /**
     * Used so Mockito can be used in JUNIT tests
     */
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * Simple JUNIT Test that makes sure that the class PaulMockitoTest is working
     * If it is not working it will give an error
     */
    @Test
    public void simpleJUNITTest() {
        assertEquals(1, 1);
    } //Makes sure class works as expected


    /**
     * Used to test schedule, specifically this one tests if the callDateParser method is working
     * If callDateParser is not working it will not be true. Simple to make sure the method
     * works and specifically that mocking works.
     */
    @Test
    public void scheduleTest()  {
        //This creates a Mock Object of the class that we have not fully implemented
        ScheduleActivity test = mock(ScheduleActivity.class);

        String day = "14";
        String month = "12";
        String year = "1998";
        String date = month + "/" + day + "/" + year;

        when(test.callDateParser()).thenReturn(date);

        Assert.assertEquals(date, test.callDateParser());
    }

    /**
     * Used to test schedule, specifically this one tests if the callDateParser method is working
     * If callDateParser is not working it will not be true. Simple to make sure the method
     * works and specifically that mocking works.
     */
    @Test
    public void scheduleTest2() {
        //This creates a Mock Object of the class that we have not fully implemented
        ScheduleActivity test = mock(ScheduleActivity.class);
        DateParser dateParser = new DateParser(14, 12, 1998); //day month year

        String day = "14";
        String month = "12";
        String year = "1998";
        String date = month + "/" + day + "/" + year;

        when(test.callDateParser()).thenReturn(date);

        Assert.assertEquals(test.callDateParser(),dateParser.parseDate());
    }

    /**
     * Testing with JSONObject's to verify how they work
     */
    @Test
    public void jsonObjectTest() throws JSONException{
        DayActivity test = mock(DayActivity.class);

        String day = "14";
        String month = "12";
        String year = "1998";
        String date = month + "/" + day + "/" + year;

        JSONObject response = new JSONObject();

        response.put("StartTime", "2:00pm");
        response.put("EndTime", "3:00pm");
        response.put("EventName", "Demo");
        response.put("User", "Paul");
        response.put("Date", date);

        when(test.jsonGetSchedule()).thenReturn(response);

        Assert.assertEquals(response.getString("StartTime"), test.jsonGetSchedule().getString("StartTime"));
        Assert.assertEquals(response.getString("EndTime"), test.jsonGetSchedule().getString("EndTime"));
        Assert.assertEquals(response.getString("EventName"), test.jsonGetSchedule().getString("EventName"));
        Assert.assertEquals(response.getString("User"), test.jsonGetSchedule().getString("User"));
    }

    /**
     * Testing with JSONObject's to verify how they work
     */
    @Test
    public void jsonObjectTestFalse() throws JSONException{
        DayActivity test = mock(DayActivity.class);

        String day = "14";
        String month = "12";
        String year = "1998";
        String date = month + "/" + day + "/" + year;

        JSONObject response = new JSONObject();

        response.put("StartTime", "2:00pm");
        response.put("EndTime", "3:00pm");
        response.put("EventName", "Demo");
        response.put("User", "Paul");
        response.put("Date", date);


        when(test.jsonGetSchedule()).thenReturn(response);

        Assert.assertNotEquals(response.getString("StartTime"), test.jsonGetSchedule().getString("EndTime"));
        Assert.assertNotEquals(response.getString("EndTime"), test.jsonGetSchedule().getString("StartTime"));
        Assert.assertNotEquals(response.getString("EventName"), test.jsonGetSchedule().getString("User"));
        Assert.assertNotEquals(response.getString("User"), test.jsonGetSchedule().getString("EventName"));
    }

    /**
     * Testing with permissions between users
     */
    @Test
    public void PermissionTest1(){
        HomeActivity test = mock(HomeActivity.class);
//        SessionManager sessionManager = new SessionManager(test);
//        sessionManager.setPermission("Owner");
//        sessionManager.setRoomid("1");
//        sessionManager.setRoom("Room");

        when(test.getPermissionHome()).thenReturn("Owner");

        test.setPermissionForTesting("Owner");
        String temp = test.getPermissionHome();

        Assert.assertEquals("Owner", temp);

        Assert.assertEquals(View.VISIBLE, test.getButtonVisibility());
        Assert.assertNotEquals(View.INVISIBLE, test.getButtonVisibility());
    }
    /**
     * Testing with permissions between users
     */
    @Test
    public void PermissionTest2() {
        HomeActivity test = mock(HomeActivity.class);
//        SessionManager sessionManager = new SessionManager(test);
//        sessionManager.setPermission("Owner");
//        sessionManager.setRoomid("1");
//        sessionManager.setRoom("Room");

        when(test.getPermissionHome()).thenReturn("Editor");

        test.setPermissionForTesting("Editor");
        String temp = test.getPermissionHome();

        Assert.assertEquals("Editor", temp);

        Assert.assertEquals(View.VISIBLE, test.getButtonVisibility());
        Assert.assertNotEquals(View.INVISIBLE, test.getButtonVisibility());
    }
    /**
     * Testing with permissions between users
     */
    @Test
    public void PermissionTest3() {
        HomeActivity test = mock(HomeActivity.class);
//        SessionManager sessionManager = new SessionManager(test);
//        sessionManager.setPermission("Owner");
//        sessionManager.setRoomid("1");
//        sessionManager.setRoom("Room");
        when(test.getPermissionHome()).thenReturn("Viewer");

        test.setPermissionForTesting("Viewer");
        String temp = test.getPermissionHome();

        Assert.assertEquals("Viewer", temp);

        Assert.assertEquals(View.VISIBLE, test.getButtonVisibility());
        Assert.assertNotEquals(View.INVISIBLE, test.getButtonVisibility());
    }


    /**
     * Testing with subtasks
     */
    @Test
    public void SubtaskTest() throws JSONException{
        SubtaskActivity test = mock(SubtaskActivity.class);

        String contents = "Subtask Test Task";
        String day = "Today";
        JSONObject response = new JSONObject();

        response.put("contents", contents);
        response.put("dateCreate", day);
        when(test.jsonGetSubtask()).thenReturn(response);

        Assert.assertEquals(response.getString("contents"), test.jsonGetSubtask().getString("contents"));
        Assert.assertEquals(response.getString("dateCreate"), test.jsonGetSubtask().getString("dateCreate"));
        Assert.assertNotEquals(response.getString("contents"), test.jsonGetSubtask().getString("dateCreate"));
        Assert.assertNotEquals(response.getString("dateCreate"), test.jsonGetSubtask().getString("contents"));
    }
    /**
     * Testing for room settings
     */
    @Test
    public void RoomSettingsTest() throws JSONException{
        RoomSettingsActivity test = mock(RoomSettingsActivity.class);

        String user = "Paul";
        String permission = "Owner";

        JSONObject response = new JSONObject();

        response.put("User", user);
        response.put("Permission", permission);

        when(test.jsonGetRoomSettings()).thenReturn(response);

        Assert.assertEquals(response.getString("User"), test.jsonGetRoomSettings().getString("User"));
        Assert.assertEquals(response.getString("Permission"), test.jsonGetRoomSettings().getString("Permission"));
        Assert.assertNotEquals(response.getString("User"), test.jsonGetRoomSettings().getString("Permission"));
        Assert.assertNotEquals(response.getString("Permission"), test.jsonGetRoomSettings().getString("User"));
    }

    /**
     * Testing for inversion pattern on any session manager class
     */
    @Test
    public void TestInversionSession(){
        ISessionManagerInversionPattern test = mock(ISessionManagerInversionPattern.class); //mock for expected

        SharedPreferences mockPrefs = mock(SharedPreferences.class); //mock for actual
        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class); //second mock for actual

        Context mockContext = mock(Context.class); //mock for context

        Mockito.when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs); //sets mock prefs
        Mockito.when(mockContext.getSharedPreferences(anyString(), anyInt()).edit()).thenReturn(mockEditor); //sets mock editor

        ISessionManagerInversionPattern test2 = new SessionManager(mockContext); //mock for actual to test

        when(mockPrefs.getString("NAME", null)).thenReturn("1");
        when(test.getName()).thenReturn("testname");
        when(test.getEmail()).thenReturn("testemail"); //says never used but used below"?
        test.getEmail();
        when(test.getID()).thenReturn("1");
        when(test.getRoomid()).thenReturn("1");
        when(test.getPermission()).thenReturn("Owner");
        when(test.getRoom()).thenReturn("room");
        when(test.isInRoom()).thenReturn(true);
        when(test.isLoggin()).thenReturn(true);
        when(test.isRoom("room")).thenReturn(true);

        test2.createSession("testname", "testemail", "1");
        test2.setRoomid("1");
        test2.setRoom("room");
        test2.setPermission("Owner");
        test2.setName("testname");
        test2.setEmail("testemail");

        mockEditor.apply();
        mockEditor.commit();

        Assert.assertEquals("1", test.getRoomid());
        Assert.assertEquals("testemail", test.getEmail());
        Assert.assertEquals("1", test.getID());
        Assert.assertEquals("testname", test.getName());
        Assert.assertEquals("Owner", test.getPermission());
        Assert.assertEquals("room", test.getRoom());
        Assert.assertTrue(test.isInRoom());
        Assert.assertTrue( test.isLoggin());
        Assert.assertTrue(test.isRoom("room"));
        Assert.assertEquals("1",mockPrefs.getString("NAME", null));
    }

    /**
     * Testing for inversion for date class
     */
    @Test
    public void TestInversionDate(){
        IDateParserInversionPattern test = mock(DateParser.class); //mock for expected

        ScheduleActivity schedule = mock(ScheduleActivity.class);

        IDateParserInversionPattern test2 = new DateParser(14, 12, 1998); //mock for actual to test

        when(test.parseYear()).thenReturn("1998");
        when(test.parseMonth()).thenReturn("12");
        when(test.parseDay()).thenReturn("14");
        when(test.parseDate()).thenReturn("12/14/1998");

        when(schedule.callDateParser()).thenReturn("12/14/1998");
        schedule.calenderChange(14, 12, 1998);

        test2.setDay(14);
        test2.setMonth(11); //which is actually 12 because of the way calender change works it has to work like this<<<<---dont forget, important
                            //knowledge to find out while testing
        test2.setYear(1998);

        Assert.assertEquals(test.parseYear(), test2.parseYear());
        Assert.assertEquals(test.parseMonth(), test2.parseMonth());
        Assert.assertEquals(test.parseDay(), test2.parseDay());
        Assert.assertEquals(test.parseDate(), test2.parseDate());
        Assert.assertEquals(test.parseDate(), schedule.callDateParser());
    }

    /**
     * Testing the MVP Pattern stuff
     */
    @Test
    public void TestMVPPattern(){
        //IScheduleActivity test = mock(ScheduleActivity.class); //this isnt actually used because of the way MVP pattern works
                                                               //explanation below
        ISchedulePresenter presenter = mock(SchedulePresenter.class);
        IDateParserInversionPattern dateParser = mock(DateParser.class);

        IDateParserInversionPattern test2 = new DateParser(14, 12, 1998); //mock for actual to test


        when(presenter.callDataParser()).thenReturn("12/14/1998");
        when(dateParser.parseDate()).thenReturn("12/14/1998");
        when(dateParser.parseDay()).thenReturn("14");
        when(dateParser.parseMonth()).thenReturn("12");
        when(dateParser.parseYear()).thenReturn("1998");
        ///test class has nothing to test because it is all in the
        //other classes which is exactly how it should be. This means
        //that everything is much easier to test. ScheduleActivity does
        //not need to rely on anything to test. Using the presenter and data
        //classes I can test it, which is what makes it MVP Pattern



        Assert.assertEquals(presenter.callDataParser(), test2.parseDate());
        Assert.assertEquals(dateParser.parseDay(), test2.parseDay());
        Assert.assertEquals(dateParser.parseDate(), test2.parseDate());
        Assert.assertEquals(dateParser.parseMonth(), test2.parseMonth());
        Assert.assertEquals(dateParser.parseYear(), test2.parseYear());
    }
}
