package com.techelevator.tenmo.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
	

	private TransferDao transferDao;
	
	public TransferController(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	@RequestMapping(path = "/accounts/{id}/transfers", method = RequestMethod.GET)
	public List<Transfer> getTransfersByUserAccountId(@PathVariable("id") int userAccountId) {
		return transferDao.viewTranserByUserAccountId(userAccountId);

	}

	@RequestMapping(path = "/accounts/{id1}/transfers/{id}", method = RequestMethod.GET)
	public Transfer getTransferById(@PathVariable("id1") int accountId, @PathVariable("id") int transferId) {
		return transferDao.getTransferByTransferId(transferId, accountId);
	}

	@RequestMapping(path = "/transfers/{id}/approve", method = RequestMethod.PUT)
	public void approveTransfer(@RequestBody Transfer transfer, @PathVariable("id") int transferId) {
		transferDao.approveRequest(transferId);
	}

	@RequestMapping(path = "/accounts/{id}/transfers/pending", method = RequestMethod.GET)
	public List<Transfer> viewPendingTransfers(@PathVariable("id") int accountId) {
		List<Transfer> pendingTransfers = transferDao.viewPending(accountId);

		return pendingTransfers;
	}

	@RequestMapping(path = "/transfers/{id}/reject", method = RequestMethod.PUT)
	public void rejectTransfer(@RequestBody Transfer transfer, @PathVariable("id") int transferId) {
		transferDao.rejectRequest(transferId);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public Transfer sendMoney(@RequestBody Transfer transfer) {

		return transferDao.sendMoney(transfer.getAccountFromId(), transfer.getAccountToId(),
				transfer.getAmountTransferred());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfers/requests", method = RequestMethod.POST)
	public Transfer requestMoney(@RequestBody Transfer transfer) {

		return transferDao.requestMoney(transfer.getAccountFromId(), transfer.getAccountToId(),
				transfer.getAmountTransferred());
	}
}
