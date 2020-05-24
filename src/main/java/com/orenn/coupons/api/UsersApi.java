package com.orenn.coupons.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.User;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {
	
	@Autowired
	private UsersController usersController;
	
//	@PostMapping("/login")
//	public ValidLoginData login(@RequestBody UserLoginDetails userLoginDetails) throws ApplicationException {
//		return this.usersController.login(userLoginDetails);
//	}
	
	@PostMapping
	public void addUser(@RequestBody User user) throws ApplicationException {
		this.usersController.addUser(user);
	}
}
