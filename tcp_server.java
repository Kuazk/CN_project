import java.io.*;
import java.net.*;
import java.util.*;
import java.time.Duration;
import java.time.Instant;

class TCPServer {
    
    public static void main(String argv[]) throws Exception {
        
        // create a server socket that listens on port 6789
        ServerSocket welcomeSocket = new ServerSocket(6789);
        
        while (true) {
            
            // wait for a client to connect
            Socket connectionSocket = welcomeSocket.accept();
            
            // create a new thread to handle the client connection
            Thread clientThread = new Thread(new ClientHandler(connectionSocket));
            
            // start the thread to handle the client connection
            clientThread.start();
        }
    }
}

class ClientHandler implements Runnable {
    
    private Socket connectionSocket;
    private String clientName;
    private Instant joinTime;
    private Duration duration;

    
    public ClientHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }
    
    public void run() {
        
        String clientMessage;
        String[] tokens;
        
        try {
            
            // create input and output streams for the client socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            
            // loop to continuously handle client requests
            while (!connectionSocket.isClosed()) {
            // get the client's request
                clientMessage = inFromClient.readLine();
                if (clientMessage == null) {
                    break;
                }
                // split the message into tokens using '|'
                tokens = clientMessage.split("\\|");
                
                File logFile = new File("log.txt");
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
            
                FileWriter fw = new FileWriter(logFile, true);
                
                // check the type of the message
                if (tokens[0].equals("JOIN_REQUEST")) {
                    clientName = tokens[1];
                    outToClient.writeBytes("JOIN_ACK|" + clientName + "\n");
                    //start the timer
                    joinTime = Instant.now();
                    // write to log file
                    fw.write(clientName + ", " + joinTime + ", " + clientMessage+"\n");
                    System.out.println("New client joined: " + clientName);
                    
                } else if (tokens[0].equals("TERMINATION_REQUEST")) {
                    clientName = tokens[1];
                    outToClient.writeBytes("TERMINATION_ACK|" + clientName + "\n");
                    //end the timer
                    Instant endTime = Instant.now();
                    duration = Duration.between(joinTime, endTime);
                    fw.write(clientName + ", " + endTime + ", "+ duration.getSeconds() +", "+ clientMessage+"\n");
                    System.out.println("Client " + clientName + " terminated connection");
                    // close the client socket
                    connectionSocket.close();
                } else if (tokens[0].equals("CALCULATION_REQUEST")) {
                    System.out.println("CALCULATION_REQUEST");
                    String mathOperation = tokens[1];
                    int operand1 = Integer.parseInt(tokens[2]);
                    int operand2 = Integer.parseInt(tokens[3]);
                    int result = 0;
                    switch (mathOperation) {
                        case "ADD":
                            result = operand1 + operand2;
                            break;
                        case "SUB":
                            result = operand1 - operand2;
                            break;
                        case "MUL":
                            result = operand1 * operand2;
                            break;
                        case "DIV":
                            result = operand1 / operand2;
                            break;
                        default:
                            outToClient.writeBytes("CALCULATION_ERROR|" + mathOperation + "\n");
                            break;
                    }
                    outToClient.writeBytes("CALCULATION_ACK|" + mathOperation + "|" + operand1 + "|" + operand2 + "|" + result + "\n");

                    Instant currTime = Instant.now();
                    duration = Duration.between(joinTime, currTime);
                    fw.write(clientName + ", " + currTime + ", "+ duration.getSeconds() +", "+ clientMessage+"\n");

                    System.out.println("Calculation request received from " + clientName + ": " + mathOperation + "|" + operand1 + "|" + operand2);
                    System.out.println("Result sent to " + clientName + ": " + mathOperation + "|" + operand1 + "|" + operand2 + "|" + result);
                    
                } else {
                    // invalid message
                    outToClient.writeBytes("ERROR\n");
                }
    
                fw.flush();
                fw.close();
            }
            
        } catch (IOException e) {
            // handle any network errors that occur
            e.printStackTrace();
        } finally {
            try {
                // close the client socket
                connectionSocket.close();
            } catch (IOException e) {
                // handle any errors that occur while closing the socket
                e.printStackTrace();
            }
        }
    }
}
   


