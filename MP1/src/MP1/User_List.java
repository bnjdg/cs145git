package MP1;

/**
 * Similar to user_manager
 * @author Baguio
 */
public class User_List {
    User users;
    int count;
    public User_List(){
        
    }
    
    public void addUser(String name, String status){
        count++;
        System.out.println("adding "+name+" : "+status);
        if (users == null){
            users = new User(name,status);
        }else{
            User temp = new User(name,status);
            temp.setNext(users);
            users = temp;
        }
    }
    
    public void setUsers(String[] msg){
        String name = "";
        String status = "";
        
        System.out.println("printing users  xx");
        for (int v=0;v<msg.length;v++){
            System.out.println(v+"*"+msg[v]);
        }
        for (int i=0;i<msg.length;i++){
            System.out.println("msg is " +msg[i]);
            int v = 0;
            char c = msg[i].charAt(0);
            while(true){
                if (c == ':')
                    break;
                c = msg[i].charAt(v++);
            }
            name = msg[i].substring(0,v-1);
            if (msg[i].length()>=v+1)
                status = msg[i].substring(v);
            addUser(name,status);
            try{
                String x = msg[i+1];
            }catch(ArrayIndexOutOfBoundsException e){
                break;
            }
        }
    }
    
    public void printUsers(){
        User temp = users;
        System.out.println("printing users");
        for(int i=0;i<count;i++){
            System.out.println(temp.getName()+" : "+temp.getStatus());
            temp = temp.getNext();
        }
    }
    
   public User getUserAt(int index){
        if (index == 0){
            return users;
        }else if (index>0 && index < count){
            User temp = users;
            for (int i=0;i<index;i++)
                temp = temp.getNext();
            return temp;
        }else
            return null;
    }
   
    public int getUserCount(){
        return this.count;
    }
}