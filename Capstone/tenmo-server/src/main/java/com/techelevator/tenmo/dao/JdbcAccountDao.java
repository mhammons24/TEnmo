package com.techelevator.tenmo.dao;

import java.math.BigDecimal;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
	@Override
	public Account getAccount(long userId) {
		String selectSql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, userId);
		Account account = null;
		while(rowSet.next() ) {
			account = mapToAccount(rowSet);
		}
		
		return account;
	}

	@Override
	public void withdraw(long userId, BigDecimal amount) {
		String updateSql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
		jdbcTemplate.update(updateSql, amount, userId);
		
		
	}

	@Override
	public void deposit(long userId, Account account) {
		String updateSql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		jdbcTemplate.update(updateSql, account.getBalance(), userId);	
		
	}
	
	private Account mapToAccount(SqlRowSet rowSet) {
		Account account = new Account();
		
		account.setAccountId(rowSet.getInt("account_id"));
		account.setUserId(rowSet.getInt("user_id"));
		account.setBalance(rowSet.getDouble("balance"));
		return account;
	}



}
