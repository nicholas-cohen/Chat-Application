import java.io.*;
import java.net.*;

public class ClientReadThread extends Thread{

	private BufferedReader reader;
	private Socket socket;
	private Client client;
	private boolean ready=false;

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

				//send the respone to the protocol class and check if it is the available users string
				// client.getMessagingProtocol().processOutput(response);
				// client.getMessagingProtocol().setState(1);

				if(client.getUserName()!=null){
					//---------> LOOK INTO NEXT LINE after reading from server
					if(ready){System.out.print("[" + client.getUserName() + "]: ");}
					else ready=true;

				}

			}catch(IOException e){
				System.out.println("You have been disconnected: " + e.getMessage());
        break;
			}
		}
	}

	public void receiveFile(File file){
		DataOutputStream dos;
		FileInputStream fis;
			try{
			dos = new DataOutputStream(socket.getOutputStream());
			fis = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int read;
			while ((read =fis.read(buffer)) > 0) {
				dos.write(buffer,0,read);
			}
			fis.close();
			dos.close();
		}catch(IOException e){
			System.out.println("File not found: "+e.getMessage());
		}

}

}
