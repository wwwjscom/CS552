
package aa.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.TreeMap;

import aa.gui.View;

/**
 *  This class represents Actor Manager to handle the state information 
 *     about actors that are working or created on this platform.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 24, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> change the 'setMobileActorState' method.
 * 	</ul>
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
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/24$ $Revision: 0.0.3$
 */

public class ActorManager 
{
    // ========== Public Constants ========== //
    
    /**
     *  An actor state to indicate that
     *  the state of an actor is unknown.
     */
    public static final Integer UNKNOWN    = new Integer(0);
    
    /**
     *  An actor state to indicate that
     *  an actor is working, and hence it can receive and process a message.
     */
    public static final Integer ACTIVE     = new Integer(1);
    
    /**
     *  An actor state to indicate that
     *  an actor is suspended by the platform. 
     *  Therefore, even though an actor receives a message,
     *  the actor cannot process the message.
     */
    public static final Integer SUSPENDED  = new Integer(2);

    /**
     *  An actor state to indicate that
     *  an actor is moving from one platform to another.
     *  During actor migration, the actor cannot receive a message.
     */
    public static final Integer IN_TRANSIT = new Integer(3);
    
    /**
     *  An actor state to indicate that
     *  an actor moved to another platform.
     *  The current platform is different from the platform
     *  where the actor was created.
     */
    public static final Integer TRANSIT    = new Integer(4);
    
    /**
     *  Index for the actor state field in the actor infromation array.
     */
    public static final int IDX_STATE = 0;

    /**
     *  Index for the actor field in the actor infromation array.
     */
    public static final int IDX_AGENT = 1;

    /**
     *  Index for the actor name field in the actor infromation array.
     */
    public static final int IDX_AN    = 1;

    /**
     *  Index for the actor location field in the actor infromation array.
     */
    public static final int IDX_LOC   = 2;
    
    
    // ========== Class Constants ========== //

    /**
     *  String names about actor states.
     */
    private static final String m_strState[] = { 
	"Unknown",
    	"Active",
    	"Suspended",
    	"In Transit",
    	"Transit"
    };
    
    
    // ========== Object Variables ========== //

    private TreeMap m_tmActors;		//  information about actors
    					//  { state, actor, key, destination address }
    private TreeMap m_tmMobileActors;	//  information about mobile actors
    
    private Platform m_pPlatform;	//  reference to a platform
    private View m_viewMain;		//  GUI of this platform
    private MigrationManager m_mmMigrationManager;
    					//  reference to a Migration Manager
    private DelayedMessageManager m_dmmDelayedMessageManager;
    					//  reference to a Delayed Message Manager

    private String m_strHostAddress;	//  internet address of this platform
    
    
    // ========== Object Methods ========== //

