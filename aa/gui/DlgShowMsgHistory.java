
package aa.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import aa.core.ActorMessage;
import aa.util.ObjectHandler;
import aa.util.Queue;

/**
 *  This class creates a dialog to show actor messages delivered.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 8, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> add an exception handling routine.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.2
 *      <ul>
 *          <li> change the format of dialog box.
 *      </ul>
 *      <li> August 14, 2003 - version 0.0.1
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */
 
public class DlgShowMsgHistory extends JDialog implements ActionListener
{
    // ========== Object Variables ========== //

    private Queue m_qMsgQueue;		//  message queue
	
    private JButton m_jbOk;		//  Ok button
    
    
    // ========== Object Methods ========== //

    /**
     *  Creates a dialog box to show actor messages delivered.
     * 
     *  @param p_qMsgQueue the reference to the message queue.
     */
    public DlgShowMsgHistory(Queue p_qMsgQueue) 
    {
    	super((Frame)null, "Actor Messages History", true);

	//
	//  set object variable(s).
	//    	
    	m_qMsgQueue = p_qMsgQueue;
    	
	//
	//  create a dialog box.
	//
    	setSize(600, 300);
    	
    	createPane();  
    	  	
    	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dimWindow = getSize();
     	setLocation(dimScreen.width/2 - dimWindow.width/2, 
     		    dimScreen.height/2 - dimWindow.height/2);  	

    	setResizable(true);
    }


    /**
     *  Creates the main pane of this dialog box.
     */
    private void createPane()
    {
	//
	//  make table contents.
	//
	//  Note: This part does not guarantee the final result is correct.
	//        Because of the multi-thread execution, some data can be duplicated or
	//	  some data can be missed.
	//
    	String strColNames[] = { 
    	    "Sender",
    	    "Receiver",
    	    "Method",
    	    "Arguments"
    	};
    	
    	Iterator iterMsgQueue = m_qMsgQueue.iterator();
    	
    	String strData[][];
    	
    	strData = new String[m_qMsgQueue.size()][4];
    	
    	for (int i=0; ( (i < strData.length) && (iterMsgQueue.hasNext()) ); i++) {
    	    try {
		ActorMessage amMsg = (ActorMessage) iterMsgQueue.next();
		if (amMsg.getSender() != null) {
		    strData[i][0] = amMsg.getSender().toString();
		} else {
		    strData[i][0] = ">> Unknown <<";
		}
    	    
		if (amMsg.getReceiver() != null) {
		    strData[i][1] = amMsg.getReceiver().toString();
		} else {
		    strData[i][0] = ">> Unknown <<";
		}

		if (amMsg.isErrorMessage()) {
		    strData[i][2] = "errorMessage";
		    strData[i][3] = amMsg.getErrorMessage();
		} else {
		    strData[i][2] = amMsg.getMethod();
		    strData[i][3] = ObjectHandler.toStringArgs(amMsg.getArguments());
		}
    	    } catch (NoSuchElementException e) {
    	    	//
    	    	//  This exception can happen on multi-thread execution.
    	    	//
    	    	
    	    	String strDataTmp[][] = new String[i][4];
    	    	
    	    	for (int j=0; j<i; j++) {
    	    	    strDataTmp[j] = strData[j];
    	    	}
    	    	
    	    	strData = strDataTmp;
    	    }
    	}
    	
    	//
    	//  create an information panel.
    	//
    	JTable jtMsgInfo = new JTable(strData, strColNames);
    	jtMsgInfo.setEnabled(false);
    	
 	TableColumn tcFirst = jtMsgInfo.getColumn(strColNames[0]);
    	tcFirst.setMinWidth(100);
    	tcFirst.setMaxWidth(300);
    	tcFirst.setPreferredWidth(100);

 	TableColumn tcSecond = jtMsgInfo.getColumn(strColNames[1]);
    	tcSecond.setMinWidth(100);
    	tcSecond.setMaxWidth(300);
    	tcSecond.setPreferredWidth(100);

 	TableColumn tcThird = jtMsgInfo.getColumn(strColNames[2]);
    	tcThird.setMinWidth(100);
    	tcThird.setMaxWidth(200);
    	tcThird.setPreferredWidth(100);
    	
    	JScrollPane jspInfo = new JScrollPane(jtMsgInfo);

  	//
  	//  create the 'OK' button panel.
  	//
    	m_jbOk = new JButton("OK");
    	m_jbOk.addActionListener(this);

	JPanel jpButtons = new JPanel(new FlowLayout());
	jpButtons.add(m_jbOk);
	
	//
	//  create the main content pane.
	//
    	Container container = getContentPane();
	container.setLayout(new BorderLayout());
	container.add(jspInfo, BorderLayout.CENTER);
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
	    dispose();
	}
    }    
}
