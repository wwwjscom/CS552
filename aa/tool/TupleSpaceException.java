
package aa.tool;

/**
 *  A TupleSpaceException is thrown 
 *  to indicate that a tuple operation cannot be performed correctly.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> January 26, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> change the name of this object.
 * 	</ul>
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
 *  @version $Date: 2004/01/26$ $Revision: 0.0.3$
 */

public class TupleSpaceException extends Exception
{
    // ========== Object Methods ========== //

    /**
     *  Constructs an exception reporting an error found in a tuple space operation.
     * 
     *  @param p_strMsg a message to describe the problem.
     */
    public TupleSpaceException(String p_strMsg)
    {
	super(p_strMsg);
    }
}
