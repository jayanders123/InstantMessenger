package instantMessenger.src;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * 
 * @author Joshua Anderson
 * 
 * This program contains all logic that computes the chat program.
 * It contains 4 main logic 
 *
 */

public class server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4741570456492137899L;
	private JTextField userText; 
	private JTextArea chatWindow; 
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server; 
	private Socket connection; 
	
	/**
	 * The constructor insures that as soon as this class is run, everything within the constructors automatically runs with it.
	 * 
	 */
	server(){
		super("Server");
		userText = new JTextField();
		userText.setEditable(false); 
		
		userText.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {  
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(500,250);
		setVisible(true);
	}
	
	/**
	 * This method contains the backend (server) of the chat application 
	 */
	public void startRunning() {
		try{ 
			server = new ServerSocket(6789, 100);
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException) {
					showMessage("Server ended the connection.");
				}finally {
					closeApp();
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	/**
	 * This method waits for a connection and then once the connection has been established a message will appear saying "Connection established!".
	 */
	
	 private void waitForConnection() throws IOException {
		 showMessage("Waiting for connection... \n");
		 connection = server.accept();
		 showMessage("You are now connected to" + connection.getInetAddress().getHostName());
	}
	
	/**
	 * This method sets up connection streams so data can be transfered between conversation participants.
	 */
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now established!\n");
	}
	
	/**
	 * This method contains all the "running" code that runs continuously during the conversation. 
	 */

	private void whileChatting() throws IOException{
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("The participant is trying send foreign data to you that is prohibited.");
			}
	  //have conversation
		}while(!message.equals("CLIENT - END")); {
			
		}
	}
	
	/**
	 * This method, after the conversation has been ended, closes all the streams and sockets.
	 */
	
	private void closeApp() {
		showMessage("\n Closing Connectionss... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
			
		}
	}
	/**
	 * This method is used to transport text data to the receiving client whenever it is prompted during the program execution.
	 */
	private void sendMessage(String message) {
		try {
			output.writeObject("Server: " + message);
			output.flush();
			showMessage("\nSERVER: " + message);
		}catch(IOException ioException) {
			chatWindow.append("\n ERROR: Message cannot be sent!");
		}
	}
	
	/**
	 * This method updates the chat window with various messages
	 */
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
						chatWindow.append(text);
				}
			}
		);
	}
	
	/**
	 * This method allows the user to text type text into their text field
	 */
	private void ableToType(final boolean trueOrFalse) {
		SwingUtilities.invokeLater( 
			new Runnable() {
				public void run() {
					userText.setEditable(trueOrFalse);
				}
			}
		);
	}
}
