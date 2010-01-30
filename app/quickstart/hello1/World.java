
package app.quickstart.hello1;

import aa.core.Actor;

/**
 *  The World actor.
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

public class World extends Actor
{
    // ========== Object Methods ========== //

    /** 
     *  Creates the World actor.
     */
    public World()
    {
    	System.out.println(" World!");
    }
}
