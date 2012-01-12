package MP1;

import java.net.Socket;

/**
 *
 * @author Baguio
 * Linked list for clients information
 */
public class User {
    private String name;
    private String status;
    private Socket s;
    private User next;
    private MyConnection con;
    
    public User(Socket s){
        this.s = s;
        this.next = null;
        this.status = "";
    }
    
    public User(String name, String status){
        this.name = name;
        this.status = status;
        this.s = null;
        this.next = null;
    }
    
    public void setName(String n){
        this.name = n;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public void setNext(User n){
        this.next = n;
    }
    
    public void setMyConnection(MyConnection c){
        this.con = c;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public Socket getSocket(){
        return this.s;
    }
    
    public User getNext(){
        return this.next;
    }
    
    public MyConnection getMyConnection(){
        return this.con;
    }
}
