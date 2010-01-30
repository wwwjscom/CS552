
package app.quickstart.hello;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

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
    // ========== Object Variables ========== //
    
    private ActorName m_anWorld = null;
    
    
    // ========== Object Methods ========== //

    /**
     *  Creates the Hello actor.
     */
    public Hello()
    {
    	System.out.print(" Hello");
    	
    	try {
    	    m_anWorld = create("app.quickstart.hello.World");
    	} catch (CreateActorException e) {
    	    System.out.println("> Hello.Hello: " + e); 
    	}
    }
    
    
    /**
     *  Do test 1.
     */
    public void test()
    {
    	System.out.print(" Hello");

    	try {
    	    call(m_anWorld, "world", " World!!");
    	} catch (CommunicationException e) {
    	    System.err.println(e.toString());
    	}
    }
    
    
    /**
     *  Do test 2.
     */
    public void test2()
    {
    	this.println("Hello");

    	try {
    	    call(m_anWorld, "println", "World!!!");
    	} catch (CommunicationException e) {
    	    System.err.println(e.toString());
    	}
    }    
}
