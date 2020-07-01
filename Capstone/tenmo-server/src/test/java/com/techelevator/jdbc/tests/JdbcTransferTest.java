package com.techelevator.jdbc.tests;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;


public class JdbcTransferTest {
	private static JdbcTransferDao transferDao;
	private static JdbcTemplate jdbcTemplate;
	private static SingleConnectionDataSource dataSource;
	private static UserSqlDAO userDao;
	private User testUser1;
	private User testUser2;
	private AccountDao accountDao;

	
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
		transferDao = new JdbcTransferDao(jdbcTemplate);
		userDao = new UserSqlDAO(jdbcTemplate);
		userDao.create("testuser1", "testuser1");
		userDao.create("testuser2", "testuser2");
		accountDao = new JdbcAccountDao(jdbcTemplate);
		testUser1 = userDao.findByUsername("testuser1");
		testUser2 = userDao.findByUsername("testuser2");

	}
	
	@Test
	public void view_transfer_by_account_to_id() {

		Transfer testTransfer = makeATestTransfer();
		saveTransfer(testTransfer);
		List<Transfer> transfers = transferDao.viewTranserByUserAccountId(accountDao.getAccount(testUser2.getId()).getAccountId());
		Assertions.assertEquals(1, transfers.size());
	}
	
	@Test
	public void view_transfer_by_account_from_id() {

		Transfer testTransfer = makeATestTransfer();
		saveTransfer(testTransfer);
		List<Transfer> transfers = transferDao.viewTranserByUserAccountId(accountDao.getAccount(testUser1.getId()).getAccountId());
		Assertions.assertEquals(1, transfers.size());
	}
	
	@Test 
	public void sending_money_approved_status() {
		Transfer transfer = transferDao.sendMoney(accountDao.getAccount(testUser1.getId()).getAccountId(),accountDao.getAccount(testUser2.getId()).getAccountId(), BigDecimal.valueOf(30).setScale(2));
		Assertions.assertEquals(BigDecimal.valueOf(30).setScale(2), transfer.getAmountTransferred());
		Assertions.assertEquals(2, transfer.getTransferStatusId());
		Assertions.assertEquals(2, transfer.getTransferTypeId());
		Assertions.assertEquals(accountDao.getAccount(testUser1.getId()).getAccountId(), transfer.getAccountFromId());
		Assertions.assertEquals(accountDao.getAccount(testUser2.getId()).getAccountId(), transfer.getAccountToId());
	}
	
	@Test 
	public void request_money() {
		Transfer transfer = transferDao.requestMoney(accountDao.getAccount(testUser1.getId()).getAccountId(),accountDao.getAccount(testUser2.getId()).getAccountId(), BigDecimal.valueOf(30).setScale(2));
		Assertions.assertEquals(BigDecimal.valueOf(30).setScale(2), transfer.getAmountTransferred());
		Assertions.assertEquals(1, transfer.getTransferStatusId());
		Assertions.assertEquals(1, transfer.getTransferTypeId());
		Assertions.assertEquals(accountDao.getAccount(testUser1.getId()).getAccountId(), transfer.getAccountFromId());
		Assertions.assertEquals(accountDao.getAccount(testUser2.getId()).getAccountId(), transfer.getAccountToId());
	}
	
	@Test
	public void view_pending_from_id() {
		Transfer testTransfer = makeATestTransfer();
		saveTransfer(testTransfer);
		List<Transfer> transfers = transferDao.viewPending(accountDao.getAccount(testUser1.getId()).getAccountId());
		Assertions.assertEquals(1, transfers.size());
	}
	
	@Test
	public void view_pending_to_id() {
		Transfer testTransfer = makeATestTransfer();
		saveTransfer(testTransfer);
		List<Transfer> transfers = transferDao.viewPending(accountDao.getAccount(testUser2.getId()).getAccountId());
		Assertions.assertEquals(1, transfers.size());
	}
	
	@Test
	public void reject_transfer() {
		Transfer firstTransfer = makeATestTransfer();
		saveTransfer(firstTransfer);
		transferDao.rejectRequest(firstTransfer.getTransferId());
		Transfer transfer = transferDao.getTransferByTransferId(firstTransfer.getTransferId(), accountDao.getAccount(testUser2.getId()).getAccountId());
		Assertions.assertEquals(BigDecimal.valueOf(30).setScale(2), transfer.getAmountTransferred());
		Assertions.assertEquals(3, transfer.getTransferStatusId());
		Assertions.assertEquals(1, transfer.getTransferTypeId());
		Assertions.assertEquals(accountDao.getAccount(testUser1.getId()).getAccountId(), transfer.getAccountFromId());
		Assertions.assertEquals(accountDao.getAccount(testUser2.getId()).getAccountId(), transfer.getAccountToId());
	}
	
	@Test
	public void approve_transfer() {
		Transfer firstTransfer = makeATestTransfer();
		saveTransfer(firstTransfer);
		transferDao.approveRequest(firstTransfer.getTransferId());
		Transfer transfer = transferDao.getTransferByTransferId(firstTransfer.getTransferId(), accountDao.getAccount(testUser2.getId()).getAccountId());
		Assertions.assertEquals(BigDecimal.valueOf(30).setScale(2), transfer.getAmountTransferred());
		Assertions.assertEquals(2, transfer.getTransferStatusId());
		Assertions.assertEquals(1, transfer.getTransferTypeId());
		Assertions.assertEquals(accountDao.getAccount(testUser1.getId()).getAccountId(), transfer.getAccountFromId());
		Assertions.assertEquals(accountDao.getAccount(testUser2.getId()).getAccountId(), transfer.getAccountToId());
	}

	private void saveTransfer(Transfer transfer) {
		String insertSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, transfer.getTransferTypeId(),
				transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(),
				transfer.getAmountTransferred());
		while (rowSet.next()) {
			transfer.setTransferId(rowSet.getInt("transfer_id"));
		}
	}

	private Transfer makeATestTransfer() {

		Transfer transfer = new Transfer();

		transfer.setAccountFromId(accountDao.getAccount(testUser1.getId()).getAccountId());
		transfer.setAccountToId(accountDao.getAccount(testUser2.getId()).getAccountId());
		transfer.setAmountTransferred(BigDecimal.valueOf(30).setScale(2));
		transfer.setTransferTypeId(1);
		transfer.setTransferStatusId(1);
		return transfer;
	}
}
