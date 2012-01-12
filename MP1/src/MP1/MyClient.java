/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MP1;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import javax.swing.text.Document;

/**
 *
 * @author Baguio
 */
public class MyClient extends JFrame{
    private JButton btn_send;
    private JTextArea chat_box;
    private JTextArea input;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    private JScrollPane scroll3;
    private JMenuItem men_about;
    private JMenuItem men_chg_name;
    private JMenuItem men_chg_status;
    private JMenu men_commands;
    private JMenuItem men_exit;
    private JMenu men_file;
    private JMenu men_help;
    private JMenuItem men_help_contents;
    private JMenuBar menubar;
    private MyConnection m;
    private Client_listener cl;
    private User_List ul;
    
    public MyClient(){
        init_connection();
        init_components();
        cl.start();
        init_actions();
    }
    
    private void init_components() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int monitor_height = screenSize.height;
        int monitor_width = screenSize.width;
        int frame_height = 340;
        int frame_width = 660;
        
        this.setResizable(false);
        this.setBounds( monitor_width/2 - frame_width/2,monitor_height/2 - frame_height/2
                ,frame_width, frame_height);
        
        scroll1 = new JScrollPane();
        scroll2 = new JScrollPane();
        scroll3 = new JScrollPane();
        chat_box = new JTextArea();
        input = new JTextArea();
        btn_send = new JButton();
        menubar = new JMenuBar();
        men_file = new JMenu();
        men_commands = new JMenu();
        men_chg_name = new JMenuItem();
        men_chg_status = new JMenuItem();
        men_exit = new JMenuItem();
        men_help = new JMenu();
        men_help_contents = new JMenuItem();
        men_about = new JMenuItem();
        
        m.sendMessage("GET_USERS");

        chat_box.setColumns(20);
        chat_box.setEditable(false);
        chat_box.setLineWrap(true);
        chat_box.setRows(5);
        chat_box.setWrapStyleWord(true);
        scroll2.setViewportView(chat_box);

        input.setColumns(20);
        input.setLineWrap(true);
        input.setRows(5);
        input.setToolTipText("Type your message here!");
        input.setWrapStyleWord(true);
        scroll3.setViewportView(input);

        btn_send.setText("Send");

        men_file.setText("File");

        men_commands.setText("Commands");

        men_chg_name.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        men_chg_name.setText("Change Name");
        men_commands.add(men_chg_name);

        men_chg_status.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        men_chg_status.setText("Change Status");
        men_commands.add(men_chg_status);

        men_file.add(men_commands);

        men_exit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        men_exit.setText("Exit");
        men_file.add(men_exit);

        menubar.add(men_file);

        men_help.setText("Help");

        men_help_contents.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK));
        men_help_contents.setText("Help Contents");
        men_help.add(men_help_contents);

        men_about.setText("About");
        men_help.add(men_about);

        menubar.add(men_help);

        setJMenuBar(menubar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(scroll3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                    .addComponent(scroll2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(scroll1, GroupLayout.Alignment.LEADING)
                    .addComponent(scroll2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll3, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        this.repaint();
    }
    private void init_connection(){
        try{
            Socket s = new Socket("localhost",8888);
            m = new MyConnection(s);
            cl = new Client_listener(this);
        }catch(IOException e){
            int yes = JOptionPane.showConfirmDialog(null, "Unable to Connecto to Server\n"
                    + "Try again?","Connection Error!", JOptionPane.YES_NO_OPTION);
            if (yes==0)
                init_connection();
            else
                System.exit(0);
        } 
    }
    
    private void init_actions(){
        /* send button */
        btn_send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                send_message();
            }
        });
        
        /* allowing SHIFR+ENTER to directly send message */
        input.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isShiftDown() && e.getKeyCode()==10){
                    send_message();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {} 
        });
        
        /* properly closes and exits the program */
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        
        this.men_exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        
        this.men_chg_name.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(rootPane, "Input new name");
                m.sendMessage("/changename "+name);
            }
        });
        
        this.men_chg_status.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String status = JOptionPane.showInputDialog(rootPane, "Input new status");
                m.sendMessage("/changestatus "+status);
            }            
        });
        
        this.men_help_contents.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }    
        });
        
        this.men_about.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "Chat Prgoram v1.0\n"
                        + "Author: Ivan Dominic Baguio\n");
            } 
        });
        
    }
    
    public void setUserList(MyList ml){
        this.scroll1.setViewportView(ml);
        ToolTipManager.sharedInstance().registerComponent(ml);
        this.repaint();
    }
    public static void main(String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyClient().setVisible(true);
            }
        });
    }
    
/**
     * displays the message on the text area
     * appends the message
     * @param msg 
     */
    public void display(String msg){
        this.chat_box.append(msg+"\n");
        this.chat_box.setCaretPosition(this.chat_box.getText().length());
    }
    
    private void showHelp(){
        String help = "*List of Commands*\n"
                + " Action\t\tCommand\t\tShortcut\n"
                + " Change Status\t\t/changestatus <new_status>\tALT+s\n"
                + " Change Name\t\t/changename <new_name>\tALT+n\n"
                + " Whisper\t\t/whisper <name> <message>\tClick user on list beside\n"
                + " Show Help\t\t/help\t\tALT+h\n"
                + " Quit\t\t/quit\t\tALT+q\n\n"
                + "SHIFT+ENTER to send the message\n"
                + "* * * * * * * * * * * * * * * * * * * * * * * * * *";
        display(help);
    }
    
    public void setMyTitle(String msg){
        this.setTitle(msg);
    }
    private void send_message(){
        String msg = removeTrailingWhiteSpaces(input.getText());
        if (msg.equalsIgnoreCase("/help"))
            showHelp();
        else if (msg.equalsIgnoreCase("/quit")){
            exit();
        }else if (msg.length() >= 9+cl.getMyName().length() &&
                msg.substring(0, 9+cl.getMyName().length()).equalsIgnoreCase("/whisper "+cl.getMyName())) {
            display("Cannot whisper to yourself");
        }else if (!msg.equals("") && !msg.equals(" "))
            this.m.sendMessage(msg);
        System.out.println(msg);
        input.setText("");
    }
    
    private void exit(){
        System.out.println("Client terminated");
        m.sendMessage("/quit");
        cl.interrupt();
        System.exit(1);
    }
    
    public MyConnection getMyConnection(){
        return this.m;
    }
    
    public User_List getList(){
        return this.ul;
    }
    
    public JTextArea getTextArea(){
        return this.chat_box;
    }
    
    public JTextArea getInputArea(){
        return this.input;
    }

   private String removeTrailingWhiteSpaces(String msg){
       String newmsg = "";
       int count = 0;
       for (int i=msg.length()-1;i>0;i--){
           if (msg.charAt(i) == ' ' || msg.charAt(i) == '\n')
               count++;
           else
               break;
       }
       newmsg = msg.substring(0, msg.length()-count);
       return newmsg;
   }
}