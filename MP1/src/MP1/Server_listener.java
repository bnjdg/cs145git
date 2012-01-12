package MP1;

/**
 * 
 * @author Baguio
 * listens for commands and messages sent from client
 */
public class Server_listener extends Thread{    
    private User myUser;
    private MyConnection myCon;
    private User_Manager um;
    private String msg_to_self;
    private boolean done = false;
    private Broadcaster b;
    
    public Server_listener(User usr, User_Manager um){
        this.myUser = usr;
        this.myCon = new MyConnection(usr.getSocket());
        this.myUser.setMyConnection(myCon);
        this.um = um;
        String msg = "[Server Message]: "+usr.getName() + " has connected!";
        broadcast(msg);
    }
    
    @Override
    public void run(){
        String msg;
        while(!done){
            msg = myCon.getMessage();
            System.out.println("From "+myUser.getName()+" *"+msg+"*");
            if(msg.startsWith("/")){
                extractCommand(msg);
            }else if (msg.equals("GET_USERS")){
                getOnline();
                um.printUsers();
            }else if (msg.equals("GET_MYNAME")){
                msg_to_self = "YOUR_NAME " + myUser.getName();
                sendToSelf();
            }else if (!msg.equals("") || !msg.equals(" ") || !msg.equals("\n")){
                broadcast("[ "+myUser.getName()+" ]: " +msg);
            }
        }
    }
    
    /**
     * extracts the command from client
     * @param msg 
     */
    private void extractCommand(String msg){
        /* command from user */
        try{
            if (msg.substring(0, 5).equalsIgnoreCase("/quit")){
                if (um.removeUser(myUser.getName())){
                    broadcast("[Server Message]: " + myUser.getName() + " has disconnected!");
                    done = true;
                    this.interrupt();
                }
            }else if (msg.substring(0,9).equalsIgnoreCase("/whisper ")){
                String name="", msg2;
                int i;
                for (i=9;i<msg.length();i++){
                    if (msg.charAt(i) != ' '){
                        name += msg.charAt(i);
                    }else
                        break;
                }
                msg2 = "[[ "+myUser.getName()+" ]]: " + msg.substring(i+1);
                whisper(msg2,name);
            }else if (msg.substring(0,12).equalsIgnoreCase("/changename ")){
                if (validName(msg.substring(12)))
                    setClientName(msg.substring(12));
                else
                    sendToSelf();
            }else if (msg.substring(0,14).equalsIgnoreCase("/changestatus ")){
                clientChangeStatus(msg.substring(14));
            }else{
                msg_to_self = "[Server Message]: Invalid Command! *"+msg+"*";
                sendToSelf();
            }
        }catch(StringIndexOutOfBoundsException e){
            msg_to_self = "[Server Message]: Invalid Command!";
            sendToSelf();
        }
    }
    
    private void setClientName(String name){
        String msg = "[Server Message]: " + myUser.getName() 
                +" has changed name to '" + name+"'";
        this.myUser.setName(name);
        broadcast(msg);
    }
    
    private void clientChangeStatus(String status){
        String msg = "[Server Message]: " + myUser.getName()
                +" has change status to '" + status+"'";
        this.myUser.setStatus(status);
        broadcast(msg);
    }
    
    /**
     * sends message to ALL users
     * @param msg  - message to send
     */
    private void broadcast(String msg){
        b = new Broadcaster(msg, um);
        b.start();
    }
    
    /**
     * checks if a specific user exists, and whispers to that user
     * @param msg - message to send
     * @param usr - name of user to whisper
     * @return 
     */
    private int whisper(String msg, String usr){
        User temp = um.getUsers();
        for (int i=0;i<um.getClientCount();i++){
            if (temp.getName().equalsIgnoreCase(usr)){
                b = new Broadcaster(msg, temp);
                b.start();
                msg_to_self = "[[ to "+usr+" ]]"+msg.substring(myUser.getName().length()+6);
                sendToSelf();
                return 0;
            }
            temp = temp.getNext();
        }
        invalid_whisper(usr);
        return 0;
    }
    
    private void invalid_whisper(String usr){
        msg_to_self = "[Server Message]: Whisper failed, "+usr+" is not a valid user!";
        sendToSelf();
    }
    
    /**
     * sends error message to self
     */
    private void sendToSelf(){
        Broadcaster b = new Broadcaster(msg_to_self,myUser);
        b.start();
        msg_to_self = "";
    }
    
    /**
     * 
     * @param name
     * @return true if name is valid
     */
    private boolean validName(String name){
        char m=name.charAt(0);
        if (m>'0' && m<'9'){
            msg_to_self = "[Server Message]: Cannot start name with integer. Try again";
            return false;
        }
        for(int i=0;i<name.length();i++){
            m = name.charAt(i);
            if (m < 'a' || m> 'z'){
                if (m <'A' || m >'Z'){
                    if (m < '0' || m > '9'){
                        if (m !='_'){
                            msg_to_self = "[Server Message]: Invalid name! Use Alphanumerical characters only";
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * sends to the user the list of online
     * users connected to the server
     *  PROTOCOL:
     *    <name>:<status>;
     * @return 
     */
    private void getOnline(){
        msg_to_self = um.getOnline();
        this.sendToSelf();
    }
}