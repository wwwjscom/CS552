
package app.quickstart.animal;

import aa.core.Actor;
import aa.core.BecomeActorException;

/**
 *  This class describes an actor who can change its behavior.
 * 
 *  <p>
 *  <b>History</b>
 *  <ul>
 *      <li> February 28, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> re-make this actor from the Chameleon1 actor
 * 	</ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> February 11, 2004. - version 0.0.1
 * 	<ul>
 *          <li> create this file.
 * 	</ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/28$ $Revision: 0.0.3$
 */

public class ChameleonBlue extends Actor
{
    // ========== Object Methods ========== //

    /**
     *  Changes the behavior of this actor.
     */
    public void change()
    {
    	try {
	    become("app.quickstart.animal.ChameleonRed");
	} catch (BecomeActorException e) {
	    System.err.println("> ChameleonRed.change: I cannot change my behavior.");
	    e.printStackTrace();
	}
    }
    
    
    /**
     *  Prints the current state.
     */
    public void color()
    {
        System.out.println("> " + getActorName() + ": ---> My color is BLUE.");
    }
}
