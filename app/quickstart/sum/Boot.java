package app.quickstart.sum;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CreateActorException;

/**
 *  This class create a boot actor to create the Add actor.
 * 
 *  <p>
 *  <b>History</b>
 *  <ul>
 *      <li> February 28, 2004
 * 	<ul>
 *          <li> create this file.
 * 	</ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang and Abha Gupta
 *  @version $Date: 2004/02/28$ $Revision: 0.0.1$
 */

public class Boot extends Actor
{
    // ========== Object Methods ========== //

    private ActorName m_anAdd;		//  name of the Add actor.
	
	
    // ========== Object Methods ========== //

    /**
     *  Creates the Boot actor.
     */
    public Boot()
    {
    	try {
    	    m_anAdd = create("app.quickstart.sum.Add");
    	    send(m_anAdd, "addNumber");
    	} catch (CreateActorException e) {
    	    System.out.println("> Hello.Hello: " + e); 
    	}
    }
}
