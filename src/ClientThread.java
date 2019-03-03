import java.util.*;
import java.net.*;
import java.io.*;

public class ClientThread extends Thread{

  private String name;
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
        name = clientName;
        server.addClientName(clientName);

        onlineClients();


        String message;
        String serverMessage;
        //System.out.println(name);

        do{
          message = read.readLine();

          serverMessage = "[" + clientName + "]: " + message;
          sendMessage(message);
        }
        while((message != "Exit"));




      }catch(IOException e){
        System.out.println("Error in user thread: " + e.getMessage());
        e.printStackTrace();
      }
  }

  public void onlineClients(){  //add user has check
      writer.println("Available Users: " + server.getClients());
  }
  public String getUserName(){
    return name;
  }

  public void sendMessage(String message){
    String[] words = message.split(" ",2);

    String destinationName = words[0].substring(1);

    words[1] = words[1].trim();
    System.out.println(words[1]);
    for(int i =0;i<server.clientThreads.size();i++){
      if(server.clientThreads.get(i).getUserName().equals(destinationName));
        server.clientThreads.get(i).getPrintWriter().println("[" + name + "]: "+words[1]);
    }

  }

  public PrintWriter getPrintWriter(){
    return this.writer;
  }
}
