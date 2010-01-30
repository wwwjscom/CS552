
package aa.tool;

import java.util.NoSuchElementException;

import aa.core.ActorMessage;
import aa.core.Platform;
import aa.util.Queue;

/**
 * This class includes fundamental behavior of System Actors
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> May 22, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */

public abstract class SystemThread extends Thread
{
    // ========== Object Variables ========== //

    /**
     *  In-coming message queue
     */
    private Queue m_qMsgQueue;
    
    
    // ========== Object Methods ========== //

    /**
     *  Cretes a system thread instance. 
     */
    public SystemThread()
    {
    	m_qMsgQueue  = new Queue();
    }
	
	
    /**
     *  Invoked when this thread is executed.
     */
    public void run()
    {
    	ActorMessage amMsg;
    	
    	init();
    	
    	while (true) {
	    //
	    //  process in-coming messages.
	    //
	    try {
	    	synchronized (m_qMsgQueue) {
		    if (m_qMsgQueue.isEmpty()) {
	    	    	//
	    	    	//  if there is nothing to do, 
	    	    	//  then go to sleep.
	    	    	//
	    	    	m_qMsgQueue.wait();
	    	    	continue;
	    	    } else {
	    	    	//
	    	    	//  if there are some in-coming messages, 
	    	    	//  then process the first message.
	    	    	//
	    	    	try {
			    amMsg = (ActorMessage)m_qMsgQueue.remove();
	    	    	} catch (NoSuchElementException e) {
	    	    	    continue;
	    	    	}
	    	    }
	    	}
	    	
	    	processMessage(amMsg);
	    	
	    //
	    //  process an exception.
	    //
	    } catch (InterruptedException e) {
		if (Platform.bERROR) {
    		    String strClassName = getClass().getName();
    		    strClassName = strClassName.substring(strClassName.lastIndexOf('.') + 1);
    		    
	    	    System.err.println("% " + strClassName + ".run : " + e);
    	            e.printStackTrace();
		}
	    }
    	}
    }


    /**
     *  Processes some necessay routines 
     *      before entering the while loop in the run method.
     *  <br>
     *  This is an abstract method, and hence, 
     *      the body part of this method should be implemented by its sub-class.
     */
    protected abstract void init();


    /**
     *  Processes a communication message.
     *  <br>
     *  This is an abstract method, and hence, 
     *      the body part of this method should be implemented by its sub-class.
     * 
     *  @param p_amMsg an actor communication message to be processed.
     */
    protected abstract void processMessage(ActorMessage p_amMsg);


    /**
     *  Inserts a message into the in-coming mail queue of this actor, and
     *  wake up sleeping actors.
     * 
     *  @param p_amMsg an actor communication message to be delivered to this actor.
     */
    public void deliverMessage(ActorMessage p_amMsg)
    {
    	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.insert(p_amMsg);
    	    m_qMsgQueue.notifyAll();
    	}
    }
}
