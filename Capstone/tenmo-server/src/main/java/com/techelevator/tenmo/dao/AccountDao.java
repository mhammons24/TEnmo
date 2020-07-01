package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

	Account getAccount( long userId);
	
	void withdraw( long userId, BigDecimal amount);
	
	void deposit( long userId, BigDecimal amount);
	
}
