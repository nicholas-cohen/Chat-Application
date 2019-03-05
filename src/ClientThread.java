import java.util.*;
import java.net.*;
import java.io.*;

public class ClientThread extends Thread{

  private String name;
  private Socket socket;
  private Server server;
  private PrintWriter writer;
  private OutputStream output = null;
  private InputStream input = null;
  private byte[] byteArray;


  public ClientThread(Socket socket, Server server) {
    this.socket = socket;
    this.server = server;
  }

  public void run(){
      try{

        //get input from the reader thread of that client over that socket
        input = socket.getInputStream();
        //read input from the writer thread of client over that socket
        BufferedReader read = new BufferedReader(new InputStreamReader(input));


        //writes to the clients writer thread over current socket
        output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
        //reads input from client and adds the input as a name to the server client list
        String clientName =  read.readLine();
        name = clientName;
        server.addClientName(clientName);

        //Prints the names on online user
        onlineClients();


        String message;
        //System.out.println(name);

        /* Continuously checks the message sent by the client and executes
        the method that sends it to the destination client.  Quits if user enters: Exit.
        */
        do{
          message = read.readLine();
          String[] messageWords = message.split(" ",2);
          if(fileCheck(message)){
            readClientBytes();
            sendMessage(messageWords[0]+" "+"You are currently receiving a file");
            sendFile(message);
          }
          else
            sendMessage(message);
        }
        while((message != "Exit"));




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

  /*
  This method is responsible for sending the message from one client to another.

  It splits the message into an array of size 2, checks the first element for the destination
  name and sends to that client the second element of the array (the message)
  */
  public void sendMessage(String message){

    String[] words = message.split(" ",2);

    String destinationName = words[0].substring(1);

    words[1] = words[1].trim();
    //System.out.println(words[1]);
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

  public OutputStream getThreadOutputStream(){
    return output;
  }

  public boolean fileCheck(String check){
    boolean temp= false;
    String[] words = check.split(" ",3);
    if(words[1].equals("$file"))
      temp = true;
    return temp;
  }

  public void sendFile(String message){
    System.out.println("System sending file");
    String[] words = message.split(" ",2);

    String destinationName = words[0].substring(1);

    words[1] = words[1].trim();
    System.out.println(words[1]);
    boolean check = false;
    for(int i =0;i<server.clientThreads.size();i++){
      if(server.clientThreads.get(i).getUserName().equals(destinationName)){
        try{
          server.clientThreads.get(i).getThreadOutputStream().write(byteArray,0,byteArray.length);
          System.out.println("System writing to client");
        }catch(IOException ex){
          System.out.println("Error writing to server: " + ex.getMessage());
        }
        check = true;
        System.out.println("Succesfully sent it");
        System.out.println(byteArray.toString());
      }
    }
    if(check == false){
      writer.println("Error: Could not find name of User / User is not online");
    }

  }

  public void readClientBytes(){

    try{
      int current=0;
      byteArray = new byte[4096];

      int bytesRead = input.read(byteArray,0,4096);
      current = bytesRead;

      /*do{
        bytesRead = input.read(byteArray, current, (byteArray.length-current));
        if(bytesRead >= 0)
          current += bytesRead;

      }while(bytesRead > -1);*/
    }catch(IOException ex){
      System.out.println("Error writing to server: " + ex.getMessage());
    }
  }

}
