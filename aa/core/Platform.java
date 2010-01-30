
package aa.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import aa.gui.View;
import aa.tool.SystemThread;
import aa.util.Queue;

/**
 * This class represents an actor platform.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> March 17, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 * @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class Platform extends SystemThread
{
    // ========== Public Constants ========== //
    
    /**
     *  Indicates whether debuging messages are printed on the screen. 
     */
    public final static boolean bDEBUG = false;

    /**
     *  Indicates whether error messages are printed on the screen. 
     */
    public final static boolean bERROR = true;

    /**
     *  Actor communication message
     *  that indicates that a mobile actor is destroyed, and hence, 
     *  information about the agnet should be removed.
     */    					
    public final static String MSG_DEREGISTER_AGENT = "deregisterActor";

    /**
     *  Actor communication message 
     *  that includes a mobile actor.
     */
    public final static String MSG_RECEIVE_AGENT    = "receiveActor";
    
    /**
     *  Actor communication message
     *  that indicates that a mobile actor starts its migration.
     */
    public final static String MSG_TRANSITION_START = "informTransitionStart";
    
    /**
     *  Actor communication message
     *  that indicates that a mobile actor finishes its migration.
     */
    public final static String MSG_TRANSITION_END   = "infromTransitionEnd";

    /**
     *  Actor communicaiton message
     *  that requests an actor creation.
     */
    public final static String MSG_CREATE_ACTOR     = "createActor";


    // ========== Public Variables ========== //
    
    /**
     *  Hasth table to be used for delivering a name of a new actor 
     *  from the actor thread class to the actor behavior class 
     */
    public static Hashtable g_htCreators = new Hashtable();
    

    // ========== Object Contants ========== //

    private static final int ID_PLATFORM = 0;
    					//  identifier of platform.
    private static final int ID_DF = 1;	
    					//  identifier of the default Directory Manager.


    // ========== Object Variables ========== //

    private static Platform m_pPlatform;
    					//  reference to this actor platform.

    private static int m_iID_AGENT;	//  index of a new UAN.
    private static String m_strHostAddress;
    					//  InetAddress of this platform.
    
    private static int m_iID_MSG;	//  index of a new message.


    private int m_iAppActorStartID;	//  start number for identifications of application actors.

    private ActorName m_anPlatform;	//  actor name of this platform.

    private Queue m_qMsgQueue;		//  in-coming message queue of this actor.

    private View m_viewMain;		//  GUI of this platform.

    private ActorManager m_amActorManager;   
    					//  reference to the Actor Manager of this platform.
    private MessageManager m_mmMessageManager;
    					//  reference to the Message Manager of this platform.
    private MigrationManager m_mmMigrationManager;
    					//  reference to the Migration Manager of this platform.

    private ActorName m_anDirectoryManager;
    					//  reference to the default Directory Manager of this platform.


    // ========== Object Methods ========== //

    /**
     *  Creates a mulit-actor platform.
     * 
     *  @param p_viewMain the reference to View
     *  @param p_intPort the port number of a server socket for in-coming messages
     */
    public Platform(View p_viewMain, Integer p_intPort)
    {
    	//
    	//  call the constructor of the super-class.
    	//
    	super();

	//
	//  print the title of this program.
	//    	
    	System.out.println("% AA (Actor Architecture) Platform:");
//    	System.out.println("% AAA (Adaptive Actor Architecture) Platform:");
    	
    	//
    	// set class varibles by parameters.
	//
    	m_viewMain = p_viewMain;

    	//
    	//  initialize the Internet address.
    	//
    	try {
    	    InetAddress ia = InetAddress.getLocalHost();
    	    Platform.m_strHostAddress = ia.getHostAddress();

	    System.out.println("     - Host Name:    " + ia.getHostName());
	    System.out.println("     - Host Address: " + ia.getHostAddress());
	    System.out.println();
    	} catch (UnknownHostException e) {
    	    if (Platform.bERROR) {
    	    	System.err.println("% Platform.init: " + e);
    	    	e.printStackTrace();
    	    }
    	    
    	    reportFailure(e.toString());
    	    return;
    	}

	//
	//  initialize object variables.
	// 
    	Platform.m_pPlatform = this;
    	Platform.m_iID_AGENT = Platform.ID_PLATFORM;
    	
    	m_anPlatform = createNewActorName();
    	
    	m_qMsgQueue = new Queue();
    	
	//
	//  inform information about this platform to View.
	//
    	if (m_viewMain != null) {
    	    m_viewMain.setPlatform(this);
    	}

	//
	//  create managers.
	//
    	m_amActorManager = new ActorManager(this, m_viewMain);
    	
    	m_mmMigrationManager = new MigrationManager(this, m_amActorManager);
    	m_mmMigrationManager.start();
    	
    	m_mmMessageManager = 
    	    new MessageManager(this, m_amActorManager, m_mmMigrationManager, p_intPort);
    	m_mmMessageManager.start();

    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    
    
    /**
     *  Reports that starting this platform fails.
     *  <br>
     *  This method is called by the <code>Platform</code> constructor, or
     *  the <code>init</code> method, or the Message Manager.
     * 
     *  @param p_strMsg a message to describe the reason of a failure.
     */
    protected void reportFailure(String p_strMsg)
    {
    	System.out.println("% Starting ths platform failed.");
    	System.out.println("%    - because of " + p_strMsg);
    	
    	if (m_viewMain != null) {
    	   m_viewMain.exit();
    	}
    	
  	System.exit(0);
    }
    

    /**
     *  Processes some necessay routines 
     *  before entering the while loop in the run method .
     *  <br>
     *  This is called by the run method of the super class.
     */
    protected void init()
    {
    	System.out.println("% AA platform started.");
//    	System.out.println("% AAA platform started.");
    	System.out.println();
    	
    	if (m_viewMain != null) {
    	    m_viewMain.printOnStatusBar("AA platform started");
//    	    m_viewMain.printOnStatusBar("AAA platform started");
    	}
    	
    	//
    	//  create the default Directory Manager
    	//
    	try {
    	    m_anDirectoryManager = createActor("aa.app.dm.DirectoryManager", new Object[0]);
    	    
    	    m_iAppActorStartID = Platform.m_iID_AGENT;
    	} catch (CreateActorException e) {
    	    System.err.println("% Platform.init: " + e);
    	    reportFailure("the Failure of Creating System Actors");
    	}
    }
    
    
    /**
     *  Uninitializes this program.
     *  <br>
     *  This method is called by the 'View' class.
     */
    public void uninit()
    {
    	m_amActorManager.uninit();
    	m_mmMessageManager.uninit();
    	m_mmMigrationManager.uninit();
    	    	
    	System.out.println("% AA Platform ended.");
//    	System.out.println("% AAA Platform ended.");
    }
    

    /**
     *  Processes a communcation message.
     * 
     *  @param p_amMsg the communication message.
     */
    protected void processMessage(ActorMessage p_amMsg)
    {
    	String strMethod = p_amMsg.getMethod();
    	Object[] oaArg = p_amMsg.getArguments();
    	
    	if (p_amMsg.isErrorMessage()) {
    	    if (m_viewMain != null) {
    	    	m_viewMain.displayErrorMessage(p_amMsg.getErrorMessage());
    	    }
    	    
    	    if (strMethod.equalsIgnoreCase(Platform.MSG_RECEIVE_AGENT)) {
     	        m_mmMigrationManager.receiveActor((String)oaArg[0],
    	    					  (byte[])oaArg[1],
    	    					  (byte[])oaArg[2],
    	    				           p_amMsg.getSender());    	    					 
    	    }
    	} else if (strMethod.equalsIgnoreCase(Platform.MSG_DEREGISTER_AGENT)) {
    	    m_amActorManager.deregisterMobileActor((ActorName)oaArg[0]);
    	    				    
    	} else if (strMethod.equalsIgnoreCase(Platform.MSG_RECEIVE_AGENT)) {
    	    m_mmMigrationManager.receiveActor((String)oaArg[0],
    	    				      (byte[])oaArg[1],
    	    				      (byte[])oaArg[2],
    	    				      p_amMsg.getSender());
    	    					 
    	} else if (strMethod.equalsIgnoreCase(Platform.MSG_TRANSITION_START)) {
    	    m_mmMigrationManager.informMigrationStart((ActorName)oaArg[0],
    	    					      (String)oaArg[1]);
    	    
    	} else if (strMethod.equalsIgnoreCase(Platform.MSG_TRANSITION_END)) {
    	    m_mmMigrationManager.informMigrationEnd((ActorName)oaArg[0],
    	    					    (String)oaArg[1]);
    	    
    	} else if (strMethod.equalsIgnoreCase(Platform.MSG_CREATE_ACTOR)) {
    	    createRemoteActor(p_amMsg);
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("> Unknown Method: " + strMethod); 
    	    }
    	}
    }
    
    
    /**
     *  Creates a new actor name.
     * 
     *  @return a new actor name.
     */
    private static synchronized ActorName createNewActorName()
    {
    	return new ActorName(Platform.m_strHostAddress, Platform.m_iID_AGENT++);
    }
    
    
    /**
     *  Creates a new message ID.
     * 
     *  @return a new message ID.
     */
    protected static synchronized String creteMessageID()
    {
    	return new String("mid://" + Platform.m_strHostAddress + ":" + Platform.m_iID_MSG++);
    }
    
    
    /**
     *  Returns the reference of this platform.
     * 
     *  @return the reference of this platform.
     */
    protected static Platform getPlatform()
    {
    	return Platform.m_pPlatform;
    }


    /**
     *  Returns the start number for identifications of application actors.
     *  <br>
     *  This method is called by the 'View' class.
     *  
     *  @return the start number for identifications of application actors.
     */
    public int getAppActorStartID()
    {
    	return m_iAppActorStartID;
    }
    

    /**
     *  Returns the Inet adress of this platform.
     * 
     *  @return the Inet adress of this platform.
     */
    protected String getHostAddress()
    {
    	return Platform.m_strHostAddress;
    }
    

    /**
     *  Returns the actor name of this platform.
     *
     *  @return the actor name of this platform.
     */
    public ActorName getActorNameOfPlatform()
    {
        return m_anPlatform;
    }


    /**
     *  Returns the actor name of the specified platform.
     *
     *  @param p_strHostName the host address of a platform.
     * 
     *  @return the actor name of this platform or the specified paltform 
     *               if the platform exists; <br>
     *           <code>null</code> otherwise.
     */
    public ActorName getActorNameOfPlatform(String p_strHostName)
    {
        ActorName anPlatform;
        
        try {
            InetAddress ia = InetAddress.getByName(p_strHostName);
            anPlatform = new ActorName(ia.getHostAddress(), Platform.ID_PLATFORM);
        } catch (UnknownHostException e) {
            if (Platform.bERROR) {
                System.err.println("% Actor.getActorNameOfPlatform: " + e);
                e.printStackTrace();
            }
            
            anPlatform = null;
        }
        
        return anPlatform;
    }
    
    
    /**
     *  Returns the name of the default Direcotry Manager actor.
     * 
     *  @return the name of the default Directory Manager actor.
     */
    protected ActorName getDefaultDirectoryManager()
    {
        return m_anDirectoryManager;
    }


    /**
     *  Returns the name of the default Direcotry Manager actor 
     *  of the specified platform.
     * 
     *  @param p_strHostName the host address of a platform.
     * 
     *  @return the name of the default Directory Manager 
     *               of the specified platform
     * 		     if the platform exists; <br>
     * 		<code>null</code> otherwise.
     */
    protected ActorName getDefaultDirectoryManager(String p_strHostName)
    {
        ActorName anDF;

        try {
            InetAddress ia = InetAddress.getByName(p_strHostName);
            anDF = new ActorName(ia.getHostAddress(), Platform.ID_DF);
        } catch (UnknownHostException e) {
            if (Platform.bERROR) {
                System.err.println("% Actor.getDefaultDirectoryManager: " + e);
                e.printStackTrace();
            }
            
            anDF = null;
        }
        
        return anDF;
    }


    /**
     *  Creates a remote actor.
     *  <br>
     *  This method is initiated by an actor on another platform, and
     *  it is called by the <code>processMessage</code> method.
     * 
     *  @param p_amMsg the communication message.
     */
    private void createRemoteActor(ActorMessage p_amMsg)
    {
    	//
    	//  decompose the input message into the name of a new actor and
    	//  its initial arguments .
    	//
    	Object[] objaMsgArgs = p_amMsg.getArguments();
    	Object[] objaArgs    = new Object[objaMsgArgs.length-1];
    	
	String strActorClass = (String)objaMsgArgs[0];
	
	for (int i=1; i<objaMsgArgs.length; i++) {
	    objaArgs[i-1] = objaMsgArgs[i];
	}
	
	//
	//  create a new actor.
	//
	ActorName anNewActor;
	ActorMessage amMsg;
	
	try {
	    anNewActor = createActor(strActorClass, objaArgs);
	    
	    amMsg = p_amMsg.makeReturnMessage(anNewActor);
	} catch (CreateActorException e) {
	    amMsg = p_amMsg.makeErrorMessage(e.getMessage());
	}
	
	//
	//  reply the in-coming message.
	//
	sendMessage(amMsg);
    }
    

    /**
     *  Creates an actor.
     *  <br>
     *  This method is called by either an actor thread or GUI.
     *  
     *  @param p_strActorClass the class of an actor to be created.
     *  @param p_objaArgs arguments of the constructor of the actor.
     * 
     *  @return the name of a new actor.
     * 
     *  @throws CreateActorException it occurs when actor creation fails.
     */
    public ActorName createActor(String p_strActorClass, Object[] p_objaArgs)
    	throws CreateActorException
    {
    	if (Platform.bDEBUG) {
            System.out.println("> Actor Class Name: " + p_strActorClass); 
    	}
     
  	ActorName actorName = createNewActorName();  	
	ActorThread athread = new ActorThread(actorName, true);
	
	try {
	    athread.setBehavior(p_strActorClass, p_objaArgs, null);
	    
	    //
	    //  if the become operator is called in the constructor of this actor, ...
	    //
	    if (athread.m_bRetire == false) { 
    		m_amActorManager.updateActor(athread, ActorManager.ACTIVE);
	    }
	} catch (ActorException e) {
	    m_amActorManager.removeActor(actorName);
	    throw new CreateActorException(e.getMessage());
	}
     
        //
        //  start the actor.
        //
    	String strName = athread.getActorClassName();
    	
        Thread threadActor = new Thread(athread);
        
        threadActor.setName(strName.substring(strName.lastIndexOf('.')+1));
        threadActor.start();

        return actorName;
    }
    
    
    /**
     *  Change the behavior of an actor.
     *  <br>
     *  This method is called by an actor.
     *  
     *  @param p_athreadCaller the name of an actor thread that called this method.
     *  @param p_baFlags the internal state flag of the caller actor.
     *  @param p_strActorClass the class name of a new actor to be replaced.
     *  @param p_objaArgs arguments of the constructor of the new actor.
     * 
     *  @return the name of a new actor.
     * 
     *  @throws CreateActorException it occurs when actor creation fails.
     */
    protected void becomeActor(ActorThread p_athreadCaller,
    			       Queue p_qMsgQueue,
    			       boolean[] p_baFlags, 
    			       String p_strActorClass, 
    			       Object[] p_objaArgs)
    	throws BecomeActorException
    {
    	if (Platform.bDEBUG) {
            System.out.println("> Actor Class Name: " + p_strActorClass); 
    	}
     
	//
	//  create a new actor with the existing actor name and security key.
	//
	ActorThread athread = new ActorThread(p_athreadCaller.getActorName(), false);

	try {
	    //
	    // set new behavior of the new actor. 
	    //
	    athread.setBehavior(p_strActorClass, p_objaArgs, p_qMsgQueue);
    	    m_amActorManager.updateActor(athread, ActorManager.ACTIVE);
           
            //
            //  start the actor.
            //
    	    String strName = athread.getActorClassName();
    	    
            Thread threadActor = new Thread(athread);
            
            threadActor.setName(strName.substring(strName.lastIndexOf('.')+1));
            threadActor.start();

        //
        //  handle an exception.
        //
        } catch (ActorException e) {
            //
            //  re-register the caller actor.
            //
      	    updateActor(p_athreadCaller, ActorManager.ACTIVE);
      	    
    	    throw new BecomeActorException(e.getMessage());
        } 
    }
    
    
    /**
     *  Sends a message through the Message manager.
     *  <br>
     *  When the platform receives a message from an actor or other components,
     *  it delivers the message to the Message manager
     *  that will deliver the message to the receiver actor.
     * 
     *  @param p_amMsg an actor communication message.
     */
    public void sendMessage(ActorMessage p_amMsg)
    {
	m_mmMessageManager.deliverMessage(p_amMsg);
    }


    /**
     *  Registers an actor thread to the Actor Manager of this platform.
     *  <br>
     *  This method is (should be) called by an actor thread or
     *  the Migration Manager of this platform.
     * 
     *  @param p_athread the reference to an actor thread.
     *  @param p_intState an actor state.
     */
    protected void registerActor(ActorThread p_athread, Integer p_intState)
    {
        m_amActorManager.registerActor(p_athread, p_intState);
    }
    
    
    /**
     *  Updates information about an actor on the Actor Manager.
     *  <br>
     *  This method is (should be) called by an actor thread.
     * 
     *  @param p_athread reference to an actor thread.
     *  @param p_intState actor state.
     */
    protected void updateActor(ActorThread p_athread, Integer p_intState)
    {
        m_amActorManager.updateActor(p_athread, p_intState);
    }
    
    
    /**
     *  Removes the specified actor from the Actor Manager.
     *  <br>
     *  This method is (shoule be) called by an actor thread.
     * 
     *  @param p_anActor Actor Name of the selected actor.
     */
    protected void removeActor(ActorName p_anActor)
    {
        m_amActorManager.removeActor(p_anActor);
    }
    

    /**
     *  Migrates the specified actor to a different host.
     *  <br>
     *  This method is (should be) called by the actor.
     * 
     *  @param p_anActor the name of an actor to be moved.
     *  @param p_iaDestHost the Internet address of the destination host.
     * 
     *  @throws MigrateActorException it occurs when an exception happens 
     * 		during the actor migration.
     */
    protected void migrateActor(ActorName p_anActor, InetAddress p_iaDestHost)
    	throws MigrateActorException
    {
    	m_mmMigrationManager.migrateActor(p_anActor, p_iaDestHost);
    }
    
    
    /**
     *  Changes the port number for the Transprot Manager.
     *  <br>
     *  This method is called by the 'View' class.
     *  
     *  @param p_intPort the new port number for transport manager.
     */
    public void changePortNumber(Integer p_intPort)
    {
    	m_mmMessageManager.changePortNumber(p_intPort);
    }
    
    
    /**
     *  Reports a message.
     * 
     *  @param p_amMsg an actor communicaiton message.
     */
    protected void reportMessage(ActorMessage p_amMsg)
    {
    	if (m_viewMain != null) {
    	    m_viewMain.reportMessage(p_amMsg);
    	}
    }
}
