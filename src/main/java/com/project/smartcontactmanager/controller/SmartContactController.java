package com.project.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.smartcontactmanager.dao.UserRepository;
import com.project.smartcontactmanager.entities.User;

@Controller
public class SmartContactController {
	
	@Autowired
	private UserRepository ur;
	
	@GetMapping("/te")
	@ResponseBody
	public String test() {
		
		User user= new User();
		user.setName("Durgesh");
		user.setEmail("durgesh@23.com");
		ur.save(user);
		return "working";
	}

}
