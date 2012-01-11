import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;

public class ChatGUI extends JFrame{
	/*
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JScrollPane ChatScrollPane;
	public JTextArea ChatTextArea;
	public JLabel ConvoLabel;
	public JTextField MsgTextField;
	public JButton SendButton;
	
	DataOutputStream outgoing;
	ApplicationInfo server;
	ApplicationInfo client;
	String clntorsrvr;
	
	public ChatGUI(DataOutputStream os ,String clntorsrvr, ApplicationInfo server, ApplicationInfo client){
		this.outgoing = os;
		this.clntorsrvr = clntorsrvr;
		this.server = server;
		this.client = client;
		initComponents();
	}

	public ChatGUI(){
		initComponents();
	}
	
	

	private void initComponents() {

		SendButton = new JButton();
		ConvoLabel = new JLabel();
		MsgTextField = new JTextField();
		ChatScrollPane = new JScrollPane();
		ChatTextArea = new JTextArea();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle(clntorsrvr + " Conversation Window");
		setName("ChatGUI"); // NOI18N

		SendButton.setText("Send");
		SendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SendButtonActionPerformed(evt);
			}
		});

		ConvoLabel.setText("Conversation:");

		MsgTextField.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
		MsgTextField.setText("(type message here)");
		MsgTextField.setDragEnabled(true);
		MsgTextField.addKeyListener(new  KeyAdapter() {
			public void keyPressed( KeyEvent evt) {
				MsgTextFieldKeyPressed(evt);
			}
		});

		ChatScrollPane.setAutoscrolls(true);

		ChatTextArea.setColumns(20);
		ChatTextArea.setEditable(false);
		ChatTextArea.setRows(5);
		ChatScrollPane.setViewportView(ChatTextArea);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup( GroupLayout.Alignment.LEADING)
				.addGroup( GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup( GroupLayout.Alignment.TRAILING)
								.addComponent(ChatScrollPane,  GroupLayout.Alignment.LEADING,  GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup( GroupLayout.Alignment.LEADING)
												.addComponent(ConvoLabel)
												.addComponent(MsgTextField,  GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
												.addPreferredGap( LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(SendButton)))
												.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(ConvoLabel)
						.addPreferredGap( LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(ChatScrollPane, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(MsgTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(SendButton))
								.addGap(24, 24, 24))
				);

		pack();
	}                      

	private void MsgTextFieldKeyPressed(KeyEvent evt) {                                        
		// TODO add your handling code here:
		if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
			//writethread.sendMsg();
			String message = MsgTextField.getText();
			sendMsg(message);
			MsgTextField.setText("");
			MsgTextField.repaint();
		}
		else{
			
			
		}
		
		
	}                                       

	
	private void SendButtonActionPerformed(ActionEvent evt) {                                           
		// TODO add your handling code here:
		//writethread.sendMsg();
		String message = MsgTextField.getText();
		sendMsg(message);
		MsgTextField.setText("");
		MsgTextField.repaint();
	}                                          


	public void sendMsg(String msg2){
		String msg = msg2;
		try{ 

			this.getTxtArea().append("\r");
			if (msg.endsWith("KThanksBye!")){
				outgoing.writeBytes("disconnect:"+msg+"\n");
				ChatTextArea.append("Disconnecting..OK\n");
				this.dispose();
			}
			else if (msg.startsWith("@")){
				outgoing.writeBytes("cusername:"+msg+"\n");
				if(clntorsrvr.equals("Client")){
					client.setUsername(msg.substring(1));
					this.getTxtArea().append("You have change your name into " + client.getUsername() + "\n");
					this.getTxtArea().repaint();
					
				}else {
					server.setUsername(msg.substring(1));
					this.getTxtArea().append("You have change your name into " + server.getUsername() + "\n");
					this.getTxtArea().repaint();
				}
			} else {
				outgoing.writeBytes("message:"+msg+"\n");
				if(clntorsrvr.equals("Client")){
					this.getTxtArea().append(client.getUsername() + ": " + msg+"\n");
					this.getTxtArea().repaint();
				}else {
					this.getTxtArea().append(server.getUsername() + ": " + msg+"\n");
					this.getTxtArea().repaint();
				}
			}
	
		}catch (Exception e){
			System.err.println("Error reading input: "+ e);
			return;
		}
	}
	
	public void runGUI() {

/*		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
*/
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				setVisible(true);
			}
		});

	}

	public JTextArea getTxtArea(){
		return this.ChatTextArea;
	}

}
