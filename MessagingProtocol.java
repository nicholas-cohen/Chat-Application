import java.util.*;
import java.net.*;
import java.io.*;

public class MessagingProtocol{
  private String inputString;
  private String outputString;

  private static final int BANDWIDTH_CONSTRAINT=140; //number of characters

  private int WAITING_CONNECTION =0;
  private int CONNECTED =1;
  private int SYNTAX_CHECK =2;
  private int NULL_CHECK =3;
  private int MSG_SENDING =5;
  private int FILE_SYNTAX = 4;
  private int FILE_SENDING = 6;
  private int EXITING = 7;
  private int FETCHING_USERS = 8;
  private int BANDWIDTH_CHECK = 9;
  private int AWAITING_FILE_RESPONSE = 10;
  private int FILE_ACCEPTED=11;
  private int FILE_REJECTED=12;
  private int STATE;

//@matt file cat.jpeg

  public MessagingProtocol(){
    STATE = WAITING_CONNECTION;
  }

  public String processInput(String inputString){
      String syntaxErrorString="";
      String nullErrorString="";
      String fileError="";
      String bandwidthString = "";

      if(inputString.equals("Exit")){
        STATE=EXITING;
        return outputString;
      }
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
      if(STATE == NULL_CHECK){
          if(nullCheck(inputString))
            STATE = BANDWIDTH_CHECK;
          else
            nullErrorString = "You have not entered a message";
      }

      String[] fileOrMessageCheckString=null;
      if(STATE==BANDWIDTH_CHECK){
        fileOrMessageCheckString = inputString.split(" ",2);
        if(fileOrMessageCheckString[1].substring(0,1).equals("$"))
          STATE = FILE_SYNTAX;
        else
          STATE = BANDWIDTH_CHECK;

        if(STATE == FILE_SYNTAX){
            if(fileSyntaxCheck(inputString))
              STATE = FILE_SENDING;
            else
              fileError = "File does not exist or it is not a file";
        }
      }


      if(STATE==BANDWIDTH_CHECK){
        if(bandwidthCheck(inputString))
          STATE = MSG_SENDING;
        else
          bandwidthString = "Your message is too long";
      }


      outputString = syntaxErrorString + " " + nullErrorString+" "+fileError+" "+bandwidthString;
      //System.out.pritln(outputString);
      return outputString;


  }
  public void processOutput(String outputString){
      String check = outputString.substring(0,1);
      if(check.equals("A")){
        STATE = 8;
      }
  }

  public boolean bandwidthCheck(String message){
    boolean check = true;
    String[] fileOrMessageCheckString = message.split(" ",2);
    if(!fileOrMessageCheckString[1].substring(0,1).equals("$")){
      byte[] byteArray = fileOrMessageCheckString[1].getBytes();
      if (byteArray.length>BANDWIDTH_CONSTRAINT)
        check = false;
    }
    return check;
  }

  public int getState(){
    return STATE;
  }
  public void setState(int state){
    this.STATE = state;

  }

  public boolean SyntaxCheck(String atCheck){
    boolean temp = false;
    if(atCheck.equals("")){
      return temp;
    }
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
    String syntaxString[] = fileCheck.split(" ",3);
    if(syntaxString[1].equals("$file"))
      temp = true;
    
    return temp;
  }


}
