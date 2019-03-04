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

    public void sendFile(File file){
    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
    FileInputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[4096];
    int read;
    while ((read =fis.read(buffer)) > 0) {
      dos.write(buffer,0,read);
    }

    fis.close();
    dos.close();

  }
}
