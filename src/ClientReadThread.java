import java.io.*;
import java.net.*;

public class ClientReadThread extends Thread{

	private BufferedReader reader;
	private Socket socket;
	private Client client;

	public ClientReadThread(Socket socket, Client client){
		this.socket = socket;
		this.client = client;

		//creates and inputstream and a reader for it
		try{
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		}catch(IOException e){
			System.out.println("Error in getting message from server: "+e.getMessage());
			e.printStackTrace();
			}
		}

	public void run(){
		while(true){
			try{
				//Reads the response from the server and prints it out to the client console
				String response = reader.readLine();
				System.out.println("\n"+response);


				if(client.getUserName()!=null){
					//---------> LOOK INTO NEXT LINE
					System.out.print("[" + client.getUserName() + "]: ");
				}

			}catch(IOException e){
				System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
			}
		}
	}
}
