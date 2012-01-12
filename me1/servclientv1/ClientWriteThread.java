import java.io.DataOutputStream;
import java.util.Scanner;
/**
 *
 * @author Benj
 */
public class ClientWriteThread extends Thread{
    String clntorsrvr;
    DataOutputStream outgoing;
    
       public ClientWriteThread( DataOutputStream os ,String clntorsrvr){
         this.outgoing = os;
         this.clntorsrvr = clntorsrvr;
       }
       
       @Override
       public void run(){
        String msg;
		Scanner sc = new Scanner(System.in);
		while (true){
			
			System.out.print("Client: ");
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
					this.interrupt();
					break;
				}
			}catch (Exception e){
				System.err.println("Error reading input: "+ e);
				this.interrupt();
				break;
			}
		}
      
	  
	  }
    
    
    
}

