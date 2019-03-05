import java.util.*;
import java.net.*;
import java.io.IOException;

public class Server{
	private int port;
	private Set<String> clientNames = new HashSet<>();
 	ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
    ServerSocket myService;

		//Constructor with port number as only parameter
    public Server(int port){
    	this.port = port;
    }


		public static void main(String[] args) {
			if (args.length < 1) {
      	System.out.println("Syntax: java ChatServer <port-number>");
        System.exit(0);
      }
			int port = Integer.parseInt(args[0]);
			Server server = new Server(port);
      server.execute();
		}

		//Add a new client name into the client name hash set
		public void addClientName(String clientName) {
        clientNames.add(clientName);
    }


		//Remove a client that exited the application from the
		//client name set as well as from the clientThreads arraylist
		public void removeClient(String clientName, ClientThread aUser) {
        boolean removed = clientNames.remove(clientName);
        if (removed) {
            clientThreads.remove(aUser);
            System.out.println("The user " + clientName + " is no longer available to talk");
        }
    }

		public void broadcast(String message, ClientThread excludeUser) {
		    for (ClientThread aUser : clientThreads) {
		        if (aUser != excludeUser) {
		            aUser.broadcastMessage(message);
		        }
		    }
		}

		//return an array of all the clients in the set of client names
		public Set<String> getClients(){
			return this.clientNames;
		}

		//Execution of the server setup and listening for new Client connections
		//Referenced in main method
    public void execute(){
    	try{
    		myService = new ServerSocket(port);
    		System.out.println("Server is listening on port: "+port);
				System.out.println("Ip Address: "+myService.getInetAddress());

				//Listening for Client connection requests and initializing a clientThread for that
				//specific connection.
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


}
