import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

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
	String details[];
	Username srvclnt;
	
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
		s.details = new String[8];
		s.start(50000);
		s.listen();
		s.init(args[0]);
		s.handshake();
		s.threads();		
		s.close();
		System.exit(0);
	}	

	public void start(int port) throws Exception{
		srvclnt = new Username();
		System.out.print("Server: Opening port "+port+"...");
		serverConnection = new ServerSocket(port);
		System.out.print("opened.\n");
	}

/*	public String readLine() throws Exception{
		return incoming.readLine();
	}
*/
	
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
	
	public void handshake(){
		try{
			System.out.println("Server: waiting for handshake.");
			clnthash = incoming.readLine();
			if(clnthash.startsWith("hash:")){
				System.out.println("Client hash:"+ clnthash.substring(5));
			}
			outgoing.writeBytes("hash:"+srvhash+"\n");
			clntuser = incoming.readLine();
			if(clntuser.startsWith("username:")){
				System.out.println("You are now talking to :"+ clntuser.substring(9));
			}
			outgoing.writeBytes("username:"+srvuser+"\n");
			clntip = incoming.readLine();
			if(clntip.startsWith("ipaddress:")){
				System.out.println("Client IP Address:"+ clntip.substring(10));
			}
			outgoing.writeBytes("ip:"+srvip+"\n");
			clntport =  incoming.readLine();
			if(clntport.startsWith("port:")){
				System.out.println("Client Port:"+ clntport.substring(5));
			}
			outgoing.writeBytes("port:"+srvport+"\n");
			details[4] = clnthash.substring(5);
			details[5] = clntuser.substring(9);
			srvclnt.setClntUser(clntuser.substring(9));
			details[6] = clntip.substring(10);
			details[7] = clntport.substring(5);
			System.out.println("Handshake ok");
		} catch(Exception e){
			System.out.println("Handshake failed error: " + e);
			System.exit(0);
		}
		
	}
	
	public void init(String username){
		srvip = clientConnection.getLocalAddress().getHostAddress();
		srvport = clientConnection.getLocalPort();
		srvhash = generateHash();
		srvuser = username;
		srvclnt.setSrvUser(srvuser);
		details[0] = srvhash;
		details[1] = srvuser;
		details[2] = srvip;
		details[3] = ""+ srvport;
	}
	
	public String generateHash(){
		try{
		SecureRandom random = new SecureRandom();
		String plaintext = new BigInteger(130, random).toString(32);
		System.out.println("Md5("+plaintext+")=");
		//codefromtheinternet
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(plaintext.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		//pad 0s to achieve 32 chars
		while(hashtext.length()<32){
			hashtext = "0" + hashtext;
		}
		System.out.println(hashtext);
		return hashtext;
		}catch(Exception e){
			System.out.println("Generate  hash error: "+e);
			System.exit(0);
		}
		return null;
	}
	
	public void threads(){
		ReadThread srvreadthread = new ReadThread(this.incoming, "Server", srvclnt);
		WriteThread srvwritethread = new WriteThread(this.outgoing, "Server",srvclnt);
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
