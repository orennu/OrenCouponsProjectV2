package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.SuccessfulLoginData;
import com.orenn.coupons.beans.User;
import com.orenn.coupons.beans.UserLoginData;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {
	
	@Autowired
	private UsersController usersController;
	
	@PostMapping("/login")
	@ResponseBody
	public SuccessfulLoginData login(@RequestBody UserLoginData userLoginData) throws ApplicationException {
		return this.usersController.login(userLoginData);
	}
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> addUser(@RequestBody User user) throws ApplicationException {
		//TODO is this suppose to go to controller? if so how to return correct http code?
		if (user.getType().name() == "CUSTOMER") {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		this.usersController.addUser(user);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public User getUserById(@PathVariable("id") long id) throws ApplicationException {
		return this.usersController.getUserById(id);
	}
	
	@GetMapping(value = "/search", params = "userName")
	@ResponseBody
	public User getUserByUserName(@RequestParam("userName") String userName) throws ApplicationException {
		return this.usersController.getUserByUserName(userName);
	}
	
	@GetMapping
	@ResponseBody
	public List<User> getAllUsers() throws ApplicationException {
		return this.usersController.getAllUsers();
	}
	
	@GetMapping(value = "/search", params = "companyId")
	@ResponseBody
	public List<User> getUsersByCompany(@RequestParam("companyId") Long companyId) throws ApplicationException {
		return this.usersController.getUsersByCompany(companyId);
	}
	
	@GetMapping(value = "/search", params = "type")
	@ResponseBody
	public List<User> getUsersByType(@RequestParam("type") String userTypeStr) throws ApplicationException {
		return this.usersController.getUsersByType(userTypeStr);
	}
	
	@PutMapping
	@ResponseBody
	public ResponseEntity<Object> updateUser(@RequestBody User user) throws ApplicationException {
		this.usersController.updateUser(user);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}", params = "lock")
	@ResponseBody
	public ResponseEntity<Object> lockUser(@PathVariable("id") long id, @RequestParam("lock") boolean lockUser) throws ApplicationException {
		this.usersController.lockUser(id, lockUser);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> removeUser(@PathVariable("id") long id) throws ApplicationException {
		this.usersController.removeUser(id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@DeleteMapping(params = "companyId")
	@ResponseBody
	public ResponseEntity<Object> removeUsersByCompany(@RequestParam("companyId") Long companyId) throws ApplicationException {
		this.usersController.removeUsersByCompany(companyId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
}
