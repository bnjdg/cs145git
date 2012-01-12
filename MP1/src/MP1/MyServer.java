package MP1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private static User_Manager cm = new User_Manager();
    private static boolean first = true;
    public static void main(String args[]){
        try {
            ServerSocket ss = new ServerSocket(8888);
            Socket usr;
            Server_listener sl;
            System.out.println("Waiting for connections in port 8888");
            while(true){
                usr = ss.accept();
                sl = new Server_listener(cm.addClient(usr),cm);
                sl.start();
                first=false;
            }
        } catch (IOException ex) {
            System.out.println("Hey, an error occured!");
        }   
    }
}