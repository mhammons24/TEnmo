package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private int transferId;
	private String transferType;
	private String transferStatus;
	private int accountFromId;
	private int accountToId;
	private double amountTransferred;
	
	public int getTransferId() {
		return transferId;
	}
	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	public int getAccountFromId() {
		return accountFromId;
	}
	public void setAccountFromId(int accountFromId) {
		this.accountFromId = accountFromId;
	}
	public int getAccountToId() {
		return accountToId;
	}
	public void setAccountToId(int accountToId) {
		this.accountToId = accountToId;
	}
	public double getAmountTransferred() {
		return amountTransferred;
	}
	public void setAmountTransferred(double amountTransferred) {
		this.amountTransferred = amountTransferred;
	}
	

	
	
}
