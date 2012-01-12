package MP1;

import java.net.Socket;

/**
 *
 * @author Baguio
 * Manages the User
 */
public class User_Manager {
    private User users;
    private int count = 0;
    public User_Manager(){
        
    }
    
    public User addClient(Socket s){
        count++;
        if (users == null){
            users = new User(s);
            users.setName("Client"+count);
            System.out.println("Added "+users.getName()+" to users");
            this.printUsers();
            return users;
        }else{
            User temp = new User(s);
            temp.setName("Client"+count);
            temp.setNext(users);
            users = temp;
            System.out.println("Added "+users.getName()+" to users");
            //this.printUsers();
            return temp;
        }
    }
    
    public boolean removeUser(String name){
        User temp = users;
        if (temp.getName().equalsIgnoreCase(name)){
            users = users.getNext();
            temp = null;
            this.count--;
            return true;
        }else{
            for (int i=0; i<count;i++){
                if (temp.getNext() != null && temp.getNext().getName().equalsIgnoreCase(name)){
                    temp.setNext(temp.getNext().getNext());
                    this.count--;
                    return true;
                }
            }
            return false;
        }
    }
    
    public User getUsers(){
        return this.users;
    }
    
    public int getClientCount(){
        return this.count;
    }
    
    public void printUsers(){
        User temp = users;
        for (int i=0;i<count;i++){
            System.out.println(temp.getName());
            temp = temp.getNext();
        }
    }
    
    public String getOnline(){
        String msg = "USERS;";
        User temp = users;
        for (int i=0;i<count;i++){
            msg += temp.getName()+":"+temp.getStatus()+";";
            temp = temp.getNext();
        }
        return msg;
    }
}
