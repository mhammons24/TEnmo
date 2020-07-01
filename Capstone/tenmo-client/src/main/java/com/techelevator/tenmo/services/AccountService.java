package com.techelevator.tenmo.services;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.view.ConsoleService;

public class AccountService {
	private ConsoleService console;
	private RestTemplate restTemplate = new RestTemplate();
	private String baseUrl;
	
	public AccountService(ConsoleService console, String baseUrl) {
		this.console = console;
		this.baseUrl = baseUrl;
	}

	public Account getAccount(long userId) {
		Account account = null;
		
		try {
			account = restTemplate.getForObject(baseUrl + "accounts/" + userId, Account.class);
		    } catch (RestClientResponseException ex) {
		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      console.printError(ex.getMessage());
		    }
		return account;
	}
	
	//	  public Reservation getReservation(int reservationId) {
//		    Reservation reservation = null;
//		    try {
//		      reservation = restTemplate.getForObject(BASE_URL + "reservations/" + reservationId, Reservation.class);
//		    } catch (RestClientResponseException ex) {
//		      console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
//		    } catch (ResourceAccessException ex) {
//		      console.printError(ex.getMessage());
//		    }
//		    return reservation;
//		  }

}
