
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
 *  This class creates a dialog to change server port number.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 13, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> separate some of this part as the ActorThread class.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.3
 *      <ul>
 *          <li> change the format of dialog box.
 *      </ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 18, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class DlgChangePortNumber extends JDialog implements ActionListener
{
    // ========== Object Variables ========== //

    private Integer m_intPort;		//  the original port number
    
    private JTextField m_jtfPortNumber;	//  text field for port number
    
    private JButton m_jbOk;		//  Ok button
    private JButton m_jbCancel;		//  Cancel button
	
    private View m_vView;		//  reference to the View class
    
	
    // ========== Object Methods ========== //

    /**
     *  Creates a dialog box to show options of this platform.
     * 
     *  @param p_view the reference to the view class.
     *  @param p_intPort the port number of the server socket of this platform.
     */
    public DlgChangePortNumber(View p_view, Integer p_intPort)
    {
    	super((Frame)null, "Change Port Number", true);
    	
    	//
    	//  initialized object variables.
    	//
    	m_vView = p_view;
    	m_intPort = p_intPort;
    	
    	//
    	//  create a dialog box.
    	//
 	createPane();    	

    	setSize(300, 140);

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
    	JLabel jlPortNumber = new JLabel("New Server Port Number: ");
    	
    	m_jtfPortNumber = new JTextField(6);
    	m_jtfPortNumber.setText(m_intPort.toString());
    	
    	JPanel jpPortNumber = new JPanel();
    	jpPortNumber.setLayout(new BorderLayout());
    	jpPortNumber.add(jlPortNumber, BorderLayout.WEST);
    	jpPortNumber.add(m_jtfPortNumber, BorderLayout.EAST);
    	
    	//
    	//  create the options pane.
    	//
    	JPanel jpOptions = new JPanel(new BorderLayout());
    	jpOptions.setBorder(new EmptyBorder(20, 30, 0, 30));	//  (top, left, bottom, right)
    	jpOptions.add(jpPortNumber, BorderLayout.CENTER);
    	
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
	    String strPortNumber = m_jtfPortNumber.getText();
 	    try {
 	        Integer intPortNumber = new Integer(strPortNumber);
 	        m_vView.checkPortNumber(new Object[] { intPortNumber } ); 	        
		dispose();
 	    } catch (NumberFormatException e) {
 	    	//
 	    	//  if user inputer is not integer value,
 	    	//  then show a message box.
 	    	//
    	        JOptionPane.showMessageDialog(this, 
    	    		"The port number should be integer.\n" +
    	    		"    - Your input: '" + strPortNumber + "'",
    	    		"Error",
    	    		JOptionPane.ERROR_MESSAGE);
 	    }
	} else if (objSource == m_jbCancel) {
	    dispose();
	}
    }
}
