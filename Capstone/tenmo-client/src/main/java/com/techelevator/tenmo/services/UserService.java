package com.techelevator.tenmo.services;



import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class UserService {
	private ConsoleService console;
	private RestTemplate restTemplate = new RestTemplate();
	private String baseUrl;
	private String AUTH_TOKEN = "";
	
	public UserService(ConsoleService console, String baseUrl ) {
		this.console = console;
		this.baseUrl = baseUrl;
	}
	
	public User getUserByUsername(String username) {
		User user = null;
		try {
			user = restTemplate.exchange(baseUrl + "users/"+ username, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return user;
	}
	
	public int getIdByUsername(String username) {
		User user = null;
		try {
			user =  restTemplate.exchange(baseUrl + "users/"+ username, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return (int) user.getId();
	}
	
	public User[] listUsers() {
		User[] users = null;
		try {
			users = restTemplate.exchange(baseUrl + "users/", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return users;
	}
	
	
	
	
	public void setAuthToken(String authToken) {
		this.AUTH_TOKEN = authToken;
	}
	
	private HttpEntity<User> makeHeader(User user) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity<User> entity = new HttpEntity<>(user, headers);
	    return entity;
	}
	
	private HttpEntity<User> makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<User> entity = new HttpEntity<>(headers);
		return entity;
		}

}
