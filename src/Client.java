import java.net.*;
import java.io.*;

/**
 * A command line client for the date server. Requires the IP address of
 * the server as the sole argument. Exits after printing the response.
 */
public class Client {

	private static int port;
	private String hostName;
	private String userName;

	public Client(String hostname,int port){
		this.hostName= hostName; //IP address
		this.port = port;
	}

	public void executeClient(){
		try{


		//tries to connect the client to server
		Socket socket = new Socket(hostName, port);

		System.out.println("Connected to Server");

		//starts thread to reads the servers input and writes it to standard
		//output
		ClientReadThread readThread = new ClientReadThread(socket, this);

		//this thread writes to the user thread and it is identified by the socket from the standard input
		//the input is inputed from the user
		// each user has their own reader and writer thread and is connected to the user thread
		ClientWriteThread writeThread = new ClientWriteThread(socket, this);

		readThread.start();
		writeThread.start();

	}catch(UnknownHostException ex){
		System.out.println("Server not found: "+ex.getMessage());
	}catch(IOException e){
		System.out.println ("I/O Error "+e.getMessage());
	}

	}

	//sets the username of this client to a specific string
	public void setUserName(String userName){
		this.userName = userName;
	}
	//returns the username of a client
	public String getUserName(){
		return this.userName;
	}



    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Pass the server IP and port number");
            return;
        }
				//reads the hostname as the first argument of the command line
        String hostname = args[0];
				//reads the port number as the second argument of the command line
        int portNum = Integer.parseInt(args[1]);

				/*initializes a new client object and runs executeClient() to
				create and start the threads that write from the thread and read to the thread*/
        Client client = new Client(hostname, portNum);
        client.executeClient();

    }
}
