
package aa.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *  This class creates a dialog to change the size of the actor message log.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.2
 *      <ul>
 *          <li> change the format of dialog box.
 *      </ul>
 *      <li> November 24, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */

public class DlgChangeSizeOfMessageHistory extends JDialog implements ActionListener
{
    // ========== Object Variables ========== //

    private static final int MIN_LOG_SIZE = 100;
    					//  minimum size of message log
    private static final int MAX_LOG_SIZE = 2000;
    					//  maximum size of message log

	
    // ========== Object Variables ========== //

    private int m_iSizeOfMsgHistory;	//  the size of message log
    
    private JTextField m_jtfSizeOfMsgHisotry;	
    					//  text field for the size of message log
    
    private JButton m_jbOk;		//  Ok button
    private JButton m_jbCancel;		//  Cancel button
	
    private View m_vView;		//  reference to the View class
    
	
    // ========== Object Methods ========== //

    /**
     *  Creates a dialog box to show options of this platform.
     * 
     *  @param p_view the reference to the view class.
     *  @param p_iSizeOfMsgHistory the size of message history.
     */
    public DlgChangeSizeOfMessageHistory(View p_view, int p_iSizeOfMsgHistory)
    {
    	super((Frame)null, "Change the Size of Message History", true);
    	
    	//
    	//  initialized object variables.
    	//
    	m_vView = p_view;
    	m_iSizeOfMsgHistory = p_iSizeOfMsgHistory;
    	
    	//
    	//  create a dialog box.
    	//
 	createPane();    	

    	setSize(320, 170);

    	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dimWindow = getSize();
     	setLocation(dimScreen.width/2 - dimWindow.width/2, 
     		    dimScreen.height/2 - dimWindow.height/2);  	

    	setResizable(false);
    }
    
    
    /**
     *  Creates the main pane of this dialog box.
     */
    private void createPane()
    {
    	JLabel jlSizeOfMsgHistory = new JLabel("New Size of Message History: ");
    	
    	m_jtfSizeOfMsgHisotry = new JTextField(6);
    	m_jtfSizeOfMsgHisotry.setText("" + m_iSizeOfMsgHistory);
    	
    	JPanel jpSizeOfMsgHistory = new JPanel();
    	jpSizeOfMsgHistory.setLayout(new BorderLayout());
    	jpSizeOfMsgHistory.add(jlSizeOfMsgHistory, BorderLayout.WEST);
    	jpSizeOfMsgHistory.add(m_jtfSizeOfMsgHisotry, BorderLayout.EAST);
    	
    	//
    	//  create the message pane.
    	//
    	JLabel jlMessage = new JLabel("( Range: " + MIN_LOG_SIZE + " ~ " + MAX_LOG_SIZE + " )");
    	
    	JPanel jpMessage = new JPanel(new BorderLayout());
    	jpMessage.setBorder(new EmptyBorder(10, 50, 0, 50));	//  (top, left, bottom, right)
    	jpMessage.add(jlMessage, BorderLayout.CENTER);
    	
    	//
    	//  create the options pane.
    	//
    	JPanel jpOptions = new JPanel(new BorderLayout());
    	jpOptions.setBorder(new EmptyBorder(20, 30, 0, 30));	//  (top, left, bottom, right)
    	jpOptions.add(jpSizeOfMsgHistory, BorderLayout.CENTER);
    	jpOptions.add(jpMessage, BorderLayout.SOUTH);
    	
  	//
  	//  create the 'OK' button panel.
  	//
    	m_jbOk = new JButton("OK");
    	m_jbOk.addActionListener(this);
    	
	m_jbCancel = new JButton("Cancel");
	m_jbCancel.addActionListener(this);

	JPanel jpButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
	jpButtons.add(m_jbOk);
	jpButtons.add(m_jbCancel);

	//
	//  create the main content pane.
	//
    	Container container = getContentPane();
	container.setLayout(new BorderLayout());
	container.add(jpOptions, BorderLayout.CENTER);
	container.add(jpButtons, BorderLayout.SOUTH);	
	
	getRootPane().setDefaultButton(m_jbOk);
    }
    

    /**
     *  Invoked when an action occurs. 
     * 
     *  @param p_ae an action event to be invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent p_ae)
    {
	Object objSource = p_ae.getSource();

	if (objSource == m_jbOk) {
	    String strSizeOfMsgHistory = m_jtfSizeOfMsgHisotry.getText();
 	    try {
 	        Integer intSizeOfMsgHistory = new Integer(strSizeOfMsgHistory);
 	        int     iSizeOfMsgHistory   = intSizeOfMsgHistory.intValue();
 	        
 	        if (iSizeOfMsgHistory < MIN_LOG_SIZE) {
    	            JOptionPane.showMessageDialog(this, 
    	    		"The size of message log should be equal to or more than " + 
    	    		MIN_LOG_SIZE + ".\n" +
    	    		"    - Your input: '" + strSizeOfMsgHistory + "'",
    	    		"Error",
    	    		JOptionPane.ERROR_MESSAGE);
 	        } else if (iSizeOfMsgHistory > MAX_LOG_SIZE) {
     	            JOptionPane.showMessageDialog(this, 
    	    		"The size of message log should be equal to or less than " + 
    	    		MAX_LOG_SIZE + ".\n" +
    	    		"    - Your input: '" + strSizeOfMsgHistory + "'",
    	    		"Error",
    	    		JOptionPane.ERROR_MESSAGE);
 	        } else {
 	            m_vView.changeSizeOfMsgHistory(iSizeOfMsgHistory); 	        
		    dispose();
 	        } 	        
 	    } catch (NumberFormatException e) {
 	    	//
 	    	//  if user inputer is not integer value,
 	    	//  then show a message box.
 	    	//
    	        JOptionPane.showMessageDialog(this, 
    	    		"The size of message log should be integer.\n" +
    	    		"    - Your input: '" + strSizeOfMsgHistory + "'",
    	    		"Error",
    	    		JOptionPane.ERROR_MESSAGE);
 	    }
	} else if (objSource == m_jbCancel) {
	    dispose();
	}
    }
}
