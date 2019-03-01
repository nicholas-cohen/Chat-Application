import java.util.*;
import java.net.*;
import java.io.IOException;

public class Server{
	private int port;
	private Set<String> clientNames = new HashSet<>();
    private Set<ClientThread> clientThreads = new HashSet<>();
    ServerSocket myService;


    public Server(int port){
    	this.port = port;
    }

		public void addClientName(String clientName) {
        clientNames.add(clientName);
    }

		public void removeClient(String clientName, ClientThread aUser) {
        boolean removed = clientNames.remove(clientName);
        if (removed) {
            clientThreads.remove(aUser);
            System.out.println("The user " + clientName + " is no longer available to talk");
        }
    }

		public Set<String> getClients(){
			return this.clientNames;
		}

    public void execute(){//change name
    	try{
    		myService = new ServerSocket(port);
    		System.out.println("Server is listening on port: "+port);

    		while(true){
    			Socket userSocket = myService.accept();
    			System.out.println("New Connected User");
    			ClientThread newClient = new ClientThread(userSocket, this);
    			clientThreads.add(newClient);
    			newClient.start();


    		}

    	}

    	catch(IOException e){
    		System.out.println(e);
    	}

    }

		public void deliverMessage(String message, ClientThread sourceClient){
			//Code that sends message from one client to another through the server.
		}
}
