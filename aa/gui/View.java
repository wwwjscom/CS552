
package aa.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import aa.Start;
import aa.core.ActorManager;
import aa.core.ActorMessage;
import aa.core.ActorName;
import aa.core.CreateActorException;
import aa.core.Platform;
import aa.util.ObjectHandler;
import aa.util.Lexer;
import aa.util.Queue;

/**
 * This class creates a window for MAA (Multi-Actor Architecture) Platform.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> March 5, 2004 - version 0.0.5
 * 	<ul>
 * 	    <li> add 'Save Message History' option on the menu.
 * 	</ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.3
 *      <ul>
 *          <li> change menu items.
 *      </ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> March 5, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/03/05$ $Revision: 0.0.5$
 */

public class View 
    extends JFrame 
    implements ActionListener, TreeSelectionListener
{
    // ========== Class Constants ========== //

    /**
     *  Title of this interface window.
     */
    private static final String m_strTitle = new String("AA: Actor Architecture");
//    private static final String m_strTitle = new String("AAA: Adaptive Actor Architecture");


    // ========== Object Variables ========== //

    private JMenuItem m_jmiClose;		//  menu item for 'Close'
    private JMenuItem m_jmiExit;		//  menu item for 'Exit'
    private JMenuItem m_jmiCreate;		//  menu item for 'Create a New Actor'
    private JMenuItem m_jmiSend;		//  mene item for 'Send a Message'
    private JMenuItem m_jmiSuspend;		//  menu item for 'Suspend an Actor'
    private JMenuItem m_jmiResume;		//  menu item for 'Resume an Actor'
    private JMenuItem m_jmiKill;		//  menu item for 'Kill an Actor'

    private JCheckBoxMenuItem m_jcbmiEnableMsgHistory;	//  menu item for 'Enable Message History'
    private JMenuItem m_jmiShowMsgHistory;	//  menu item for 'Show Messages History'
    private JMenuItem m_jmiClearMsgHistory;	//  menu item for 'Cleaer Message History'
    private JMenuItem m_jmiChangeSizeOfHistory;	//  menu item for 'Change Size of Message History'
    private JMenuItem m_jmiSaveMsgHistory;	//  menu item for 'Save Message History'

    private JMenuItem m_jmiSysInfo;		//  menu item for 'System Information'
    private JMenuItem m_jmiChangePortNumber;	//  menu item for 'Options...'
    private JMenuItem m_jmiAbout;		//  menu item for 'About MAA'
    
    private DefaultMutableTreeNode m_dmtnRoot;	//  root node of the actor tree
    private JTree m_jtreeActor;			//  actor tree

    private JScrollPane m_jspLeft;		//  left  pane of the jspMain pane
    private JScrollPane m_jspRight;		//  right pane of the jspMain pane
    
    private JSplitPane	m_jspMain;		//  main panel
    private JLabel	m_jlStatusBar;		//  status bar
		
    private Platform     m_pPlatform;		//  this Platform
    private ActorManager m_amActorManager;	//  pointer to the actor manager
    
    private int m_iSizeOfMsgHistoryQueue;	//  the size of the actor message history queue.
	
    private boolean m_bMsqHistory;		//  flag for logging communication message
    private Queue m_qMsgQueue;			//  message history queue
    
    private Integer m_intPort;			//  port number for transport service
    
	
    // ========== Object Methods ========== //

    /**
     *  Create the graphical user interface of this platform.
     * 
     *  @param p_intPort the port number of a server socket for in-comming messages.
     */
    public View(Integer p_intPort)
    {
    	super(m_strTitle);
    	
    	m_intPort = p_intPort;
    
    	m_bMsqHistory = false;
    	m_iSizeOfMsgHistoryQueue = 1000;
    	m_qMsgQueue = new Queue();
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	createMenu();
    	createPane();
    	
    	setSize(640, 480);
 
    	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dimWindow = getSize();
     	setLocation(dimScreen.width/2 - dimWindow.width/2, 
     		    dimScreen.height/2 - dimWindow.height/2);  	

//   	setMinimunSize(new Dimension(640, 480));

    	setVisible(true);
    }
    
    
    /**
     *  Create a menu bar with menu items. 
     */
    private void createMenu()
    {
    	//
    	//  create sub menu items
    	//
    	m_jmiClose = new JMenuItem("Close", KeyEvent.VK_C);
    	m_jmiClose.addActionListener(this);
    	
    	m_jmiExit = new JMenuItem("Exit", KeyEvent.VK_E);
    	m_jmiExit.addActionListener(this);
    	
    	m_jmiCreate = new JMenuItem("Create a New Actor", KeyEvent.VK_C);
    	m_jmiCreate.addActionListener(this);
    	m_jmiCreate.setEnabled(true);
    	
    	m_jmiSend = new JMenuItem("Send a Message", KeyEvent.VK_S);
    	m_jmiSend.addActionListener(this);
    	m_jmiSend.setEnabled(false);
    	
    	m_jmiSuspend = new JMenuItem("Suspend an Actor", KeyEvent.VK_U);
    	m_jmiSuspend.addActionListener(this);
    	m_jmiSuspend.setEnabled(false);
    	
    	m_jmiResume = new JMenuItem("Resume an Actor", KeyEvent.VK_R);
    	m_jmiResume.addActionListener(this);
    	m_jmiResume.setEnabled(false);
    	
    	m_jmiKill = new JMenuItem("Kill an Actor", KeyEvent.VK_K);
    	m_jmiKill.addActionListener(this);
    	m_jmiKill.setEnabled(false);
    	
    	m_jmiSysInfo = new JMenuItem("System Information", KeyEvent.VK_I);
    	m_jmiSysInfo.addActionListener(this);
    	
    	m_jmiChangePortNumber = new JMenuItem("Change Port Number", KeyEvent.VK_P);
    	m_jmiChangePortNumber.addActionListener(this);
    	
    	m_jcbmiEnableMsgHistory = new JCheckBoxMenuItem("Enable Message History");
    	m_jcbmiEnableMsgHistory.addActionListener(this);
     	
    	m_jmiChangeSizeOfHistory = new JMenuItem("Change Size of Message History", KeyEvent.VK_H);
    	m_jmiChangeSizeOfHistory.addActionListener(this);
    	
    	m_jmiClearMsgHistory = new JMenuItem("Clear Message History", KeyEvent.VK_C);
    	m_jmiClearMsgHistory.addActionListener(this);
    	
    	m_jmiSaveMsgHistory = new JMenuItem("Save Message History", KeyEvent.VK_S);
    	m_jmiSaveMsgHistory.addActionListener(this);
    	
    	m_jmiShowMsgHistory = new JMenuItem("Show Message History", KeyEvent.VK_W);
    	m_jmiShowMsgHistory.addActionListener(this);
    	
    	/*
    	jmiOptions = new JMenuItem("Options...", KeyEvent.VK_O);
    	jmiOptions.addActionListener(this);
    	jmiOptions.setEnabled(true);
    	*/
    	
	m_jmiAbout = new JMenuItem("About AA", KeyEvent.VK_A);
//	m_jmiAbout = new JMenuItem("About AAA", KeyEvent.VK_A);
	m_jmiAbout.addActionListener(this);
	m_jmiAbout.setEnabled(true);

	//
	//  create main menu items
	//
	JMenu jmFile = new JMenu("File");
	jmFile.setMnemonic('F');
	jmFile.add(m_jmiClose);
	jmFile.add(m_jmiExit);
	
	JMenu jmActions = new JMenu("Actions");
	jmActions.setMnemonic('A');
	jmActions.add(m_jmiCreate);
	jmActions.add(m_jmiSend);
	jmActions.addSeparator();
	jmActions.add(m_jmiSuspend);
	jmActions.add(m_jmiResume);
	jmActions.add(m_jmiKill);

    	JMenu jmMsgHistory = new JMenu("Message History" );    	    	
	jmMsgHistory.setMnemonic('M');
	jmMsgHistory.add(m_jcbmiEnableMsgHistory);
	jmMsgHistory.add(m_jmiChangeSizeOfHistory);
	jmMsgHistory.add(m_jmiClearMsgHistory);
	jmMsgHistory.add(m_jmiSaveMsgHistory);
	jmMsgHistory.add(m_jmiShowMsgHistory);

	JMenu jmTools = new JMenu("Tools");
	jmTools.setMnemonic('T');
	jmTools.add(m_jmiSysInfo);
	jmTools.addSeparator();
	jmTools.add(m_jmiChangePortNumber);
	jmTools.add(jmMsgHistory);
	
    	
	JMenu jmHelp = new JMenu("Help");
	jmHelp.setMnemonic('H');
	jmHelp.add(m_jmiAbout);
    	
    	//
    	//  create a menu bar
    	//
    	JMenuBar jmbMain = new JMenuBar();
    	jmbMain.add(jmFile);
    	jmbMain.add(jmActions);
    	jmbMain.add(jmTools);
    	jmbMain.add(jmHelp);
    	
    	setJMenuBar(jmbMain);
    }
    
    
    /**
     *  Creates the main pane of this window.
     */
    private void createPane()
    {
    	//
    	//  make a tree to describe actors.
    	//
    	m_dmtnRoot = new DefaultMutableTreeNode("Actor Platform");
     	
    	m_jtreeActor = new JTree(m_dmtnRoot);
    	m_jtreeActor.addTreeSelectionListener(this);
    	
    	//
    	//  make the left and right panes on the main pane.
    	//
    	m_jspLeft  = new JScrollPane(m_jtreeActor);
    	m_jspRight = new JScrollPane();
    	
    	//
    	//  make the main pane.
    	//
    	m_jspMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    	 
    	m_jspMain.setLeftComponent(m_jspLeft);
    	m_jspMain.setRightComponent(m_jspRight);
    	m_jspMain.setDividerSize(2);
    	m_jspMain.setDividerLocation(200);
    	
    	//
    	//  make the status bar.
    	//
    	m_jlStatusBar = new JLabel();
    	m_jlStatusBar.setText("Initializing...");
    	m_jlStatusBar.setForeground(Color.black);
    	m_jlStatusBar.setBorder(BorderFactory.createCompoundBorder(
    				new SoftBevelBorder(SoftBevelBorder.LOWERED,
    						    Color.white,
    						    new Color(134, 134, 134)), 
    				BorderFactory.createEmptyBorder(0, 5, 0, 5)));
    	
    	//
    	//  make the content pane.
    	//
    	getContentPane().setLayout(new BorderLayout());
    	getContentPane().add(m_jspMain, BorderLayout.CENTER);
    	getContentPane().add(m_jlStatusBar, BorderLayout.SOUTH);
    }
    
    
    /**
     *  Invoked when an action occurs. 
     * 
     *  @param p_ae an action event to be invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent p_ae)
    {
    	Object objSource = p_ae.getSource();
    	
    	if (objSource == m_jmiClose) {
    	    actionClose();
    	} else if (objSource == m_jmiExit) {
    	    actionExit();
    	} else if (objSource == m_jmiCreate) {
    	    actionCreate();
    	} else if (objSource == m_jmiSend) {
   	    actionSend();
    	} else if (objSource == m_jmiSuspend) {
    	    actionSuspend();
    	} else if (objSource == m_jmiResume) {
    	    actionResume();
    	} else if (objSource == m_jmiKill) {
    	    actionKill();
    	} else if (objSource == m_jmiSysInfo) {
    	    actionViewSysInfo();
    	} else if (objSource == m_jmiShowMsgHistory) {
    	    actionShowMsgHistory();
    	} else if (objSource == m_jmiSaveMsgHistory) {
    	    actionSaveMsgHistory();
    	} else if (objSource == m_jcbmiEnableMsgHistory) {
    	    actionEnableMsgHistory();
    	} else if (objSource == m_jmiChangePortNumber) {
    	    actionChangePortNumber();
    	} else if (objSource == m_jmiClearMsgHistory) {
    	    actionClearMsgHistory();
    	} else if (objSource == m_jmiChangeSizeOfHistory) {
    	    actionChangeSizeOfMsgHistory();
    	} else if (objSource == m_jmiAbout) {
    	    actionAbout();
    	}
    }


    /**
     *  Closes this window.
     */
    private void actionClose()
    {
    	setVisible(false);
    	System.out.println("% The graphical interface of this platform is closed,\n" + 
			   "    but this platform is still working.");
    }
    

    /**
     *  Exits this program.
     */
    private void actionExit()
    {
    	if (m_pPlatform != null) {
    	    m_pPlatform.uninit();
    	}
    	
    	System.out.println("    --- The End ---");
    	System.exit(0);
    }
    

    /**
     *  Creates a new actor.
     */
    private void actionCreate()
    {
    	//
    	//  receive input
    	//
    	String strInput = (String) JOptionPane.showInputDialog(null,
    	    				"Enter the class name of a new actor:",
    	    				"Create an Actor",
    	    				JOptionPane.QUESTION_MESSAGE);

	//
	//  if 'cancel' in the dialog box is selected, ...
	//
	if (strInput == null) {
	    return;
	}

    	//
    	//  parse the input string.
    	//
	String[] straToken = Lexer.getTokens(strInput);

	if ( (straToken == null) || (straToken.length == 0) ) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Your input (" + strInput + ") is incorrect.\n",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
	}

	String strActorClass = straToken[0];
		
	Object[] oaArgs = new Object[straToken.length-1];
	for (int i=1; i<straToken.length; i++) {
	    oaArgs[i-1] = straToken[i];
	}

	//
	//  create an actor.
	//
    	printOnStatusBar("Actor Class Name: " + strActorClass);
	
	try {
	    m_pPlatform.createActor(strActorClass, oaArgs);
	} catch (CreateActorException e) {
	    printOnStatusBar(e.toString());
	    JOptionPane.showMessageDialog(this,
					  "Cannot create an actor with '" + 
					  strActorClass + 
					  ObjectHandler.toStringArgs(oaArgs) + "'.",
	    	    		          "Error",
	    	    			  JOptionPane.ERROR_MESSAGE);
	}
    }


    /**
     *  Sends a message to the selected actor.
     */
    private void actionSend()
    {
    	//
    	//  check whether one item is selected.
    	//
    	TreePath tpath = m_jtreeActor.getSelectionPath();
    	
    	if (tpath == null) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "No actor is selected.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether an actor is selected.
    	//
    	ActorName anActorName = new ActorName(tpath.getLastPathComponent().toString());
    	
    	if (!anActorName.isValid()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Selection [" + tpath.getLastPathComponent().toString() + "] is incorrect.\n" +
    	    				  "You should select a proper actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  recevie input.
    	//    	
    	String strInput = 
    	    (String)JOptionPane.showInputDialog(null,
    	    					"Enter a method and string parameters",
    	    					"Send a message to the actor [" + tpath.getLastPathComponent().toString() + "]",
    	    					JOptionPane.QUESTION_MESSAGE);

	//
	//  if 'cancel' in the dialog box is selected, ...
	//
	if (strInput == null) {
	    return;
	}

    	//
    	//  parse the input string.
    	//
	String[] straToken = Lexer.getTokens(strInput);

	if ( (straToken == null) || (straToken.length == 0) ) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Your input (" + strInput + ") is incorrect.\n",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
	}

	String strMethod = straToken[0];
		
	Object[] oaArgs = new Object[straToken.length-1];
	for (int i=1; i<straToken.length; i++) {
	    oaArgs[i-1] = straToken[i];
	}

	//
	//  send a message.
	//
	ActorMessage amMsg = new ActorMessage(m_pPlatform.getActorNameOfPlatform(), 
					      anActorName, 
					      strMethod, 
					      oaArgs, 
					      false);
	m_pPlatform.sendMessage(amMsg); 
    }
    
    
    /**
     *  Suspends the selected actor.
     */
    private void actionSuspend()
    {
    	//
    	//  check whether one item is selected.
    	//
    	TreePath tpath = m_jtreeActor.getSelectionPath();
    	
    	if (tpath == null) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "No actor is selected.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether an actor is selected.
    	//
    	ActorName anActorName = new ActorName(tpath.getLastPathComponent().toString());
    	
    	if (!anActorName.isValid()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Selection [" + tpath.getLastPathComponent().toString() + "] is incorrect.\n" +
    	    				  "You should select a proper actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check if the selected actor is a system actor.
    	//
    	if (anActorName.getID() < m_pPlatform.getAppActorStartID()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "The Actor [" + tpath.getLastPathComponent().toString() + "] is a system actor.\n" +
    	    				  "You cannot suspend a system actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether the state of the actor selected is active.
    	//
    	Integer intActorState = m_amActorManager.getActorState(anActorName);
    	
    	if (intActorState != ActorManager.ACTIVE) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "The selected actor [" + tpath.getLastPathComponent().toString() + "] is not active.\n" +
    	    				  "You should select an active actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}

	//
	//  check again if a user want to kill the selected actor.
	//
	int iResponse = JOptionPane.showConfirmDialog(null,
						      "Are you sure you want to suspend the actor [" + anActorName.getUAN() + "] ?",
						      "Confirm Actor Suspend",
						      JOptionPane.YES_NO_OPTION, 
						      JOptionPane.QUESTION_MESSAGE);

	//
	//  perform the action according to the user response.
	//
	if (iResponse == JOptionPane.OK_OPTION) {
	    //
	    //  try to suspend an actor
	    //
	    m_amActorManager.suspendActor(anActorName); 
	    
	    m_jmiSuspend.setEnabled(false);
    	    m_jmiResume.setEnabled(true);
    	    
    	    displayActorInfo(anActorName);
	}
    }
    
    
    /**
     *  Resumes the selected actor.
     */
    private void actionResume()
    {
    	//
    	//  check whether one item is selected.
    	//
    	TreePath tpath = m_jtreeActor.getSelectionPath();
    	
    	if (tpath == null) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "No actor is selected.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether an actor is selected.
    	//
    	ActorName anActorName = new ActorName(tpath.getLastPathComponent().toString());
    	
    	if (!anActorName.isValid()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Selection [" + tpath.getLastPathComponent().toString() + "] is incorrect.\n" +
    	    				  "You should select a proper actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check if the selected actor is a system actor.
    	//
    	if (anActorName.getID() < m_pPlatform.getAppActorStartID()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "The Actor [" + tpath.getLastPathComponent().toString() + "] is a system actor.\n" +
    	    				  "You cannot resume a system actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether the state of the actor selected is active.
    	//
    	Integer intActorState = m_amActorManager.getActorState(anActorName);
    	
    	if (intActorState != ActorManager.SUSPENDED) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "The selected actor [" + tpath.getLastPathComponent().toString() + "] is not active.\n" +
    	    				  "You should select an active actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}

	//
	//  check again if a user want to kill the selected actor.
	//
	int iResponse = JOptionPane.showConfirmDialog(null,
						      "Are you sure you want to resume the actor [" + anActorName + "] ?",
						      "Confirm Actor Resume",
						      JOptionPane.YES_NO_OPTION, 
						      JOptionPane.QUESTION_MESSAGE);

	//
	//  perform the action according to the user response.
	//
	if (iResponse == JOptionPane.OK_OPTION) {
	    //
	    //  try to resume an actor
	    //
	    m_amActorManager.resumeActor(anActorName); 
	    
	    m_jmiSuspend.setEnabled(true);
    	    m_jmiResume.setEnabled(false);
    	    
    	    displayActorInfo(anActorName);
	}    	
    }
    
    
    /**
     *  Kills the selected actor.
     */
    private void actionKill()
    {
    	//
    	//  check whether one item is selected.
    	//
    	TreePath tpath = m_jtreeActor.getSelectionPath();
    	
    	if (tpath == null) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "No actor is selected.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether an actor is selected.
    	//
    	ActorName anActor = new ActorName(tpath.getLastPathComponent().toString());
    	
    	if (!anActor.isValid()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "Selection [" + tpath.getLastPathComponent().toString() + "] is incorrect.\n" +
    	    				  "You should select a proper actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
    	//
    	//  check whether the selected actor is a system actor.
    	//
    	if (anActor.getID() < m_pPlatform.getAppActorStartID()) {
    	    JOptionPane.showMessageDialog(this, 
    	    				  "The Actor [" + tpath.getLastPathComponent().toString() + "] is a system actor.\n" +
    	    				  "You cannot kill a system actor.",
    	    				  "Error",
    	    				  JOptionPane.ERROR_MESSAGE);
    	    return;
    	}
    	
	//
	//  check again if a user want to kill the selected actor.
	//
	int iResponse = JOptionPane.showConfirmDialog(null,
						      "Are you sure you want to delete the actor [" + anActor + "] ?",
						      "Confirm Actor Delete",
						      JOptionPane.YES_NO_OPTION, 
						      JOptionPane.QUESTION_MESSAGE);

	//
	//  perform the action according to the user response.
	//
	if (iResponse == JOptionPane.OK_OPTION) {
	    //
	    //  try to kill an actor.
	    //
	    m_amActorManager.killActor(anActor); 
	}
    }
    
    
    /**
     *  Shows a dialog box for the system information of this platform.
     */
    private void actionViewSysInfo()
    {
    	DlgShowSysInfo jdSysInfo = new DlgShowSysInfo();
    	jdSysInfo.show();
    }


    /**
     *  Shows a dialog box for changing server port number.
     */
    private void actionChangePortNumber()
    {
    	DlgChangePortNumber dlgPortNumber = new DlgChangePortNumber(this, m_intPort);
    	dlgPortNumber.show();
    }
    
    
    /**
     *  Change otpion for enabling actor message logging
     */
    private void actionEnableMsgHistory()
    {
    	m_bMsqHistory = m_jcbmiEnableMsgHistory.getState();
    }


    /**
     *  Shows a dialog box for changing the size of log messages.
     *
     */
    private void actionChangeSizeOfMsgHistory()
    {
    	DlgChangeSizeOfMessageHistory dlgSizeOfHistory = new DlgChangeSizeOfMessageHistory(this, m_iSizeOfMsgHistoryQueue);
    	dlgSizeOfHistory.show();
    }
    
    
    /**
     *  Removes all log messages.
     */
    private void actionClearMsgHistory()
    {
    	if (JOptionPane.showConfirmDialog(this,
    				          "Are you sure you want to remove all messages in history?",
    				      	  "Confirm Message Delete",
    				      	  JOptionPane.OK_CANCEL_OPTION)
    	    	== JOptionPane.OK_OPTION) {
    	    m_qMsgQueue = new Queue();
    
    	    JOptionPane.showMessageDialog(this,
    				     	  "All messages are deleted.",
    				    	  "Clear Message History",
    				    	  JOptionPane.INFORMATION_MESSAGE);
    	}    	
    }
    
    /**
     *  Saves the message history.
     */
    private void actionSaveMsgHistory()
    {
    	JFileChooser jfchooser = new JFileChooser(".");
    	jfchooser.setDialogTitle("Save File As");
    	
    	int iReturn = jfchooser.showSaveDialog(this);
    	if (iReturn == JFileChooser.APPROVE_OPTION) {
    	    File file = jfchooser.getSelectedFile();
    	    
    	    String strFileName = file.toString();
    	    
//    	    System.out.println(strFileName);
    	    if (file.exists()) {
 		iReturn = JOptionPane.showConfirmDialog(this,
    				file.getAbsoluteFile() + "already exists. " +
    				"Do you want to replact it?",
    				"Save As",
    				JOptionPane.OK_CANCEL_OPTION);
    				
    		if (iReturn == JOptionPane.CANCEL_OPTION) {
    		    return;
    		}
    	    }
    	    
	    try {
		PrintStream psLog = new PrintStream(new FileOutputStream(strFileName));
		
		psLog.println("#. Number of Messages: " + m_qMsgQueue.size());
		
		Iterator iterMsgQueue = m_qMsgQueue.iterator();
		
		int i=1;
		
		while (iterMsgQueue.hasNext()) {
		    ActorMessage amMsg = (ActorMessage) iterMsgQueue.next();

		    psLog.print("[" + (i++)  + "] " + amMsg.toString());		    
		}
		
		psLog.close();
	    } catch (FileNotFoundException e) {
	    }
    	}
    }


    /**
     *  Shows a dialob box for actor messages delivered.
     */
    private void actionShowMsgHistory()
    {
    	DlgShowMsgHistory jdMsgInfo = new DlgShowMsgHistory(m_qMsgQueue);
    	jdMsgInfo.show();
    }


    /**
     *  Displays a dialog box to show information about this program. 
     */
    private void actionAbout()
    {
    	JOptionPane.showMessageDialog(this,
    				      "AA: Actor Architecture\n" +
//    				      "AAA: Adaptive Actor Architecture\n" +
    				      Start.AA_VERSION + "\n\n" +
    				      Start.AA_DATE + "\n" +
    				      "Myeong-Wuk Jang\n" +
    				      "Copyright (c) 2003-2004 UIUC\n",
    				      "About AA",
//    				      "About AAA",
    				      JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    /**
     *  Sets new option parameters.
     *  <br>
     *  This method is called by the Options dialog.
     * 
     *  @param p_objaOptions arguments for new options.
     */
    public void checkPortNumber(Object[] p_objaOptions)
    {
    	Integer intNewPort = (Integer) p_objaOptions[0];

    	if (!m_intPort.equals(intNewPort)) { 
	    //
	    //  check again if a user want to kill the selected actor.
	    //
 	    int iResponse = JOptionPane.showConfirmDialog(null,
						      "Are you sure you want to restart this platform with the port number [" + intNewPort + "] ?",
						      "Confirm New Port Number",
						      JOptionPane.YES_NO_OPTION, 
						      JOptionPane.QUESTION_MESSAGE);

	    //
	    //  perform the action according to the user response.
	    //
	    if (iResponse == JOptionPane.OK_OPTION) {
	        //
	        //  try to kill an actor.
	        //
	        m_pPlatform.changePortNumber(intNewPort); 
	    
	        m_intPort = intNewPort;

    		printOnStatusBar("New Port Number for Transport Service: " + m_intPort);
	    }
    	}    	
    }
    

    /**
     *  Prints a message on the status bar.
     * 
     *  @param p_strMsg a message to be displayed on the status bar.
     */
    public void printOnStatusBar(String p_strMsg)
    {
    	m_jlStatusBar.setText(p_strMsg);
    }
    
    
    /**
     *  Sets the pointer to this Platform.
     * 
     *  @param p_pPlatform the reference to this platform.
     */
    public void setPlatform(Platform p_pPlatform)
    {
    	m_pPlatform = p_pPlatform;
    }
    

    /**
     *  Sets the reference to the Actor Manager of this actor platform.
     * 
     *  @param p_amActorManager the reference to the Actor Manager of this actor platform.
     */
    public void setActorManager(ActorManager p_amActorManager)
    {
        m_amActorManager = p_amActorManager;
    }
    
    
    /**
     *  Clears the information table.
     */
    private void clearInfo()
    {
    	m_jspRight = new JScrollPane();

    	m_jspMain.setRightComponent(m_jspRight);
    	m_jspMain.setDividerSize(2);
    	m_jspMain.setDividerLocation(200);

    	m_jspMain.invalidate();
    }
    
    
    /**
     *  Displays information about an actor on the information table.
     * 
     *  @param p_straData contents of the information table.
     */
    private void displayActorInfo(String[][] p_straData)
    {
    	String strColNames[] = { "Attribute", "Value" };
    	
    	displayInfo(strColNames, p_straData);
    }
    
    
    /**
     *  Displays information about actors on the information table.
     */
    private void displayActorsInfo(String[][] p_straData)
    {
    	String strColNames[] = { "Actor", "Status" };
    	
    	displayInfo(strColNames, p_straData);
    }

    
    /**
     *  Displays information on the jspRight pane.
     * 
     *  @param p_straColName names for columns of the information table.
     *  @param p_straData contents of the information table.
     */
    private void displayInfo(String[] p_straColName, String[][] p_straData)
    {
    	JTable jtableInfo = new JTable(p_straData, p_straColName);
    	jtableInfo.setEnabled(false);
  	
    	TableColumn tcFirst = jtableInfo.getColumn(p_straColName[0]);
    	tcFirst.setMinWidth(100);
    	tcFirst.setMaxWidth(150);
    	tcFirst.setPreferredWidth(150);
    	
    	m_jspRight = new JScrollPane(jtableInfo);

    	m_jspMain.setRightComponent(m_jspRight);
    	m_jspMain.setDividerSize(2);
    	m_jspMain.setDividerLocation(200);

    	m_jspMain.invalidate();
    }
    
    
    /**
     *  Adds a node for the new actor on the actor list.
     * 
     *  @param p_anActor the name of an actor.
     */
    public void addNewActor(ActorName p_anActor)
    {
    	m_dmtnRoot.add(new DefaultMutableTreeNode(p_anActor.getUAN()));
    	
	DefaultTreeModel dtm = (DefaultTreeModel)m_jtreeActor.getModel();
	dtm.reload();
	
	clearInfo();
    }


    /**
     *  Finds a node matched with the given input string.
     *
     *  @param p_strNodeName the name of a tree node.
     * 
     *  @return a node matched matched with the given input string.
     */
    private DefaultMutableTreeNode findNodeInTree(String p_strNodeName)
    {
        Enumeration enumDF = m_dmtnRoot.depthFirstEnumeration();
        
        while (enumDF.hasMoreElements()) {
            DefaultMutableTreeNode dmtnNode = (DefaultMutableTreeNode) enumDF.nextElement();
            if (dmtnNode.toString().equals(p_strNodeName)) {
            	return dmtnNode;
            }
        }
        
        return null;
    }
    

    /**
     *  Removes an actor from the jtreeRoot.
     *  <br>
     *  This method is (should be) called by the Actor Manager of this actor platform.
     *  <br>
     *  Note:
     *  <ul>
     *      <li> This code may have a security problem.
     *           If this function is called by a malicious actor to remove another actor,
     *           the actor would be removed from the interface
     *           without any authorization checking.
     *  </ul>
     * 
     *  @param p_anActor the name of an actor.
     */
    public void removeActor(ActorName p_anActor)
    {
    	DefaultMutableTreeNode dmtnNode = findNodeInTree(p_anActor.getUAN());

	if (dmtnNode != null) {    	
    	    m_dmtnRoot.remove(dmtnNode);

	    DefaultTreeModel dtm = (DefaultTreeModel)m_jtreeActor.getModel();
	    dtm.reload();
	
	    clearInfo();
	}
    }


    /**
     *  Redraws the window.
     */
    public void refreshWindow()
    {
	DefaultTreeModel dtm = (DefaultTreeModel)m_jtreeActor.getModel();
	dtm.reload();
	
	clearInfo();
    }


    /**
     *  Displays information about one actor.
     * 
     *  @param p_anActor the name of an actor.
     */
    private void displayActorInfo(ActorName p_anActor)
    {
	String[][] strData = m_amActorManager.getActorData(p_anActor);
    	            
	displayActorInfo(strData);
    	            
	Integer intActorState = m_amActorManager.getActorState(p_anActor);
    	            
	m_jmiSend.setEnabled(true);
	m_jmiKill.setEnabled(true);

	if (intActorState.equals(ActorManager.ACTIVE)) {
	    m_jmiSuspend.setEnabled(true);
	    m_jmiResume.setEnabled(false);
	} else {
	    m_jmiSuspend.setEnabled(false);
	    m_jmiResume.setEnabled(true);
	}
    }


    /**
     *  Invoked whenever the value of the selection changes.
     *  <br>
     *  This method is called because this class implements TreeSelectionListener.
     * 
     *  @param p_tse an event to be invoked when any node of the tree is selected.
     */
    public void valueChanged(TreeSelectionEvent p_tse)
    {
    	TreePath tpath = p_tse.getNewLeadSelectionPath();
    	
    	if (tpath != null) {
     	    String strLastComponent = tpath.getLastPathComponent().toString();
    	    ActorName anActor = new ActorName(strLastComponent);
    	    
    	    //
    	    //  if the selected item is an actor
    	    //
    	    if (anActor.isValid()) {
    	        printOnStatusBar("Selected Item: " + anActor);
    	        
    	        if (m_amActorManager != null) {
    	            displayActorInfo(anActor);
    	        }
    	    
    	    //
    	    //  if the selected item is this platform
    	    //
    	    } else {
    	    	printOnStatusBar("Selected Item: " + strLastComponent);
    	    	
    	        if (m_amActorManager != null) {
    	            String[][] strData = m_amActorManager.getAllActorData();
    	            
    	            displayActorsInfo(strData);
    	        }

		m_jmiSend.setEnabled(false);
    	        m_jmiSuspend.setEnabled(false);
    	        m_jmiResume.setEnabled(false);
    	        m_jmiKill.setEnabled(false);
    	    }
    	}
    }
    
    
    /**
     *  Displays an error message.
     * 
     *  @param p_strMsg an error message to be displayed.
     */
    public void displayErrorMessage(String p_strMsg) 
    {
	JOptionPane.showMessageDialog(this, 
    	    			      p_strMsg,
    	    			      "Error",
    	    			      JOptionPane.ERROR_MESSAGE);
    }


    /**
     *  Reports a message.
     *  <br>
     *  This method is called by the Message Manager of this actor platform.
     * 
     *  @param p_amMsg an actor communication message.
     */
    public void reportMessage(ActorMessage p_amMsg)
    {
    	if (m_bMsqHistory) {
    	    ActorMessage amMsg = (ActorMessage) ObjectHandler.deepCopy(p_amMsg);

     	    synchronized (m_qMsgQueue) {
   	    	if (m_qMsgQueue.size() >= m_iSizeOfMsgHistoryQueue) {
    	    	    m_qMsgQueue.remove();
    	    	}
    	
    	    	m_qMsgQueue.insert(amMsg);
     	    }
    	}
    }


    /**
     *  Changes the size of actor message log queue. 
     *  <br>
     *  This method is called by the View class.
     *
     *  @param p_iHistorySize the new size of the actor message log queue.
     */
    public void changeSizeOfMsgHistory(int p_iHistorySize)
    {
    	//
    	//  change the object variable.
    	//
    	m_iSizeOfMsgHistoryQueue = p_iHistorySize;
    	
    	//
    	//  remove old messages if they exceed the new size of queue.
    	//
    	synchronized (m_qMsgQueue) {
    	    int iOffset = m_qMsgQueue.size() - m_iSizeOfMsgHistoryQueue;
    
    	    for (int i=0; i<iOffset; i++) {
    	    	m_qMsgQueue.remove();	
    	    }
    	}
    }


    /**
     *  Exits this program.
     *  <br>
     *  This method is called by the Platform.
     */
    public void exit()
    {
    	System.exit(1);
    }
}
