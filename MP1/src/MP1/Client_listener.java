package MP1;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

public class Client_listener extends Thread{
    private MyConnection m;
    private User_List ul;
    private JTextArea chatbox;
    private MyClient gui;
    private String myname;
    public Client_listener(MyClient gui){
        this.gui = gui;
        this.m = gui.getMyConnection();
        this.ul = gui.getList();
        this.chatbox = gui.getTextArea();
    }
    
    @Override
    public void run(){
        String msg="";
        while(true){
            msg = m.getMessage();
            
            if (extractCommand(msg)){
                /*code here*/
            }else if (msg.length()>16 && msg.substring(0,16).equalsIgnoreCase("[Server Message]")){
                gui.display(msg);
                m.sendMessage("GET_USERS");
            }else {
                gui.display(msg);
            }
        }
    }
    
    /**
     * checks if the message sent from server
     * is a command
     * @return 
     */
    private boolean extractCommand(final String msg){
        if (msg.substring(0, 5).equals("USERS")){
            System.out.println("command found");
            refresh_user_list(msg);
            return true;
        }else if (msg.length() >10 && msg.substring(0, 10).equals("YOUR_NAME ")){
            myname = msg.substring(10);
            gui.setMyTitle(myname);
            return true;
        }
        return false;
    }
    private void refresh_user_list(String msg){
        m.sendMessage("GET_MYNAME");
        ul = null;
        ul = new User_List();
        ul.setUsers(extract_users(msg));
        ul.printUsers();
        DefaultListModel lm = new DefaultListModel();
        
        for (int i=0;i<ul.getUserCount();i++)
            lm.addElement(ul.getUserAt(i).getName());
        
        MyList user_list = new MyList(ul,gui.getInputArea());

        user_list.setModel(lm);
        gui.setUserList(user_list);
    }
    private static String[] extract_users(String msg){
        int count=0;
        char c;
        String temp = msg.substring(6);
        /* gets the number of users */
        for (int i=0;i<temp.length();i++){
            c = temp.charAt(i);
            if (c==';')
                count++;            
        }
        String users[] = new String[count];
        temp = msg.substring(6);
        
        for (int i=0,v=0;i<count;i++){
            users[i] = "";
            boolean done = false;
            while(!done){
                c=temp.charAt(v);
                if (c==';'){
                    done = true;
                }
                else
                    users[i] += c;
                v++;
            }
        }
        return users;
    }
    
    public String getMyName(){
       return this.myname;
   }
}