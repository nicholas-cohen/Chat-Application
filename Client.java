import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;

public class Client{
	public static final int PORT_NO = 2000;


	public static void main(String[] args) throws IOException {
		if(args.length != 1){
			System.err.println("Pass the server IP");
			return;
		}

		String serverIP = args[0];
		Socket socket = new Socket(serverIP,PORT_NO);
		Scanner scInput = new Scanner(socket.getInputStream());
		System.out.println("Server Response:	"+ scInput.nextLine());
	}
}