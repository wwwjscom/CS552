
package app.quickstart.hello3;

import aa.core.Actor;
import aa.core.BecomeActorException;

/**
 *  The Hello actor.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> March 22, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class Hello extends Actor
{
    // ========== Object Methods ========== //

    /**
     *  Creates the Hello actor.
     */
    public Hello()
    {
    	System.out.print(" Hello");
    	
    	try {
    	    become("app.quickstart.hello3.World");
    	} catch (BecomeActorException e) {
    	    System.out.println("> Hello.Hello: " + e); 
    	}
    }
}
