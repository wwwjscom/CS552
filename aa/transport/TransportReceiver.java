
package aa.transport;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import aa.core.ActorMessage;
import aa.core.MessageManager;
import aa.core.Platform;
import aa.util.ObjectHandler;

/**
 *  This class sepcifies Transport Manager to receive a in-coming message.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.5
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 14, 2003 - version 0.0.4
 *      <ul>
 *          <li> modify the 'run' method.
 *      </ul>
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
 *  @version $Date: 2004/02/19$ $Revision: 0.0.5$
 */

public class TransportReceiver extends Thread
{
    // ========== Object Variables ========== //

    /**
     *  The flag to indicates whether this thread should be destroyed. <br>
     *  Note: <br>
     *  This variable is not used actually so far.
     */
    private boolean m_bDestroy;

    /**
     *  The socket to receive a message from another actor. 
     */
    private Socket m_sock;
    
    /**
     *  The reference to the Message Manager of this actor platform.
     */
    private MessageManager m_mmMessageManager;
    
    /**
     *  The reference to the Transport Manager of this actor platform.
     */
    private TransportManager m_tmTransportManager;

	
    // ========== Object Methods ========== //

    /**
     *  Creates a Transport Receiver.
     * 
     *  @param p_mmMessageManager the reference to a Message Manager.
     *  @param p_sock a socket to receive a message from another platform.
     */
    public TransportReceiver(MessageManager p_mmMessageManager, 
    			     TransportManager p_tmTransportManager, 
    			     Socket p_sock)
    {
	//
	//  initialize object variables with given parameters.
	//    	
    	m_mmMessageManager   = p_mmMessageManager;
    	m_tmTransportManager = p_tmTransportManager;  	
    	m_sock		     = p_sock;
    	
    	//
    	//  initialized object variables.
    	//
    	m_bDestroy = false;

    	//
    	//  set the name of this thread with the class name.
    	//
    	String strClassName = getClass().getName();
    	setName(strClassName.substring(strClassName.lastIndexOf('.') + 1));
    }
    
    
    /**
     *  Main routine to process a message coming from another platform.
     */
    public void run()
    {
    	InputStream is = null;
//    	ObjectInputStream ois = null;
    	
    	//
    	//  bind an input stream with socket.
    	//
	try {
	    is = m_sock.getInputStream();
	    
//	    ois = new ObjectInputStream(is);
	} catch (IOException e) {
	    System.err.println("% TransportReceiver[1].run: " + e);  	    	
	    e.printStackTrace();
	    return;
	}

    	while (!m_bDestroy) {
	    try {
	    	//
	    	//  receive the size of data.
	    	//
/* */
	    	int iSize;
    	    	iSize = is.read();
    	    	iSize <<= 8;
    	    	iSize += is.read();
    	    
    	    	//
    	    	//  receive data.
    	    	//

    	    	byte[] baData = new byte[iSize];

	    	for (int i=0; i<iSize; i++) {
		    baData[i] = (byte) is.read();     	    	
    	    	}

	    	//
	    	//  make an object from the data.
	    	//    	    
    	    	ActorMessage amMsg = (ActorMessage)ObjectHandler.deserialize(baData);    	    

/* *
	    	//
	    	//  receive a message.
	    	//
	    	ActorMessage amMsg = (ActorMessage)ois.readObject();
*/   	    
    	    	if (Platform.bDEBUG) {
    	    	    System.out.println("% TransportReceiver.run[2]:");
    	    	    System.out.println();
    	    	    System.out.println("* " + amMsg.toString());
    	    	}
    	    
    	   	m_mmMessageManager.deliverMessage(amMsg);
	    } catch (EOFException e) {
	    	//
	    	//  TO DO
	    	// 
	    	try {
		    sleep(100);
		} catch (InterruptedException e1) {
//		    e1.printStackTrace();
		}
	    } catch (SocketException e) {
//	    	System.err.println("% TransportReceiver.run[3]: " + e);
	    	System.err.println("% TransportReceiver.run: Disconnected with Another AA Platform.");
	    	
	    	//
	    	//  TO DO SOMETHING
	    	//
	    	m_tmTransportManager.deregisterTransportManager(this);
	    	
	    	break;
    	    } catch (IOException e) {
    	    	if (Platform.bERROR) {
		    System.err.println("% TransportReceiver.run[4]: " + e);  	    	
    	            e.printStackTrace();
    	        
    	            //
    	            //  TO DO
    	            //
    	    	}
    	    } catch (ClassNotFoundException e) {
    	    	if (Platform.bERROR) {
		    System.err.println("% TransportReceiver.run[5]: " + e);  	    	
    	            e.printStackTrace();
    	        
    	            //
    	            //  TO DO
    	            //
    	    	}
    	    }
    	}
    	
    	//
    	//  close the object input stream and the socket.
    	//
    	try {
/*
    	    if (ois != null) {
	    	ois.close();
    	    }
*/
    	    if (is != null) {
	    	is.close();
    	    }

    	    if (m_sock != null) {
    	        m_sock.close();
    	    }
    	} catch (IOException e) {
    	    if (Platform.bERROR) {
		System.err.println("% TransportReceiver.run[5]: " + e);  	    	
    	        e.printStackTrace();
    	        
    	        //
    	        //  TO DO
    	        //
    	    }
    	}
    }
    
    /**
     *  Destroys this thread.
     *  <br>
     *  NOTE: Now, bDestroy is not used actually.
     */
    public void destroy()
    {
    	m_bDestroy = true;

    	try {
    	    if (m_sock != null) {
    	    	m_sock.close();
    	    }
    	} catch (IOException e) {
    	    System.out.println("% TransportReceiver.destroy: " + e);
    	}
    }    
}
