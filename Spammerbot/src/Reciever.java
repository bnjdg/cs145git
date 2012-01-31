import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Reciever {
	
	DatagramSocket clientSocket;
	DatagramPacket packet;
	
	
	public Reciever() {
		try {
			clientSocket = new DatagramSocket(60001);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) throws Exception{

		Reciever c = new Reciever();
	
		while(true){
			c.readline();
		}
		
		
		
	}
	
	public void readline() throws Exception{
		byte b[] = new byte[102400];
		packet = new DatagramPacket(b, b.length);
		clientSocket.receive(packet);
		String msg = new String (packet.getData());
		System.out.println(msg);			
	}
	
	
}
