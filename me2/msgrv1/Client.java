import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Client {
	Socket clientConnection;
	DataOutputStream outgoing;
	BufferedReader incoming;
	String srvhash;
	String srvuser;
	String srvip;
	String srvport;
	String clnthash;
	String clntuser;
	String clntip;
	int clntport;
	int port;
	String details[];
	String ipaddress;
	Username srvclnt;
	//srvhash,srvuser,srvip,srvport,clnthash,clntuser,clntip,clntport

	public static void main(String args[]) throws Exception{
		Client c = new Client();
		try{
			if (args == null){
				System.out.println("Usage: java Client <ip-address> <username>");
				System.exit(0);
			}
			
		}catch (Exception e){
			System.out.println("Usage: java Client <ip-address> <username>");
			System.exit(0);
		}
		c.port = 50000;
		c.getip(args[0]);
		c.details = new String[8];
		
		c.setupStreams();
		c.init(args);
		c.handshake();
		c.threads();
		c.disconnect();
		System.exit(0);
	}

	public void getip(String ipd) throws Exception{
		srvclnt = new Username();
		ipaddress = ipd;
		System.out.println(ipd);
		connect(ipaddress, port);
	}
	
	public String readLine() throws Exception{
		return incoming.readLine();
	}
/*	
	public void send() throws Exception{
		Scanner c = new Scanner(System.in);
		System.out.print("Client: ");
		outgoing.writeBytes(c.nextLine()+"\n");
		//System.out.println("SENT");
	}
*/
	public void disconnect() throws Exception{
		System.out.print("Client: closing...");
		clientConnection.close();
		System.out.print("closed!\n");
	}

	public void connect(String ipaddress, int port) throws Exception{
		System.out.print("Client: Connecting to "+ipaddress+":"+port+"...");
		clientConnection = new Socket(ipaddress, port);
		System.out.print("Connected!!\n");
	}

	public void setupStreams() throws Exception{
		System.out.print("Setting up streams...");
		outgoing = new DataOutputStream(clientConnection.getOutputStream());
		incoming = new BufferedReader(new InputStreamReader (clientConnection.getInputStream()));
		System.out.print("OK!\n");		
	}

	public void handshake(){
		try{
			System.out.println("Client: starting handshake.");
			outgoing.writeBytes("hash:"+clnthash+"\n");
			srvhash = incoming.readLine();
			if(srvhash.startsWith("hash:")){
				System.out.println("Server hash:"+ srvhash.substring(5));
			}
			
			outgoing.writeBytes("username:"+clntuser+"\n");
			srvuser = incoming.readLine();
			if(srvuser.startsWith("username:")){
				System.out.println("You are now talking to :"+ srvuser.substring(9));
			}
			
			outgoing.writeBytes("ip:"+clntip+"\n");
			srvip = incoming.readLine();
			if(srvip.startsWith("ipaddress:")){
				System.out.println("Server IP Address:"+ srvip.substring(10));
			}
			
			outgoing.writeBytes("port:"+clntport+"\n");
			srvport =  incoming.readLine();
			if(srvport.startsWith("port:")){
				System.out.println("Server Port:"+ srvport.substring(5));
			}
			details[0] = srvhash.substring(5);
			details[1] = srvuser.substring(9);
			details[2] = srvip.substring(10);
			details[3] = srvport.substring(5);
			srvclnt.setSrvUser(srvuser.substring(9));
			System.out.println("Handshake ok");
		}catch(Exception e){
			System.out.println("Handshake failed error: " + e);
			System.exit(0);
		}
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
	
	
	public void init(String[] args){
	
		clntip = clientConnection.getLocalAddress().getHostAddress();
		clntport = clientConnection.getLocalPort();
		clnthash = generateHash();
		clntuser = args[1];
		srvclnt.setClntUser(clntuser);
		details[4] = clnthash;
		details[5] = clntuser;
		details[6] = clntip;
		details[7] = ""+ clntport;
		
		
	}
	
	public void threads(){
		ReadThread clntreadthread = new ReadThread (this.incoming, "Client",srvclnt);
		WriteThread clntwritethread = new WriteThread(this.outgoing, "Client",srvclnt);
				
		clntwritethread.start();
		clntreadthread.start();
		try{
			while(true){
			if(clntreadthread.isInterrupted()){
				clntreadthread.interrupt();
				clntwritethread.interrupt();
				break;
			}
			if(clntwritethread.isInterrupted()){
				clntreadthread.interrupt();
				clntwritethread.interrupt();
				break;
			}
			}
			clntreadthread.interrupt();
			clntwritethread.interrupt();
		} catch ( Exception e){
			System.err.println("Thread stopped");
		}
	}
	
	
}
