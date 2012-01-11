import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
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
	ApplicationInfo srvclnt;
	ApplicationInfo server;
	ApplicationInfo client;
	ChatGUI clntGUI;

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

	public void init(String[] args){
		client = new ApplicationInfo();
		clntip = clientConnection.getLocalAddress().getHostAddress();
		clntport = clientConnection.getLocalPort();
		clnthash = generateHash();
		clntuser = args[1];
		client.setUsername(clntuser);
		client.setHash(clnthash);
		client.setIpAdd(clntip);
		client.setPort(""+ clntport);
	}
	
	public void getip(String ipd) throws Exception{
		server = new ApplicationInfo();
		server.setIpAdd(ipd);
		System.out.println(ipd);
		connect(ipd, port);
	}
	
	public String readLine() throws Exception{
		return incoming.readLine();
	}

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
			outgoing.writeBytes("hash:"+client.getHash()+"\n");
			srvhash = incoming.readLine();
			if(srvhash.startsWith("hash:")){
				System.out.println("Server hash:"+ srvhash.substring(5));
				server.setHash(srvhash.substring(5));
			}
			outgoing.writeBytes("username:"+client.getUsername()+"\n");
			srvuser = incoming.readLine();
			if(srvuser.startsWith("username:")){
				System.out.println("You are now talking to :"+ srvuser.substring(9));
				server.setUsername(srvuser.substring(9));
			}
			
			outgoing.writeBytes("ip:"+client.getIpAdd()+"\n");
			srvip = incoming.readLine();
			if(srvip.startsWith("ipaddress:")){
				System.out.println("Server IP Address:"+ srvip.substring(10));
				server.setIpAdd(srvip.substring(10));
			}
			
			outgoing.writeBytes("port:"+client.getPort()+"\n");
			srvport =  incoming.readLine();
			if(srvport.startsWith("port:")){
				System.out.println("Server Port:"+ srvport.substring(5));
				server.setPort(srvport.substring(5));
			}
			
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
		clntGUI = new ChatGUI(this.outgoing, "Client", server, client);
		ReadThread clntreadthread = new ReadThread(this.incoming, "Client", server, client, clntGUI);
		//WriteThread clntwritethread = new WriteThread(this.outgoing, "Server", server, client, clntGUI);
		//clntGUI.writethread = clntwritethread;
		//clntwritethread.start();
		clntreadthread.start();
		clntGUI.runGUI();
		try{
			while(true){
			if(clntreadthread.isInterrupted()){
				return;			
				}
			if (clntGUI == null){
				return;
			}
		   }
		} catch ( Exception e){
			System.err.println("Thread stopped");
		}
	}
	
	
}


