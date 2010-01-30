 
package aa.transport;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import aa.core.ActorMessage;
import aa.core.ActorName;
import aa.core.MessageManager;
import aa.core.Platform;
import aa.tool.SystemThread;
import aa.util.ObjectHandler;

/**
 *  This class specifies Transport Sender to handle out-going messages.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *  	<li> September 12, 2003 - version 0.0.3
 *  	<ul>
 *  	    <li> modify code for continuous communication
 *  	</ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 23, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class TransportSender extends SystemThread
{
    // ========== Object Variables ========== //

    /**
     *  The actor name of this actor platform.
     */   					
    private ActorName m_anPlatform;

    /**
     *  The port number of the server socket of this actor platform.
     */
    private int m_iPort;

    /**
     *  The reference to a Message Manager.
     */
    private MessageManager m_mmMessageManager;

    /**
     *  An array list for socket opened to send messages to actors in other platforms.
     */    					
    private ArrayList m_alSocket;

    /**
     *  A tree map for object output streams. <br>
     *  Each element in the map consists of the host address of a platform and 
     *  its output stream. <br>
     *  <the host address, the output stream> 
     */
    private TreeMap m_tmOOS;
    				

    // ========== Object Methods ========== //

    /**
     *  Creates a Transport Sender thread.
     * 
     *  @param p_mmMessageManager the reference to a Message Manager.
     *  @param p_intPort the port number of a server socket for out-going messages.
     */
    public TransportSender(ActorName p_anPlatform,
    			   MessageManager p_mmMessageManager, 
    			   Integer p_intPort)
    {
    	super();

    	//
    	//  set object variables by parameters.
    	//
	m_anPlatform = p_anPlatform;
    	m_mmMessageManager = p_mmMessageManager;
    	m_iPort = p_intPort.intValue();
    	
    	//
    	//  initialized object variables.
    	//
    	m_alSocket = new ArrayList();
	m_tmOOS = new TreeMap();

    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    
    
    /**
     *  Processes some necessay routines 
     *      before entering the while loop in the run method .
     *  <br>
     *  This is called by the run method of the super class.
     */
    protected void init()
    {
    	System.out.println("% Transport Sender started.");
    }
    
    
    /**
     *  Uninitializes this program.
     *  <br>
     *  This method is called by a Message Manager.
     */
    public void uninit()
    {
    	System.out.println("% Transport Sender ended.");
    }
    
    
    /**
     *  Processes a message.
     *  <br>
     *  This is called by the run method of the super class.
     * 
     *  @param p_amMsg a communication message.
     */
    protected void processMessage(ActorMessage p_amMsg)
    {
    	boolean bNewConnection = true;
    	
    	//
    	//  debuggin messages
    	//
    	if (Platform.bDEBUG) {	
    	    System.out.println("> TransportSender.processMessage:");
    	    System.out.println();
    	    System.out.println("* " + p_amMsg.toString());
    	}

	//
	//  check a proper server socket.
	//
	OutputStream os = null;
//	ObjectOutputStream oos = null;
	String strHostAddr = p_amMsg.getDestHostAddress();
	
	if (m_tmOOS.containsKey(strHostAddr)) {
	    os = (OutputStream) m_tmOOS.get(strHostAddr);
//	    oos = (ObjectOutputStream) m_tmOOS.get(strHostAddr);

	    bNewConnection = false;
	} else {
	    Socket socket = null; 
	   	
    	    try {
	    	//
	    	//  make a connection with the transport manager 
	    	//	    of the destination platform.
	    	//    
    	    	socket = new Socket(p_amMsg.getDestHostAddress(), m_iPort);

		//
		//  create an object output stream for this socket. 
		//
    	    	os = socket.getOutputStream();
    	    
//		oos = new ObjectOutputStream(os);
	    
	   	m_alSocket.add(socket);
//		m_tmOOS.put(strHostAddr, oos);
		m_tmOOS.put(strHostAddr, os);
    	    } catch (IOException e) {
    	    	if (Platform.bERROR) {
    	            System.err.println("> TransportSender.processMessage[1]: " + e);
    	            e.printStackTrace();
    	    	}
    	    
    	    	//
    	   	//  send an error message to the sender of this message.
    	    	//
    	    	ActorMessage amMsg = 
    	    	    p_amMsg.makeErrorMessage(m_anPlatform, 
					     "Cannot connect to the AA platform on " + 
					     p_amMsg.getDestHostAddress() + 
					     " with the port number " +
					     m_iPort + ".");
    	    	
    	    	amMsg.setComments("This message is created by the Transport Sender.");
	    	m_mmMessageManager.deliverMessage(amMsg);
	    	return;
    	    }  
    	}

	try {
	    //
	    //  send the size of data.
	    //
/* */
   	    byte[] baData = ObjectHandler.serialize(p_amMsg);
    	    int iSize = (baData.length & 0xFF00) >> 8;
    	    os.write(iSize);
    	    iSize = baData.length & 0x00FF;
    	    os.write(iSize);
    	    
    	    //
    	    //  send data.
    	    //
    	    os.write(baData);
    	    
    	    os.flush();
//    	    os.close();
/* */

/*
	    //
	    //  send a message.
	    //
	    oos.writeObject(p_amMsg);
	    oos.flush();
*/
    	}  catch (IOException e) {
    	    //
    	    //  if the connection is broken, then try it again.
    	    //
    	    if (bNewConnection == false) {
    	    	m_tmOOS.remove(strHostAddr);
    	    	processMessage(p_amMsg);
    	    } else {
    	        if (Platform.bERROR) {
    	            System.err.println("> TransportSender.processMessage[3]: " + e);
    	            e.printStackTrace();
    	    	}
    	    
    	    	//
    	    	//  send an error message to the sender of this message.
    	    	//
    	    	ActorMessage amMsg = p_amMsg.makeErrorMessage(null, e.getMessage());
    	    	amMsg.setComments("This message is created by Transport Sender.");
	    	m_mmMessageManager.deliverMessage(amMsg);
    	    }
	}
    }
  
  
   /**
    *  Closes object output streams and sockets.
    */  
   private void closeAll()
   {
    	//
    	//  close object output streams.
    	//
    	Iterator iterSocket = m_tmOOS.keySet().iterator();
    	while (iterSocket.hasNext()) {
    	    String strHost = (String) iterSocket.next();
    	    
    	    m_tmOOS.remove(strHost);
    	}
    	
    	//
    	//  close sockets.
    	//
    	Socket sock;
    	
    	for (int i=0; i<m_alSocket.size(); i++) {
    	    sock = (Socket) m_alSocket.get(i);
    	    try {
    	    	if (sock != null) {
		    sock.close();
    	    	}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
    	}
   }
    
    
   /**
     *  Changes the port number for transprot manager.
     *  
     *  @param p_intPort a new port number for transport manager.
     */
    public void changePortNumber(Integer p_intPort)
    {
    	closeAll();
    	
    	m_alSocket = new ArrayList();
    	m_tmOOS = new TreeMap();
    	
    	m_iPort = p_intPort.intValue();
    }
}
