import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {

    public static void main(String argv[]) throws Exception {

        // create a socket to connect to the server
        Socket clientSocket = new Socket("localhost", 6789);

        // create output stream attached to the socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        // create input stream attached to the socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        int count = 3;
        while (count>=0) {
            System.out.print("Enter req: ");
            String request = scanner.nextLine() + "\n"; // add newline character
            outToServer.writeBytes(request);
            String serverMessage = inFromServer.readLine();
            System.out.println("Server response: " + serverMessage);
            count--;
        }

        scanner.close();
        

        // close the socket
        clientSocket.close();
    }
}
