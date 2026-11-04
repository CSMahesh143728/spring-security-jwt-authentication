package com.mahesh.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mahesh.security.model.Users;
import com.mahesh.security.repo.UserRepo;



@Service
public class MyUserDetailsService implements UserDetailsService {

	//Constructor Injection
	private final UserRepo repo;

	public MyUserDetailsService(UserRepo userRepo) {
		this.repo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    System.out.println("STEP 1: Received username: " + username);

		Users byUserName = repo.findByUserNameIgnoreCase(username);

		if (byUserName == null) {
			  System.out.println("STEP 2: User not found");
			throw new UsernameNotFoundException("User Not Found"+byUserName);
		}
		 System.out.println("STEP 2: User found successfully");

		return new UserPrincipal(byUserName);
	}

}
