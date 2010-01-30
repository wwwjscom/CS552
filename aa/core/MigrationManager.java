
package aa.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.TreeMap;

import aa.tool.MessageObjectInputStream;
import aa.tool.SystemThread;

/**
 *  This class specifies a Migration Manger to handle mobile actors.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 14, 2004 - version 0.0.2
 *      <ul>
 *          <li> debug the 'fetchClassFile' method.
 *      </ul>
 *      <li> April 12, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */

public class MigrationManager extends SystemThread
{
    // ========== Object Variables ========== //

    /**
     *  The host address of this platform.
     */
    private String m_strHostAddressOfPlatform;
    
    /**
     *  The actor name of this actor platform.
     */
    private ActorName m_anPlatform;

    /**
     *  The class data repository for mobile actors.
     */
    private TreeMap m_tmMobileActorClasses;
    
    /**
     *  The reference to this actor platform.
     */
    private Platform m_pPlatform;
    
    /**
     *  The reference to the Actor Manager of this actor platform.
     */
    private ActorManager m_amActorManager;

    /**
     *  The reference to the Message Manager of this actor platform.
     */
    private MessageManager m_mmMessageManager;

    /**
     *  The reference to the Delayed Message Manager of this actor platform.
     */
    private DelayedMessageManager m_dmmDelayedMessageManager;
    					   
    
    // ========== Object Methods ========== //

