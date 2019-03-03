import java.io.*;
import java.net.*;

public class ClientReadThread extends Thread{

	private BufferedReader reader;
	private Socket socket;
	private Client client;

	public ClientReadThread(Socket socket, Client client){
		this.socket = socket;
		this.client = client;


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
				String response = reader.readLine();
				System.out.println("\n"+response);

				if(client.getUserName()!=null){
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
