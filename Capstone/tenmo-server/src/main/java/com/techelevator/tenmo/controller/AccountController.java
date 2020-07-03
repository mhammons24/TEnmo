package com.techelevator.tenmo.controller;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
	
	private String baseUrl = "http://localhost:8080";
	private AccountDao accountDao;
	
	public AccountController(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
	public Account getAccount(@PathVariable("id") long userId) {
		return accountDao.getAccount(userId);
		
	}
	
	@RequestMapping(path = "/accounts/{id}/deposits", method = RequestMethod.PUT)
	public void addMoney(@RequestBody Account account, @PathVariable("id") long userId ) {
		accountDao.deposit(userId, account);
	}
	
	@RequestMapping(path = "/accounts/{id}/withdraws", method = RequestMethod.PUT)
	public void giveMoney(@PathVariable("id") long userId, BigDecimal amountToAdd) {
		accountDao.withdraw(userId, amountToAdd);
	}
	

}
