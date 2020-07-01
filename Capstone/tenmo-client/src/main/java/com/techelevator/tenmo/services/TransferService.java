package com.techelevator.tenmo.services;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.view.ConsoleService;

public class TransferService {
	private ConsoleService console;
	private RestTemplate restTemplate = new RestTemplate();
	private String baseUrl;
	
	public TransferService(ConsoleService console, String baseUrl) {
		this.console = console;
		this.baseUrl = baseUrl;
	}
	
	public void approveTransfer(int transferId) {
		
		try {
			restTemplate.put(baseUrl + "transfers/" + transferId, Transfer.class);
		    } catch (RestClientResponseException ex) {
		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      console.printError(ex.getMessage());
		    }

	}
}