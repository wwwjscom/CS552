
package aa.tool;

import java.io.Serializable;

import aa.core.ActorName;
import aa.util.ObjectHandler;

/**
 *  This class describes an actor tuple.
 *  An actor tuple consists of the name of an actor and its attributes. 
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> January 27, 2004 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> June 3, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */
 
public class ActorTuple implements Serializable
{
    // ========== Object Variables ========== //
    
    /**
     *  The name of an actor. 
     */
    private ActorName m_anActor;

    /**
     *  An array of elements to represent information for the actor.
     */
    private Object[] m_objaElements;
    

    // ========== Object Methods ========== //

    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1)
    {
    	Object[] objaElements = { p_serEle1 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
    
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
    
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
    
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
    
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
    
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     *  @param p_serEle6 the sixth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5,
    		      Serializable p_serEle6)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5,
    				  p_serEle6 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
        
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     *  @param p_serEle6 the sixth element to represent information for the actor.
     *  @param p_serEle7 the seventh element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5,
    		      Serializable p_serEle6, Serializable p_serEle7)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5,
    				  p_serEle6, p_serEle7 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
        
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     *  @param p_serEle6 the sixth element to represent information for the actor.
     *  @param p_serEle7 the seventh element to represent information for the actor.
     *  @param p_serEle8 the eighth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5,
    		      Serializable p_serEle6, Serializable p_serEle7, Serializable p_serEle8)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5,
    				  p_serEle6, p_serEle7, p_serEle8 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
        
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     *  @param p_serEle6 the sixth element to represent information for the actor.
     *  @param p_serEle7 the seventh element to represent information for the actor.
     *  @param p_serEle8 the eighth element to represent information for the actor.
     *  @param p_serEle9 the ninth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5,
    		      Serializable p_serEle6, Serializable p_serEle7, Serializable p_serEle8,
    		      Serializable p_serEle9)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5,
    				  p_serEle6, p_serEle7, p_serEle8, p_serEle9 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
        
    /**
     *  Creates an actor tuple.
     * 
     *  @param p_anActor the name of an actor.
     *  @param p_serEle1 the first element to represent information for the actor.
     *  @param p_serEle2 the second element to represent information for the actor.
     *  @param p_serEle3 the third element to represent information for the actor.
     *  @param p_serEle4 the fourth element to represent information for the actor.
     *  @param p_serEle5 the fifth element to represent information for the actor.
     *  @param p_serEle6 the sixth element to represent information for the actor.
     *  @param p_serEle7 the seventh element to represent information for the actor.
     *  @param p_serEle8 the eighth element to represent information for the actor.
     *  @param p_serEle10 the tenth element to represent information for the actor.
     */
    public ActorTuple(ActorName p_anActor, Serializable p_serEle1, Serializable p_serEle2,
    		      Serializable p_serEle3, Serializable p_serEle4, Serializable p_serEle5,
    		      Serializable p_serEle6, Serializable p_serEle7, Serializable p_serEle8,
    		      Serializable p_serEle9, Serializable p_serEle10)
    {
    	Object[] objaElements = { p_serEle1, p_serEle2, p_serEle3, p_serEle4, p_serEle5,
    				  p_serEle6, p_serEle7, p_serEle8, p_serEle9, p_serEle10 };
    	
    	m_anActor = p_anActor;
    	m_objaElements = objaElements;
    }
        

    /**
     *  Returns the actor name registered.
     * 
     *  @return the actor name registered.
     */
    public final ActorName getActorName()
    {
	return m_anActor;    	
    }


    /**
     *  Rerturns elements in this actor tuple as an array of objects.
     * 
     *  @return elements in this actor tuple as an array of objects.
     */
    public final Object[] getElements()
    {
        return m_objaElements;
    }


    /**
     *  Returns the element at the specified index of this actor tuple.
     * 
     *  @param p_iIdx the index of the element.
     * 
     *  @return the element at the specified index of this actor tuple 
     *               if the object exists; <br>
     *           <code>null</code> otherwise.
     */
    public final Object getElement(int p_iIdx)
    {
    	if (p_iIdx < m_objaElements.length) {
	    return m_objaElements[p_iIdx];
    	} else {
    	    return null;
    	}
    }


    /**
     *  Returns the number of elements in this actor tuple.
     * 
     *  @return the the number of elements in this actor tuple.
     */
    public int sizeOfElements()
    {
        return m_objaElements.length;
    }


    /**
     *  Converts this actor tuple into a string representation. 
     * 
     *  @return a string representaiton of the given object array.
     */
    public String toString()
    {
    	return "[ AgentName: " + m_anActor + ", Information: " + 
    		ObjectHandler.toStringArgs(m_objaElements) + " ]";
    }
}
