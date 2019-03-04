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
    String text;


    do{
      text = console.readLine("[" + clientName + "]: ");

      String inputCheck = client.getMessagingProtocol().processInput(text);
      int state = client.getMessagingProtocol().getState();
      if(state < 5){
          System.out.println(inputCheck);
          client.getMessagingProtocol().setState(1);
      }
      else if(state == 6){
        String[] tempStringArray = text.split(" ",3);
        sendFile(tempStringArray[2]);
      }

      else{
        writer.println(text);
        client.getMessagingProtocol().setState(1);
      }
    }while(!text.equals("Exit"));

    try{
      socket.close();
    }catch (IOException ex){
      System.out.println("Error writing to server: " + ex.getMessage());
    }
  }


  public void sendFile(String file){
      FileInputStream fis =null;
      BufferedInputStream bis = null;
      OutputStream os = null;
      try{
        File myFile = new File (file);
        System.out.println(file);
          byte [] mybytearray  = new byte [(int)myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);
          os = socket.getOutputStream();
          System.out.println("Sending " + file + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray,0,mybytearray.length);
          os.flush();
          System.out.println("Done.");
      }catch(IOException e){
        System.out.println("Input Error: "+e.getMessage());
      }

    }
}
