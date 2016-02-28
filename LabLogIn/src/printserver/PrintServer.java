package printserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class PrintServer {

	public PrintServer() {
		connectToClient();
	}

	private void connectToClient() {

		ServerSocket serverSocket;
		
			try {
				serverSocket = new ServerSocket(8011);
				System.out.println("Prints Server started at: " + new Date());
				Socket socket = serverSocket.accept();
				
				while (true) {
				DataInputStream inputStream = new DataInputStream(socket.getInputStream());
				DataOutputStream outputStram = new DataOutputStream(socket.getOutputStream());
				
					String printOut = inputStream.readUTF();
					System.out.println("Printing data from client");
					System.out.println(printOut);
					outputStram.writeUTF("" + new Date());
					outputStram.flush();
					serverSocket.close();
				}
			} catch (EOFException eof) {
				System.out.println("(End of file exception occured)");
			} catch (IOException ex) {
					ex.printStackTrace();
			}	
	}
}
