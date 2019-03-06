import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;
import java.awt.*;

public class ClientReadThread extends Thread{

	private BufferedReader reader;
	private Socket socket;
	private Client client;
	private boolean ready=false;
	//changes
	private InputStream input;
	FileOutputStream fos;
	BufferedOutputStream bos;
	byte [] mybytearray;
	int bytesRead;

	public ClientReadThread(Socket socket, Client client){
		this.socket = socket;
		this.client = client;

		//creates and inputstream and a reader for it
		try{
			input = socket.getInputStream();
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

				if(client.getMessagingProtocol().getState() == 10){
					String[] clientResponse = response.split(" ",2);

					if(clientResponse[1].equals("#Accept")){
						client.getMessagingProtocol().setState(11);
					}
					else if(clientResponse[1].equals("#Decline"))
						client.getMessagingProtocol().setState(12);
				}


				else if(fileCheck(response)){
					System.out.println("\n"+response);
					receiveFile();
				}else{

				System.out.println("\n"+response);
}



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

	public void receiveFile(){
		try{
			JFileChooser jfc = new JFileChooser();
			int result = jfc.showSaveDialog(null);
			if(result == JFileChooser.CANCEL_OPTION)
				return;
			fos = new FileOutputStream(jfc.getSelectedFile());

			mybytearray  = new byte [128];
			int bytesRead=0;
			while((bytesRead=input.read(mybytearray))>0){
				fos.write(mybytearray,0,bytesRead);
			}
			fos.flush();

		}catch(IOException ex){
			System.out.println(ex.getMessage());
		}
		System.out.println("File Received");
}

	public boolean fileCheck(String check){
		boolean temp= false;
		String[] words = check.split(" ",2);
		if(words[1].equals("You are currently receiving a file"))
			temp = true;
		return temp;
	}
}
