import java.io.*;
import java.net.*;

class TCPClient {
    public static void main(String argv[]) throws Exception {
        String operRequest, operAns;
		String joinRequest, termRequest;
		String joinAns, termAns;

		// create an input stream from user and a socket that the client will use to connect to the server
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 6789);

		// create output and input streams attached to the socket
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		// ask whether client would like to connect to server
		System.out.print("Would you like to connect to server? (y/n): ");
		String join = inFromUser.readLine();
		while (!validInput(join)) {
			System.out.print("Invalid input, try again: ");
			join = inFromUser.readLine();
		}

        String name = "";
		
		// if client wants to connect to server
		if (join.equalsIgnoreCase("y")) {
            // ask user for client name
            System.out.print("Client name: ");
            name = inFromUser.readLine();

            while (name.isEmpty()){
                System.out.print("Invalid name, try again: ");
                name = inFromUser.readLine();
            }
			// make the join request and send it to server
			joinRequest = "JOIN_REQUEST|" + name;
			outToServer.writeBytes(joinRequest + '\n');
			
			// get the ACK for the join request
			joinAns = inFromServer.readLine();
			
			if (joinAns.equals("JOIN_ACK|" + name)) {
				// if ACK received properly do nothing
				System.out.println("Connection established");
			}
			// if ACK not received then display an error
			else {
				System.out.println("Error: wasn't able to connect to server");
				System.exit(0);
			}
		}
		else {
			// terminate
			System.out.println("Terminated client");
			clientSocket.close();
			System.exit(0);
		}
		
		System.out.print("Would you like to make a calculation request? (y/n): ");
		String makeReq = inFromUser.readLine();
		while (!validInput(makeReq)) {
			System.out.print("Invalid input, try again: ");
			makeReq = inFromUser.readLine();
		}
		
		int n = 3;
		
		// while the client wants to make more requests and they make at least 3 requests
		while (makeReq.equalsIgnoreCase("y")) {
			// get the calculation request from the user
			System.out.print("Enter request (operands are MUL, DIV, ADD, SUB) using the format 'ADD|5|2': ");
			String operation = inFromUser.readLine();

			// check if calculation request is valid
            while (!validRequest(operation)) {
                System.out.print("Invalid request, try again: ");
                operation = inFromUser.readLine();
            }
			
			// make the operation request and send it to the server
			operRequest = "CALCULATION_REQUEST|" + operation;
			outToServer.writeBytes(operRequest + '\n');
	
			// get answer from server and output it on the screen
			operAns = inFromServer.readLine();
			// if there was an error in the calculation print 'error'
			if (operAns.contains("CALCULATION_ERROR|")) {
				System.out.println("Server response: " + operAns);
			}
			// else print the answer
			else {
				String[] res = operAns.split("\\|");
				System.out.println("Answer from server: " + res[res.length-1]);
			}
			n--;

			// if client already made 3 requests then give them an option to make more requests
			if (n <= 0) {
				System.out.print("Would you like to make a calculation request? (y/n): ");
				makeReq = inFromUser.readLine();
				while (!validInput(makeReq)) {
					System.out.print("Invalid input, try again: ");
					makeReq = inFromUser.readLine();
				}
			}
		}
		
		// since client doesn't want to make any more requests, terminate connection
		// make the termination request and send it to server
		termRequest = "TERMINATION_REQUEST|" + name;
		outToServer.writeBytes(termRequest + '\n');

		// get the ACK for the termination request
		termAns = inFromServer.readLine();
		if (termAns.equals("TERMINATION_ACK|" + name)) {
			// ACK received properly so terminate connection
            System.out.println("Connection terminated");
			clientSocket.close();
		}
		// if ACK not received then display an error
		else {
			System.out.println("Error: wasn't able to terminate connection");
			System.exit(0);
		}
		
		// close the socket
		clientSocket.close();

    }

	public static boolean validInput(String answer) {
		if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
			return true;
		}
		return false;
	}
	
	// check if the client request is in the correct format with the correct inputs
    public static boolean validRequest(String opr) {
        String[] operation = opr.split("\\|");

		// check that format of the request
		if (operation.length != 3) {
			return false;
		}

		String operator = operation[0];
		int operand1;
		int operand2;
		// check that the operands inputted are integers
		try {
			operand1 = Integer.parseInt(operation[1]);
        	operand2 = Integer.parseInt(operation[2]);
		} catch (NumberFormatException ex) {
			return false;
		}
        
        boolean result = true;

		// check that its a valid operator option
        switch (operator) {
            case "ADD":
                break;
            case "SUB":
                break;
            case "MUL":
                break;
            case "DIV":
				// cehck for division by zero
                if (operand2 == 0) {
					result = false;
				};
                break;
			// if operator is anything else
            default:
                result = false;
                break;
        }

        return result;
    }
}
