import java.io.BufferedReader;

/**
 *
 * @author Benj
 */
public class ServerReadThread extends Thread{
   BufferedReader instrm; 
   String clntorsrvr;
    
	public ServerReadThread(BufferedReader instream, String clntorsrvr){
        this.instrm = instream;
		this.clntorsrvr = clntorsrvr;
    }
    
    @Override
    public void run(){
	String msg;
		while(true){
		
			
			try{
				msg = instrm.readLine();
		
				if (msg == null){
					this.interrupt();
					break;
				}
				System.out.print("\r");
				System.out.print("Client: ");
				System.out.print(msg + "\nServer: " );  
				if (msg.equals("KThanksBye!")){
					this.interrupt();
					break;
				}
			} catch(Exception e){
				System.err.println("error reading: " + e);
				this.interrupt();
				break;
			}
			
        }
    }
    
}
