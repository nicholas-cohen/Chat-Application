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
        //reads input from client and adds the input as a name to the server client list
        String clientName =  read.readLine();
        name = clientName;
        server.addClientName(clientName);

        //Prints the names of online users
        onlineClients();

        
        String serverMessage = "New user connected: " + clientName;
        server.broadcast(serverMessage, this);

        String message;
        //System.out.println(name);

        /* Continuously checks the message sent by the client and executes
        the method that sends it to the destination client.  Quits if user enters: Exit.
        */
        do{
          message = read.readLine();
          if(message.equals("Exit")){
            serverMessage = clientName + " quit.";
            server.broadcast(serverMessage,this);
            break;
          }
          else{
            sendMessage(message);
          }
         }
        while((message != "Exit"));
        server.removeClient(clientName,this);
        socket.close();



      }catch(IOException e){
        System.out.println("Error in user thread: " + e.getMessage());
        e.printStackTrace();
      }
  }

  //prints out the set of online users
  public void onlineClients(){  //add user has check
      writer.println("Available Users: " + server.getClients());
  }


  public String getUserName(){
    return name;
  }

  public void broadcastMessage(String message) {
        writer.println(message);
    }

  /*
  This method is responsible for sending the message from one client to another.

  It splits the message into an array of size 2, checks the first element for the destination
  name and sends to that client the second element of the array (the message)
  */
  public void sendMessage(String message){
    String[] words = message.split(" ",2);

    String destinationName = words[0].substring(1);

    words[1] = words[1].trim();
    boolean check = false;
    for(int i =0;i<server.clientThreads.size();i++){
      if(server.clientThreads.get(i).getUserName().equals(destinationName)){
        server.clientThreads.get(i).getPrintWriter().println("[" + name + "]: "+words[1]);
        check = true;
      }
    }
    if(check == false){
      writer.println("Error: Could not find name of User / User is not online");
    }

  }

  //returns the PrintWriter object from a specific clientThread
  public PrintWriter getPrintWriter(){
    return this.writer;
  }


}
