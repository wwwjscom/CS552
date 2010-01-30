
package aa.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 *  This class defines the queue data type. 
 *  It is a simple extension of the Linked List class to
 *  support syncrhonized method invocation.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 26, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> add the <code>insertAll</code> method.
 * 	</ul>
 * 	<li> February 9, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> change some method names.
 * 	</ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> March 26, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/09$ $Revision: 0.0.3$
 */

public class Queue 
	extends LinkedList 
	implements Serializable
{
    // ========== Object Methods ========== //

    /**
     *  Creates a queue instance.
     */
    public Queue()
    {
    	super();
    }
    
    
    /**
     *  Inserts the sepcified element at the end of this queue.
     * 
     *  @param p_object the element to be inserted into this queue.
     */ 
    public synchronized void insert(Object p_object) 
    {
	add(p_object); 
    }
    
    
    /**
     *  Inserts all of the elements of the specified queue at the end of this queue.
     * 
     *  @param p_queue all of the elements to be inserted into this queue.
     */ 
    public synchronized void insertAll(Queue p_queue) 
    {
    	int iSizeOfQueue = p_queue.size();
    	
    	for (int i=0; i<iSizeOfQueue; i++) {
    	    add(p_queue.removeFirst());
    	}
    }
    
    
    /**
     *  Removes the first element from this queue, and 
     *  returns the elemement that was removed from the list.
     * 
     *  @return the element previously at the first position of this queue
     *               if it was.
     * 
     *  @throws NoSuchElementException if this list is empty.
     */
    public synchronized Object remove()
        throws NoSuchElementException
    {
    	return removeFirst();
    }   
}
