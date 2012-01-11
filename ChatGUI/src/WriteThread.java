import java.io.DataOutputStream;
/**
 *
 * @author Benj
 */
public class WriteThread{
	String clntorsrvr;
	DataOutputStream outgoing;
	ApplicationInfo server;
	ApplicationInfo client;
	ChatGUI chatUI;
	int isCon = 1;
	
	public WriteThread(DataOutputStream os ,String clntorsrvr, ApplicationInfo server, ApplicationInfo client){
		this.outgoing = os;
		this.clntorsrvr = clntorsrvr;
		this.server = server;
		this.client = client;
	}

	public void sendMsg(String msg){
			try{ 

				chatUI.getTxtArea().append("\r");
				if (msg.endsWith("KThanksBye!")){
					outgoing.writeBytes("disconnect:"+msg+"\n");
					chatUI.getTxtArea().append("Disconnecting..OK");
					chatUI.dispose();
					this.isCon = 0;
					return;
				}
				else if (msg.startsWith("@")){
					outgoing.writeBytes("cusername:"+msg+"\n");
					if(clntorsrvr.equals("Client")){
						client.setUsername(msg.substring(1));
					}else {
						server.setUsername(msg.substring(1));
					}
				} else {
					outgoing.writeBytes("message:"+msg+"\n");
					if(clntorsrvr.equals("Client")){
						chatUI.getTxtArea().append(client.getUsername() + ": " + msg);
						chatUI.getTxtArea().repaint();
					}else {
						chatUI.getTxtArea().append(server.getUsername() + ": " + msg);
						chatUI.getTxtArea().repaint();
					}
				}
		
			}catch (Exception e){
				System.err.println("Error reading input: "+ e);
				return;
			}
		}

	}


