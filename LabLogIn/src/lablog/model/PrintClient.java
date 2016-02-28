package lablog.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.collections.ObservableList;
import printserver.PrintServer;

public interface PrintClient {
	
	/**
	 * For connecting to printer server and printing ObservableList<String>
	 * and send it to printer server.
	 * Closes client server and socket after use.
	 * @param listEntries
	 * @return a date string from the printer server.
	 */
	public static String printToServer(String description, ObservableList<String> entries) {

			connectToServer();
			
			DataInputStream fromServer = null;
			DataOutputStream printServer = null;
			Socket socket = null;
			String returnMessage = null;

			try {
				socket = new Socket("localhost", 8011);
				fromServer = new DataInputStream(socket.getInputStream());
				printServer = new DataOutputStream(socket.getOutputStream());

				String loggedIn = description + "\n\n";
				for (String string : entries) {
					loggedIn += string + "\n";
				}

				printServer.writeUTF(loggedIn);
				printServer.flush();
				
				returnMessage = fromServer.readUTF();
				
				fromServer.close();
				socket.close();
									
			} catch (IOException e) {
				e.printStackTrace();
			}

			return returnMessage;
		}
	

	/**
	 * Opens a new thread and connect to printer server.
	 */
	public static void connectToServer() {
		Thread thread = new Thread(() -> {
			new PrintServer();
		});
		thread.start();
	}
}
