
package aa.app.dummy;

import aa.core.*;

/**
 *  Dummy Actor.
 *  <br>
 *  This class is used for testing purpose.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> March 18, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2003/03/18$ $Revision: 0.0.1$
 */

public class Dummy extends Actor
{
    // ========== Object Methods ========== //

    /**
     *  constructor
     */
    public Dummy()
    {
    	System.out.println("> Dummy");
    	
    	send(getActorName(), "Hello");
    	send(getActorName(), "Hello", "World !");
    }
    
    
    /**
     *  print 'Hello'
     */
    public void Hello()
    {
    	this.println("Hello");
    }
    
    public void Hello(String strMsg)
    {
    	this.println("Hello " + strMsg);
    }
}
