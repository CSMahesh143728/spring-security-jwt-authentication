package com.mahesh.security.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mahesh.security.model.Users;
import com.mahesh.security.service.UserService;

@RestController
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping("/registeruser")
	public Users registerUser(@RequestBody Users user) {
		return service.register(user);
	}
	
	@DeleteMapping("/deleteuser/{id}")
	public String deleteUser(@PathVariable Integer id) {
		 service.delUser(id);
		 return "User Deleted Succesfully";
		
	}

}
