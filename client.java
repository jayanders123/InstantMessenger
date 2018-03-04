package instantMessenger.src;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
	
	/**
	 * author Joshua Anderson
	 */

public class client extends JFrame {

	
	private static final long serialVersionUID = -6871475960495799213L;
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	/**
	 * 
	 * @param host
	 */
	public client (String host) {
		super("Client");
		
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());//This method ensures that whatever is in the text area can be sent to the other person in the conversation.
					userText.setText("");//'Blank' is the default state of the text area.
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	/**
	 * This method starts the execution of the client-side application components.
	 */
	
	
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException) {
			showMessage("\n Client Stopped the connection.");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally {
			closeApp();
		}
	}
	
	/**
	 * Method that connects the client to the server.
	 */
	
	private void connectToServer() throws IOException{
		showMessage("Attempting Connection...\n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);//First parameter: Clients ipAddress  /  Second parameter: port ip address is connected to.
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}
	
	/**
	 * This method initialises streams to carry data.
	 */
	
	private void setupStreams() throws IOException {
		output =  new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Your Streams are now connected!\n");
	}
	
	/**
	 * This method computes "While chatting" components during the conversation.
	 */
	
	private void whileChatting() throws IOException{
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();//Reads the data object as a string.
				showMessage("\n" + message);// so all messages sent appear on the underneath earlier messages.
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n This is not a accepted object type.");
			}
		}while(!message.equals("Server: END"));
	}
	
	/**
	 * This method closes all the streams and sockets when the conversation has ended.
	 */
	
	private void closeApp() {
		showMessage("\n Closing down application");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeApp();
		}
	}
	
	/**
	 * This method sends messages to the server.
	 */
	
	private void sendMessage(String message) {
		try {
			output.writeObject("Client: " + message);//message being sent to the chat window.
			output.flush();
			showMessage("\nClient: " + message);//message appearing inside the chat window.
		}catch(IOException ioException) {
			chatWindow.append("\n An error occured whilst trying to send a message to the server.");
		}
	}
	
	/**
	 * This method updates the chat window whenever prompted with its specified argument.
	 */
	
	private void showMessage(final String m) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(m);
				}
			}
		);
	}
	
	/**
	 * This method gives the user permission to input text inside the text box.
	 */
	private void ableToType(final boolean ToF) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {	
					userText.setEditable(ToF);
				}
			}
		);
	}
}
