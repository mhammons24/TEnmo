package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.exec.util.StringUtils;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.UserService;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);

	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	public double getMoneyChoiceFromUser(String prompt) {
		double choice = -1;
		while (choice == -1) {
			System.out.println(prompt);
			try {
				choice = Double.parseDouble(in.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("That is not a vaild amount.");
				System.out.println();
			}
			if (choice == -1) {
				continue;
			}
			if (BigDecimal.valueOf(choice).scale() > 2 || choice < 0) {
				System.out.println("Send a positive amount with no more that 2 decimal places!! (42.42)");
				choice = -1;
			} else {
				break;
			}

		}
		out.println();
		return choice;

	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt + ": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				out.println("\n*** " + userInput + " is not valid ***\n");
			}
		} while (result == null);
		return result;
	}

	public void printError(String errorMessage) {
		System.err.println(errorMessage);
	}

	public void printMessageToUser(String message) {
		System.out.println(message);
		System.out.println();
	}

	public void printTransferDeatils(String fromUsername, String toUsername, Transfer transfer, String message) {
		String repeat = new String(new char[message.length() + 16]).replace("\0", "-");
		System.out.println(repeat);
		System.out.println(message + " Details >>>>>>>");
		System.out.println(repeat);
		System.out.println("Id: " + transfer.getTransferId());
		System.out.println("From: " + fromUsername);
		System.out.println("To: " + toUsername);
		System.out.println("Type: " + transfer.getTransferType());
		System.out.println("Status: " + transfer.getTransferStatus());
		System.out.println("Amount: " + BigDecimal.valueOf(transfer.getAmountTransferred()).setScale(2));
		System.out.println();
		// Id: 23
		// From: Bernice
		// To: Me Myselfandi
		// Type: Send
		// Status: Approved
		// Amount: $903.14
	}

	public void printApproveRejectTransfer(Transfer transfer, String toUsername) {
		String printFormat = "%-5s %-20s %17s";
		String amount = "$" + String.valueOf(BigDecimal.valueOf(transfer.getAmountTransferred()).setScale(2));
		System.out.println(String.format(printFormat, Integer.toString(transfer.getTransferId()), toUsername, amount));

	}

	public void printHeading3(String heading, String firstItem, String secondItem, String thirdItem) {
		String printFormat = "%-5s %-20s %17s";
		String repeat = new String(new char[44]).replace("\0", "-");
		System.out.println(repeat);
		System.out.println(heading);
		System.out.println(String.format(printFormat, firstItem, secondItem, thirdItem));
		System.out.println(repeat);
	}

	public void printHeading2(String heading, String firstItem, String secondItem) {
		String printFormat = "%-5s %24s";
		String repeat = new String(new char[30]).replace("\0", "-");
		System.out.println(repeat);
		System.out.println(heading);
		System.out.println(String.format(printFormat, firstItem, secondItem));
		System.out.println(repeat);
	}

	public Object getUserFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayUsers(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private void displayUsers(Object[] options) {
		String printFormat = "%-5s %24s";
		for (int i = 0; i < options.length; i++) {
			String optionNum = i + 1 + ") ";
			out.println(String.format(printFormat, optionNum, options[i]));
		}
		out.println();
		out.print("Please select a user from the list.");
		out.flush();
	}

}
