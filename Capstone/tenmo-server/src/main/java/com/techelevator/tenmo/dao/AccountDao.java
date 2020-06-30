package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

	Account getAccount( int userId);
	
	void withdraw( int user );
	
	void deposit( int user );
	
}
