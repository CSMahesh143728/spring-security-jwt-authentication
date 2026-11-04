package com.mahesh.security.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahesh.security.model.Users;
import com.mahesh.security.repo.UserRepo;

@Service
public class UserService {

	private final UserRepo repo;

	public UserService(UserRepo repo) {
		this.repo = repo;
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	public Users register(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);

	}

	public void delUser(Integer id) {
		// TODO Auto-generated method stub
		 repo.deleteById(id);
	}

}
