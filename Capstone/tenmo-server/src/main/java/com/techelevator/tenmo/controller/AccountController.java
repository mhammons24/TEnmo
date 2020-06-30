package com.techelevator.tenmo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;

@RestController
public class AccountController {
	
	private String baseUrl = "http://localhost:8080";
	private AccountDao accountDao;
	
	public AccountController(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
	public Account getAccount(@PathVariable("id") int userId) {
		return accountDao.getAccount(userId);
		
	}
	

}
