import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class ServerSideThread extends Thread {
	Socket clientConnection;
	BufferedReader incoming;
	DataOutputStream outgoing;
	ApplicationInfo server;
	ApplicationInfo client;
	ChatGUI srvGUI;
	
	public ServerSideThread(Socket clientConnection, ApplicationInfo server){
		this.clientConnection = clientConnection;
		this.server = server;
		client = new ApplicationInfo();
	}
		
	public void run() {
		try {
			init();
			setupStreams();
			handshake();
			threads();		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void init(){
		String srvhash;
		String srvip;
		int srvport;
		srvip = clientConnection.getLocalAddress().getHostAddress();
		srvport = clientConnection.getLocalPort();
		srvhash = generateHash();
		server.setHash(srvhash);
		server.setIpAdd(srvip);
		server.setPort(""+srvport);
	}
	
	public void setupStreams() throws Exception{
		System.out.print("Server: Setting up streams...");
		outgoing = new DataOutputStream(clientConnection.getOutputStream());
		incoming = new BufferedReader(new InputStreamReader (clientConnection.getInputStream()));
		System.out.print("OK\n");
	}

	public void close() throws Exception{
		if (!(clientConnection.isClosed())){
			clientConnection.close();
			System.out.println("Client: "+ client.getUsername() +" disconnected.");
			}
	}

	public void handshake(){
		try{
			String clnthash;
			String clntuser;
			String clntip;
			String clntport;
			System.out.println("Server: waiting for handshake.");
			clnthash = incoming.readLine();
			if(clnthash.startsWith("hash:")){
				System.out.println("Client hash:"+ clnthash.substring(5));
				client.setHash(clnthash.substring(5));
			}
			outgoing.writeBytes("hash:"+server.getHash()+"\n");
			clntuser = incoming.readLine();
			if(clntuser.startsWith("username:")){
				System.out.println("You are now talking to :"+ clntuser.substring(9));
				client.setUsername(clntuser.substring(9));
			}
			outgoing.writeBytes("username:"+server.getUsername()+"\n");
			clntip = incoming.readLine();
			if(clntip.startsWith("ipaddress:")){
				System.out.println("Client IP Address:"+ clntip.substring(10));
				client.setIpAdd(clntip.substring(10));
			}
			outgoing.writeBytes("ip:"+server.getIpAdd()+"\n");
			clntport =  incoming.readLine();
			if(clntport.startsWith("port:")){
				System.out.println("Client Port:"+ clntport.substring(5));
				client.setPort(clntport.substring(5));
			}
			outgoing.writeBytes("port:"+server.getPort()+"\n");
						
			System.out.println("Handshake ok");
		} catch(Exception e){
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
	
	public void formWindowClosing(java.awt.event.WindowEvent evt) throws Exception {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
		close();
    }//GEN-LAST:event_formWindowClosing

	public void threads(){
		srvGUI = new ChatGUI(this.outgoing, "Server", server, client);
		srvGUI.setTitle("Chat: "+server.getUsername()+" - "+client.getUsername());
		srvGUI.runGUI();
		srvGUI.addWindowListener(new java.awt.event.WindowAdapter() {
	            public void windowClosing(java.awt.event.WindowEvent evt) {
	                try {
						formWindowClosing(evt);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });
		while(true){
			try{
				if(clientConnection.isClosed()){
					srvGUI.dispose();
					break;
				}
				if (incoming == null){
					srvGUI.dispose();
					break;
				}
				else{
				String msg = incoming.readLine();
				if (msg == null){
					srvGUI.setTitle("Chat: "+server.getUsername()+" - "+client.getUsername());
					srvGUI.repaint();
					srvGUI.getTxtArea().append("Disconnecting..OK\n");
					srvGUI.getTxtArea().repaint();
					srvGUI.dispose();
					close();
					break;
				}
				srvGUI.getTxtArea().append("\r");
				if (msg.startsWith("disconnect:") || msg.endsWith("KThanksBye!")){
					srvGUI.setTitle("Chat: "+server.getUsername()+" - "+client.getUsername());
					srvGUI.repaint();
						srvGUI.getTxtArea().append(client.getUsername()+": "+ msg.substring(11)+"\n");
						srvGUI.getTxtArea().repaint();
					srvGUI.getTxtArea().append("Disconnecting..OK\n");
					srvGUI.getTxtArea().repaint();
					srvGUI.dispose();
					close();
					break;
				}
				if (msg.startsWith("message:")){
						srvGUI.setTitle("Chat: "+server.getUsername()+" - "+client.getUsername());
						srvGUI.repaint();
						srvGUI.getTxtArea().append(client.getUsername()+": " + msg.substring(8)+"\n");
						srvGUI.getTxtArea().repaint();
								
				}
				if (msg.startsWith("cusername:")||msg.startsWith("cusername:@")){
					System.out.print("\r");
						srvGUI.getTxtArea().append(client.getUsername() + " has changed his\\her username to ");
						client.setUsername(msg.substring(11));
						srvGUI.getTxtArea().append(client.getUsername() +"\n");
						srvGUI.getTxtArea().repaint();
						srvGUI.setTitle("Chat: "+server.getUsername()+" - "+client.getUsername());
						srvGUI.repaint();
				}
				}
			} catch(Exception e){
				//System.err.println("error reading: " + e);
				break;
			}
			
		}

	}
	
}
