package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.view.ConsoleService;

public class AccountService {
	private ConsoleService console;
	private RestTemplate restTemplate = new RestTemplate();
	private String baseUrl;
	private String AUTH_TOKEN = "";
	
	public AccountService(ConsoleService console, String baseUrl) {
		this.console = console;
		this.baseUrl = baseUrl;
	}
	
	public void setAuthToken(String authToken) {
		this.AUTH_TOKEN = authToken;
	}
	
	public Account getAccount(long userId) {
		Account account = null;
		
		try {
			account = restTemplate.exchange(baseUrl + "accounts/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
		    } catch (RestClientResponseException ex) {
		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      console.printError(ex.getMessage());
		    }
		return account;
	}
	
	
	public void addMoney(Account account, long userId, double amountToAdd) {
		account.setBalance(amountToAdd  + account.getBalance());
		try {
			restTemplate.exchange(baseUrl + "accounts/" + userId + "/deposits", HttpMethod.PUT, makeAccountEntity(account), Void.class);
		    } catch (RestClientResponseException ex) {
		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      console.printError(ex.getMessage());
		    }
	}
	
	public void removeMoney(Account account, long userId, double amountToRemove) {
		account.setBalance(account.getBalance() - amountToRemove);
		try {
			restTemplate.exchange(baseUrl + "accounts/" + userId + "/deposits", HttpMethod.PUT, makeAccountEntity(account), Void.class);
		    } catch (RestClientResponseException ex) {
		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      console.printError(ex.getMessage());
		    }
	}
	
	private HttpEntity<Account> makeAccountEntity(Account account) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Account> entity = new HttpEntity<>(account, headers);
		return entity;
		}
	
	private HttpEntity<Account> makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Account> entity = new HttpEntity<>(headers);
		return entity;
	}

}
