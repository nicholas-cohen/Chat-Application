import java.io.*;
import java.net.*;


public class ClientWriteThread extends Thread {
  private PrintWriter writer;
  private Socket socket;
  private ChatClient client;


  public ClientWriteThread(Socket socket, Client client){
    this.socket = socket;
    this.client = client;

    try{
      OutputStream output = socket.getOutputStream();
      writer = new PrintWriter(output, true);
    } catch (IOException ex){
      ex.printStackTrace();
    }

  }

  public void run(){
    Console console = System.console();
    String clientName = console.readLine("\nEnter your name: ");
    client.setUserName(userName);
    writer.println(userName);
    String destinationClient = console.readLine("\nEnter the person with whom you'd like to chat with: ");
    client.setDestinationClient(destinationClient);

  }
}
