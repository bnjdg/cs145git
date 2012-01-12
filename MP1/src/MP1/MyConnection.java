package MP1;

import java.io.*;
import java.net.*;

/**
 * CS 145 ME 1
 * @author Baguio
 */
public class MyConnection {
    Socket connection;
    public MyConnection(Socket s){
        this.connection = s;
    }
    
    /**
     * Accepts a String as input and sends that string msg to the socket
     * @param String msg
     * @return 
     */
    public boolean sendMessage(String msg){
        try {
            /* create BufferedOutputStream and OutputStreamWriter */
            BufferedOutputStream b = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(b,"US-ASCII");
            
            /* add (char)13 to the end of the msg to signify end of message*/
            //msg += (char)13;
            msg += (char)13;
            
           /* write to the socket and flush the buffer */
            osw.write(msg);
            osw.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public String getMessage(){
        StringBuffer msg_buf = new StringBuffer();
        try{
            /* create BufferedInputStream and InputStremReader */
            BufferedInputStream i = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(i, "US-ASCII");
            
            /* read the socket's InputStream */
            int c=0;
            while((c=isr.read()) != (char)13)
                msg_buf.append((char)c);
            
            String msg = msg_buf.toString();
            /* closes the connection */
            return msg;
        }catch(IOException e){
            return "";
        }
    }
}
