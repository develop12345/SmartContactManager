package com.project.smartcontactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.smartcontactmanager.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("Select u from User u where u.email= :email")
	public User getUserByUserName(@Param("email") String email);
}
