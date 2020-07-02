package com.techelevator.tenmo.services;

import java.lang.reflect.Array;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import com.techelevator.tenmo.models.Transfer;
import com.techelevator.view.ConsoleService;

public class TransferService {
	private ConsoleService console;
	private RestTemplate restTemplate = new RestTemplate();
	private String baseUrl;
	private String AUTH_TOKEN = "";

	public TransferService(ConsoleService console, String baseUrl) {
		this.console = console;
		this.baseUrl = baseUrl;

	}

	public void approveTransfer(int transferId) {

		try {
			restTemplate.put(baseUrl + "transfers/approve/" + transferId, Transfer.class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}

	}
	
	public Transfer getTransferById(int transferId, int accountId) {
		Transfer transfer = null;
		try {
			transfer = restTemplate.exchange(baseUrl + "accounts/"+ accountId + "/transfers/"  + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return transfer;
	}

	public Transfer[] getTransferByAccountId(int accountId) {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(baseUrl + "accounts/"+ accountId + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return transfers;
	}
	
	public Transfer[] getPendingTransfers(int accountId) {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(baseUrl + "accounts/" + accountId + "/transfers/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return transfers;
	}
	
	public Transfer sendMoney(Transfer transfer) {
		
		try {
			transfer = restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, makeHeader(transfer), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return transfer;
	}
	
	public Transfer requestMoney(Transfer transfer) {
		
		try {
			transfer = restTemplate.exchange(baseUrl + "transfers/requests", HttpMethod.POST, makeHeader(transfer), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return transfer;
	}
	
	public void rejectTransfer(Transfer transfer, int transferId) {

		try {
			restTemplate.exchange(baseUrl + "transfers/" + transferId + "/reject", HttpMethod.PUT, makeHeader(transfer),
					Void.class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
	}

	public void approveTransfer(Transfer transfer, int transferId) {

		try {
			restTemplate.exchange(baseUrl + "transfers/" + transferId + "/approve", HttpMethod.PUT,
					makeHeader(transfer), Void.class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
	}

	public void setAuthToken(String authToken) {
		this.AUTH_TOKEN = authToken;
	}
	
	private HttpEntity<Transfer> makeHeader(Transfer transfer) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
	    return entity;
	}
	
	private HttpEntity<Transfer> makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(headers);
		return entity;
		}
}
