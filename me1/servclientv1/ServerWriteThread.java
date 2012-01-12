import java.io.DataOutputStream;
import java.util.Scanner;
/**
 *
 * @author Benj
 */
public class ServerWriteThread extends Thread{
    String clntorsrvr;
    DataOutputStream outgoing;
    
       public ServerWriteThread( DataOutputStream os ,String clntorsrvr){
         this.outgoing = os;
         this.clntorsrvr = clntorsrvr;
       }
       
       @Override
       public void run(){
        String msg;
		Scanner sc = new Scanner(System.in);
		while (true){
			System.out.print("Server: ");

				try{
				msg = sc.nextLine();
				if (msg == null){
					this.interrupt();
					break;
				}
				System.out.print("\r");
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

