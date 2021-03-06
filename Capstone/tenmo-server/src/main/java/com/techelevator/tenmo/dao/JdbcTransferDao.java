package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {

	 private JdbcTemplate jdbcTemplate;


	    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }

	
	@Override
	public List<Transfer> viewTranserByUserAccountId(int userAccountId) {
		List<Transfer> transferList = new ArrayList<Transfer>();
		String selectSql = "SELECT transfers.transfer_id, transfer_type_desc AS transfer_type, transfer_status_desc AS transfer_status, account_from, account_to, amount FROM transfers \n" + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id\n" + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id\n" + 
				"WHERE account_from = ? OR account_to = ?";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transferList.add(mapRowToTransfer(rowSet));
		}
		
		return transferList;
	}

	@Override
	public Transfer sendMoney(int senderId, int recipientId, double amountToTransfer) {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId(senderId);
		transfer.setAccountToId(recipientId);
		transfer.setAmountTransferred(amountToTransfer);
		transfer.setTransferStatus("Approved");
		transfer.setTransferType("Send");
		String insertSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES ( 2, 2, ?, ?, ?) RETURNING transfer_id";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(insertSql, senderId, recipientId, amountToTransfer);
		
		while (rowSet.next()) {
			transfer.setTransferId(rowSet.getInt("transfer_id"));
		}
		
		return transfer;
	}

	@Override
	public Transfer requestMoney(int senderId, int personAskingId, double amountToTransfer) {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId(senderId);
		transfer.setAccountToId(personAskingId);
		transfer.setAmountTransferred(amountToTransfer);
		transfer.setTransferStatus("Pending");
		transfer.setTransferType("Request");
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
		String selectSql = "SELECT transfers.transfer_id, transfer_type_desc AS transfer_type, transfer_status_desc AS transfer_status, account_from, account_to, amount FROM transfers \n" + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id\n" + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id\n" + 
				"WHERE (account_from = ? OR account_to = ?) AND transfers.transfer_status_id = 1";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transferList.add(mapRowToTransfer(rowSet));
		}
		
		return transferList;
	}

	@Override
	public void approveRequest(int transferId) {

		String updateSql = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		jdbcTemplate.update(updateSql, transferId);

	}

	@Override
	public void rejectRequest(int transferId) {
		
		String updateSql = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		jdbcTemplate.update(updateSql, transferId);
		
		
	}


	@Override
	public Transfer getTransferByTransferId(int transferId, int userAccountId) {
		Transfer transfer = new Transfer();
		String selectSql ="SELECT transfers.transfer_id, transfer_type_desc AS transfer_type, transfer_status_desc AS transfer_status, account_from, account_to, amount FROM transfers \n" + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id\n" + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id\n" + 
				"WHERE transfer_id = ? AND (account_from = ? OR account_to = ?)";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, transferId, userAccountId, userAccountId);
		
		while (rowSet.next()) {
			transfer = mapRowToTransfer(rowSet);
		}
		
		return transfer;
	}
	
	
	private Transfer mapRowToTransfer(SqlRowSet rowSet) {
		Transfer transfer = new Transfer();
		transfer.setTransferType(rowSet.getString("transfer_type"));
		transfer.setTransferStatus(rowSet.getString("transfer_status"));
		transfer.setAccountFromId(rowSet.getInt("account_from"));
		transfer.setAccountToId(rowSet.getInt("account_to"));
		transfer.setAmountTransferred(rowSet.getDouble("amount"));
		transfer.setTransferId(rowSet.getInt("transfer_id"));
		
		return transfer;
	}



}
