
package aa.core;

/**
 * An BecomeActorException is thrown 
 * by certain methods of the <code>aa.core.platform</code> class
 * to indicate that the caller cannot change its behavior.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 11, 2004 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/11$ $Revision: 0.0.1$
 */

public class BecomeActorException extends Exception 
{
    // ========== Object Methods ========== //

    /**
     *  Constructs an exception reporting an error happens 
     *  when the behavior of an actor is changed.
     * 
     *  @param p_strMsg a message to describe the problem.
     */
    public BecomeActorException(String p_strMsg)
    {
    	super(p_strMsg);
    }
}
