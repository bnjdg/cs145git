import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
	DatagramSocket serverSocket;
	DatagramPacket packet;
	
	public static void main(String args[]) throws Exception{
		Server s = new Server();
		String name = args[0];
		s.start();
		while(true){
		s.readline(name);
		}
		

		
	}
	
	public void start() throws Exception{
		serverSocket = new DatagramSocket(60000);
		System.out.println("Waiting for messages from port 60000");
		
	}
	
	public void readline(String name) throws Exception{
		byte b[] = new byte[1024];
		packet = new DatagramPacket(b, b.length);
		serverSocket.receive(packet);
		String msg = new String (packet.getData());
		System.out.println(msg);		
		
		if (msg.startsWith("Who is " + name)||msg.startsWith("who is " + name)){
			int port = 60001;
			String ip = packet.getAddress().getHostAddress();
			String message = "I am " + name;
			send(ip, port, message);
		}
		
	}
	
	public void send(String ip, int port, String Message) throws Exception{
		packet = new DatagramPacket (Message.getBytes(), Message.getBytes().length);
		packet.setAddress(InetAddress.getByName(ip));
		packet.setPort(port);
		serverSocket.send(packet);
	}
}
