
package aa.core;

import java.io.Serializable;

import aa.util.ObjectHandler;

/**
 *  This class specifies a communication message between actors.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> March 26, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class ActorMessage implements Serializable
{
    // ========== Object Variables ========== //
    
    private ActorName m_anSender;	//  sender actor
    private ActorName m_anReceiver;	//  receiver actor
    private String m_strMethod;		//  the method name
    private Object[] m_objaArgs;	//  the list of arguments
    private boolean m_bReturnRequest;	//  if the return message is required
    private String m_strReplyWith;	//  identifier of this message
    private String m_strReplyTo;	//  identifier of the original message
    private String m_strComments;	//  comments of this message
    private String m_strErrorMessage;	//  error message

    private String m_strDestHostAddress;	//  Internet address of the destination host 

    private boolean m_bReturnMessage = false;	//  if this is a return message
    private boolean m_bErrorMessage  = false;	//  if this is an error message
	

    // ========== Object Methods ========== //

    /**
     *  Creates an actor communication message.
     * 
     *  @param p_anSender the name of the sender actor.
     *  @param p_anReceiver the name of the receiver actor.
     *  @param p_strMethod the name of the method to be called.
     *  @param p_objaArgs an array of argument objects.
     *  @param p_bReturnRequest the flag to request the return value.
     */
    public ActorMessage(ActorName p_anSender,
    			ActorName p_anReceiver,
    			String p_strMethod,
    			Object[] p_objaArgs,
    			boolean p_bReturnRequest)
    {
    	m_anSender        = p_anSender;
    	m_anReceiver      = p_anReceiver;
    	m_strMethod       = p_strMethod;
    	m_objaArgs        = ObjectHandler.deepCopy(p_objaArgs);
    	m_bReturnRequest  = p_bReturnRequest;
    	m_strReplyWith    = Platform.creteMessageID();
    	m_strReplyTo      = new String("");
    	m_strComments     = new String("");
    	m_strErrorMessage = new String("");
    	m_strDestHostAddress = new String("");
    	
    	if (m_objaArgs == null) {
    	    m_objaArgs = new Object[0];
    	}
    }
    
    
    /**
     *  Returns the name of the sender actor.
     *
     *  @return the name of the sender actor.
     */
    public final ActorName getSender()
    {
    	return m_anSender;
    }
    
    
    /**
     *  Returns the name of the receiver actor.
     *
     *  @return the name of the receiver actor.
     */
    public final ActorName getReceiver()
    {
    	return m_anReceiver;
    }
    
    
    /**
     *   Sets the Internet address of the destination host.
     * 
     *   @param p_strDestHostAddress the Internet address of 
     *   		the destination host of this message.
     */
    public void setDestHostAddress(String p_strDestHostAddress)
    {
    	m_strDestHostAddress = p_strDestHostAddress;
    }
    
    
    /**
     *  Returns the Internet address of the destination host.
     * 
     *  @return the Internet address of the destination host.
     */ 
    public final String getDestHostAddress()
    {
    	return m_strDestHostAddress;
    }
    
    
    /**
     *  Returns the method name.
     * 
     *  @return the metond name.
     */
    public final String getMethod()
    {
    	return m_strMethod;
    }
    

    /**
     *  Returns arguments of the method.
     *
     *  @return arguments of the method.
     */
    public final Object[] getArguments()
    {
    	return m_objaArgs;
    }
    
    
    /**
     *  Checkes if the ruturn message is required.
     */
    public boolean isReturnRequested()
    {
    	return m_bReturnRequest;
    }
    
    
    /**
     *  Initialize the return value of a call message.
     * 
     *  @param p_objReturn the return value of a call message.
     */
    public void setReturnMessage(Object p_objReturn)
    {
    	m_bReturnMessage = true;

    	m_objaArgs = new Object[1];
    	m_objaArgs[0] = ObjectHandler.deepCopy((Serializable)p_objReturn);
    }
    
    
    /**
     *  Checkes if this is a return message.
     */
    public boolean isReturnMessage()
    {
    	return m_bReturnMessage;
    } 
    
        
    /**
     *  Sets an error message.
     * 
     *  @param p_strMsg an error message.
     */
    public void setErrorMessage(String p_strMsg)
    {
    	m_bErrorMessage  = true;
    	m_bReturnMessage = true;
    	
    	m_strErrorMessage = new String(p_strMsg);
    }
    
    
    /**
     *  Returns an error message.
     * 
     *  @return an error message.
     */
    public final String getErrorMessage()
    {
    	return m_strErrorMessage;
    }
    
    
    /**
     *  Checkes if this is an error message.
     */
    public boolean isErrorMessage()
    {
    	return m_bErrorMessage;
    } 


    /**
     *  Returns the strRepyWith value.
     * 
     *  @return the strRepyWith value.
     */
    public final String getReplyWith()
    {
    	return m_strReplyTo;
    }


    /**
     *  Sets the identifier of the original message.
     * 
     *  @param p_strMsgID the identifier of the original message.
     */
    public void setReplyTo(String p_strMsgID)
    {
    	m_strReplyTo = new String(p_strMsgID);
    }


    /**
     *  Sets comments of this message.
     * 
     *  @param p_strComments comments of this message.
     */
    public void setComments(String p_strComments)
    {
    	m_strComments = new String(p_strComments);
    }


    /**
     *  Creates a return message.
     * 
     *  @param p_objReply the refrence to an object, a return value.
     * 
     *  @return a return message of this message.
     */
    public ActorMessage makeReturnMessage(Object p_objReply)
    {
    	ActorMessage amReply = new ActorMessage(m_anReceiver, m_anSender, m_strMethod, m_objaArgs, false);
    	amReply.setReturnMessage(p_objReply);
    	amReply.setReplyTo(m_strReplyWith);
    	return amReply;
    }


    /**
     *  Create an error message.
     * 
     *  @param p_strErrorMsg an error message.
     * 
     *  @return a return message of this message.
     */
    public ActorMessage makeErrorMessage(String p_strErrorMsg)
    {
    	ActorMessage amReply = new ActorMessage(m_anReceiver, m_anSender, m_strMethod, m_objaArgs, false);
    	amReply.setErrorMessage(p_strErrorMsg);
    	amReply.setReplyTo(m_strReplyWith);
    	return amReply;
    }


    /**
     *  Create an error message.
     * 
     *  @param p_anSender the name of the sender actor of this message.
     *  @param p_strErrorMsg an error message.
     * 
     *  @return a return message of this message.
     */
    public ActorMessage makeErrorMessage(ActorName p_anSender, String p_strErrorMsg)
    {
    	ActorMessage amReply = new ActorMessage(p_anSender, m_anSender, m_strMethod, m_objaArgs, false);
    	amReply.setErrorMessage(p_strErrorMsg);
    	amReply.setReplyTo(m_strReplyWith);
    	return amReply;
    }


    /**
     *  Returns a string representation of this object.
     * 
     *  @return a string representation of this object.
     */
    public String toString()
    {
    	String strReturn = new String();
    	String strLineSeparator = System.getProperty("line.separator");
    	
    	
    	strReturn += "Actor Message: " + strLineSeparator;
    	strReturn += "    - Sender:   " + m_anSender + strLineSeparator;
    	strReturn += "    - Receiver: " + m_anReceiver + strLineSeparator;
    	strReturn += "    - Destination: " + m_strDestHostAddress + strLineSeparator;
    	strReturn += "    - Reply With: " + m_strReplyWith + strLineSeparator;
    	strReturn += "    - Reply To: " + m_strReplyTo + strLineSeparator;
    	    	
    	strReturn += "    - Return Request Flag: " + m_bReturnRequest + strLineSeparator;
    	strReturn += "    - Return Message Flag: " + m_bReturnMessage + strLineSeparator;
    	strReturn += "    - Error Message Flag:  " + m_bErrorMessage + strLineSeparator;
    	
    	if (m_bErrorMessage) {
    	    strReturn += "    - Error Message: " + m_strErrorMessage + strLineSeparator;
    	}

    	if (m_strComments.length() != 0) {
    	    strReturn += "    - Comments: " + m_strComments + strLineSeparator;
    	}
    	
    	if (m_bReturnMessage) {
    	    strReturn += "    - Return Value: " + m_objaArgs[0] + strLineSeparator;
    	} else {
    	    strReturn += "    - Method Name: " + m_strMethod + strLineSeparator;
    	    strReturn += "    - Arguments : #. " + m_objaArgs.length + strLineSeparator;
    	
    	    for (int i=0; i<m_objaArgs.length; i++) {
    	    	strReturn += "        - [" + i + "]: " + m_objaArgs[i] + strLineSeparator; 
    	    }
    	}
    	
    	return strReturn;
    } 
}
