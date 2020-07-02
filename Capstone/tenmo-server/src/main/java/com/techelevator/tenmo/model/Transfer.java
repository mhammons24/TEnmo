package com.techelevator.tenmo.model;

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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountFromId;
		result = prime * result + accountToId;
		long temp;
		temp = Double.doubleToLongBits(amountTransferred);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + transferId;
		result = prime * result + ((transferStatus == null) ? 0 : transferStatus.hashCode());
		result = prime * result + ((transferType == null) ? 0 : transferType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transfer other = (Transfer) obj;
		if (accountFromId != other.accountFromId)
			return false;
		if (accountToId != other.accountToId)
			return false;
		if (Double.doubleToLongBits(amountTransferred) != Double.doubleToLongBits(other.amountTransferred))
			return false;
		if (transferId != other.transferId)
			return false;
		if (transferStatus == null) {
			if (other.transferStatus != null)
				return false;
		} else if (!transferStatus.equals(other.transferStatus))
			return false;
		if (transferType == null) {
			if (other.transferType != null)
				return false;
		} else if (!transferType.equals(other.transferType))
			return false;
		return true;
	}
	
	
	
}
