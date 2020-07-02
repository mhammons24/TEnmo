package com.techelevator.tenmo;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String MAIN_MENU_APPROVE_REJECT = "Approve or reject request";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MAIN_MENU_APPROVE_REJECT ,MENU_OPTION_EXIT };
	private static final String YOUR_BALANCE_IS = "Your current account balance is: $";
	private AuthenticatedUser currentUser;
    private static ConsoleService console = new ConsoleService(System.in, System.out);
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public static void main(String[] args) {
    	App app = new App(new AuthenticationService(API_BASE_URL), new AccountService(console , API_BASE_URL), new TransferService(console, API_BASE_URL), new UserService(console,  API_BASE_URL));
    	app.run();
    }

    public App(AuthenticationService authenticationService, AccountService accountService, TransferService transferService, UserService userService) {

		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.userService = userService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		setAuthToken();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
				setAuthToken();
			} else if(MAIN_MENU_APPROVE_REJECT.equals(choice)) {
				viewUsersRequests();
			}
			else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Account account = accountService.getAccount(currentUser.getUser().getId());
		console.printMessageToUser(YOUR_BALANCE_IS + account.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		setAuthToken();
	
		System.out.println(accountService.getAccount(currentUser.getUser().getId()).getBalance());
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		int userAccountId = accountService.getAccount(currentUser.getUser().getId()).getAccountId();
		List<Transfer> transfers = Arrays.asList(transferService.getPendingTransfers(userAccountId));
		
		for (int i = 0; i < transfers.size(); i++) {
    		console.printTransferDeatils(userService.findUserByAccountId(transfers.get(i).getAccountFromId()).getUsername(), 
    				userService.findUserByAccountId(transfers.get(i).getAccountToId()).getUsername(), transfers.get(i), i + 1 + ".");
    		}
	}
	
	private void viewUsersRequests() {
		int userFromAccountId = accountService.getAccount(currentUser.getUser().getId()).getAccountId();
		List<Transfer> transfers = Arrays.asList(transferService.getPendingTransfers(userFromAccountId));
		
		for (int i = 0; i < transfers.size(); i++) {
			if (userFromAccountId == transfers.get(i).getAccountFromId()) {
				console.printApproveRejectTransfer(transfers.get(i), userService.findUserByAccountId(transfers.get(i).getAccountToId()).getUsername());
			}
		}
		
	
	}

	private void sendBucks() {
		console.printMessageToUser("Which user would you like to send money to? (0 to cancel)");
		String userToName = getNameFromUser();
		if(userToName.equals(currentUser.getUser().getUsername())) {
			console.printMessageToUser("You cannot send money to yourself.......");
		}
		Transfer transfer = getTransferInfo();
		transfer.setAccountToId(accountService.getAccount(userService.getIdByUsername(userToName)).getAccountId());
		
		
		if(transfer.getAmountTransferred() == Integer.MAX_VALUE) {
			console.printMessageToUser("Transaction cancelled.");
			return;
		}  else if (accountService.getAccount(currentUser.getUser().getId()).getBalance() < transfer.getAmountTransferred()) {
			console.printMessageToUser("You only have $" + BigDecimal.valueOf(accountService.getAccount(currentUser.getUser().getId()).getBalance()).setScale(2) + ". Please try later.");
			return;
		}
		
		transfer = transferService.sendMoney(transfer);
		User userTo = userService.getUserByUsername(userToName);
		accountService.addMoney(accountService.getAccount(userTo.getId()), userTo.getId(), transfer.getAmountTransferred());
		accountService.removeMoney(accountService.getAccount(currentUser.getUser().getId()), currentUser.getUser().getId(), transfer.getAmountTransferred());
		console.printTransferDeatils(currentUser.getUser().getUsername(), userTo.getUsername(), transfer, "You successfully transferred money to " + userToName + "!");
	}

	private void requestBucks() {
		console.printMessageToUser("Which user would you like to request money from? (0 to cancel)");
		String userFromName = getNameFromUser();
		if(userFromName.equals(currentUser.getUser().getUsername())) {
			console.printMessageToUser("You cannot request money from yourself.......");
		}
		Transfer transfer = getTransferRequestInfo();
		transfer.setAccountFromId(accountService.getAccount(userService.getIdByUsername(userFromName)).getAccountId());
		if(transfer.getAmountTransferred() == Integer.MAX_VALUE) {
			console.printMessageToUser("Transaction cancelled.");
			return;
		}
		
		transfer = transferService.requestMoney(transfer);
		User userFrom = userService.getUserByUsername(userFromName);
		console.printTransferDeatils(userFrom.getUsername(), currentUser.getUser().getUsername(), transfer, "You successfully requested money from " + userFromName + "!");
		
		
	}
	
	private void setAuthToken() {
		transferService.setAuthToken(currentUser.getToken());
		accountService.setAuthToken(currentUser.getToken());
	}
	
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}
	private String getNameFromUser() {
		List<String> users = new ArrayList<String>();
		for( User user : userService.listUsers()) {
			users.add(user.getUsername());
		}
		return console.getChoiceFromOptions(users.toArray()).toString();
	}
	
	private Transfer getTransferInfo() {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId(accountService.getAccount(currentUser.getUser().getId()).getAccountId());
		double amountToSend = console.getMoneyChoiceFromUser("How much money would you like to send?  >>>> ");
		if (amountToSend == 0) {
			transfer.setAmountTransferred(Integer.MAX_VALUE);
		}
		transfer.setAmountTransferred(amountToSend);
		return transfer;
	}
	
	private Transfer getTransferRequestInfo() {
		Transfer transfer = new Transfer();
		transfer.setAccountToId(accountService.getAccount(currentUser.getUser().getId()).getAccountId());
		double amountToSend = console.getMoneyChoiceFromUser("How much money would you like to request?  >>>> ");
		if (amountToSend == 0) {
			transfer.setAmountTransferred(Integer.MAX_VALUE);
		}
		transfer.setAmountTransferred(amountToSend);
		return transfer;
	}
	
	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
