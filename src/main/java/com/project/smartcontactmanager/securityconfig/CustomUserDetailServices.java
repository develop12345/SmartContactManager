package com.project.smartcontactmanager.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.smartcontactmanager.dao.UserRepository;
import com.project.smartcontactmanager.entities.User;

public class CustomUserDetailServices implements UserDetailsService {

  
	@Autowired 
	private UserRepository ur;
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User u= ur.getUserByUserName(username);
		
		if(u==null) {
			throw new UsernameNotFoundException("Could not found user");
		}
		
		CustomUserDetails cu= new CustomUserDetails(u);
		return cu;
	}

}
