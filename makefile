JCC = javac

default: TCPClient.class TCPServer.class

TCPServer.class: tcp_server.java
	$(JCC) $(JFLAGS) tcp_server.java

TCPClient.class: tcp_client.java
	$(JCC) $(JFLAGS) tcp_client.java

clean: $(RM) *.class