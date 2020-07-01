package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

	List<Transfer> viewTranserByUserAccountId(int userAccountId);
	
	Transfer sendMoney(int senderId, int recipientId, BigDecimal amountToTransfer);
	
	Transfer requestMoney(int senderId, int personAskingId, BigDecimal amountToTransfer);
	
	List<Transfer> viewPending(int userAccountId);
	
	void approveRequest(int transferId);
	
	void rejectRequest( int transferId);
	
	Transfer getTransferByTransferId(int transferId, int userAccountId);
}
