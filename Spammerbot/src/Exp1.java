import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class Exp1 {
	
	DatagramSocket clientSocket;
	DatagramPacket packet;
	
	public Exp1() {
		try {
			clientSocket = new DatagramSocket(60002);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) throws Exception{

		//String ip = "127.0.0.1";
		String ip = "10.40.71.114";
		int port = 60000;
		Exp1 c = new Exp1();
		c.send(ip,port);
		
	}
	
	
	public void send(String ip, int port) throws Exception{
		
	for(int i = 0; i<1000; i++){
		byte b[] = new byte[50000];
		String Message = i + " out of 1000";
		byte b2[] = Message.getBytes();
		
		if (Message.length()==12){
			for (int j = 0; j<12;j++){
				b[j]= b2[j];
		}
			b[12]= 64;
			b[13]= 64;
			b[14]= 64;
			b[15]= 64;
		}
		if (Message.length()==13){
			for (int j = 0; j<13;j++){
				b[j]= b2[j];
			}
			b[13]= 64;
			b[14]= 64;
			b[15]= 64;
		}
		if (Message.length()==14){
			for (int j = 0; j<14;j++){
				b[j]= b2[j];
			}
			b[14]= 64;
			b[15]= 64;
		}
		if (Message.length()==15){
			for (int j = 0; j<15;j++){
				b[j]= b2[j];
			}
			b[15]= 64;
		}
		for (int k = 16; k<50000;k++){
			b[k] = 64;
		}
		System.out.println(b.length);
		packet = new DatagramPacket (b, b.length);
		packet.setAddress(InetAddress.getByName(ip));
		packet.setPort(port);
		clientSocket.send(packet);
		}
	}
	
}
