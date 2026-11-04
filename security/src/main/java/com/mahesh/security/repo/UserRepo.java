package com.mahesh.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahesh.security.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

	Users findByUserNameIgnoreCase(String username);

}
