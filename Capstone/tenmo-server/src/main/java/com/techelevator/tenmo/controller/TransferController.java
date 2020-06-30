package com.techelevator.tenmo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
//	@RequestMapping(path = "/users/{id}/transfers", method = RequestMethod.GET)
//	public List<Transfer> getTransfersByUserId( int userId) {
//		return transferDao.viewTranserByUserId(userId);
//		
//	}
//	
	
}
