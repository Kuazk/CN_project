all: compile

compile:
	javac tcp_server_make.java
	javac tcp_client_make1.java
	javac tcp_client_make2.java
	javac tcp_client_make3.java

server:
	java TCPServer
	sleep 1
clients:
	java TCPClientM1 
	sleep 1
	java TCPClientM2 
	sleep 1
	java TCPClientM3 

clean:
	rm -f *.class
	rm make_log.txt
