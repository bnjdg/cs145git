import java.io.DataOutputStream;
import java.util.Scanner;
/**
 *
 * @author Benj
 */
public class WriteThread extends Thread{
    String clntorsrvr;
    DataOutputStream outgoing;
    Username srvclnt;
    	//details[] = srvhash,srvclnt.getSrvUser,srvip,srvport,clnthash,srvclnt.getClntUser,clntip,clntport
       public WriteThread(DataOutputStream os ,String clntorsrvr, Username srvclnt){
         this.outgoing = os;
         this.clntorsrvr = clntorsrvr;
         this.srvclnt = srvclnt;
       }
       
       @Override
       public void run(){
        String msg;
		Scanner sc = new Scanner(System.in);
		while (true){
			if(clntorsrvr.equals("Client")){
			System.out.print(srvclnt.getClntUser() +": ");}
				else 
				System.out.print(srvclnt.getSrvUser() +": ");
			try{
				msg = sc.nextLine();
				System.out.print("\r");
							
				if (msg.endsWith("KThanksBye!")){
					outgoing.writeBytes("disconnect:"+msg+"\n");
					System.out.println("Disconnecting..OK");
					System.exit(0);
				}
				else if (msg.startsWith("@")){
					outgoing.writeBytes("cusername:"+msg+"\n");
					if(clntorsrvr.equals("Client")){
						srvclnt.setClntUser(msg.substring(1));
					}else {
						srvclnt.setSrvUser(msg.substring(1));
					}
				}
				else{
					outgoing.writeBytes("message:"+msg+"\n");
				}
				if (msg==null){
					System.out.println("Disconnecting..OK");
					System.exit(0);
				}
			}catch (Exception e){
				System.err.println("Error reading input: "+ e);
				this.interrupt();
				break;
			}
		}
      
	  
	  }
    
    
    
}

