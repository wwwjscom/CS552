
package aa.tool;

/**
 * This class provides a tuple space that supports 
 *     the direct reference to actor tuples in this tuple space.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> January 26, 2004 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 * @author Myeong-Wuk Jang
 *  @version $Date: 2004/01/26$ $Revision: 0.0.1$
 */

public class PublicTupleSpace extends TupleSpace
{
    // ========== Object Methods ========== //

    /**
     *  Returns the array of actor tuples in this tuple space. 
     * 
     *  @return the array of actor tuples in this tuple space.
     */
    public final ActorTuple[] getActorTuples()
    {  	
//      return m_llActorTuples.toArray();
	ActorTuple[] ataData = new ActorTuple[m_llActorTuples.size()];
	m_llActorTuples.toArray(ataData);
	return ataData;
    }
}
