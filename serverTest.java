package instantMessenger.src;
import javax.swing.JFrame;

public class serverTest {
	
	public static void main(String[] args) {
		server chatServer = new server();
		chatServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatServer.startRunning();
	}
}
