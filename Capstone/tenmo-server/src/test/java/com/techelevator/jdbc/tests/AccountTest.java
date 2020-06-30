package com.techelevator.jdbc.tests;


import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;

import io.jsonwebtoken.lang.Assert;

public class AccountTest extends DAOIntegrationTest {
	
	private JdbcAccountDao accountDao;
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private Account testAccount;
	
	
	@BeforeAll
	public void setupTest() {
		dataSource = (DataSource) getDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);
		accountDao = new JdbcAccountDao(jdbcTemplate);
	
		}
	
	@Test
	public void getAccountByIdTest() {
		saveAccount(getTestAccount());
		
		
		Assert.isTrue(testAccount.equals(accountDao.getAccount(testAccount.getUserId())));
	}
	
	@Test 
	public void addBalanceToAccount() {
		saveAccount(getTestAccount());
		BigDecimal testBalance = testAccount.getBalance().add(BigDecimal.valueOf(200.00));
		accountDao.deposit(testAccount.getUserId(), BigDecimal.valueOf(200.00));
		Assert.isTrue(testBalance.equals(testAccount.getBalance()));
		
	}
	
	@Test
	public void subtractBalanceFromAccount() {
		saveAccount(getTestAccount());
		BigDecimal testBalance = testAccount.getBalance().subtract(BigDecimal.valueOf(200.00));
		accountDao.withdraw(testAccount.getUserId(), BigDecimal.valueOf(200.00));
		Assert.isTrue(testBalance.equals(testAccount.getBalance()));
		
	}
	
	
	
	private void saveAccount(Account account) {
		String insertSql = "INSERT INTO accounts ( user_id, balance) VALUES ( ?, ?) RETURNING account_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, account.getUserId(), account.getBalance());
		while (rowSet.next()) {
			account.setAccountId(rowSet.getInt("account_id"));
		}
		testAccount = account;
	}
	
	

	private Account getTestAccount() {
		Account account = new Account();
		account.setUserId(3);
		account.setBalance(BigDecimal.valueOf(1000.00));
		return account;
	}
}
