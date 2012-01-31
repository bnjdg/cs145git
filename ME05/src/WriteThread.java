import java.util.Scanner;
/**
 *
 * @author Benj
 */
public class WriteThread extends Thread{
	Client c;
	String ip;
	int port;
       public WriteThread(String ip, int port, Client c){
    	   this.c = c;
    	   this.ip = ip;
    	   this.port = port;
       }
       
       @Override
       public void run(){
        String msg;
		Scanner sc = new Scanner(System.in);
		while (true){
			try{
				msg = sc.nextLine();
				System.out.print("\r");
				c.send(ip,port, msg);
			}catch (Exception e){
				System.err.println("Error reading input: "+ e);
				this.interrupt();
				break;
			}
		}
      
	  
	  }
    
    
    
}

