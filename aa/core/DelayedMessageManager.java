
package aa.core;

import aa.util.Queue;

/**
 *  This class manages communication messages for mobile actors
 *      that are in transit.
 *  <br>
 *  When an actor is moving from one host to another,
 *  the messages to the actor should be delayed.
 *  This deleay is managed by a Delayed Message Manager.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 24, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> change the 'processMessage' method.
 * 	</ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> April 19, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class DelayedMessageManager extends Thread
{
    // ========== Object Variables ========== //

    /**
     *  The flag to indicate whether the main loop should continue.
     */
    private boolean m_bContinue;
    
    /**
     *  The in-coming message queue of this module.
     */
    private Queue m_qMsgQueue;
    
    /**
     *  The reference to an Actor Manager
     */
    private ActorManager m_amActorManager;

    /**
     *  The reference to a Message Manager.
     */
    private MessageManager m_mmMessageManager;
    					

    // ========== Object Methods ========== //

    /**
     *  Creates a Delayed Message Manager.
     * 
     *  @param p_amActorManager the reference to an Actor Manager.
     *  @param p_mmMessageManager the reference to a Message Manager.
     *  @param p_mmMigrationManager the reference to a Migration Manager.
     */
    public DelayedMessageManager(ActorManager p_amActorManager,
    				 MessageManager p_mmMessageManager,
    				 MigrationManager p_mmMigrationManager)
    {
    	//
    	//  set object variables with parameters.
    	//
    	m_amActorManager   = p_amActorManager;
    	m_mmMessageManager = p_mmMessageManager;

	//
	//  initialize object variables.
	//
	m_bContinue = true;
    	m_qMsgQueue = new Queue();
    	
    	//
    	//  inform the reference of this object to an Actor Manager.
    	//
    	p_mmMigrationManager.setDelayedMessageManager(this);
    	
    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    

    /**
     *  Processes some necessay routines 
     *  before entering the while loop in the run method.
     */
    protected void init()
    {
    	System.out.println("% Delayed Message Manager started.");
    }
    

    /**
     *  Uninitializes this program.
     *  <br>
     *  This method is called by the Plaform.
     */
    protected void uninit()
    {
    	System.out.println("% Delayed Message Manager ended.");    	
    }


    /**
     *  Invoked when this thread is executed.
     */
    public void run()
    {
    	 init();
    	
    	 while (true) {
	    //
	    //  process in-coming messages.
	    //
	    synchronized (m_qMsgQueue) {
	    	try {
		    if (!m_qMsgQueue.isEmpty()) {
	    	    	//
	    	    	//  if there are some in-coming messages, 
	    	    	//  then process the first message.
	    	    	//
	    	    	int iSize = m_qMsgQueue.size();
	    	    	m_bContinue = false;
	    	    	
	    	    	for (int i=0; i<iSize; i++) {
	    	    	    ActorMessage amMsg = (ActorMessage)m_qMsgQueue.remove();
	    	    	    if (processMessage(amMsg) == true) {
	    	    	    	m_bContinue = true;
	    	    	    }
	    	    	}
	    	    	
	    	    	if (m_bContinue == true) {
	    	    	    continue;
	    	    	}
	    	    }
	    	     
	    	    //
	    	    //  if there is nothing to do, 
	    	    //  then go to sleep.
	    	    //
	    	    m_qMsgQueue.wait();
	    	} catch (InterruptedException e) {
	    	    if (Platform.bERROR) {
	    	    	System.err.println("% MessageManager.run : " + e);
    	            	e.printStackTrace();
	    	    }
	    	}
	    }
    	}
    }


    /**
     *  Processes a communication message.
     * 
     *  @param p_amMsg an actor communication message.
     */
    private boolean processMessage(ActorMessage p_amMsg)
    {
    	ActorName anReceiver = p_amMsg.getReceiver();

	//
	//  check if the receiver actor exists in the same platform.
	//
	Integer intActorState = m_amActorManager.getMobileActorState(anReceiver);

	if (intActorState.equals(ActorManager.UNKNOWN)) {
	    /*
	    if (Platform.bERROR) {
	    	System.err.println("% DelayedMessageManager.processMessage: " +
	    			   "Cannot find the receiver actor [" +
	    			   anReceiver + "]");
	    }

	    return false;	    
	    */

	    m_mmMessageManager.deliverMessage(p_amMsg);
	    return true;
	} else if (intActorState.equals(ActorManager.TRANSIT)) {
	    m_mmMessageManager.deliverMessage(p_amMsg);
	    return true;
	} else {
    	    m_qMsgQueue.insert(p_amMsg);
    	    return false;	
	}
    }
    

    /**
     *  Delivers a message between two actors.
     *  <br>
     *  This method is controled by another thread.
     * 
     *  @param p_amMsg an actor communication message.
     */
    protected void deliverMessage(ActorMessage p_amMsg)
    {
    	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.insert(p_amMsg);
    	    m_qMsgQueue.notify();
    	    m_bContinue = true;
    	}
    }
    
    
    /**
     *  Wakes up the curren sleeping thread.
     *  <br>
     *  This method is called by the 'Migration Manager' actor.
     */
    protected void resend()
    {
    	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.notify();
    	}
    }
}
