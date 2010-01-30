
package aa.core;

/**
 *  An CommunicationException is thrown 
 *  to indicate that an error happens during the actor communication.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> March 28, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2003/05/09$ $Revision: 0.0.2$
 */

public class CommunicationException extends Exception
{
    // ========== Object Methods ========== //

    /**
     *  Constructs an exception reporting an error happens 
     *  during the actor communication.
     * 
     *  @param p_strMsg a message to describe the problem.
     */
    public CommunicationException(String p_strMsg)
    {
    	super(p_strMsg);
    }
}
