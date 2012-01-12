import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;


public class Client extends Thread {
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
		Client t = new Client();
		try{
			if (args == null){
				System.out.println("Usage: java Client <ip-address> <username>");
				System.exit(0);
			}
			
		}catch (Exception e){
			System.out.println("Usage: java Client <ip-address> <username>");
			System.exit(0);
		}
		t.ipaddress = args[0];
		t.clntuser = args[1];
		t.start();
	}

	public void run(){
	
		port = 50000;
		try {
			getip(ipaddress);
			setupStreams();
			init();
			handshake();
			threads();
			disconnect();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.exit(0);

	}
	
	public void init(){
		client = new ApplicationInfo();
		clntip = clientConnection.getLocalAddress().getHostAddress();
		clntport = clientConnection.getLocalPort();
		clnthash = generateHash();
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
		if (!(clientConnection.isClosed()))
		clientConnection.close();
		System.out.println("closed!");
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
	
   public void formWindowClosing(java.awt.event.WindowEvent evt) throws Exception {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
	   disconnect();
	   System.exit(0);
    }//GEN-LAST:event_formWindowClosing
	
	public void threads(){
		clntGUI = new ChatGUI(this.outgoing, "Client", server, client);
		clntGUI.setTitle("Chat: "+client.getUsername()+" - "+server.getUsername());
		clntGUI.runGUI();
        clntGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
					formWindowClosing(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

		
		String msg;
		while(true){
			try{
				if(clientConnection.isClosed()){
					clntGUI.dispose();
					break;
				}
				if (incoming == null){
					clntGUI.dispose();
					break;
				}
				msg = incoming.readLine();
				if (msg == null){
					clntGUI.setTitle("Chat: "+client.getUsername()+" - "+server.getUsername());
					clntGUI.repaint();
					clntGUI.getTxtArea().append("Disconnecting..OK\n");
					clntGUI.getTxtArea().repaint();
					clntGUI.dispose();
					break;
				}
				clntGUI.getTxtArea().append("\r");
				if (msg.startsWith("disconnect:") || msg.endsWith("KThanksBye!")){
					clntGUI.setTitle("Chat: "+client.getUsername()+" - "+server.getUsername());
					clntGUI.repaint();	
					clntGUI.getTxtArea().append(server.getUsername() + ": "+ msg.substring(11)+"\n");
						clntGUI.getTxtArea().repaint();
					clntGUI.getTxtArea().append("Disconnecting..OK\n");
					clntGUI.getTxtArea().repaint();
					clntGUI.dispose();
					break;
				}
				if (msg.startsWith("message:")){
						clntGUI.setTitle("Chat: "+client.getUsername()+" - "+server.getUsername());
						clntGUI.repaint();
						clntGUI.getTxtArea().append(server.getUsername() + ": " + msg.substring(8)+"\n");
						clntGUI.getTxtArea().repaint();
										
				}
				if (msg.startsWith("cusername:")||msg.startsWith("cusername:@")){
					System.out.print("\r");
						clntGUI.getTxtArea().append(server.getUsername() + " has changed his\\her username to ");
						server.setUsername(msg.substring(11));
						clntGUI.getTxtArea().append(server.getUsername() +"\n");
						clntGUI.getTxtArea().repaint();
						clntGUI.setTitle("Chat: "+client.getUsername()+" - "+server.getUsername());
						clntGUI.repaint();
						
				}
			} catch(Exception e){
				//System.err.println("error reading: " + e);
				break;
			}
	
	}
	
	
 }
}

