
package aa.core;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *  This class includes basic behavior of an actor.
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
 *      <li> October 2, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify this file.
 *      </ul>
 *      <li> March 17, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class Actor implements Serializable
{
    // ========== Object Variables ========== //
    
    /**
     *  The reference to the actor thread of this actor behavior class.
     */
    protected ActorThread m_athread = null;
    

    // ========== Object Methods ========== //

    /**
     *  Creates an actor.
     */
    public Actor()
    {
    	m_athread = (ActorThread) Platform.g_htCreators.remove(Thread.currentThread());
    }
    

    /**
     *  Returns the name of this actor.
     * 
     *  @return the name of this actor.
     */
    protected ActorName getActorName()
    {
    	return m_athread.getActorName();
    }


    /**
     *  Releases resource occupied by this actor.
     *  <br>
     *  This method of this class does not include any operation.
     *  When there is the same method in the sub-class of this class,
     *  the method is called.
     *  <br>
     *  This method is triggered by the 'destory' method of this class.
     */
    protected void uninit()
    {
    }
    

    /**
     *  Sets the error message of an asynchronous operation.
     *  <br>
     *  This method is called by the Actor Thread of this actor.
     *  <br>
     *  This method of this class does not include any operation.
     *  When there is the same method in the sub-class of this class,
     *  the method is called.
     *  
     *  @param p_strMsg the error message of an asynchronous operation.
     */
    protected void setErrorMessage(String p_strMsg)
    {
    }
    

    /**
     *  Echos a message received to the sender actor.
     *  <br>
     *  This behavior is a default behavior of every actor.
     * 
     *  @param p_anSender the name of the sender actor.
     *  @param p_strMsg a message to be echoed.
     */
    public void echo(ActorName p_anSender, String p_strMsg)
    {
    	System.out.println("> Actor [" + m_athread.getActorName() + "]: " + p_strMsg);
    	
    	send(p_anSender, "println", p_strMsg);
    }
    
    
    /**
     *  Prints a string message.
     *  <br>
     *  This behavior is a default behavior of every actor.
     * 
     *  @param p_strMsg a message string to be printed.
     */
    public void print(String p_strMsg)
    {
    	System.out.print("> Actor [" + m_athread.getActorName() + "] - " + p_strMsg);
    }
    

    /**
     *  Prints a string message with a line break.
     *  <br>
     *  This behavior is a default behavior of every actor.
     * 
     *  @param p_strMsg a message string to be printed.
     */
    public void println(String p_strMsg)
    {
    	System.out.println("> Actor [" + m_athread.getActorName() + "] - " + p_strMsg);
    }
    

    /**
     *  Prints the last error message of asynchronous operations.
     */
    public void printLastErrorMessage()
    {
    	System.out.println("> Actor [" + m_athread.getActorName() + "] - " + 
		m_athread.getLastErrorMessage());
    }
    
    
    /**
     *  Returns the name of the default Direcotry Manager actor.
     * 
     *  @return the name of the default Directory Manager actor.
     */
    protected ActorName getDefaultDirectoryManager()
    {
    	return m_athread.getDefaultDirectoryManager();
    }


    /**
     *  Returns the name of the default Direcotry Manager actor
     *  on the specified host.
     * 
     *  @param p_strHostName the specified host.
     * 
     *  @return the name of the default Directory Manager actor
     *           on the specified host.
     */
    protected ActorName getDefaultDirectoryManager(String p_strHostName)
    {
    	return m_athread.getDefaultDirectoryManager(p_strHostName);
    }


    /**
     *  Returns the last error message of asynchronous operations.
     *  
     *  @return the last error message of asynchronous operations.
     */
    protected String getLastErrorMessage()
    {
    	return m_athread.getLastErrorMessage();	
    }


    /**
     *  Creates a new actor.
     * 
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return the name of a new actor.
     */
    protected ActorName createActor(String p_strClassName, Object[] p_objaArgs)
        throws CreateActorException
    {
    	return m_athread.createActor(p_strClassName, p_objaArgs);
    }
    
    
    /**
     *  Creates a new actor on a remote system.
     * 
     *  @param p_strHost the host name of a new actor.
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return the name of a new actor.
     */
    protected ActorName createRemoteActor(String p_strHost, String p_strClassName, Object[] p_objaArgs)
        throws CreateActorException
    {
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, p_objaArgs);
    }
    

    /**
     *  Changes the behavior of this actor.
     * 
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return the name of a new actor.
     */
    protected void becomeActor(String p_strClassName, Object[] p_objaArgs)
        throws BecomeActorException
    {
    	m_athread.becomeActor(p_strClassName, p_objaArgs);
    }
    
    
    /**
     *  Sends an out-going message to the sepcified actor.
     * 
     *  @param p_anReceiver the name of the receiver actor of this message.
     *  @param p_strMethod the message (or method) name (or type).
     *  @param p_objaArgs a set of argument objects.
     */
    protected void sendMessage(ActorName p_anReceiver, String p_strMethod, Object[] p_objaArgs)
    {
    	m_athread.sendMessage(p_anReceiver, p_strMethod, p_objaArgs);
    }


    /**
     *  Sends an out-going message, and wait the response.
     * 
     *  @param p_anReceiver the name of the receiver actor of this message.
     *  @param p_strMethod a message (or method) name (or type)
     *  @param p_objaArgs a set of argument objects.
     * 
     *  @return object to be returned.
     * 
     *  @throws CommunicationException if an error happens 
     *               while synchoronous message is processed.
     */
    protected Object callMessage(ActorName p_anReceiver, String p_strMethod, Object[] p_objaArgs)
        throws CommunicationException
    {
   	return m_athread.callMessage(p_anReceiver, p_strMethod, p_objaArgs);
    }


    // -------
    // migrate
    // -------
    
    /**
     *  Initiates the migration of this actor.
     * 
     *  @param p_strHostName the symbolic name of the destination host
     *                       where this actor migrate.
     */
    protected void migrate(String p_strHostName)
        throws UnknownHostException
    {
    	InetAddress iaHost = InetAddress.getByName(p_strHostName);
    	
    	m_athread.migrateActor(iaHost);
    }
    

    //  ---------------
    //  destroy methods
    //  ---------------

    /**
     *  Initiates the destrunction of this actor.
     * 
     *  @param p_strReason the reason of the destruction of this actor.
     */
    protected void destroy(String p_strReason)
    {
    	//
    	//  perform some operations before this actor dies.
    	//
    	uninit();
    	
    	//
    	//  print why this method is called.
    	//
    	if (Platform.bDEBUG) {
    	    System.out.println("> Actor [" + m_athread.getActorName() + "] is destroyed because\n" +
    	    		       "    " + p_strReason);
    	}

	m_athread.destroyActor();
    }

    
    //  --------------------
    //  create actor methods
    //  --------------------
    
    protected ActorName create(String p_strClassName)
    	throws CreateActorException
    {
    	Object[] objaArgs = {};   	
    	return m_athread.createActor(p_strClassName, objaArgs);
    }


    protected ActorName create(String p_strClassName, Serializable p_serArg1)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }    

    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
				Serializable p_serArg6)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
				Serializable p_serArg6, Serializable p_serArg7)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
				Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
				Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8, 
				Serializable p_serArg9)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }
    
    protected ActorName create(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
				Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
				Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8, 
				Serializable p_serArg9, Serializable p_serArg10)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };    
    	return m_athread.createActor(p_strClassName, objaArgs);
    }


    //  --------------------
    //  create actor methods
    //  --------------------
    
    protected ActorName createRemote(String p_strHost, String p_strClassName)
    	throws CreateActorException
    {
    	Object[] objaArgs = {};   	
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }


    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }    

    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
				Serializable p_serArg2)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
 				Serializable p_serArg2, Serializable p_serArg3)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
				Serializable p_serArg5)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
				Serializable p_serArg5, Serializable p_serArg6)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
    				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
    				Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
    				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
    				Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7, 
    				Serializable p_serArg8)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
    				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
    				Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7, 
    				Serializable p_serArg8, Serializable p_serArg9)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }
    
    protected ActorName createRemote(String p_strHost, String p_strClassName, Serializable p_serArg1, 
    				Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4, 
    				Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7, 
    				Serializable p_serArg8, Serializable p_serArg9, Serializable p_serArg10)
    	throws CreateActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };    
    	return m_athread.createRemoteActor(p_strHost, p_strClassName, objaArgs);
    }


    //  --------------
    //  become methods
    //  --------------
    
    protected void become(String p_strClassName)
    	throws BecomeActorException
    {
    	Object[] objaArgs = {};   	
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }


    protected void become(String p_strClassName, Serializable p_serArg1)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }    

    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
			  Serializable p_serArg6)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
			  Serializable p_serArg6, Serializable p_serArg7)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
			  Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
			  Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8, 
			  Serializable p_serArg9)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }
    
    protected void become(String p_strClassName, Serializable p_serArg1, Serializable p_serArg2, 
			  Serializable p_serArg3, Serializable p_serArg4, Serializable p_serArg5, 
			  Serializable p_serArg6, Serializable p_serArg7, Serializable p_serArg8, 
			  Serializable p_serArg9, Serializable p_serArg10)
    	throws BecomeActorException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };    
    	m_athread.becomeActor(p_strClassName, objaArgs);
    }


    //  ------------
    //  send methods
    //  ------------
    
    protected void send(ActorName p_anReceiver, String p_strMethod)
    {
    	Object[] objaArgs = {};    	 
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1)
    {
        Object[] objaArgs = { p_serArg1 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8, Serializable p_serArg9)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected void send(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8, Serializable p_serArg9, Serializable p_serArg10)
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };    
	m_athread.sendMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    

    //  ------------
    //  call methods
    //  ------------
    
    protected Object call(ActorName p_anReceiver, String p_strMethod)
    	throws CommunicationException
    {
    	Object[] objaArgs = {};    	 
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }    

    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8, Serializable p_serArg9)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
    
    protected Object call(ActorName p_anReceiver, String p_strMethod, Serializable p_serArg1,
			Serializable p_serArg2, Serializable p_serArg3, Serializable p_serArg4,
			Serializable p_serArg5, Serializable p_serArg6, Serializable p_serArg7,
			Serializable p_serArg8, Serializable p_serArg9, Serializable p_serArg10)
    	throws CommunicationException
    {
        Object[] objaArgs = { p_serArg1, p_serArg2, p_serArg3, p_serArg4, p_serArg5,
        		      p_serArg6, p_serArg7, p_serArg8, p_serArg9, p_serArg10 };    
	return m_athread.callMessage(p_anReceiver, p_strMethod, objaArgs);
    }
}
