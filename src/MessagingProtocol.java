import java.util.*;
import java.net.*;
import java.io.*;

public class MessagingProtocol{
  private String inputString;
  private String outputString;

  private int WAITING_CONNECTION =0;
  private int CONNECTED =1;
  private int SYNTAX_CHECK =2;
  private int NULL_CHECK =3;
  private int MSG_SENDING =5;
  private int FILE_SYNTAX = 4;

  private int STATE;

//@matt file cat.jpeg

  public MessagingProtocol(){
    STATE = WAITING_CONNECTION;
  }

  public String processInput(String inputString){
      String syntaxErrorString="";
      String nullErrorString="";
      if(STATE == WAITING_CONNECTION){
        if(inputString.equals("Connected to Server"))
          STATE = CONNECTED;
      }

      STATE = SYNTAX_CHECK;
      if(STATE == SYNTAX_CHECK){
        if(SyntaxCheck(inputString)){
          STATE = NULL_CHECK;

        }
        else{
          syntaxErrorString = "Syntax error: Format = @ReceiverName message";
        }
      }

      String[] fileOrMessageCheckString = inputString.split(" ",)

      if(STATE == NULL_CHECK){
          if(nullCheck(inputString))
            STATE = MSG_SENDING;
          else
            nullErrorString = "You have not entered a message";
      }

      //file transfer check
      // String fileHeaderCheck = inputString.substring(1);
      // fileHeaderCheck = fileHeaderCheck.split[" ",2];
      // if(fileHeaderCheck[0].equals("file")){
      //}

      outputString = syntaxErrorString + " " + nullErrorString;
      //System.out.pritln(outputString);
      return outputString;

  }
  public void processOutput(String outputString){

  }

  public int getState(){
    return STATE;
  }
  public void setState(int state){
    this.STATE = state;

  }

  public boolean SyntaxCheck(String atCheck){
    boolean temp = false;
    String symbol = atCheck.substring(0,1);
    //check if tag is there
    if(symbol.equals("@")){
      temp = true;
    }
    return temp;
  }


  public boolean nullCheck(String nullCheck){
    boolean temp = false;

    String[] stringCut = nullCheck.split(" ",2);
    if (stringCut.length == 1)
      return temp;


    stringCut[1] = stringCut[1].trim();

    if(!stringCut[1].equals(""))
      temp = true;

    return temp;
  }

  public boolean fileSyntaxCheck(String fileCheck){
    boolean temp = false;
  }


}
