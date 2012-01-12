import java.io.DataOutputStream;
import java.util.Scanner;
/**
 *
 * @author Benj
 */
public class WriteThread extends Thread{
    String clntorsrvr;
    DataOutputStream outgoing;
    
       public WriteThread(DataOutputStream os ,String clntorsrvr){
         this.outgoing = os;
         this.clntorsrvr = clntorsrvr;
       }
       
       @Override
       public void run(){
        String msg;
		Scanner sc = new Scanner(System.in);
		while (true){
			if(clntorsrvr.equals("Client")){
			System.out.print("Client: ");}
				else 
				System.out.print("Server: ");
			try{
				msg = sc.nextLine();
			
				if (msg == null){
					this.interrupt();
					break;
				}
				System.out.print("\r");
			//	System.out.println("Client: "+ msg);
				outgoing.writeBytes(msg+"\n");	
				if (msg.equals("KThanksBye!")){
					System.out.println("Disconnecting..OK");
					System.exit(0);
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

