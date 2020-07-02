package com.techelevator.jdbc.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;



public class JdbcAccountTest {
	
	private static JdbcAccountDao accountDao;
	private static JdbcTemplate jdbcTemplate;
	private static SingleConnectionDataSource dataSource;
	private static UserSqlDAO userDao;
	private User testUser;

	
	@BeforeAll
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/*
		 * The following line disables autocommit for connections returned by this
		 * DataSource. This allows us to rollback any changes after each test
		 */
		dataSource.setAutoCommit(false);
	}

	/*
	 * After all tests have finished running, this method will close the DataSource
	 */
	@AfterAll
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	/*
	 * After each test, we rollback any changes that were made to the database so
	 * that everything is clean for the next test
	 */
	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@BeforeEach
	public void setupTest() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		accountDao = new JdbcAccountDao(jdbcTemplate);
		userDao = new UserSqlDAO(jdbcTemplate);
		userDao.create("testuser", "testuser");
		testUser = userDao.findByUsername("testuser");
		
		
	
		}
	
	@Test
	public void getAccountByIdTest() {
		Account testAccount = getTestAccount();
		saveAccount(testAccount);
		
		
		assertEquals(testAccount, accountDao.getAccount(testAccount.getUserId()));
	}
	
	@Test 
	public void addBalanceToAccount() {
		Account testAccount = getTestAccount();
		saveAccount(testAccount);
		double testBalance = testAccount.getBalance();
		accountDao.deposit(testAccount.getUserId(), testAccount);
		testAccount = accountDao.getAccount(testAccount.getUserId());
		assertEquals(testBalance, testAccount.getBalance());
		
	}
	
	@Test
	public void subtractBalanceFromAccount() {
		Account testAccount = accountDao.getAccount(testUser.getId());
		double expectedBalance = 800.00;
		accountDao.withdraw(testAccount.getUserId(), BigDecimal.valueOf(200.00));
		testAccount = accountDao.getAccount(testAccount.getUserId());
		assertEquals(expectedBalance, testAccount.getBalance());
		
	}
	
	
	
	private void saveAccount(Account account) {
		String insertSql = "INSERT INTO accounts ( user_id, balance) VALUES ( ?, ?) RETURNING account_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, account.getUserId(), account.getBalance());
		while (rowSet.next()) {
			account.setAccountId(rowSet.getInt("account_id"));
			
		}
	}
	
	

	private Account getTestAccount() {
		Account account = new Account();
		account.setUserId(3);
		account.setBalance(1030.00);
		return account;
	}
}
