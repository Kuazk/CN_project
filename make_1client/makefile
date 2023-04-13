all: compile

compile:
	javac tcp_server.java
	javac tcp_client.java


server:
	java TCPServer
	sleep 1
clients:
	java TCPClient

clean:
	rm -f *.class
	rm log.txt
