import java.util.*;
import java.net.*;
import java.io.*;

public class ClientThread extends Thread{

  private Socket socket;
  private Server server;
  private PrintWriter writer;


  public ClientThread(Socket socket, Server server) {
    this.socket = socket;
    this.server = server;
  }

  public void run(){
      try{

        //get input from the reader thread of that client over that socket
        InputStream input = socket.getInputStream();
        //read input from the writer thread of client over that socket
        BufferedReader read = new BufferedReader(new InputStreamReader(input));

        //writes to the clients writer thread over current socket
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);

        String clientName =  read.readLine();
        server.addClientName(clientName);

        onlineClients();


        String destinationClient = read.readLine();
        






        String message;
        String serverMessage;

        do{
          message = read.readLine();
          serverMessage = "[" + clientName + "]: " + message;
          server.deliverMessage(message,this);
        }
        while(!(message != "Exit"));




      }catch(IOException e){
        System.out.println("Error in user thread: " + e.getMessage());
        e.printStackTrace();
      }
  }

  public void onlineClients(){  //add user has check
      writer.println("Available Users: " + server.getClients());
  }

  public void sendMessage(String msg){
    writer.println(msg);
  }
}
