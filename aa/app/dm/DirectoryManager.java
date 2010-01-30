
package aa.app.dm;

import java.io.Serializable;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.Platform;
import aa.tool.ActorTuple;
import aa.tool.PublicTupleSpace;
import aa.tool.TupleSpaceException;
import aa.util.ObjectHandler;

/**
 *  This class provides three different types of middle man service:
 *  matchmaking, brokering, and active brokering service.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 19, 2004 - version 0.0.4
 *	<ul>
 *	    <li> move the active brokering service from this actor
 *		 to aa.app.atspace.ATSpace.
 *	    <li> add the 'update' method.
 *	</ul>
 *      <li> January 26, 2003 - version 0.0.3
 *      <ul>
 *          <li> modify this file.
 *      </ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 5, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class DirectoryManager extends Actor
{
    // ========== Object Variables ========== //

    /**
     *  Tuple Space as a blackboard.
     */
    protected PublicTupleSpace m_pubTSpace;
    
    
    // ========== Object Methods ========== //
    
    /**
     *  Creates a Directory Manager.
     */
    public DirectoryManager()
    {
    	m_pubTSpace = new PublicTupleSpace();
    }
    
    
    /**
     *  Delivers a message to a randomly selected actor
     *      whose information is matched with the given tuple pattern.
     * 
     *  @param p_atReceiver the tuple pattern to find a receiver actor.
     *  @param p_strMethod the method name to be called.
     *  @param p_objaArgs arguments to be deliverd to the receiver actor.
     */
    protected boolean deliverImpl(ActorTuple p_atReceiver, String p_strMethod, Object[] p_objaArgs)
	throws Exception
    {
	ActorName anReceiver = search(p_atReceiver);

	if (anReceiver != null) {
	    sendMessage(anReceiver, p_strMethod, p_objaArgs);

	    return true;
	} else {
    	    if (Platform.bERROR) {
            	System.err.println("% TupleSpace.deliverMessage: " +
            	    "WARNING - No actor is matched");
    	    }
    	    
	    return false;
	}
    }


    /**
     *  Broadcasts a message to all actors 
     *      whose information is matched with the given tuple pattern.
     * 
     *  @param p_atReceiver the tuple pattern to find receiver actors.
     *  @param p_strMethod the method name to be called.
     *  @param p_objaArgs arguments to be deliverd to receiver actors. 
     */
    protected boolean deliverAllImpl(ActorTuple p_atReceiver, String p_strMethod, Object[] p_objaArgs)
	throws Exception
    {
	ActorName[] anaReceivers = searchAll(p_atReceiver);

	if ( (anaReceivers != null) && (anaReceivers.length != 0) ) {
	    for (int i=0; i<anaReceivers.length; i++) {
	    	sendMessage(anaReceivers[i], p_strMethod, p_objaArgs);
	    }
		
	    return true;
	} else {
    	    if (Platform.bERROR) {
                System.err.println("> DirectoryManager.deliverAllImpl[1]: " +
                    "WARNING - No actors are matched");
    	    }
    	    
	    return false;
	}
    }


    /**
     *  Delivers a message to a randomly selected actor
     *      whose information is matched with the given tuple pattern.
     * 
     *  @param p_atReceiver the tuple pattern to find a receiver actor.
     *  @param p_strMethod the method name to be called.
     *  @param p_objaArgs arguments to be deliverd to the receiver actor.
     */
    protected boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Object[] p_objaArgs)
    {
	try {
	    return(deliverImpl(p_atReceiver, p_strMethod, p_objaArgs));
	} catch (Exception e) {
    	    if (Platform.bERROR) {
	    	System.err.println("> DirectoryManager.deliver[1]: " + e);
    	    }
    	    
	    return false;
	}
    }


    /**
     *  Broadcasts a message to all actors 
     *      whose information is matched with the given tuple pattern.
     * 
     *  @param p_atReceiver the tuple pattern to find receiver actors.
     *  @param p_strMethod the method name to be called.
     *  @param p_objaArgs arguments to be deliverd to receiver actors. 
     */
    protected boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Object[] p_objaArgs)
    {
	try {
	    return(deliverAllImpl(p_atReceiver, p_strMethod, p_objaArgs));
	} catch (Exception e) {
    	    if (Platform.bERROR) {
	    	System.err.println("% DirectoryManager.deliverAll[1]: " + e);
    	    }
    	    
	    return false;
	}
    }


    /**
     *  Registers an actor tuple to this Directory Manager.
     *  
     *  @param p_atTuple an actor tuple to be inserted in this Directory Manager.
     */
    public void register(ActorTuple p_atTuple)
    {
    	try {
    	    m_pubTSpace.write(p_atTuple);
    	} catch (TupleSpaceException e) {
    	    if (Platform.bERROR) {
            	System.err.println("% DirectoryManager.register: " + e);
    	    	e.printStackTrace();
    	    }
    	}
    }
    
    
    /**
     *  Removes an actor tuple in this Directory Manager.
     * 
     *  @param p_atTuple a tuple data to be removed in this Directory Manager.
     */
    public void deregister(ActorTuple p_atTuple)
    {
    	try {
    	    m_pubTSpace.removeAll(p_atTuple);
    	} catch (TupleSpaceException e) {
    	    if (Platform.bERROR) {
            	System.err.println("% DirectoryManager.remove: " + e);
    	    	e.printStackTrace();
    	    }
    	}
    }
    
    
    /**
     *  Removes actor tuples matched with the given tuple template,
     *      and then inserts an actor tuple in this tuple space.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     *  @param p_atTuple an actor tuple to be stored in this tuple space.
     */
    public void update(ActorTuple p_atTemplate, ActorTuple p_atTuple)
    {
    	try {
    	    m_pubTSpace.update(p_atTemplate, p_atTuple);
    	} catch (TupleSpaceException e) {
    	    if (Platform.bERROR) {
            	System.err.println("% ATSpace.remove: " + e);
    	    	e.printStackTrace();
    	    }
    	}
    }


    /**
     *  Retrieves any one of names of actors that 
     *      support the specified tuple.
     *  
     *  @param p_atTemplate a tuple template to find the name of the actor 
     *                      that supports the specified attributes.
     */
    public ActorName search(ActorTuple p_atTemplate)
        throws Exception
    {
    	if (p_atTemplate.getActorName() != null) {
    	    throw new Exception("Exception: the field of actor name should be null");
    	}
    	
    	try {
    	    return m_pubTSpace.search(p_atTemplate);
     	} catch (TupleSpaceException e) {
    	    return null;
    	}
    }


    /**
     *  Retrieves names of all actors that 
     *      support the specified attribute.
     *  
     *  @param p_atTemplate a tuple template to find the actor name of the actor 
     *                      that supports the specified attributes.
     */
    public ActorName[] searchAll(ActorTuple p_atTemplate)
        throws Exception
    {
    	if (p_atTemplate.getActorName() != null) {
    	    throw new Exception("Exception: the first parameter should be null");
    	}
    	
    	try {
    	    return m_pubTSpace.searchAll(p_atTemplate);    	    
    	} catch (TupleSpaceException e) {
    	    return (ActorName[]) new ActorName[0];
    	}
    }
    
    
    /**
     *  Prints all actor tuples in this Directory Manager.
     */
    public void printAll()
    {
    	ActorTuple[] ataActorTuples = m_pubTSpace.getActorTuples();

	System.out.println("> Directory Manager: ");
	for (int i=0; i<ataActorTuples.length; i++) {
	    System.out.println("    [" + i + "] " + ataActorTuples[i].getActorName() + " - " +
	    			ObjectHandler.toStringArgs(ataActorTuples[i].getElements()));    	    
	}
    }
    
    
    //  -----------------
    //   deliver methods 
    //  -----------------
    
    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod)
    {
	return(deliver(p_atReceiver, p_strMethod, new Object[0]));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1)
    {
	Object[] objaArgs = { p_serArg1 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

     public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5, Serializable p_serArg6)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6};

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			   Serializable p_serArg8)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			   Serializable p_serArg8, Serializable p_serArg9)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliver(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			   Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			   Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			   Serializable p_serArg8, Serializable p_serArg9, Serializable p_serArg10)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };

	return(deliver(p_atReceiver, p_strMethod, objaArgs));
    }


    //  --------------------
    //   deliverAll methods
    //  --------------------
    
    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod)
    {
	return(deliverAll(p_atReceiver, p_strMethod, new Object[0]));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1)
    {
	Object[] objaArgs = { p_serArg1 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5, Serializable p_serArg6)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6};

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			      Serializable p_serArg8)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			      Serializable p_serArg8, Serializable p_serArg9)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }

    public boolean deliverAll(ActorTuple p_atReceiver, String p_strMethod, Serializable p_serArg1,
			      Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			      Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			      Serializable p_serArg8, Serializable p_serArg9, Serializable p_serArg10)
    {
	Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
			      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };

	return(deliverAll(p_atReceiver, p_strMethod, objaArgs));
    }   
}