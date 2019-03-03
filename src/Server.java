import java.util.*;
import java.net.*;
import java.io.IOException;

public class Server{
	private int port;
	private Set<String> clientNames = new HashSet<>();
 	ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
    ServerSocket myService;




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
				System.out.println("Ip Address: "+myService.getInetAddress());

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

		public void deliverMessage(String message){
			//
		}
}
