package com.mahesh.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahesh.security.jwt.JWTService;
import com.mahesh.security.model.Users;
import com.mahesh.security.repo.UserRepo;

@Service
public class UserService {

	private final UserRepo repo;

	private final AuthenticationManager manager;

	private final JWTService service;

	public UserService(UserRepo repo, AuthenticationManager manager, JWTService service) {
		this.repo = repo;
		this.manager = manager;
		this.service = service;
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	public Users register(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);

	}

	public void delUser(Integer id) {
		repo.deleteById(id);
	}

	//Generate token
	public String verify(Users user) {

		Authentication authenticate = manager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		if (authenticate.isAuthenticated()) {
			return service.generateToken(user.getUserName());
		}
		return "User Is Not Authenticated";
	}

}
