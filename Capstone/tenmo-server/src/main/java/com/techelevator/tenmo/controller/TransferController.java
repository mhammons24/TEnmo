package com.techelevator.tenmo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;

@RestController
public class TransferController {
	
	private String baseUrl = "http://localhost:8080";
	private TransferDao transferDao;
	
	public TransferController(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	@RequestMapping(path = "/accounts/{id}/transfers", method = RequestMethod.GET)
	public List<Transfer> getTransfersByUserAccountId(@PathVariable("id") int userAccountId) {
		return transferDao.viewTranserByUserAccountId(userAccountId);

	}

	@RequestMapping(path = "/accounts/{id}/transfers/{id}", method = RequestMethod.GET)
	public Transfer getTransferById(@PathVariable("id") int accountId, @PathVariable("id") int transferId) {
		return transferDao.getTransferByTransferId(transferId, accountId);
	}

	@RequestMapping(path = "/transfers/approve/{id}", method = RequestMethod.PUT)
	public void approveTransfer(@PathVariable("id") int transferId) {
		transferDao.approveRequest(transferId);
	}

	@RequestMapping(path = "/accounts/{id}/transfers/filter/transfer_status_id=1", method = RequestMethod.GET)
	public List<Transfer> viewPendingTransfers(@PathVariable("id") int accountId) {
		List<Transfer> pendingTransfers = transferDao.viewPending(accountId);

		return pendingTransfers;
	}

	@RequestMapping(path = "/transfers/{id}", method = RequestMethod.PUT)
	public void rejectTransfer(@PathVariable("id") int transferId) {
		transferDao.approveRequest(transferId);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public Transfer sendMoney(Transfer transfer) {

		return transferDao.sendMoney(transfer.getAccountFromId(), transfer.getAccountToId(),
				transfer.getAmountTransferred());
	}
}
