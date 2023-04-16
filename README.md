# CN_project
Java TCP Server

This is a simple TCP server implemented in Java that allows clients to connect, perform simple mathematical operations, and terminate the connection.

Prerequisites
* Java 11 or later
* An IDE or command line tool for running Java programs

Getting Started
1. Clone the repository: git clone https://github.com/Kuazk/CN_project.git
2. Open the project in your IDE or navigate to the project directory in the command line
3. open make_1client folder
4. Run make
5. open seprate terminal window(one for client and one for server)
6. Run make clients and make server

Connect to the server from a client application using the server's IP address and port number (default port is 6789)

Usage:
    Once a client has connected to the server, the client can send one of the following types of requests:

    JOIN_REQUEST: The client sends this message to join the server. The message must be in the format JOIN_REQUEST|clientName.

    TERMINATION_REQUEST: The client sends this message to terminate the connection with the server. The message must be in the format TERMINATION_REQUEST|clientName.

    CALCULATION_REQUEST: The client sends this message to perform a mathematical operation. The message must be in the format CALCULATION_REQUEST|operation|operand1|operand2. Supported operations are ADD, SUB, MUL, and DIV.

    The server will respond to each request with an appropriate response message in the following format:

    JOIN_ACK: Sent in response to a JOIN_REQUEST message. The message is in the format JOIN_ACK|clientName.

    TERMINATION_ACK: Sent in response to a TERMINATION_REQUEST message. The message is in the format TERMINATION_ACK|clientName.

    CALCULATION_ACK: Sent in response to a CALCULATION_REQUEST message. The message is in the format CALCULATION_ACK|operation|operand1|operand2|result.

    CALCULATION_ERROR: Sent if an invalid operation is specified in a CALCULATION_REQUEST message. The message is in the format CALCULATION_ERROR|operation.

The server also logs each client connection, disconnection, and calculation request to a log file named log.txt. The log file is created in the same directory as the Java files if it does not already exist.
