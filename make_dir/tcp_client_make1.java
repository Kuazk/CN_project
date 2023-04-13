import java.io.*;
import java.net.*;

class TCPClientM1 {
    public static void main(String argv[]) throws Exception {
        String operRequest, operAns;
		String joinRequest, termRequest;
		String joinAns, termAns;

		Socket clientSocket = new Socket("localhost", 8000);

		// create output and input streams attached to the socket
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        String name = "Bob";
        joinRequest = "JOIN_REQUEST|" + name;
        outToServer.writeBytes(joinRequest + '\n');
        
        // get the ACK for the join request
        joinAns = inFromServer.readLine();
        
        if (joinAns.equals("JOIN_ACK|" + name)) {
            // if ACK received properly do nothing
            System.out.println("Connection established");
        }
		
		// establish request to make 3 math calculations
        for (int i = 1; i<4; i++) {
            String operation, operand1, operand2;
            switch (i) {
                case 1:
                    operation = "ADD";
                    operand1 = "1";
                    operand2 = "2";
                    break;
                case 2:
                    operation = "SUB";
                    operand1 = "3";
                    operand2 = "4";
                    break;
                case 3:
                    operation = "MUL";
                    operand1 = "5";
                    operand2 = "6";
                    break;
                default:
                    operation = "";
                    operand1 = "";
                    operand2 = "";
                    break;
            }
            operRequest = "CALCULATION_REQUEST|" + operation + "|" + operand1 + "|" + operand2;
            outToServer.writeBytes(operRequest + '\n');
            // get answer from server and output it on the screen
            operAns = inFromServer.readLine();
            // if there was an error in the calculation print 'error'
            System.out.println("Server response: " + operAns);
            String[] res = operAns.split("\\|");
            System.out.println("Answer from server: " + res[res.length-1]);
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
}

	