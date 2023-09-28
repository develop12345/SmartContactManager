package com.project.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.smartcontactmanager.dao.ContactRepository;
import com.project.smartcontactmanager.dao.UserRepository;
import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;

@RestController
public class SearchController {
	
	@Autowired 
	private UserRepository ur;
	@Autowired
	private ContactRepository cr;

	@GetMapping("search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query, Principal p){
		
		User user= ur.getUserByUserName(p.getName());
		
		List<Contact> contacts= this.cr.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
