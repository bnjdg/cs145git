import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket serverConnection;
	Socket clientConnection;
	BufferedReader incoming;
	DataOutputStream outgoing;
	String srvhash;
	String srvuser;
	String srvip;
	int srvport;
	String clnthash;
	String clntuser;
	String clntip;
	String clntport;
	ApplicationInfo server;
	ChatGUI srvGUI;

	public static void main(String args[]) throws Exception{
		Server s = new Server();
		try{
			if (args == null){
				System.out.println("Usage: java Server <username>");
				System.exit(0);
			}
		}catch (Exception e){
			System.out.println("Usage: java Server <username>");
			System.exit(0);
		}
		s.server = new ApplicationInfo();
		s.server.setUsername(args[0]);
		s.start(50000);
		s.listen();
		s.close();
	}	

	public void start(int port) throws Exception{
		System.out.print("Server: Opening port "+port+"...");
		serverConnection = new ServerSocket(port);
		System.out.print("opened.\n");
	}

	public void listen() throws Exception{
		while(true){
			System.out.print("Server: Waiting for somebody to connect...");
			clientConnection = serverConnection.accept();
			System.out.println("OK! Somebody connected!!");
			Thread t = new ServerSideThread(clientConnection, server);
			t.start();
		}
	}

	public void close() throws Exception{
		System.out.print("Server: closing...");
		serverConnection.close();
		System.out.print("closed!\n");
	}
	


}
