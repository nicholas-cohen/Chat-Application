import java.io.*;
import java.net.*;


public class ClientWriteThread extends Thread {
  private PrintWriter writer;
  private Socket socket;
  private Client client;


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
    client.setUserName(clientName);
    writer.println(clientName);
    String destinationClient = console.readLine("\nEnter the person with whom you'd like to chat with: ");
    writer.println(destinationClient);



    String text;
    do {
      text = console.readLine("[" + clientName + "]: ");
      writer.println(text);
    } while(!text.equals("Exit"));

    try{
      socket.close();
    } catch(IOException ex){
      System.out.println("Error writing to server: " + ex.getMessage());
    }

  }
}