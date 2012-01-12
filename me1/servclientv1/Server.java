import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	ServerSocket serverConnection;
	Socket clientConnection;
	BufferedReader incoming;
	DataOutputStream outgoing;


	public static void main(String args[]) throws Exception{
		Server s = new Server();
		s.start(50000);
		s.listen();
		s.threads();		
		s.close();
		System.exit(0);
	}	

	public void start(int port) throws Exception{
		System.out.print("Server: Opening port "+port+"...");
		serverConnection = new ServerSocket(port);
		System.out.print("opened.\n");
	}

	public String readLine() throws Exception{
		return incoming.readLine();
	}

	public void listen() throws Exception{
		System.out.print("Server: Waiting for somebody to connect...");
		clientConnection = serverConnection.accept();
		System.out.println("OK! Somebody connected!!");
		this.setupStreams();
	}
	
/*	public void send() throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Server: ");
		outgoing.writeBytes(sc.nextLine()+"\n");
		//System.out.println("SENT");
	}
*/

	public void setupStreams() throws Exception{
		System.out.print("Server: Setting up streams...");
		outgoing = new DataOutputStream(clientConnection.getOutputStream());
		incoming = new BufferedReader(new InputStreamReader (clientConnection.getInputStream()));
		System.out.print("OK\n");
	}

	public void close() throws Exception{
		System.out.print("Server: closing...");
		clientConnection.close();
		serverConnection.close();
		System.out.print("closed!\n");
	}
	
	public void threads(){
		ServerReadThread srvreadthread = new ServerReadThread (this.incoming, "Server");
		ServerWriteThread srvwritethread = new ServerWriteThread(this.outgoing, "Server");
		srvwritethread.start();
		srvreadthread.start();
		try{
		
		while(true){
			if(srvreadthread.isInterrupted()){
				srvreadthread.interrupt();
				srvwritethread.interrupt();
				break;
			}
			if(srvwritethread.isInterrupted()){
				srvreadthread.interrupt();
				srvwritethread.interrupt();
				break;
			}
		}
			srvreadthread.interrupt();
			srvwritethread.interrupt();
		} catch ( Exception e){
			System.err.println("Thread stopped");
		}
		
	}
	

}
