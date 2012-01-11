import java.io.BufferedReader;

/**
 *
 * @author Benj
 */
public class ReadThread extends Thread{
   BufferedReader instrm; 
   String clntorsrvr;
   ApplicationInfo server;
   ApplicationInfo client;
   ChatGUI chatUI;
	public ReadThread(BufferedReader instream, String clntorsrvr, ApplicationInfo server, ApplicationInfo client, ChatGUI chatUI){
        this.instrm = instream;
		this.clntorsrvr = clntorsrvr;
        this.server = server;
        this.client = client;
        this.chatUI = chatUI;
    }
    
    @Override
    public void run(){
	String msg;
		while(true){
			try{
				msg = instrm.readLine();
				if (msg == null){
					chatUI.getTxtArea().append("Disconnecting..OK\n");
					chatUI.getTxtArea().repaint();
					chatUI.dispose();
					this.interrupt();
					return;
				}
				chatUI.getTxtArea().append("\r");
				if (msg.startsWith("disconnect:") || msg.endsWith("KThanksBye!")){
					if(clntorsrvr.equals("Client")){
						chatUI.getTxtArea().append(server.getUsername() + ": "+ msg.substring(11)+"\n");
						chatUI.getTxtArea().repaint();
					}					
					else{
						chatUI.getTxtArea().append(client.getUsername()+": "+ msg.substring(11)+"\n");
						chatUI.getTxtArea().repaint();
					}
					chatUI.getTxtArea().append("Disconnecting..OK\n");
					chatUI.getTxtArea().repaint();
					chatUI.dispose();
					this.interrupt();
					return;
				}
				if (msg.startsWith("message:")){
					if(clntorsrvr.equals("Client")){
						chatUI.getTxtArea().append(server.getUsername() + ": " + msg.substring(8)+"\n");
						chatUI.getTxtArea().repaint();
						}
					else{
						chatUI.getTxtArea().append(client.getUsername()+": " + msg.substring(8)+"\n");
						chatUI.getTxtArea().repaint();
					}					
				}
				if (msg.startsWith("cusername:")||msg.startsWith("cusername:@")){
					System.out.print("\r");
					if(clntorsrvr.equals("Client")){
						chatUI.getTxtArea().append(server.getUsername() + " has changed his\\her username to ");
						server.setUsername(msg.substring(11));
						chatUI.getTxtArea().append(server.getUsername() +"\n");
						chatUI.getTxtArea().repaint();
						}
					else {
						chatUI.getTxtArea().append(client.getUsername() + " has changed his\\her username to ");
						client.setUsername(msg.substring(11));
						chatUI.getTxtArea().append(client.getUsername() +"\n");
						chatUI.getTxtArea().repaint();
					}
				}
			} catch(Exception e){
				System.err.println("error reading: " + e);
				this.interrupt();
				return;
			}
			
        }
    }
    
}
