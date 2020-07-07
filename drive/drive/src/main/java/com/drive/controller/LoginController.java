package com.drive.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.drive.model.File;
import com.drive.model.User;
import com.drive.repository.FileRepository;
import com.drive.service.FileService;
import com.drive.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	FileService fileService;
	@Autowired
	FileRepository fileRepo;

	@GetMapping(value = "/")
	public String home() {
		return "index";
	}

	@GetMapping(value = { "/login" })
	public String login() {
		return "login";
	}

	@GetMapping(value = "/sign-up")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "signup";

	}

	@PostMapping(value = "/sign-up")
	public String createnewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByUserName(user.getUsername());
		if (bindingResult.hasErrors()) {
			 
			return "signup";
		}
		if(userExists!=null)
		{
			model.addAttribute("userExists" ,"userExist");
			return "signup";
			
		}
		else {

			userService.saveUser(user);
			model.addAttribute("user", new User());
			model.addAttribute("successMessage", "Registered successfully");
		}
		return "signup";

	}

	@GetMapping(value = "/home") // changed
	public String home(Model model)

	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		Set<File> userfile = fileService.getFileByUser(user.getId());
		model.addAttribute("user", user);
		model.addAttribute("userfile", userfile); // add in home for files
												// download available
		model.addAttribute("username", "Welcome to Doodle Drive " + user.getUsername());
		return "file/home";
	}
	
	
}
