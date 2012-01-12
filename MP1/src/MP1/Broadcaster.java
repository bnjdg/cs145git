package MP1;

/**
 *
 * @author Baguio
 */
public class Broadcaster extends Thread{
    private String msg;
    private User dest;
    private User_Manager users;
    private boolean all;
    
    /**
     * broadcasts to a specific person only
     * @param msg
     * @param usr 
     */
    
    public Broadcaster(String msg, User usr){
        this.all = false;
        this.msg = msg;
        this.dest = usr;
    }
    
    /**
     * broadcasts to all
     * @param msg
     * @param um 
     */
    public Broadcaster(String msg, User_Manager um){
        this.all = true;
        this.msg = msg;
        this.users = um;
    }
    
    @Override
    public void run() {
        if (all){
            broadcast();
        }else{
            whisper();
        }
        this.interrupt();
    }
    
    public void broadcast(){
        User temp = users.getUsers();
        for (int i=0;i<users.getClientCount();i++){
            temp.getMyConnection().sendMessage(msg);
            temp = temp.getNext();            
        }
        users = null;
        System.out.println("*to ALL* "+msg);
    }    
    
    public void whisper(){
        dest.getMyConnection().sendMessage(msg);
        System.out.println("*to "+dest.getName()+"* "+msg);
        dest = null;
    }
}
