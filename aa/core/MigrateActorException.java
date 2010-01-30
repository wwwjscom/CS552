
package aa.core;

/**
 * An MigrateActorException is thrown 
 * to indicate that an error happens during the actor migration.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 26, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> change the name of this class from MigrationException.
 * 	</ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 12, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2003/05/09$ $Revision: 0.0.2$
 */

public class MigrateActorException extends Exception
{
    // ========== Object Methods ========== //

    /**
     *  Constructs an exception reporting an error happens 
     *  during the actor migration.
     * 
     *  @param p_strMsg a message to describe the problem.
     */
    public MigrateActorException(String p_strMsg)
    {
    	super(p_strMsg);
    }
}
