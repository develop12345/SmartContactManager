package com.project.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.smartcontactmanager.dao.ContactRepository;
import com.project.smartcontactmanager.dao.UserRepository;
import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;
import com.project.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller 
@RequestMapping("/user")
public class UserController {
	
	@Autowired 
	UserRepository ur;
	
	@Autowired
	ContactRepository cr;

	@ModelAttribute
	public void CommonDetails(Model m, Principal p) {
		User u= ur.getUserByUserName(p.getName());
		m.addAttribute("user", u);
		m.addAttribute("title","Welcome-"+u.getName());
	}
	
	@RequestMapping("/index")
	public String UserDashboard(Model m, Principal p) {
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add_contact")
	public String AddContact(Model m, Principal p) {
		m.addAttribute("contact", new Contact());
		m.addAttribute("title", "Add Contact");
		return "normal/add-contact";
	}
	
	@PostMapping("/process_contact")
	public String ProcessContact(@ModelAttribute Contact c, @RequestParam("profile_image") MultipartFile file, Principal p, HttpSession session) {
		try {
			String n= p.getName();
			User u= ur.getUserByUserName(n);
			
			
	       if(file==null) {
	    	 c.setImage("cont.png");
	       }
	       else {
	    	   c.setImage(""+file.getOriginalFilename());
	    	   File saveFile= new ClassPathResource("static/img").getFile();
	    	   
	    	   Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
	    	   Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

//	    	   System.out.print("done");    
	       }
	       
	       u.getContacts().add(c);
			c.setUser(u);
	       this.ur.save(u);
    	   session.setAttribute("message", new Message("Contact Added Successfully !!!", "alert-success"));
	       
		}
		catch(Exception e){
		e.printStackTrace();
 	     session.setAttribute("message", new Message("Something went wrong !! Try again", "alert-danger"));
		}
		return "normal/add-contact";
	}
	
	
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page,Model m, Principal p) {
		m.addAttribute("title", "Your Contacts-SCM");
		User u= ur.getUserByUserName(p.getName());
		
		//Current page
		//Contact per page
		Pageable pageable= PageRequest.of(page, 5);		
		Page<Contact> c= this.cr.findContactByUser(u.getId(), pageable);
		m.addAttribute("contact",c);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", c.getTotalPages());
		return "normal/contactspage";
	}
	
	@GetMapping("/{cId}/contact")
	public String ShowDetails(@PathVariable("cId")Integer cId, Model m, Principal p){
		
		Optional<Contact> contactOptional= this.cr.findById(cId);
		Contact contact= contactOptional.get();
		
		User user= ur.getUserByUserName(p.getName());
	
		
		if(user.getId()==contact.getUser().getId())		
		m.addAttribute("contact",contact);
		
		return "normal/contact_details";
		}
	
	@GetMapping("/delete/{cId}")
	public String DeleteContact(@PathVariable("cId")Integer cId, Model m, Principal p, HttpSession s ) {
		Optional<Contact> contactOptional= this.cr.findById(cId);
		Contact contact= contactOptional.get();
		
        User user= ur.getUserByUserName(p.getName());
	
		
		if(user.getId()==contact.getUser().getId())	{
			
			s.setAttribute("message", new Message("Contact Deleted Successfully !!!!!!!", "alert-success"));
			cr.deleteContactById(cId);
		}
	
		return "redirect:/user/show-contacts/0";
	}
	
	//Open update form
	@PostMapping("/update/{cId}")
	public String UpdateContact(@PathVariable("cId")Integer cId, Model m, Principal p, HttpSession session) {
		m.addAttribute("title","update-contact");
		Optional<Contact> contactOptional= this.cr.findById(cId);
		Contact contact= contactOptional.get();
		m.addAttribute("contact", contact);
		return "normal/updatePage";
	}
	
	//Process update after update form
	@PostMapping("/process_update")
	public String ProcessUpdate(@ModelAttribute Contact contact, @RequestParam("profile_image")MultipartFile file, Model m, HttpSession session, Principal p) {
		try {
			
			//get old contact
			Contact oldContact= this.cr.findByLongId(contact.getcId());
			
			
			
			if(!file.isEmpty()) {
				//rewrite file
				//delete old photo
				
//				File deleteFile= new ClassPathResource("static/img").getFile();
//				File file1= new File(deleteFile, oldContact.getImage());
//				file1.delete();
//			
				  
				//add new photo
				
				
				File saveFile= new ClassPathResource("static/img").getFile();
		    	   
		    	Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		    	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());
			}
			else {
			contact.setImage(oldContact.getImage());}
			User user= this.ur.getUserByUserName(p.getName());
			contact.setUser(user);
			this.cr.save(contact);
			
			session.setAttribute("message", new Message("Your Contact is Updated...","alert-success"));
			
		} catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Unable to update...","alert-danger"));
			
		}
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("title", "Your Profile-SCM");
		return "normal/profile";
	}
}
