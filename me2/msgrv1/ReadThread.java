import java.io.BufferedReader;

/**
 *
 * @author Benj
 */
public class ReadThread extends Thread{
   BufferedReader instrm; 
   String clntorsrvr;
   Username srvclnt;
    //details[] = srvhash,srvclnt.getSrvUser,srvip,srvport,clnthash,srvclnt.getClntUser,clntip,clntport
	public ReadThread(BufferedReader instream, String clntorsrvr, Username srvclnt){
        this.instrm = instream;
		this.clntorsrvr = clntorsrvr;
        this.srvclnt = srvclnt;
    }
    
    @Override
    public void run(){
	String msg;
		while(true){
			try{
				msg = instrm.readLine();
				
				if (msg == null){
					System.out.println("Disconnecting..OK");
					System.exit(0);
					break;
				}
				System.out.print("\r");
				if(clntorsrvr.equals("Client")){
					System.out.print(srvclnt.getSrvUser() + ": ");}
				else{
					System.out.print(srvclnt.getClntUser()+": ");
				}
				if (msg.startsWith("disconnect:") || msg.endsWith("KThanksBye!")){
					System.out.println(msg.substring(11));
					System.out.println("Disconnecting..OK");
					System.exit(0);
					break;
				}
				if (msg.startsWith("message:")){
					System.out.println(msg.substring(8));					
				}
				if (msg.startsWith("cusername:")||msg.startsWith("cusername:@")){
					System.out.print("\r");
					if(clntorsrvr.equals("Client")){
						System.out.print(srvclnt.getSrvUser() + " has changed his\\her username to ");
						}
					else {
						System.out.print(srvclnt.getClntUser() + " has changed his\\her username to ");
						}
					System.out.println(msg.substring(11));
					if(clntorsrvr.equals("Client")){
						srvclnt.setSrvUser (msg.substring(11));
					}else {
						srvclnt.setClntUser(msg.substring(11));
					}
				}
				if(clntorsrvr.equals("Client")){
					System.out.print(srvclnt.getClntUser() + ": ");}
				else {
					System.out.print(srvclnt.getSrvUser() +": ");
				}
				
			} catch(Exception e){
				System.err.println("error reading: " + e);
				this.interrupt();
				break;
			}
			
        }
    }
    
}
