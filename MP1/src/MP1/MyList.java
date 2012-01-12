package MP1;

/**
 * this class was modified from an online source
 * Source: http://www.dreamincode.net/forums/topic/44036-how-to-add-tooltip-on-jlist-item/
 */
import javax.swing.*;
import java.awt.event.*;

// Custom class to extend our JList and expose tooltip functionality.
class MyList extends JList {
    User_List ul;
    JTextArea input;
    public MyList(User_List uls, JTextArea inpt) {
        super();
        ul = uls;
        input = inpt;
	// Attach a mouse motion adapter to let us know the mouse is over an item and to show the tip.
	addMouseMotionListener( new MouseMotionAdapter() {		
            @Override
            public void mouseMoved( MouseEvent e) {
                MyList theList = (MyList) e.getSource();
                //ListModel model = theList.getModel();
                int index = theList.locationToIndex(e.getPoint());
                if (index > -1) {
                    theList.setToolTipText(null);
                    String text = ul.getUserAt(index).getStatus();
                    theList.setToolTipText(text);
                }
            }
        });             
        addMouseListener(new MouseListener(){
            int count=0;
            int index=-1;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click "+count +" index = "+index);
                MyList theList = (MyList) e.getSource();
                int indx = theList.locationToIndex(e.getPoint());
                
                if (indx == index)
                    count++;
                else{
                    count = 0;
                    index = indx;
                }
                
                if (count >= 2){
                    String name = ul.getUserAt(index).getName();
                    input.setText("/whisper "+name+" ");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    // Expose the getToolTipText event of our JList
    @Override
    public String getToolTipText(MouseEvent e){
        return super.getToolTipText();
    }
}