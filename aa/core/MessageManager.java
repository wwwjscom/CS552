
package aa.core;

import aa.tool.SystemThread;
import aa.transport.TransportManager;
import aa.transport.TransportSender;

/**
 *  This class delivers communication messages among actors.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 24, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> modify the <code>processMessage</code> method.
 * 	</ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> March 26, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class MessageManager extends SystemThread
{
    // ========== Object Variables ========== //

    /**
     *  The actor name of this actor platform.
     */   					
    private ActorName m_anPlatform;

    /**
     *  The reference to this actor platform.
     */
    private Platform m_pPlatform;

    /**
     *  The reference to the Actor Manager of this actor platform.
     */
    private ActorManager m_amActorManager;	

    /**
     *  The reference to the Transport Manager of this actor platform.
     */
    private TransportManager m_tmTransportManager;

    /**
     *  The reference to the Transport Sender of this actor platform.
     */
    private TransportSender m_tsTransportSender;

    /**
     *  The reference to the Delayed Message Manager of this actor platform.
     */
    private DelayedMessageManager m_dmmDelayedMessageManager;

    
    // ========== Object Methods ========== //

    /**
     *  Creates a Message Manager.
     * 
     *  @param p_platform the reference to a platform.
     *  @param p_amActorManager the reference to an Actor Manager.
     *  @param p_mmMigrationManager the reference to a Migration Manager.
     *  @param p_intPort the port number of a server socket for in-coming messages.
     */
    public MessageManager(Platform p_platform, 
    			  ActorManager p_amActorManager, 
    			  MigrationManager p_mmMigrationManager,
    			  Integer p_intPort)
    {
    	//
    	//  call the constructor of the super-class.
    	//
    	super();
    	
    	//
    	//  initialize object variables with parameters.
    	//
    	m_pPlatform = p_platform;
    	m_amActorManager = p_amActorManager;

	//
	//  set object variables and creates three system modules.
	//    	
    	m_anPlatform = m_pPlatform.getActorNameOfPlatform();
    	
    	m_tmTransportManager = new TransportManager(this, p_intPort);
    	m_tmTransportManager.start();
    	
    	m_tsTransportSender = new TransportSender(m_anPlatform, this, p_intPort);
    	m_tsTransportSender.start();
    	
    	m_dmmDelayedMessageManager = 
    	    new DelayedMessageManager(m_amActorManager, this, p_mmMigrationManager);
    	m_dmmDelayedMessageManager.start();
    	
    	//
    	//  inform this to a Migration Manager.
    	//
    	p_mmMigrationManager.setMessageManager(this);
    	
    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    

    /**
     *  Processes some necessay routines 
     *      before entering the while loop in the run method.
     *  <br>
     *  This is called by the run method of the super class.
     */
    protected void init()
    {
    	System.out.println("% Message Manager started.");
    }
    

    /**
     *  Uninitializes this program.
     *  <br>
     *  This method is called by the Plaform.
     */
    protected void uninit()
    {
    	m_tmTransportManager.uninit();
    	m_tsTransportSender.uninit();
    	m_dmmDelayedMessageManager.uninit();
    	
    	System.out.println("% Message Manager ended.");    	
    }


    /**
     *  Processes a communicaiton message.
     * 
     *  @param p_amMsg an actor communication message.
     */
    protected void processMessage(ActorMessage p_amMsg)
    {
    	ActorName anReceiver = p_amMsg.getReceiver();

	//
	//  check an error condition.
	//
	if (anReceiver == null) {
	    System.err.println("% MessageManager.processMessage: " + 
			       "Receiver Actor is NULL.");
	    return;
	}
	
	//
	//  if the receiver of a message is this platform,
	//  then deliver the message to this platform.
	//
	if (anReceiver.equals(m_anPlatform)) {
	    m_pPlatform.reportMessage(p_amMsg);
	    m_pPlatform.deliverMessage(p_amMsg);
	    return;
	}

	//
	//  retrieve information about the receiver actor.
	// 
	Object[] objaActorState = m_amActorManager.getActorInfo(anReceiver);

	//
	//  check if the receiver actor exists on this platform.
	//
	//  - ActorThread athread = m_amActorManager.getActorThread(anReceiver);
	//
	ActorThread athread = (ActorThread) objaActorState[0];

	if (athread != null) {
	    m_pPlatform.reportMessage(p_amMsg);
	    athread.deliverMessage(p_amMsg);
	    return;	    
	} 
	
	//
	//  if the mobile actor is in transit,
	//  then delay this message until the actor finishes its migration.
	//
	//  - Integer intState = m_amActorManager.getMobileActorState(anReceiver);
	//
	Integer intState = (Integer) objaActorState[1];

	if (intState.equals(ActorManager.IN_TRANSIT)) {
	    m_dmmDelayedMessageManager.deliverMessage(p_amMsg);
	    return;
	}

	//
	//  check if the receiver actor moved to a different platform.
	// 	
	//  - String strLocation = m_amActorManager.getMobileActorLocation(anReceiver);
	//
	String strLocation = (String) objaActorState[2];
	
	//
	//  if this platform does not have information about the receiver actor,
	//  then guess the current actor platform for the receiver actor. 
	//
	if ( (strLocation == null) &&
	     (!anReceiver.getHostAddress().equals(m_anPlatform.getHostAddress())) ) {
	    if (!anReceiver.getLocationAddress().equals(m_anPlatform.getHostAddress())) {
	        strLocation = anReceiver.getLocationAddress();
	    } else {
	    	strLocation = anReceiver.getHostAddress();
	    }
	}
	
	//
	//  if the current location of the receiver actor is fixed,
	//  then send this message to the corresponding actor platform.
	//
	if (strLocation != null) {
	    //
	    //  re-set the destination host address of a communication message.
	    //
  	    p_amMsg.setDestHostAddress(strLocation);
	    m_pPlatform.reportMessage(p_amMsg);
	    	
	    m_tsTransportSender.deliverMessage(p_amMsg);		
	} else {
	    if (Platform.bERROR) {
	    	System.err.println("% MessageManager.processMessage: " + 
				   "Cannot find the receiver actor [" +
				   anReceiver + "] of this message");
	    }
	    
	    ActorMessage amMsg = 
	    	p_amMsg.makeErrorMessage(m_pPlatform.getActorNameOfPlatform(), 
			"Cannot Find the Receiver Actor");
			
	    deliverMessage(amMsg);
	}
    }
    

    /**
     *  Changes the port number for transprot manager.
     *  
     *  @param p_intPort the new port number for transport manager.
     */
    protected void changePortNumber(Integer p_intPort)
    {
    	m_tmTransportManager.destroy();
    	
    	m_tmTransportManager = new TransportManager(this, p_intPort);
    	m_tmTransportManager.start();
    	
    	m_tsTransportSender.changePortNumber(p_intPort);
    }
    
    
    /**
     *  Reports the failure of server socket initiation.
     *  <br>
     *  This method is called by the Transport Manager.
     * 
     *  @param p_strMsg the message for which problem happens.
     */
    public void reportFailure(String p_strMsg)
    {
    	m_pPlatform.reportFailure(p_strMsg);
    }
}
