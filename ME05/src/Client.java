import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
	
	DatagramSocket clientSocket;
	DatagramPacket packet;
	
	
	public Client() {
		try {
			clientSocket = new DatagramSocket(60001);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) throws Exception{

		//String ip = "127.0.0.1";
		String ip = "10.40.71.255";
		int port = 60000;
		String name = args[0];
		String message = "who is " + name + "?";
		Client c = new Client();
		c.send(ip,port,message);
	//	WriteThread t = new WriteThread(ip,port,c);
	//	t.start();
		while(true){
			c.readline();
		}
		
		
		
	}
	
	public void readline() throws Exception{
	
		byte b[] = new byte[1024];
		packet = new DatagramPacket(b, b.length);
		clientSocket.receive(packet);
		String msg = new String (packet.getData());
	//	if (msg.startsWith("I am " + name)||msg.startsWith("i am "+ name)){
			System.out.println(msg + " His/Her IP Address is "+packet.getAddress().getHostAddress());
			System.exit(0);
		//}
			
			
	}
	
	public void send(String ip, int port, String Message) throws Exception{
		packet = new DatagramPacket (Message.getBytes(), Message.getBytes().length);
		packet.setAddress(InetAddress.getByName(ip));
		packet.setPort(port);
		clientSocket.send(packet);
	}
}