    /**
     *  Creates an Actor Manager.
     * 
     *  @param p_platform the reference to this platform.
     *  @param p_viewMain the reference to View.
     */
    public ActorManager(Platform p_platform, View p_viewMain)
    {
    	//
    	//  set class varibles by parameters
	//
	m_pPlatform = p_platform;
    	m_viewMain  = p_viewMain;
    	
	//
	//  initialize class varialbes
	// 
    	m_tmActors = new TreeMap();
    	m_tmMobileActors = new TreeMap();
    	
    	m_strHostAddress = m_pPlatform.getHostAddress();
    	
    	//
    	//  inform my reference to the view class
    	//
    	if (m_viewMain != null) {
    	    m_viewMain.setActorManager(this);
    	}
    	
    	//
    	//  display the current state
    	//
    	System.out.println("% Actor Manager started.");
    }
    
    
    /**
     *  Sets the reference of a Migration Manager.
     * 
     *  @param p_mmMigrationManager the reference to a Migration Manager.
     */
    protected void setMigrationManager(MigrationManager p_mmMigrationManager)
    {
    	m_mmMigrationManager = p_mmMigrationManager;
    }
    
    
    /**
     *  Uninitializes this program.
     *  <br>
     *  This method is called by the Platform.
     */
    protected void uninit()
    {
    	System.out.println("% Actor Manager ended.");
    }
    
    
    /**
     *  Registers a new actor into this actor platform.
     * 
     *  @param p_athread the reference to the specified actor.
     *  @param p_intState an initial state of the actor.
     */
    protected void registerActor(ActorThread p_athread, Integer p_intState)
    {
    	ActorName anActor = p_athread.getActorName();
    	
    	synchronized (m_tmActors) {
    	    if (!m_tmActors.containsKey(anActor)) {
    		
    	    	//
    	    	//  if this actor is a mobile and and comes back,
    	    	//  then remove information about this actor from the mobile actor list.
    	    	//
    	    	if (m_tmMobileActors.containsKey(anActor)) {
    	    	    m_tmMobileActors.remove(anActor);
    	    	}
    	
    	    	//
    	    	//  register information about this actor to the working actor list.
    	    	//
    	    	Object[] objaActorInfo = new Object[3];
    	    	objaActorInfo[IDX_STATE] = p_intState;
    	    	objaActorInfo[IDX_AGENT] = p_athread;
    	    
    	    	m_tmActors.put(anActor, objaActorInfo);
    	    
    	    	//
    	    	//  change GUI.
    	    	//
    	    	if (m_viewMain != null) {
    	            m_viewMain.addNewActor(anActor);
    	    	}
    	    } else {
    	    	if (Platform.bERROR) {
    	    	    System.err.println("% ActorManager.register: The actor [" + anActor + "] already exists.");
    	    	}
     	    }
    	}
    }
    
    
    /**
     *  Updates the old reference to an actor with new one.
     * 
     *  @param p_athread the reference to an actor thread.
     *  @param p_intState a new state of the actor.
     */
    protected void updateActor(ActorThread p_athread, Integer p_intState)
    {
    	ActorName anActor = p_athread.getActorName();
    	Object[] objaActorInfo = getActor(anActor);

	//
	//  check an error condition.
	//  This case happens when an actor created by the become operator
	//	calles the become operator.
	//  CHECK: check whether or not this part works correctly.
	//
	if ( (p_intState == ActorManager.ACTIVE) && 
	     (objaActorInfo[IDX_STATE] == ActorManager.ACTIVE) ) {
	    if (Platform.bDEBUG) {
	    	System.err.println(" ActorManager.updateActor: The actor [" + 
	    			   anActor + "] tries to change its thread.");
	    }
	     	
	    return;
	}
    	
	//
	//  update the actor thread fot this actor.
	//
    	if (objaActorInfo != null) {
    	    objaActorInfo[IDX_STATE] = p_intState;
	    objaActorInfo[IDX_AGENT] = p_athread;
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("% ActorManager.updateActor: The actor [" + 
    	    			   anActor + "] does not exist.");
    	    }
     	}
    }
    
    
    /**
     *  Deregisters the specified actor from this platform.
     *  <br>
     *  This method is (should be) called by a Migration Manager or 
     *  the 'removeActor' method.
     * 
     *  @param p_anActor the name of the selected actor.
     */
    protected Object[] deregisterActor(ActorName p_anActor)
    {
    	Object[] objaActorInfo = getActor(p_anActor);

    	if (objaActorInfo != null) {   
    	    synchronized (m_tmActors) {
    	    	m_tmActors.remove(p_anActor);
    	    }
    	    
    	    //
    	    //  change GUI.
    	    //
    	    if (m_viewMain != null) {
    	    	m_viewMain.removeActor(p_anActor);
    	    }
    	    
   	    return objaActorInfo;
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("% ActorManager.deregister: " +
    	    			   "Cannot find the actor [" + p_anActor + "].");
    	    }
    	}
    	
    	return null;
    }
    

    /**
     *  Removes information about the specified actor from the actor list.
     *  <br>
     *  This method is (should be) called by this Platform.
     * 
     *  @param p_anActor the name of an actor.
     */
    protected void removeActor(ActorName p_anActor)
    {
	Object[] objaActorInfo = deregisterActor(p_anActor);
	
    	if (objaActorInfo != null) {
	    //
	    //  if this actor was not created on this platform, ...
	    //
	    if (!m_strHostAddress.equals(p_anActor.getHostAddress())) {
		ActorMessage amMsg = 
		    new ActorMessage(m_pPlatform.getActorNameOfPlatform(), 
		    		     m_pPlatform.getActorNameOfPlatform(p_anActor.getHostAddress()),
		    		     Platform.MSG_DEREGISTER_AGENT,
		    		     new Object[] { p_anActor },
		    		     false );
		m_mmMigrationManager.deliverMessage(amMsg);
	    }
    	}
    }
    

    /**
     *  Returns the reference to the actor with the given actor name.
     * 
     *  @param p_anActor the name of an actor.
     * 
     *  @return the pointer to the actor with the given actor name.
     */
    protected ActorThread getActorThread(ActorName p_anActor)
    {
    	ActorThread athread = null;
    	
    	synchronized (m_tmActors) {
    	    if (m_tmActors.containsKey(p_anActor)) {
    	        try {
    	            Object[] objaActorInfo = (Object[]) m_tmActors.get(p_anActor);
    	            athread = (ActorThread) objaActorInfo[IDX_AGENT];    	    
    	    	} catch (ClassCastException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	            }
    	    	} catch (NullPointerException e) {
    	    	    if (Platform.bERROR) {
			System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	    	}    	    
    	    }
    	}
    	    	
    	return athread;
    }
    
    
    /**
     *  Returns information about the actor with the given actor name.
     * 
     *  @param p_anActor the name of an actor.
     * 
     *  @return information about the actor with the given actor name.
     */
    protected Object[] getActor(ActorName p_anActor)
    {
    	Object[] objaActorInfo = null;
    	
    	try {
    	    synchronized (m_tmActors) {
    	        objaActorInfo = (Object[]) m_tmActors.get(p_anActor);
    	    }
    	} catch (ClassCastException e) {
    	    if (Platform.bERROR) {
    	        System.err.println("% ActorManager.getActorData: " + e);
    	        e.printStackTrace();
    	    }
    	} catch (NullPointerException e) {
    	    if (Platform.bERROR) {
    	        System.err.println("% ActorManager.getActorData: " + e);
    	        e.printStackTrace();
    	    }
    	}
    	
    	return objaActorInfo;
    }
    
    
    /**
     *  Returns the state of the specified actor.
     *  <br>
     *  This method is called the 'View' class.
     * 
     *  @param p_anActor the name of an actor.
     * 
     *  @return the state of the specified actor.
     */
    public Integer getActorState(ActorName p_anActor)
    {
    	Integer intActorState = ActorManager.UNKNOWN;

	synchronized (m_tmActors) {
    	    if (m_tmActors.containsKey(p_anActor)) {
    	    	try {
    	            Object[] objaActorInfo = (Object[]) m_tmActors.get(p_anActor);
    	            intActorState = (Integer) objaActorInfo[IDX_STATE];    	    
    	    	} catch (ClassCastException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	    	} catch (NullPointerException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	        }
    	    }    	    
    	}
    	    	
    	return intActorState;
    }
    
    
    /**
     *  Returns information about the actor specifiey its actor name.
     *  <br>
     *  This method is called by the 'View' class.
     *
     *  @param p_anActor the name of an actor.
     * 
     *  @return infomation about the actor specified its actor name.
     */
    public String[][] getActorData(ActorName p_anActor)
    {
    	String[][] strInfo;
    	
    	synchronized (m_tmActors) {
    	    try {
    	    	Object[] objaActorInfo = (Object[]) m_tmActors.get(p_anActor);
    	    	ActorThread athread = (ActorThread) objaActorInfo[IDX_AGENT];    	    
    	    
    	    	if (athread != null) {
    	    	    strInfo = new String[3][2];
    	    	    strInfo[0][0] = "Actor Name";
    	    	    strInfo[0][1] = p_anActor.getUAN();
    	    	    strInfo[1][0] = "Class";
    	    	    strInfo[1][1] = athread.getActorClassName();
    		    strInfo[2][0] = "Status";
    		    strInfo[2][1] = m_strState[((Integer)objaActorInfo[IDX_STATE]).intValue()];
    	    
    	    	    return strInfo;
    	    	}    	    
    	    } catch (ClassCastException e) {
    	    	if (Platform.bERROR) {
    	    	    System.err.println("% ActorManager.getActor: " + e);
    	    	    e.printStackTrace();
    	    	}
    	    } catch (NullPointerException e) {
    	    	if (Platform.bERROR) {
    	    	    System.err.println("% ActorManager.getActor: " + e);
    	    	    e.printStackTrace();
    	    	}
    	    }
    	}
    	
    	strInfo = new String[2][2];
    	strInfo[0][0] = "Actor Name";
    	strInfo[0][1] = p_anActor.getUAN();
    	strInfo[1][0] = "Status";
    	strInfo[1][1] = "unknown";
    	
    	return strInfo;
    }


    /**
     *  Returns information about the actors registered.
     *  <br>
     *  This method is called by the 'View' class.
     *
     *  @return infomation about the actors registered.
     */
    public String[][] getAllActorData()
    {
    	String[][] strInfo;
    	
    	int i = 0;
    	
    	synchronized (m_tmActors) {
    	    strInfo = new String[m_tmActors.size() + m_tmMobileActors.size()][2];
    	
    	    Iterator iterActors = m_tmActors.values().iterator(); 	
    	    while (iterActors.hasNext()) {
    	    	Object[] objaActorInfo = (Object[]) iterActors.next();
    	    	ActorThread athread = (ActorThread) objaActorInfo[IDX_AGENT];
    	    
    	    	strInfo[i][0] = athread.getActorName().getUAN();
    	    	strInfo[i][1] = m_strState[((Integer)objaActorInfo[IDX_STATE]).intValue()];
    	    	i++;
    	    }
    	}
    	
    	synchronized (m_tmMobileActors) {
    	    Iterator iterActors = m_tmMobileActors.values().iterator();
    	    while (iterActors.hasNext()) {
    	    	Object[] objaActorInfo = (Object[]) iterActors.next();
    	    	ActorName anActor = (ActorName) objaActorInfo[IDX_AN];
    	    	InetAddress iaHost = (InetAddress) objaActorInfo[IDX_LOC];
    	    
    	    	strInfo[i][0] = anActor.getUAN();
    	    	strInfo[i][1] = m_strState[((Integer)objaActorInfo[IDX_STATE]).intValue()] +
    	    		     	": " + iaHost.getHostName() + " / " +
    	    		     	iaHost.getHostAddress();
    	    	i++;
    	    }
    	}
    	
    	return strInfo;
    }


    /**
     *  Suspends the specified actor.
     *  <br>
     *  This method must be called by the 'View' class.
     * 
     *  @param p_anActor the name of the selected actor.
     */
    public void suspendActor(ActorName p_anActor)
    {
    	Object[] objaActorInfo = getActor(p_anActor);
    	
    	if (objaActorInfo != null) {
    	    ActorThread athread = (ActorThread) objaActorInfo[IDX_AGENT];
    	    athread.suspend();

    	    objaActorInfo[IDX_STATE] = ActorManager.SUSPENDED;
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("% ActorManager.suspendActor: Cannot find the actor [" + 
    	    			   p_anActor + "] to be suspended.");
    	    }
    	}
    }


    /**
     *  Resumes the specified actor.
     *  <br>
     *  This method must be called by the 'View' class.
     * 
     *  @param p_anActor the name of the selected actor.
     */
    public void resumeActor(ActorName p_anActor)
    {
    	Object[] objaActorInfo = getActor(p_anActor);
    	
    	if (objaActorInfo != null) {
    	    ActorThread athread = (ActorThread) objaActorInfo[IDX_AGENT];
    	    athread.resume();
    	    
    	    objaActorInfo[IDX_STATE] = ActorManager.ACTIVE;
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("% ActorManager.resumeActor: " +
    	    	    "Cannot find the actor [" + p_anActor + "] to be resumed.");
    	    }
    	}
    }


    /**
     *  Tries to kill an actor.
     *  <br>
     *  This method must be called by the 'View' class.
     * 
     *  @param p_anActor the name of the selected actor.
     */
    public void killActor(ActorName p_anActor)
    {
    	Object[] objaActorInfo = getActor(p_anActor);

    	if (objaActorInfo != null) {
	    ActorThread athread = (ActorThread) objaActorInfo[IDX_AGENT];
	    athread.kill();
    	} else {
    	    if (Platform.bERROR) {
    	    	System.err.println("% ActorManager.killActor: " +
    	    	    "Cannot find the actor [" + p_anActor + "] to be killed.");
    	    }
    	}
    }
    
    
    /**
     *  Returns information about the actor with the given actor name.
     * 
     *  @param p_anActor the name of an actor.
     * 
     *  @return information about the actor with the given actor name.
     */
    protected synchronized Object[] getActorInfo(ActorName p_anActor)
    {
    	Object[] objaActorInfo = new Object[3];
    	
	objaActorInfo[0] = getActorThread(p_anActor);
	
	if (objaActorInfo[0] == null) {
	    objaActorInfo[1] = getMobileActorState(p_anActor);
	    
	    if (! ((Integer)objaActorInfo[1]).equals(ActorManager.IN_TRANSIT)) {
	    	objaActorInfo[2] = getMobileActorLocation(p_anActor);
	    }
	}

    	return objaActorInfo;
    }
    
    
    /**
     *  Deregisters the specified actor form the working actor list, and
     *  registers an actor into the mobile actors list.
     *  <br>
     *  This method is called by a Migration Manager.
     * 
     *  @param p_anActor the name of a mobile actor.
     *  @param p_iaDestHost the Inernet address of the destination host.
     */
    protected synchronized void changeToMobileActor(ActorName p_anActor, InetAddress p_iaDestHost)
    {
	deregisterActor(p_anActor);
    	registerMobileActor(p_anActor, p_iaDestHost);
    }
    
    
    /**
     *  Deregisters the specified actor form the mobile actor list, and
     *  registers an actor into the working actors list.
     *  <br>
     *  This method is called by a Migration Manager.
     * 
     *  @param p_athread the reference to the specified actor.
     *  @param p_intState an initial state of the actor.
     */
    protected synchronized void changeToActor(ActorThread p_athread, Integer p_intState)
    {
    	registerActor(p_athread, p_intState);
	deregisterMobileActor(p_athread.getActorName());
    }
    
    
    /**
     *  Registers an actor into the mobile actors list.
     *  <br>
     *  This method is called by a Migration Manager.
     * 
     *  @param p_anActor the name of a mobile actor.
     *  @param p_iaDestHost the Inernet address of the destination host.
     */
    protected void registerMobileActor(ActorName p_anActor, InetAddress p_iaDestHost)
    {
	Object[] objaActorInfo = new Object[4];
	objaActorInfo[IDX_STATE] = ActorManager.IN_TRANSIT;
	objaActorInfo[IDX_AN]    = p_anActor;
	objaActorInfo[IDX_LOC]   = p_iaDestHost;
    	
    	synchronized (m_tmMobileActors) { 
	    m_tmMobileActors.put(p_anActor, objaActorInfo);
    	}   
    }
    
    
    /**
     *  Deregisters a mobile actor.
     *  <br>
     *  This method is (should be) called by Platform.
     * 
     *  @param p_anActor the name of a mobile actor.
     */
    protected Object[] deregisterMobileActor(ActorName p_anActor)
    {
	if (m_tmMobileActors.containsKey(p_anActor)) {
	    Object[] objaActorInfo;
	    
            synchronized (m_tmMobileActors) {
    	    	objaActorInfo = (Object[]) m_tmMobileActors.remove(p_anActor);
            }
            
    	    //
    	    //  change GUI.
    	    //
    	    if (m_viewMain != null) {
    	    	m_viewMain.refreshWindow();
    	    }
    	    
   	    return objaActorInfo;
    	}
    	
    	return null;
    }
    

    /**
     *  Changes the state of a mobile actor with the new state.
     * 
     *  @param p_anActor the name of a mobile actor.
     *  @param pintNewState a new state of the actor.
     *  @param p_strHostAddress the host address of the destination platform.
     */
    protected void setMobileActorState(ActorName p_anActor, Integer pintNewState, String p_strHostAddress)
    {
    	synchronized (m_tmMobileActors) {
    	    if (m_tmMobileActors.containsKey(p_anActor)) {
    	    	//
    	    	//  get the reference to this mobile actor.
    	    	// 
    	    	Object[] objaActorInfo = (Object[]) m_tmMobileActors.get(p_anActor);
    	    	
    	    	//
    	    	//  get the address of the destination host of this mobile actor.
    	    	//
    	    	InetAddress iaHostAddress;
    	    	
    	    	try {
    	    	    iaHostAddress = InetAddress.getByName(p_strHostAddress);
    	    	} catch (UnknownHostException e) {
    	    	    iaHostAddress = null;
    	    	}
    	    	
    	    	//
    	    	//  check an error condition.
    	    	//  This unexpected situation happens  
    	    	//      when the 'transition end' message arrives 
    	    	//      before the 'transition start' message.
    	    	//
    	    	if ( (pintNewState == ActorManager.IN_TRANSIT) && 
    	    	     (iaHostAddress.equals(objaActorInfo[ActorManager.IDX_LOC]))) {
    	    	    //
    	    	    //  ignore this message.
    	    	    //
    	    	    return;
    	    	}
    	    	
    	   	//
    	    	//  update information about this actor with new information.
    	    	//
    	    	objaActorInfo[ActorManager.IDX_STATE] = pintNewState;    	    	
    	    	objaActorInfo[ActorManager.IDX_LOC] = iaHostAddress;
    	    } else {
    	    	synchronized (m_tmActors) {
    	    	    //
    	    	    //  if the actor is not any more a mobile actor,
    	    	    //  ignore this request.
    	    	    //
    	    	    if (m_tmActors.containsKey(p_anActor) == false) {
    	    	    	if (Platform.bERROR) {
    	    	    	    System.err.println("% ActorManager.setMobileActorState: " +
    	    	    		"Cannot find the actor [" + p_anActor + "]");
    	    	    	}
    	    	    }
    	    	}
    	    }
    	}
    }
    
    
    /**
     *  Returns the state of the specified mobile actor.
     * 
     *  @param p_anActor the name of a mobile actor.
     * 
     *  @return the state of the specified mobile actor.
     */
    protected Integer getMobileActorState(ActorName p_anActor)
    {
    	Integer intActorState = ActorManager.UNKNOWN;

	synchronized (m_tmMobileActors) {
    	    if (m_tmMobileActors.containsKey(p_anActor)) {
    	    	try {
    	            Object[] objaActorInfo = (Object[]) m_tmMobileActors.get(p_anActor);
    	            intActorState = (Integer) objaActorInfo[IDX_STATE];    	    
    	    	} catch (ClassCastException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getMobileActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	    	} catch (NullPointerException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getMobileActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	        }
    	    }    	    
    	}
    	    	
    	return intActorState;
    }
    
    
    /**
     *  Returns the reference to the mobile actor with the given actor name.
     * 
     *  @param p_anActor the name of a mobile actor.
     * 
     *  @return the reference to the mobile actor with the given actor name.
     */
    protected String getMobileActorLocation(ActorName p_anActor)
    {
    	if (p_anActor == null) {
    	    return null;
    	}
    	
    	String strLocation = null;
    	
    	synchronized (m_tmMobileActors) {
    	    if (m_tmMobileActors.containsKey(p_anActor)) {
    	    	try {
    	            Object[] objaActorInfo = (Object[]) m_tmMobileActors.get(p_anActor);
    	            InetAddress iaHost = (InetAddress) objaActorInfo[IDX_LOC];
    	            strLocation = iaHost.getHostAddress();
    	    	} catch (ClassCastException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	    	} catch (NullPointerException e) {
    	    	    if (Platform.bERROR) {
    	            	System.err.println("% ActorManager.getActor: " + e);
    	            	e.printStackTrace();
    	    	    }
    	    	}
    	    }
    	}    	    

    	return strLocation;
    } 
}
