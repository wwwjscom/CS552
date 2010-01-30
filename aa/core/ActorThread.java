
package aa.core;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import aa.util.ObjectHandler;
import aa.util.Queue;

/**
 *  This class describes an actor thread. 
 *  An actor thread works with an Actor (behavior) class.
 * 
 *  <p>
 *  <b>History</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 13, 2004 - version 0.0.1
 * 	<ul>
 *          <li> create this file.
 *          <li> This class is made from the Actor class.
 * 	</ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class ActorThread implements Runnable, Serializable
{
    // ========== Object Variables ========== //
    
    private ActorName m_anActorName;	//  name of this actor.

    private Class  m_classActor;	//  actor behavior class of this actor.
    private String m_strClassName;	//  name of this actor behavior class.

    private Actor m_actorBehavior;	//  reference to the behavior of this actor.

    private String m_strLastErrorMessage;
    					//  last error message	

    private InetAddress m_iaDestHost;	//  IP adress of the destination host of actor migration

    private boolean m_bSuspend;		//  trigger to suspend this actor.
    private boolean m_bMigrate;		//  trigger to migrate this actor.
    private boolean m_bDestroy;		//  trigger to destroy this actor.
    
    protected boolean m_bRetire;	//  trigger to destory this actor 
    					//      without reporting to an Actor Manager.

    private Queue m_qMsgQueue;		//  in-coming message queue of this actor.
    
    private transient Hashtable m_htMethods;	
					//  public methods of this actor
					//      that mean actor behavior.

    private transient Platform m_pPlatform;	
    					//  pointer to the current actor platform.


    // ========== Object Methods ========== //

    /**
     *  Creates an actor thread.
     * 
     *  @param p_anActor an actor name of this actor.
     */
    public ActorThread(ActorName p_anActor, boolean p_bCreate) 
    {
	//
	//  initialize object variables with parameters.
	//
  	m_anActorName = p_anActor;
  	
  	//
  	//  initialize object variables.
  	//
    	m_bSuspend = false;
    	m_bMigrate = false;
    	m_bDestroy = false;
    	m_bRetire  = false;

    	m_qMsgQueue = new Queue();

  	m_pPlatform = Platform.getPlatform();
    	m_anActorName.setLocationAddress(m_pPlatform.getHostAddress());
    	
	//
	//  register this actor or update the state of this actor.
	//
    	if (p_bCreate) {
    	    m_pPlatform.registerActor(this, ActorManager.UNKNOWN);
    	} else {
    	    m_pPlatform.updateActor(this, ActorManager.UNKNOWN);
    	}
    }
    
    
    /**
     *  Finds a proper constructor.
     *  <br>
     *  This method is called by the <code>setBehavior</code> method.
     * 
     *  @param p_classActor the class of an actor to be created.
     *  @param p_objaArgs arguments of the constructor of the actor.
     * 
     *  @return constructor of a new actor.
     * 
     *  @throws NoSuchMethodException it occurs when the construct cannot be found.
     */
    private Constructor findConstructor(Class p_classActor, Object[] p_objaArgs)
    	throws NoSuchMethodException
    {
    	//
    	//  grap all public constructors.
    	//
    	Constructor[] caConstructor = p_classActor.getConstructors();
    	if (caConstructor.length == 0) {
    	    throw new NoSuchMethodException("Class '" + p_classActor + "' has no public constructors!");
    	}
    	
    	//
    	//  find a proper constructor and return it.
    	//
    	for (int i=0; i<caConstructor.length; i++) {
    	    Class[] caParameterType = caConstructor[i].getParameterTypes();
    	    
    	    if (p_objaArgs.length != caParameterType.length) {
    	    	continue;
    	    }
    	    
    	    boolean bFound = true;
    	    for (int j=0; j<p_objaArgs.length; j++) {
    	    	if (!caParameterType[j].isInstance(p_objaArgs[j])) {
    	    	    bFound = false;
    	    	    break;
    	    	}
    	    }
    	    
    	    if (bFound) {
    	    	return caConstructor[i];
    	    }
    	}
    	
    	//
    	//  if cannot find a prorper constructor, 
    	//  then report an error.
    	//
        throw new NoSuchMethodException("No constructor for '" + p_classActor + 
      				         ObjectHandler.toStringArgs(p_objaArgs) + "'.");
    }


    /**
     *  Creates a method table.
     *  <br>
     *  This method is called by the <code>setBehavior</code> or 
     * 	<code>initAfterMigration</code>.
     */
    private void createMethodTable()
    {
    	m_htMethods = new Hashtable();

    	Method[] maMethods = m_classActor.getMethods();
    	
    	for (int i=0; i<maMethods.length; i++) {
	    //
	    //  ignore methods of java.lang.Object.
	    //
	    if (maMethods[i].getDeclaringClass().getName().toString().equalsIgnoreCase("java.lang.Object")) {
		continue;
	    }

	    //
	    //  make a linked list for a method and
	    //  insert the list to the method table.
	    //
    	    LinkedList llMethods;
    	    
    	    if (m_htMethods.containsKey(maMethods[i].getName())) {
    	    	llMethods = (LinkedList) m_htMethods.remove(maMethods[i].getName());
    	    } else {
    	    	llMethods = new LinkedList();
    	    }

	    llMethods.add(maMethods[i]);
    	    	
   	    m_htMethods.put(maMethods[i].getName(), llMethods);
    	}
    }
    
    
    /**
     *  Sets the behavior of this actor thread.
     * 
     *  @param p_strActorClass the behavior class of an actor to be created.
     *  @param p_objaArgs arguments of the constructor of the actor behavior.
     *  @param p_qMsgQueue reference to a message queue to be used in this actor.
     * 
     *  @throws CreateActorException it occurs when actor creation fails.
     */
    protected void setBehavior(String p_strActorClass, Object[] p_objaArgs, Queue p_qMsgQueue) 
        throws ActorException
    {
    	//
    	//  register information about this actor.
    	//
    	Platform.g_htCreators.put(Thread.currentThread(), this);
    	
        //
        //  create the actor behavior class.
        //
        try {
            Class classActor = Class.forName(p_strActorClass);
            
            if (p_objaArgs.length == 0) {
            	m_actorBehavior = (Actor) classActor.newInstance();
            } else {
            	Constructor constructor = findConstructor(classActor, p_objaArgs);
            	m_actorBehavior = (Actor) constructor.newInstance(p_objaArgs);
            }
            
        //
        //  exception handling routines.
        //
        } catch (ClassNotFoundException e) {
            if (Platform.bDEBUG) {
                System.out.println("% ActorThread.setBehavior: " + e + 
                		   "\n    - Class [" + p_strActorClass + "] Not Found");
    	    	e.printStackTrace();
            }
            
    	    throw new ActorException("Class [" + p_strActorClass + "] Not Found");
        } catch (InstantiationException e) {
            if (Platform.bERROR) {
                System.err.println("% ActorThread.setBehavior: " + e);
    	    	e.printStackTrace();
            }
            
    	    throw new ActorException(e.getMessage());
        } catch (InvocationTargetException e) {
            if (Platform.bERROR) {
                System.err.println("% ActorThread.setBehavior: " + e);
    	    	e.printStackTrace();
            }
            
    	    throw new ActorException(e.getMessage());
        } catch (IllegalAccessException e) {
            if (Platform.bERROR) {
                System.err.println("% ActorThread.setBehavior: " + e);
    	    	e.printStackTrace();
            }
            
    	    throw new ActorException(e.getMessage());
        } catch (NoSuchMethodException e) {
            if (Platform.bERROR) {
                System.err.println("% ActorThread.setBehavior: " + e);
    	    	e.printStackTrace();
            }
            
    	    throw new ActorException(e.getMessage());
        }
        
        //
        //  set object variables.
        //
	m_classActor = m_actorBehavior.getClass();
	m_strClassName = m_classActor.getName();

    	createMethodTable();
    	
    	//
    	//  if this receives the reference to a message queue,
    	//  then replace the current message queue with it, and
    	//       attach new messages to the message queue.
    	//
    	//  Note:
    	//	This part is performed 
    	//      when this method is called by the 'become' operatro.
    	//
    	if (p_qMsgQueue != null) {
    	    synchronized (m_qMsgQueue) {
    	        Queue qTmpQueue = m_qMsgQueue;
    	    	m_qMsgQueue = p_qMsgQueue;
    	    	m_qMsgQueue.insertAll(qTmpQueue);
    	    }
    	}
    }
    
    
    /**
     *  Re-initialize after actor migration.
     */
    protected void initAfterMigration()
    {
  	m_pPlatform = Platform.getPlatform();
    	m_anActorName.setLocationAddress(m_pPlatform.getHostAddress());	

    	createMethodTable();
    }
    
    
    /**
     *  Executes the main loop of this thread.
     */
    public void run() 
    {
    	if (Platform.bDEBUG) {
      	    System.out.println("> Actor [" + m_anActorName + "] is created");
    	}
    	
	while (true) {
	    //
	    //  if the 'become' operation is performed correctly,
	    //  then destroy this actor without reporting its death.
	    //
	    if (m_bRetire) {
	    	return;
	    }
	    	
	    //
	    //  if the 'destroy' operation is requested,
	    //  then destroy this actor after reporting its death.
	    //
	    if (m_bDestroy) {
    		m_pPlatform.removeActor(m_anActorName);
	    	return;
	    }
	    	
	    //
	    //  if the 'suspend' operation is requested, 
	    //  then go to sleep.
	    //
	    if (m_bSuspend) {
	        synchronized (m_qMsgQueue) {
	    	    try {
	    	    	//
	    	    	//  go to sleep
	    	    	//
	    	    	m_qMsgQueue.wait();
	    	    } catch (InterruptedException e) {
		    	if (Platform.bERROR) {
	    	    	    System.err.println("% Actor.run[1]: " + e);
    	            	    e.printStackTrace();
	    		}
		    }
	        }
		
		continue;	    	   	
	    }
		
	    //
	    //  if the migration operation is requested,
	    //  then migrate this actor.
	    //
	    if (m_bMigrate == true) {
    		m_bMigrate = false;
    		
    		try {
    		    m_pPlatform.migrateActor(m_anActorName, m_iaDestHost);
	    	    return;
    		} catch (MigrateActorException e) {
    		    if (Platform.bERROR) {
    		    	System.err.println("% Actor.run[2]:" + e);
    		    	e.printStackTrace();
    		    }
    		    
    		    continue;
    		}
	    }

	    //
	    //  if there are in-comming messages, 
	    //  then process the messages.
	    //  otherwise, wait.
	    //
	    ActorMessage amMsg;

	    synchronized (m_qMsgQueue) {
	    	if (m_qMsgQueue.isEmpty()) {
	    	    try {
	    	    	//
	    	    	//  go to sleep
	    	    	//
	    	    	m_qMsgQueue.wait();
	    	    } catch (InterruptedException e) {
		    	if (Platform.bERROR) {
	    	    	    System.err.println("% Actor.run[3]: " + e);
    	            	    e.printStackTrace();
	    		}
		    }
		    	
		    continue;
	    	} else {
	    	    //
	    	    //  if there are some in-coming messages, 
	    	    //	process the first message.
	    	    //
	    	    amMsg = (ActorMessage)m_qMsgQueue.remove();
	    	}
	    }
	    	
	    processMessage(amMsg);
	}
    }
    
    
    /**
     *  Processes a communication message.
     */
    protected void processMessage(ActorMessage p_amMsg)
    {
    	//
    	//  check whether the method is a reply about an error.
    	//
    	if (p_amMsg.isErrorMessage()) {
    	    m_strLastErrorMessage = p_amMsg.getErrorMessage();
    	    return;
    	}    		

    	//
    	//  check whether the method is a reply message.
    	//
    	if (p_amMsg.isReturnMessage()) {
    	    if (Platform.bERROR) {
    	    	System.err.println("> Actor.processMessage: A wrong reply message");
    	    }
    	    
    	    m_strLastErrorMessage = new String("A wrong reply message has arrived");
    	    
    	    return;
    	}
    	
    	//
    	//  check whether the method name of the given message is null.
    	//
    	boolean bFound = false;
    	
    	String strMethod = p_amMsg.getMethod();
    	Object[] oaArgs  = p_amMsg.getArguments(); 
    	
    	if (strMethod == null) {
    	    //
    	    //  TO DO
    	    //
	    if (Platform.bERROR) {
    	    	System.err.println("> Actor.processMessage: Null Method");
	    }
	        
    	    return;
    	}
    	
    	//
    	//  search a method matched with the given message.
    	//
    	LinkedList llMethods = (LinkedList)m_htMethods.get(strMethod);
    	Method method = null;
    	
    	if ( (llMethods != null) && (llMethods.size() != 0) ) {
    	    Iterator iterMethods = llMethods.iterator();
    	    while (iterMethods.hasNext()) {
    	    	method = (Method)iterMethods.next();
    	    	Class[] caParameterType = method.getParameterTypes();
    	    	
    	    	if (caParameterType.length == oaArgs.length) {
    	    	    bFound = true;
    	    	    
    	    	    for (int i=0; i<oaArgs.length; i++) {
    	    	    	if ( (oaArgs[i] != null) && 
    	    	    	     (!caParameterType[i].isInstance(oaArgs[i])) ) {
    	    	    	    bFound = false; 
    	    	    	    break;
    	    	    	}
    	    	    }
    	    	}
    	    	
    	    	if (bFound) {
    	    	    break;
    	    	}
    	    }
    	} else {
    	    bFound = false;
    	}
    	
    	//
    	//  if there is the (public) actor method 
    	//	matched with the specified parameter types
    	//
    	if (bFound) {
    	    try {
    	    	//
    	    	//  invoke a method in this actor.
    	    	//
    	    	Object objReturn = method.invoke(m_actorBehavior, oaArgs);
    	    	    
    	    	if (p_amMsg.isReturnRequested()) {
    	    	    //
    	    	    //  send a return object as a message.
    	    	    //
    	    	    ActorMessage amMsg = p_amMsg.makeReturnMessage(objReturn);
    	    	    m_pPlatform.sendMessage(amMsg);
    	    	}
    	    	 
    	    //
    	    //  exception handling
    	    //
    	    } catch (IllegalAccessException e) {
    	    	if (Platform.bERROR) {
    	    	    System.err.println("% Actor.processMessage: " + e);
      	            e.printStackTrace();
    	    	}

		ActorMessage amMsg = p_amMsg.makeErrorMessage(e.getMessage());
		m_pPlatform.sendMessage(amMsg);
    	    } catch (InvocationTargetException e) {
    	    	if (Platform.bERROR) {
    	    	    System.err.println("% Actor.processMessage: " + e);
      	            e.printStackTrace();
    	    	}

		ActorMessage amMsg = p_amMsg.makeErrorMessage(e.getMessage());
		m_pPlatform.sendMessage(amMsg);
    	    }
    	} else {
    	    //
    	    //  when the method called is not found,
    	    //      send a error message to the sender actor.
    	    //
    	    String strErrMsg = "Cannot find the method '" + 
    	    			strMethod + ObjectHandler.toStringArgs(oaArgs) + 
				"' in the actor [" +
    	    			m_strClassName + " - " +m_anActorName + "].";

    	    if (Platform.bERROR) {
    	    	System.err.println("> Actor.processMessage: " + strErrMsg);
    	    }

	    ActorMessage amMsg = p_amMsg.makeErrorMessage(strErrMsg);
	    m_pPlatform.sendMessage(amMsg);
    	}
    }
    
    
    /**
     *  Inserts a message into the mail queue of this actor, and
     *  wakes up sleeping actors.
     *  <br>
     *  This method is called by the Message Manager thread.
     *  That is, the control of this method is occupied by the Message Manager thread.
     * 
     *  @param p_amMsg an actor communication message.
     */
    protected void deliverMessage(ActorMessage p_amMsg)
    {
	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.insert(p_amMsg);
    	    m_qMsgQueue.notifyAll();
    	}
    }
    

    /**
     *  Suspends this actor.
     *  <br>
     *  This method is directly called by an Actor Manager.
     */
    protected void suspend()
    {
    	m_bSuspend = true;
    }
    
    
    /**
     *  Resumes this actor.
     *  <br>
     *  This method is directly called by an Actor Manager.
     */
    protected void resume()
    {
    	m_bSuspend = false;

    	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.notifyAll();
    	}
    }
    
    
    /**
     *  Kills this actor.
     *  <br>
     *  This method is directly called by an Actor Manager.
     */
    protected void kill()
    {
    	m_actorBehavior.destroy("This method is called by an Actor Manager");
    }
    
    
    /**
     *  Returns the name of this actor.
     * 
     *  @return the name of this actor.
     */
    protected ActorName getActorName()
    {
    	return m_anActorName;
    }


    /**
     *  Returns the class name of this actor.
     * 
     *  @return the class name of this actor.
     */
    protected String getActorClassName()
    {
    	return m_strClassName;
    }


    /**
     *  Sets the error message of an asynchronous operation.
     *  <br>
     *  This method is called by a Migration Manager.
     *  
     *  @param p_strMsg the error message of an asynchronous operation.
     */
    protected void setLastErrorMessage(String p_strMsg)
    {
    	m_strLastErrorMessage = p_strMsg;
    	
    	m_actorBehavior.setErrorMessage(p_strMsg);
    }


    /**
     *  Returns the last error message of asynchronous operations.
     *  
     *  @return the last error message of asynchronous operations.
     */
    protected String getLastErrorMessage()
    {
    	return m_strLastErrorMessage;	
    }


    // ===================================================================
    //   The following methods are called by the Actor (behavior) class.
    // ===================================================================

    /**
     *  Returns the actor name of the default Direcotry Manager actor.
     * 
     *  @return The actor name of the default Directory Manager actor.
     */
    protected ActorName getDefaultDirectoryManager()
    {
    	return m_pPlatform.getDefaultDirectoryManager();
    }


    /**
     *  Returns The actor name of the default Direcotry Manager actor
     *  on the specified host.
     * 
     *  @param p_strHostName the specified host.
     * 
     *  @return The actor name of the default Directory Manager actor
     *           on the specified host.
     */
    protected ActorName getDefaultDirectoryManager(String p_strHostName)
    {
    	return m_pPlatform.getDefaultDirectoryManager(p_strHostName);
    }


    /**
     *  Creates a new actor.
     *  <br>
     *  This method is called by the 'create' method.
     * 
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return The actor name of a new actor.
     */
    protected ActorName createActor(String p_strClassName, Object[] p_objaArgs)
        throws CreateActorException
    {
    	return m_pPlatform.createActor(p_strClassName, p_objaArgs);
    }
    
    
    /**
     *  Create a new actor on a remote system.
     * 
     *  @param p_strHost the host name of a new actor.
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return The actor name of a new actor.
     */
    protected ActorName createRemoteActor(String p_strHost, String p_strClassName, Object[] p_objaArgs)
        throws CreateActorException
    {
    	ActorName anHostPlatform = m_pPlatform.getActorNameOfPlatform(p_strHost);
    	
    	//
    	//  check an error condition.
    	//
    	if (anHostPlatform == null) {
    	    throw new CreateActorException("Cannot Find the Host - " + p_strHost);
    	}
    	
    	//
    	//  if the platform of a new actor is the same as this platform,
    	//  then call the actor creation method of the remote platform.
    	//
    	if (anHostPlatform.equals(m_pPlatform.getActorNameOfPlatform())) {
    	    return m_pPlatform.createActor(p_strClassName, p_objaArgs);
    	    
    	//
    	//  otherwise, then call the actor create method of this platform.
    	//
    	} else {
    	    ActorName anNewActor = null;
    	
    	    //
    	    //  compose the name of a new actor and its initial agruments.  
    	    //
    	    Object[] oaArgs = new Object[p_objaArgs.length+1];
    	
    	    oaArgs[0] = p_strClassName;
    	    for (int i=0; i<p_objaArgs.length; i++) {
    	    	oaArgs[i+1] = p_objaArgs[i];
    	    }
    	
    	    //
    	    //  make a call to create an actor in the different platform.
    	    //    	
    	    try {
    	    	return (ActorName) callMessage(anHostPlatform, "createActor", oaArgs);
    	    } catch (CommunicationException e) {
    	    	throw new CreateActorException(e.getMessage());
    	    }
    	}	
    }
    
    
    /**
     *  Change the behavior of this actor.
     *  <br>
     *  This method is called by the 'become' method.
     * 
     *  @param p_strClassName the class name of a new actor.
     *  @param p_objaArgs an array of arguments of a new actor.
     * 
     *  @return The name of a new actor.
     */
    protected void becomeActor(String p_strClassName, Object[] p_objaArgs)
        throws BecomeActorException
    {
    	//
    	// if the become operator is called beforehand, ... 
    	//
    	if (m_bRetire) {
    	    throw new BecomeActorException("Anonymous actor cannot request the become operator.");
    	}
    	
	//
	//  pack interal states.
	//
    	boolean[] baFlags = new boolean[3];
    	
    	baFlags[0] = m_bSuspend;
    	baFlags[1] = m_bMigrate;
    	baFlags[2] = m_bDestroy;

	//
	//  call the 'becomeActor' method in this Platform.
	//
    	m_pPlatform.becomeActor(this, m_qMsgQueue, baFlags, p_strClassName, p_objaArgs);
    	
    	m_bRetire = true;
    }
    
    
    /**
     *  Sends an out-going message to the sepcified actor.
     *  <br>
     *  This method is called by the 'send' method.
     * 
     *  @param p_anReceiver the name of the receiver actor of this message.
     *  @param p_strMethod a message (or method) name (or type).
     *  @param p_objaArgs a set of argument objects.
     */
    protected void sendMessage(ActorName p_anReceiver, String p_strMethod, Object[] p_objaArgs)
    {
    	ActorMessage amMsg = new ActorMessage(m_anActorName, p_anReceiver, p_strMethod, p_objaArgs, false);
	m_pPlatform.sendMessage(amMsg);
    }
    

    /**
     *  Sends an out-going message, and wait the response.
     *  <br>
     *  This method is called by the 'call' method.
     * 
     *  @param p_anReceiver the name of the receiver actor of this message.
     *  @param p_strMethod a message (or method) name (or type).
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
    	Object objReturn = null;
    	Queue  qWaitMail = null;
    	String strErrMsg = null;
    	
    	//
    	//  check if this acotr makes a call to itself.
    	//
    	if (p_anReceiver.equals(m_anActorName)) {
    	    throw new CommunicationException("Actor [" + m_anActorName + "] makes a call to itself");
    	}
    	
    	//
    	//  move the current messages into the waiting queue
    	//
    	synchronized (m_qMsgQueue) {
    	    qWaitMail = m_qMsgQueue;
    	    m_qMsgQueue = new Queue();
    	}
    	
    	//
    	//  send a message to the platform
    	//
    	ActorMessage amMsg = new ActorMessage(m_anActorName, p_anReceiver, p_strMethod, p_objaArgs, true);
	m_pPlatform.sendMessage(amMsg);
	
	amMsg = null;
	    
	while (true) {
	    //
	    //  if there are incomming messages, process incomming messages.
	    //  otherwise, wait.
	    //
            synchronized (m_qMsgQueue) {
		if (m_qMsgQueue.isEmpty()) {
		    try {
	    	    	//
	    	    	//  if there is nothing to do, go to sleep
	    	    	//
	    	   	m_qMsgQueue.wait();
		    } catch (InterruptedException e) {
	    	    	if (Platform.bERROR) {
	    	            System.err.println("Actor.run : " + e);
    	                    e.printStackTrace();
	    	    	}
    	            
			strErrMsg = e.getMessage();
			break;	    	            
	    	    }
	    	    
	    	    continue;
	    	} else {
	    	    //
	    	    //  if there are some in-coming messages, 
	    	    //	process the first message.
	    	    //
		    if (!m_qMsgQueue.isEmpty()) {
	    	    	amMsg = (ActorMessage)m_qMsgQueue.remove();
		    }
	    	}
	    }

	    //
	    //  if the message is the return message, ...
	    //
	    if (amMsg.isReturnMessage()) {
		//
		//  if the return message contains the return value expected
		//
		if (!amMsg.isErrorMessage()) {
		    objReturn = amMsg.getArguments()[0];
		} else {
		    //
		    //  if the return message is an error message,
		    //	throw an excecption.
		    //
		    strErrMsg = (String) amMsg.getErrorMessage();
		}
		
		break;
	    } else {
		//
		//  if the message is not, 
		//	enqueue the message into the waiting queue
		//
		qWaitMail.insert(amMsg);
	    }
	}
	
	//
	//  add messages stored in qWaitMail to m_qMsgQueue.
	//    	m_qMsgQueue <= qWaitMail;
	//
        synchronized (m_qMsgQueue) {
    	    while (!qWaitMail.isEmpty()) {
    	        amMsg = (ActorMessage)qWaitMail.remove();
    	        m_qMsgQueue.insert(amMsg);
    	    }
        }
        
        if (strErrMsg != null) {
    	    throw new CommunicationException(strErrMsg);
        }

	return objReturn;
    }


    /**
     *  Initializes the migration of this actor.
     *  <br>
     *  This method is called by the 'migrate' method of the Actor class.
     * 
     *  @param p_iaDestHost the Inet address of the destination host
     *                      where this actor migrate.
     */
    protected void migrateActor(InetAddress p_iaDestHost)
    {
    	m_bMigrate = true;
    	m_iaDestHost = p_iaDestHost;
    }
    
    
    /**
     *  Destroys this actor thread. 
     *  <br>
     *  This method is called by the 'destroy' method of the Actor class.
     */
    protected void destroyActor()
    {
	//
	//  destroy this actor thread.
	//
	m_bSuspend = false;
    	m_bMigrate = false;
    	m_bDestroy = true;
    	    
    	synchronized (m_qMsgQueue) {
    	    m_qMsgQueue.notifyAll();
    	}    	    
    }
}
