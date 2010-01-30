
package aa.transport;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import aa.core.MessageManager;
import aa.core.Platform;

/**
 *  This class specifies Transport Manager which supports the external communication 
 *  with actors in other platforms.
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
 *      <li> April 5, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class TransportManager extends Thread
{
    // ========== Object Variables ========== //

    /** 
     *  The flag to indicates whether this thread should be destroyed. <br>
     *  Note: <br>
     *  This variable is not used actually so far.
     */
    private boolean m_bDestroy;
    
    /**
     *  The port number of the server socket of this actor platform.
     */
    private int m_iPort;
    
    /**
     *  The reference to the server socket of this actor platform.
     */
    private ServerSocket m_srvsocket;
    
    /**
     *  The reference to a Message Manager
     */
    private MessageManager m_mmMessageManager;

    /**
     *  An array list for a Message Receivers.
     */
    private ArrayList m_alMessageReceiver;
    
	
    // ========== Object Methods ========== //

    /**
     *  Creates a Transport Manager thread.
     * 
     *  @param p_mmMessageManager the reference to Message Manager
     *  @param p_intPort the port number of a server socket for in-coming messages
     */
    public TransportManager(MessageManager p_mmMessageManager, Integer p_intPort)
    {
    	//
    	//  set object variables by parameters.
    	//
    	m_iPort = p_intPort.intValue();
    	m_mmMessageManager = p_mmMessageManager;
    	
    	//
    	//  initialized object variables.
    	//
    	m_bDestroy = false;
    	m_alMessageReceiver = new ArrayList();
    	
    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    
    
    /**
     *  Processes some necessay routines 
     *      before entering the while loop in the run method.
     */
    protected void init()
    {
    	System.out.println("% Transport Manager started.");
    }
    

    /**
     *  Uninitializes this class. <br>
     *  This method is called by the Plaform.
     */
    public void uninit()
    {
    	System.out.println("% Transport Manager ended.");    	
    }


    /**
     *  Main routine to process in-coming messages.
     */
    public void run()
    {
    	init();

	//
	//  create a server socket.
	//
	try {
    	    m_srvsocket = new ServerSocket(m_iPort);
	} catch (BindException e) {
    	    if (Platform.bERROR) {
    	        System.err.println("> TransportManager.run[1]: " + e);
    	        e.printStackTrace();
    	    }
    	    
    	    //
    	    //  report this error to this Platform.
    	    //
    	    m_mmMessageManager.reportFailure(e.toString());
    	    return;
    	} catch (IOException e) {
	    System.err.println("> TransportManager.run[2]: " + e);
	    e.printStackTrace();
	    return;
	}
    	    
    	System.out.println("    - Port Number for Transport Service: " + m_iPort);
    	    
    	//
    	//  receive socket connections.
    	//
	while (!m_bDestroy) {
    	    try {
    	    	//
    	    	//  if there is a connection request from another platform,
    	    	//  then create a message handler thread, and 
    	    	//       let the message hanlder handle the request.
    	    	//
    	    	Socket socket = m_srvsocket.accept();
    	    	
    	    	TransportReceiver msghandler = new TransportReceiver(m_mmMessageManager, this, socket);
    	    	msghandler.start();
		
		m_alMessageReceiver.add(msghandler);
    	    } catch (IOException e) {
    	    	if (Platform.bDEBUG) {
    	            System.err.println("> TransportManager.run[3]: " + e);
    	            e.printStackTrace();
    	    	}
    	    
    	    	//
    	    	//  TO DO
    	    	//
    	    }
	}
	
	//
	//  close the socket.
	//
    	try {
    	    if (m_srvsocket != null) {
    	    	m_srvsocket.close();
    	    } 
    	} catch (IOException e) {
    	    if (Platform.bERROR) {
    	        System.err.println("> TransportManager.run: " + e);
    	        e.printStackTrace();
    	    }
    	}

    	System.out.println("% TransportManager.run: Transport Manager ended.");
    }
    
    
    /**
     *  remove a Transport Receiver from the list of Transport Receivers.
     * 
     *  @param p_trMsgReceiver a Transport Receiver to be removed from the list.
     */
    protected void deregisterTransportManager(TransportReceiver p_trMsgReceiver)
    {
    	m_alMessageReceiver.remove(p_trMsgReceiver);
    }
    
    
    /**
     *  Destroys this thread.
     *  <br>
     *  NOTE: Now, bDestroy is not used actually.
     */
    public void destroy()
    {
    	//
    	//  set bDestroy flags of this and Transport Recivers.
    	//
    	for (int i=0; i<m_alMessageReceiver.size(); i++) {
    	    TransportReceiver msghandler = (TransportReceiver) m_alMessageReceiver.get(i);
    	    	
    	    msghandler.destroy();
    	}
    	    
    	m_alMessageReceiver = new ArrayList();
    		
    	m_bDestroy = true;

	//
	//  close server socket.
	//
    	try {
    	    if (m_srvsocket != null) {
    	    	m_srvsocket.close();
    	    }
    	} catch (IOException e) {
    	    System.out.println("% TransportManager.destroy: " + e);
    	}
    }
}
