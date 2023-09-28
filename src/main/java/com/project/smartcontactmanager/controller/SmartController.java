package com.project.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.smartcontactmanager.dao.UserRepository;
import com.project.smartcontactmanager.entities.User;
import com.project.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SmartController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository ur;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-SCM");
		return "home";
	}
	
	@GetMapping("/about")
	public String About(Model model) {
		model.addAttribute("title", "About-SCM");
		return "about";
	}
	
	@GetMapping("/signup")
	public String Signup(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "Register-SCM");
		return "register";
	}
	
	// handling register data
	@PostMapping("/do_register")
	public String RegisterUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
		try {
		
		
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User u= ur.save(user);
		System.out.println(u);
		session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
		return "register";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!"+e.getMessage(), "alert-danger"));
			return "register";
		}
	
	}
	
	//handling custom login
	@RequestMapping("/login")
	public String CustomLogin(Model m) {
		m.addAttribute("title", "Login-SCM");
		return "/login";
	}
}
