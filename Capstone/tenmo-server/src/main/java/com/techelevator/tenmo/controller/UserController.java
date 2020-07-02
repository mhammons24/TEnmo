package com.techelevator.tenmo.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;

@RestController
public class UserController {
	
	private String baseUrl = "http://localhost:8080";
	private UserDAO userDao;
	
	public UserController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> listAllUsers() {
		List<User> users = userDao.findAll();
		return users;
		
	}
	
	@RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
	public User findUserByName(@PathVariable String username) {
		return userDao.findByUsername(username);
	}
	
	@RequestMapping(path = "/users/{username}/id", method = RequestMethod.GET)
	public int findUserIdByName(@PathVariable String username) {
		return userDao.findIdByUsername(username);
	}
	
	@RequestMapping(path = "/accounts/{id}/users", method = RequestMethod.GET)
	public User findUserByAccountId(@PathVariable("id") int accountId) {
		return userDao.findUserByAccountId(accountId);
	}
}
