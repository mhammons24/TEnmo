package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

	private int transferId;
	private int transferTypeId;
	private int transferStatusId;
	private int accountFromId;
	private int accountToId;
	private BigDecimal amountTransferred;
	public int getTransferId() {
		return transferId;
	}
	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
	public int getTransferTypeId() {
		return transferTypeId;
	}
	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}
	public int getTransferStatusId() {
		return transferStatusId;
	}
	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
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
	public BigDecimal getAmountTransferred() {
		return amountTransferred;
	}
	public void setAmountTransferred(BigDecimal amountTransferred) {
		this.amountTransferred = amountTransferred;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountFromId;
		result = prime * result + accountToId;
		result = prime * result + ((amountTransferred == null) ? 0 : amountTransferred.hashCode());
		result = prime * result + transferId;
		result = prime * result + transferStatusId;
		result = prime * result + transferTypeId;
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
		if (amountTransferred == null) {
			if (other.amountTransferred != null)
				return false;
		} else if (!amountTransferred.equals(other.amountTransferred))
			return false;
		if (transferId != other.transferId)
			return false;
		if (transferStatusId != other.transferStatusId)
			return false;
		if (transferTypeId != other.transferTypeId)
			return false;
		return true;
	}
	
	
	
	
}