    /**
     *  Creates a Migration Manager.
     * 
     *  @param p_pPlatform the reference to this platform.
     *  @param p_amActorManager the reference to an Actor Manager.
     */
    public MigrationManager(Platform p_pPlatform, ActorManager p_amActorManager)
    {
    	//
    	//  call the constructor of the super-class.
    	//
    	super();
    	
    	//
    	//  set object variables with parameters.
    	//
    	m_pPlatform = p_pPlatform;

    	m_amActorManager = p_amActorManager;

    	//
    	//  initialize object variables.
    	//
    	m_strHostAddressOfPlatform = m_pPlatform.getHostAddress();
    	m_anPlatform = m_pPlatform.getActorNameOfPlatform();
    	
    	m_tmMobileActorClasses = new TreeMap();
    	
    	//
    	//  inform this object to the Actor Manager of this actor platform.
    	//
    	m_amActorManager.setMigrationManager(this);
    	
    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    

    /**
     *  Processes some necessay routines 
     *  before entering the main loop in the 'run' method.
     *  <br>
     *  This is called by the 'run' method of the super-class (SystemThread).
     */
    protected void init()
    {
    	System.out.println("% Migration Manager started.");
    }
    

    /**
     *  Uninitializes this component.
     *  <br>
     *  This method is called by the Actor Manager.
     */
    protected void uninit()
    {
    	System.out.println("% Migration Manager ended.");    	
    }


    /**
     *  Sets the reference to the Message Manager.
     *  <br>
     *  This method is called by the Message Manager.
     * 
     *  @param p_mm the reference to the Message Manager.
     */
    protected void setMessageManager(MessageManager p_mm)
    {
    	m_mmMessageManager = p_mm;
    }


    /**
     *  Sets the reference to the Delayed Message Manager.
     *  <br>
     *  This method is called by the Delayed Message Manager.
     * 
     *  @param p_dmm the reference to the Delayed Message Manager.
     */
    protected void setDelayedMessageManager(DelayedMessageManager p_dmm)
    {
    	m_dmmDelayedMessageManager = p_dmm;
    }


    /**
     *  Processes a communication messages.
     *  <br>
     *  The messages for the Migration Manager are delivered from 
     *  the Actor Manager of this actor platform.
     * 
     *  @param p_amMsg an actor communication message.
     */
    protected void processMessage(ActorMessage p_amMsg)
    {
	m_pPlatform.sendMessage(p_amMsg);
    }


    /**
     *  Sends an error message.
     *  
     *  @param p_anReceiver the name of the receiver actor.
     *  @param p_strMsg an error message.
     */
    private void sendErrorMessage(ActorName p_anReceiver, String p_strMsg)
    {
	ActorMessage amMsg = new ActorMessage(m_anPlatform, 
    	    	    			      p_anReceiver,
    	    	    			      null,
    	    	    			      null,
    	    	    			      false);
    	    	    			      
	amMsg.setErrorMessage(p_strMsg);
	amMsg.setComments("This message is creaed by a Migration Manager.");
	
	deliverMessage(amMsg);
    }
    
    
    /**
     *  Fetches the byte array of the specified class.
     *  
     *  @param p_strClassName the name of the specified class.
     *  @return the bytes of the class.
     *  @throws ClassNotFoundException throw when the system cannot find the class.
     */
    private byte[] fetchClassFile(String p_strClassName)
        throws ClassNotFoundException
    {
    	//
    	//  get the reference to an input stream for the system resource as stream.
    	//
        String strClassName = p_strClassName.replace('.', '/') + ".class";

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(strClassName);

	//
	//  if the system resource cannot find the informaiotn about the given class,
	//  then bring class information from the class data repository for mobile actors.
	//
        if (inputStream == null) {
            byte[] p_baClass = (byte[]) m_tmMobileActorClasses.remove(p_strClassName);
            
            if (p_baClass == null) {
                throw new ClassNotFoundException("The Class [" + p_strClassName + "] cannot be found");
            } else {
                return (p_baClass);            
            }            
        } 

	//
	//  if the system resource finds the information about the given class,
	//  then change it to a byte stream about the class.
	//        
        try {
            byte[] baData = new byte[inputStream.available()];
            inputStream.read(baData);
            return (baData);
        } catch (IOException ioe) {
            throw new ClassNotFoundException("IO Exception happens");
        } 
    }


    /**
     *  Serializes an object into a byte stream.
     * 
     *  @param p_serObj the input serializable object.
     * 
     *  @return a byte stream corresponding to the specified object.
     */
    private byte[] serialize(Serializable p_serObj)
        throws IOException
    {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ObjectOutputStream oos     = new ObjectOutputStream(baos);
    	
    	oos.writeObject(p_serObj);
    	oos.flush();
    	    
        return baos.toByteArray();
    }
    
    
    /**
     *  Deserialize a byte stream into an object.
     * 
     *  @param p_strClassName the name of the specified class.
     *  @param pp_baClass the input byte stream for the class information.
     *  @param p_baStream the input byte stream for an instance of the class.
     * 
     *  @return an object corresponding to the specified byte stream.
     */
    private Object deserialize(String p_strClassName, byte[] pp_baClass, byte[] p_baStream)
        throws IOException, ClassNotFoundException
    {
    	ByteArrayInputStream bais = new ByteArrayInputStream(p_baStream);
    	MessageObjectInputStream mois = 
    	    new MessageObjectInputStream(bais, p_strClassName, pp_baClass);
    	    
    	Object object = mois.readObject();
    
    	return object;	
    }


    /**
     *  Reactivates a mobile actor.
     *  
     *  @param p_athread the reference to an Actor class.
     */
    private void reactivateActor(ActorThread p_athread)
    {
    	String strName = p_athread.getActorClassName();
    	    	
        Thread threadActor = new Thread(p_athread);
        
        threadActor.setName(strName.substring(strName.lastIndexOf('.')+1));
	threadActor.start();
    }


    /**
     *  Moves the specified actor to another host.
     *  <br>
     *  This method is controlled by an actor thread.
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
    	Object[] objaActorInfo = m_amActorManager.getActor(p_anActor);

	//
	//  check whether or not the actor is still on this platform.
	//
    	if (objaActorInfo == null) {
    	    String strMsg = new String("Cannot find an actor [" + p_anActor + "] to be migrated");
    	    
    	    if (Platform.bERROR) {
    	        System.err.println("% MigrationManager.migrateActor: " + strMsg);
    	    }
    	    
    	    sendErrorMessage(p_anActor, strMsg);
    	    throw new MigrateActorException(strMsg);
    	}
    	    	
	//
	//  check whether the destionation is not the same as this host.
	//
	ActorThread athread = (ActorThread) objaActorInfo[ActorManager.IDX_AGENT];

	if (m_strHostAddressOfPlatform.equals(p_iaDestHost.getHostAddress())) {
	    String strMsg = new String("The destionation host is the same as this host.");
	    	    
	    if (Platform.bDEBUG) {
    	    	System.out.println("% MigrationManager.migrateActor: " + strMsg);
	    }
		    
	    athread.setLastErrorMessage(strMsg);
    	    throw new MigrateActorException(strMsg);
	}
	
	//
	//  remove the actor from the working actor list and 
	//  register the actor to the mobile actor list.
	//    					       
	m_amActorManager.changeToMobileActor(p_anActor, p_iaDestHost);

    	//
    	//  create a communication message for actor migration.
    	//
    	ActorName anSrcPlatform = m_anPlatform;
	ActorName anDestPlatform = 
	    m_pPlatform.getActorNameOfPlatform(p_iaDestHost.getHostAddress());
	    	    
	Object[] objaArg = new Object[3];
	    	    
	try {
	    objaArg[0] = athread.getActorClassName();
	    objaArg[1] = fetchClassFile(athread.getActorClassName());
    	    objaArg[2] = serialize(athread);
	} catch (IOException e) {
	    if (Platform.bERROR) {
	    	System.err.println("% MigrateionManager.migrateActor: " + e);
	    	e.printStackTrace();
	    }
	    
	    m_amActorManager.changeToActor(athread, ActorManager.ACTIVE);
	    	    
	    throw new MigrateActorException(e.getMessage());
	} catch (ClassNotFoundException e) {
	    if (Platform.bERROR) {
	    	System.err.println("% MigrateionManager.migrateActor: " + e);
	    	e.printStackTrace();
	    }
	    	    	
	    m_amActorManager.changeToActor(athread, ActorManager.ACTIVE);
	    
	    throw new MigrateActorException(e.getMessage());
	};
		
    	ActorMessage amMsg = new ActorMessage(anSrcPlatform, 
    					      anDestPlatform,
    					      Platform.MSG_RECEIVE_AGENT,
    					      objaArg,
    					      false);
    					       
	//
	//  if this actor is not created on this platform, ...
	//
	if (!m_strHostAddressOfPlatform.equals(p_anActor.getHostAddress())) {    	
	    //
	    //  inform to the orignal platform of the mobile actor.
	    //
	    //  NOTE: DOES THIS PARK WORK PROPERLY?
	    //
	    ActorName anOriginPlatform = 
	    	m_pPlatform.getActorNameOfPlatform(athread.getActorName().getHostAddress());

	    ActorMessage amInformMsg = 
	    	new ActorMessage(anSrcPlatform,
				 anOriginPlatform,
				 Platform.MSG_TRANSITION_START,
				 new Object[] { p_anActor, p_iaDestHost.getHostAddress() },
				 false);
					      
	    deliverMessage(amInformMsg);
	}
		    
	//
	//  send a message with a mobile actor to the destination host.
	//
    	deliverMessage(amMsg);		    
    }


    /**
     *  Receives an actor.
     *  <br>
     *  This method is controlled by the <code>Platform</code> class.
     * 
     *  @param p_strClassName the class name of a mobile actor.
     *  @param p_baClass an input byte stream of the class.
     *  @param p_baActor an input byte stream of the actor.
     *  @param p_anPlatform the actor name of this platform.
     */    
    protected void receiveActor(String p_strClassName, 
    				byte[] p_baClass, 
    				byte[] p_baActor, 
    				ActorName p_anPlatform)
    {
    	//
    	//  extract a mobile actor
    	//
    	ActorThread athread = null;
    	
    	try {
	    athread = (ActorThread) deserialize(p_strClassName, p_baClass, p_baActor);
    	} catch (Exception e) {
    	    if (Platform.bERROR) {
    	    	System.err.println("% MigrationManager.receiverActor: " + e);
    	    	e.printStackTrace();
    	    }

    	    return;
    	}
	
	//
	//  if this mobile actor is created on a different actor paltform, ...
	//
	String strHostAddressOfActor = athread.getActorName().getHostAddress();
	
	if (! m_strHostAddressOfPlatform.equalsIgnoreCase(strHostAddressOfActor)) {
	    //
	    //  if the class data repository does not have class infomation about this mobile actor,
	    //  then store the class information of the actor into class data repository.
	    //
	    if (!m_tmMobileActorClasses.containsKey(p_strClassName)) {
	    	m_tmMobileActorClasses.put(p_strClassName, p_baClass);	    
	    }
	}
	
	//
	//  inform to the host of the mobile actor arrived.
	//
	ActorName anSender   = m_anPlatform;
	ActorName anReceiver = m_pPlatform.getActorNameOfPlatform(strHostAddressOfActor);
	Object[] objaArgs    = { athread.getActorName(), m_strHostAddressOfPlatform };
	
	ActorMessage amMsg = new ActorMessage(anSender, 
					      anReceiver,
					      Platform.MSG_TRANSITION_END,
					      objaArgs,
					      false);
					      
	deliverMessage(amMsg);
	
	//
	//  inform to the host where the mobile actor started its migration.
	//
	if (!anReceiver.equals(p_anPlatform)) {
	    anReceiver = p_anPlatform;
	    
	    amMsg = new ActorMessage(anSender, 
				     anReceiver,
				     Platform.MSG_TRANSITION_END,
				     objaArgs,
				     false);
					      
	    deliverMessage(amMsg);
	}
	
	//
	//  start the mobile actor.
	//
	athread.initAfterMigration();
    	m_pPlatform.registerActor(athread, ActorManager.ACTIVE);
	
	reactivateActor(athread);
    }    


    /**
     *  Informs that migration of a mobile actor starts.
     *  <br>
     *  This method is controlled by the <code>Platform</code> class.
     * 
     *  @param p_anActor the name of a mobile actor.
     */
    protected void informMigrationStart(ActorName p_anActor, String p_strHostAddress)
    {
    	m_amActorManager.setMobileActorState(p_anActor, ActorManager.IN_TRANSIT, p_strHostAddress);
    }

  
    /**
     *  Informs that migration of a mobile actor ends.
     *  <br>
     *  This method is controlled by the <code>Platform</code> class.
     * 
     *  @param p_anActor the name of a mobile actor.
     */
    protected void informMigrationEnd(ActorName p_anActor, String p_strHostAddress)
    {
	ActorName anPlatform = m_anPlatform;
	
	if (anPlatform.getHostAddress().equals(p_anActor.getHostAddress())) {
    	    m_amActorManager.setMobileActorState(p_anActor, ActorManager.TRANSIT, p_strHostAddress);    	
	} else {
    	    m_amActorManager.deregisterMobileActor(p_anActor);
	}
    	
    	m_dmmDelayedMessageManager.resend();
    }
}
