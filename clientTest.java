package instantMessenger.src;
import javax.swing.JFrame;
public class clientTest {
	public static void main(String[] args) {
	
		client client;
		client = new client("127.0.0.1");//This ip address refers to the computer that the client is also being run from. Computer running the server program will be viewed as the "server".
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		client.startRunning();
	}
}
