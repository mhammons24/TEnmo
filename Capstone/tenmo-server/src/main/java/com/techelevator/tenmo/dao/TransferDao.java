package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

	List<Transfer> viewTranserByUserId(int userId);
	
	Transfer sendMoney(int senderId, int recipientId, BigDecimal amountToTransfer);
	
	Transfer requestMoney(int senderId, int personAskingId, BigDecimal amountToTransfer);
	
	List<Transfer> viewPending(int userId);
	
	Transfer approveRequest(int transferId);
	
	Transfer rejectRequest( int transferId);
}