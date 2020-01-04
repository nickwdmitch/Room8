package com.database.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller for all user endpoints.
 * 
 * @author Nickolas Mitchell, Thane Storley
 */
@RestController
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * Login.
	 * 
	 * This method checks if the user is inside of the database and the correct
	 * password is provided. If not then a response of "Incorrect Password" is
	 * returned. If the user is within the database and has entered the correct
	 * password then a success response is returned.
	 * 
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
	public String attemptLogin(@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		String Email = body.getString("Email");
		String Password = body.getString("Password");
		List<User> userList = userService.getUsers();
		for (User user : userList) {
			if (user.getEmail().equals(Email)) {
				if (user.getPassword().equals(Password)) {
					System.out.println("{\"Response\":\"Success\", \"Username\":\"" + user.getName()
							+ "\",\"UserId\":\"" + user.getId() + "\"}");
					return "{\"Response\":\"Success\", \"Username\":\"" + user.getName() + "\",\"UserId\":\""
							+ user.getId() + "\"}";
				} else
					return "{\"Response\":\"Incorrect Password\"}";
			}
		}
		return "{\"Response\":\"User Does Not Exist\"}";
	}

	/**
	 * Registration.
	 * 
	 * This method adds a user to the database. If the entered email or name is
	 * already within the database, then a response of "Email Already In Use" or
	 * "Name Already In Use" respectively. Otherwise a success response is returned
	 * and the user is added to the database.
	 * 
	 * @param item
	 * @return
	 */
	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	public String createUser(@RequestBody String item) {
		JSONObject body = new JSONObject(item);
		String Name = body.getString("Name");
		String Email = body.getString("Email");
		String Password = body.getString("Password");
		List<User> userList = userService.getUsers();
		for (User user : userList) {
			if (user.getName().equals(Name))
				return "{\"Response\":\"Name Already In Use\"}";
			if (user.getEmail().equals(Email))
				return "{\"Response\":\"Email Already In Use\"}";
		}
		userService.addUser(new User(Name, Email, Password));
		return "{\"Response\":\"Success\"}";
	}
	
	@PostMapping(path = "/updateuser/{user}", consumes = "application/json", produces = "application/json")
	public String updateUser(@RequestBody String item, @PathVariable String user) {
		JSONObject body = new JSONObject(item);
		String Name = null;
		String Email = null;
		String CurrentPassword = null;
		String Password = null;
		Long userId = Long.valueOf(user);
		String ret = "";
		
		try {
			Name = body.getString("Name");
		} catch(JSONException e) {}
		try {
			Email = body.getString("Email");
		} catch(JSONException e) {}
		try {
			CurrentPassword = body.getString("CurrentPassword");
			Password = body.getString("Password");
		} catch(JSONException e) {}

		Optional<User> toChange = userService.findById(userId);
		User changeTemp = null;
		try {
			changeTemp = toChange.get();
		} catch (NoSuchElementException e) {
			return "{\"Response\":\"No such user exists\"}";
		}
		
		
		
		if(Name!=null) {
			userService.updateUsername(Name, userId);
			ret += "Name has been updated.";
		}
		else
			ret += "Name was left null.";
		if(Email!=null) {
			userService.updateUserEmail(Email, userId);
			ret += "Email has been updated.";
		}
		else
			ret += "Email was left null.";
		if(CurrentPassword!=null && Password!=null) {
			if(CurrentPassword.equals(changeTemp.getPassword())) {
				userService.updateUserPassword(Password, userId);
				ret += "Password has been updated.";
			}
			else
				ret += "Current password was incorrect, Passward was left unchanged.";
		}
		else
			ret += "Must provide both Current Password and Password.";
		
		return "{\"Response\":\"Success\", \"Details\":\"" + ret + "\"}";
		
	}
}
