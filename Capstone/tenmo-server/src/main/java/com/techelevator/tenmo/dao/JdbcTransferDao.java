package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;
@Component
public class JdbcTransferDao implements TransferDao {

	 private JdbcTemplate jdbcTemplate;

	    public void JdbcAccountDao(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }
	
	
	
	@Override
	public List<Transfer> viewTranserByUserAccountId(int userAccountId) {
		List<Transfer> transferList = new ArrayList<Transfer>();
		String selectSql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE account_from = ? OR account_to = ?";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transferList.add(mapRowToTransfer(rowSet));
		}
		
		return transferList;
	}

	@Override
	public Transfer sendMoney(int senderId, int recipientId, BigDecimal amountToTransfer) {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId(senderId);
		transfer.setAccountToId(recipientId);
		transfer.setAmountTransferred(amountToTransfer);
		transfer.setTransferStatusId(2);
		transfer.setTransferTypeId(2);
		String insertSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES ( 2, 2, ?, ?, ?) RETURNING transfer_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, senderId, recipientId, amountToTransfer);
		
		while (rowSet.next()) {
			transfer.setTransferId(rowSet.getInt("transfer_id"));
		}
		
		return transfer;
	}

	@Override
	public Transfer requestMoney(int senderId, int personAskingId, BigDecimal amountToTransfer) {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId(senderId);
		transfer.setAccountToId(personAskingId);
		transfer.setAmountTransferred(amountToTransfer);
		transfer.setTransferStatusId(1);
		transfer.setTransferTypeId(1);
		String insertSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES ( 1, 1, ?, ?, ?) RETURNING transfer_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, senderId, personAskingId, amountToTransfer);
		
		while (rowSet.next()) {
			transfer.setTransferId(rowSet.getInt("transfer_id"));
		}
		return transfer;
	}

	@Override
	public List<Transfer> viewPending(int userAccountId) {
		List<Transfer> transferList = new ArrayList<Transfer>();
		String selectSql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE (account_from = ? OR account_to = ?) AND transfer_type_id = 1";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transferList.add(mapRowToTransfer(rowSet));
		}
		
		return transferList;
	}

	@Override
	public void approveRequest(int transferId) {
		//Transfer transfer = new Transfer();
		String updateSql = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		jdbcTemplate.update(updateSql, transferId);
//		String selectSql ="SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ?)";
//		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, transferId);
//		
//		while (rowSet.next()) {
//			transfer = mapRowToTransfer(rowSet);
//		}
		
		//return transfer;
	}

	@Override
	public void rejectRequest(int transferId) {
		
		String updateSql = "UPDATE transfers SET transfer_status_id = 0 WHERE transfer_id = ?";
		jdbcTemplate.update(updateSql, transferId);
		
		
	}


	@Override
	public Transfer getTransferByTransferId(int transferId, int userAccountId) {
		Transfer transfer = new Transfer();
		String selectSql ="SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ? AND (account_from = ? OR account_to = ?)";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, transferId, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transfer = mapRowToTransfer(rowSet);
		}
		
		return transfer;
	}
	
	
	private Transfer mapRowToTransfer(SqlRowSet rowSet) {
		Transfer transfer = new Transfer();
		transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
		transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
		transfer.setAccountFromId(rowSet.getInt("account_from"));
		transfer.setAccountToId(rowSet.getInt("account_to"));
		transfer.setAmountTransferred(rowSet.getBigDecimal("amount"));
		transfer.setTransferId(rowSet.getInt("transfer_id"));
		
		return transfer;
	}



}
