import java.io.BufferedReader;

/**
 *
 * @author Benj
 */
public class ReadThread extends Thread{
   BufferedReader instrm; 
   String clntorsrvr;
    
	public ReadThread(BufferedReader instream, String clntorsrvr){
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
					System.out.println("Disconnecting..OK");
					System.exit(0);
					break;
				}
				System.out.print("\r");
				if(clntorsrvr.equals("Client")){
					System.out.print("Server: ");}
				else{
					System.out.print("Client: ");
				}
				System.out.print(msg + "\n" );  
				if(clntorsrvr.equals("Client")){
					System.out.print("Client: ");}
				else {
					System.out.print("Server: ");
				}
				if (msg.equals("KThanksBye!")){
					System.out.println("Disconnecting..OK");
					System.exit(0);
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
