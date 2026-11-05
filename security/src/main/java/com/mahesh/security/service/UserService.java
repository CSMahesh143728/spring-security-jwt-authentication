package com.mahesh.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahesh.security.model.Users;
import com.mahesh.security.repo.UserRepo;

@Service
public class UserService {

	private final UserRepo repo;

	private final AuthenticationManager manager;

	public UserService(UserRepo repo, AuthenticationManager manager) {
		this.repo = repo;
		this.manager = manager;
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	public Users register(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);

	}

	public void delUser(Integer id) {
		repo.deleteById(id);
	}

	public String verify(Users user) {

		Authentication authenticate = manager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		if (authenticate.isAuthenticated()) {
			return "SUccess";
		} else
			return "Fail";

	}

}
