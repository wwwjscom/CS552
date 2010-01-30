
package aa.tool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import aa.core.ActorName;

/**
 *  This class implements a tuple space that maintains actor tuples. 
 *  An actor tuple consists of the actor name field and property fields.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 *      <li> February 19, 2004 - version0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> January 26, 2004 - version 0.0.2
 *      <ul>
 *          <li> modify this file a lot.
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

public class TupleSpace 
{
    // ========== Object Variables ========== //
    
    /**
     *  A linked list of actor tuples stored in this tuple space.
     */
    protected LinkedList m_llActorTuples;
	
	
    // ========== Object Methods ========== //

    /**
     *  Constructs an tuple space.
     */
    public TupleSpace()
    {
    	m_llActorTuples = new LinkedList();
    }
    
    
    /**
     *  Checks whether the given actor tuple has any null element.
     * 
     *  @param p_atTuple an actor tuple to be checked.
     * 
     *  @return <code>true</code> if any element of the given actur tuple is null;
     *          <code>false</code> otherwise.
     */
    private boolean isValidDataTuple(ActorTuple p_atTuple)
    {
    	//
    	//  check whether either the given actor tuple is null or 
    	//		         the ActorName field is null.
    	//
    	if ( (p_atTuple == null) || 
    	     (p_atTuple.getActorName() == null) ) {
    	    return false;
    	}
    	
    	//
    	//  check whether any element field is null.
    	//
        Object[] objaElements = p_atTuple.getElements();

	for (int i=0; i<objaElements.length; i++) {
	    if (objaElements[i] == null) {
		return false;
            }
	}
	
	return true;
    }


    /**
     *  Checks whether element types between the given tuple template and 
     *      the given actor tuple are matched.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     *  @param p_atTuple an actor tuple to be stored in this tuple space.
     * 
     *  @return true if element types between the given tuple template and 
     *               the given actor tuple are matched;
     *          false otherwise.
     */
    private boolean areTypeMatched(ActorTuple p_atTemplate, ActorTuple p_atTuple)
    {
	Object[] objaTemplateItems = p_atTemplate.getElements();
	Object[] objaTupleItems    = p_atTuple.getElements();

	if (objaTemplateItems.length != objaTupleItems.length) {
	    return false;
	}

	for (int i=0; i<objaTupleItems.length; i++) {
	    if (objaTemplateItems[i] != null) {
		if ( (!objaTupleItems[i].getClass().isInstance(objaTemplateItems[i])) ||
		     (!objaTupleItems[i].equals(objaTemplateItems[i])) ) {
		    return false;
		}
	    }
	}

	return true;
    }


    /**
     *  Finds actor tuples in this tuple space matched with the specified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with actor tuples
     * 			    in this tuple space.
     *  @param p_bActorName the flag to decide whethter ActorName will be checked or not. 
     * 
     *  @return actor tuples matched with the specified tuple tempmlate.
     * 
     *  @throws TupleSpaceException if the given tuple template is null.
     */
    protected ActorTuple[] getMatchedTupleList(ActorTuple p_atTemplate, boolean p_bActorName)
	throws TupleSpaceException
    {
    	//
    	//  check whether the given tuple template is null.
    	//
    	if (p_atTemplate == null) {
            throw new TupleSpaceException("Tuple template shoud not be null");
    	}
    	
    	//
    	//  find actor tuples matched with the specified tuple template.
    	//
	LinkedList llTmp = new LinkedList();

	ActorName anTemplate = p_atTemplate.getActorName();
	Object[] objaTemplateElement = p_atTemplate.getElements();

        Iterator iterActorTuples = m_llActorTuples.iterator();

	while (iterActorTuples.hasNext()) {
	    ActorTuple atTuple = (ActorTuple) iterActorTuples.next();
	    
	    //
	    //  compare ActorName in the template with that in an actor tuple.
	    //
	    ActorName  anTuple = atTuple.getActorName();

	    if (p_bActorName) {
	    	//
	    	//  if ActorName in the template is different from that in an actor tuple,
	    	//  then skip this tuple.
	    	//
	    	if ( (anTemplate != null) &&
	    	     (!anTemplate.equals(anTuple)) ) {
	    	     continue;
	    	}
	    }

	    //
	    //  compare elements in the template with those in an actor tuple.
	    //
	    Object[] objaDataElement = atTuple.getElements();

            if (objaDataElement.length == objaTemplateElement.length) {
                boolean bFound = true;
 
                for (int i=0; i<objaTemplateElement.length; i++) {
		    if (objaTemplateElement[i] != null) {
			if ( (!objaTemplateElement[i].getClass().isInstance(objaDataElement[i])) ||
			     (!objaTemplateElement[i].equals(objaDataElement[i])) ) {
			    bFound = false;
			    break;
			}
		    }
                }

                if (bFound) {
                    llTmp.add(atTuple);
                }
            }
        };

//	return (ActorTuple[]) llTmp.toArray();
	ActorTuple ataTmp[] = new ActorTuple[llTmp.size()];
	llTmp.toArray(ataTmp);
	return ataTmp;
    }


    /**
     *  Gets a actor tuple matched with the specified tuple template,
     *     and remove it if <code>bRemoveFlag</code> is true.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with actor tuples
     * 				in this tuple space.
     *  @param p_bRemoveFlag the flag to remove a tuple after the tuple is retrieved.
     * 
     *  @return an actor tuple matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException if this tuple space cannot find any actor tuple 
     *               that are matched with the specified tuple template.
     */
    private ActorTuple getImpl(ActorTuple p_atTemplate, boolean p_bRemoveFlag)
	throws TupleSpaceException
    {
	ActorTuple[] atMatchedTuples = getMatchedTupleList(p_atTemplate, true);

        if (atMatchedTuples.length > 0) {
	    Random rnd = new Random();
	    int iIdx = rnd.nextInt(atMatchedTuples.length);

	    if (p_bRemoveFlag) {
		m_llActorTuples.remove(atMatchedTuples[iIdx]);
	    }

            return atMatchedTuples[iIdx];
        } else {
            throw new TupleSpaceException("Cannot Find Any Tuple");
        }
    }


    /**
     *  Returns all actor tuples matched with the specified tuple template, 
     *      and remove them if <code>bRemoveFlag</code> is true.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with actor tuples
     * 			    in this tuple space.
     *  @param p_bRemoveFlag the flag to remove actor tuples after they are retrieved.
     * 
     *  @return actor tuples matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple.
     */
    private ActorTuple[] getAllImpl(ActorTuple p_atTemplate, boolean p_bRemoveFlag)
	throws TupleSpaceException
    {
	ActorTuple[] anMatchedTuples = getMatchedTupleList(p_atTemplate, true);

        if (anMatchedTuples.length > 0) {
	    if (p_bRemoveFlag) {
	    	for (int i=0; i<anMatchedTuples.length; i++) {
		    m_llActorTuples.remove(anMatchedTuples[i]);
	    	}
	    }

            return (ActorTuple[])anMatchedTuples;
        } else {
            throw new TupleSpaceException("Cannot Find Any Tuple");
        }
    }

    
    /**
     *  Inserts an actor tuple in this tuple space.
     *
     *  @param p_atTuple an actor tuple to be stored in this tuple space.
     * 
     *  @throws TupleSpaceException 
     *              either if the actor tuple is invalid or 
     *                     if the same actor tuple already exits in this tuple space.
     */
    public void write(ActorTuple p_atTuple)
	throws TupleSpaceException
    {
	//
	//  check if the actor tuple is valid.
	//
	if (!isValidDataTuple(p_atTuple)) {
	    throw new TupleSpaceException("Illegal Paramer - " +
	    		"Each element of an actor tuple should not be null");
	}

	//
	//  check whether the same tuple already exists.
	//
	ActorTuple[] anMatchedTuples = getMatchedTupleList(p_atTuple, true);

	if (anMatchedTuples.length == 0) {
	    m_llActorTuples.add(p_atTuple);
	} else {
	    throw new TupleSpaceException("This tuple is already registered");
	}
    }


    /**
     *  Reads an actor tuple matched with the specified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return an actor tuple matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public final ActorTuple read(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	return(getImpl(p_atTemplate, false));
    } 


    /**
     *  Reads all actor tuples matched with the specified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return all actor tuples matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public final ActorTuple[] readAll(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
    	return(getAllImpl(p_atTemplate, false));
    }
    

    /**
     *  Reads an actor tuple matched with the specified tuple template, 
     *      and then removes it.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return an actor tuple matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public ActorTuple take(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	return(getImpl(p_atTemplate, true));
    }


    /**
     *  Reads all actor tuples matched with the specified tuple template, 
     *      and then removes them.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return actor tuples matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public ActorTuple[] takeAll(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	return(getAllImpl(p_atTemplate, true));
    }


    /**
     *  Removes actor tuples matched with the given tuple template,
     *      and then inserts an actor tuple in this tuple space.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     *  @param p_atTuple an actor tuple to be stored in this tuple space.
     * 
     *  @throws TupleSpaceException
     *              either if the actor tuple is invalid or 
     *                     if the type of an element in the given actor tuple  
     *                         is not matched with the type of the corresponding 
     *                         element in the given tuple template.
     */
    public void update(ActorTuple p_atTemplate, ActorTuple p_atTuple)
	throws TupleSpaceException
    {
    	if ( (p_atTemplate == null) ||
    	     (p_atTuple == null) ) {
	    throw new TupleSpaceException("Illegal Paramer - " +
	    		"Any argument of this method should not be null");
    	}
    	
	//
	//  check if the actor tuple is valid.
	//
	if (!isValidDataTuple(p_atTuple)) {
	    throw new TupleSpaceException("Illegal Paramer - " +
	    		"Each element of an actor tuple should not be null");
	}

	//
	//  check if the types of elements of the given actor tuple are matched with 
	//      the types of corresponding elements of the given tuple template.
	//
	if (!areTypeMatched(p_atTemplate, p_atTuple)) {
	    throw new TupleSpaceException("The type of an element in the given tuple template " +
	                                  "is not matched with " +
				          "that of the corresponding element in the given tuple");
	}

	//
	//  remove actor tuples matched with the given actor template.
	//
	try {
	    removeAll(p_atTemplate);	
	} catch (TupleSpaceException e) {
	}
        
	//
	//  add the given actor tuple.
	//
	m_llActorTuples.add(p_atTuple);
    }
    

    /**
     *  Removes all actor tuples matched with the sepecified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @throws TupleSpaceException 
     * 		    if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */ 
    public void removeAll(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	ActorTuple[] anMatchedTuples = getMatchedTupleList(p_atTemplate, true);

        if (anMatchedTuples.length > 0) {
	    for (int i=0; i<anMatchedTuples.length; i++) {
		m_llActorTuples.remove(anMatchedTuples[i]);
	    }
	} else {
            throw new TupleSpaceException("Cannot Find Any Tuple");
        }
    }


    /**
     *  Returns the actor name in an actor tuple 
     *      matched with the specified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return the actor name in an actor tuple matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *  	    if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public final ActorName search(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	ActorTuple[] anMatchedTuples = getMatchedTupleList(p_atTemplate, false);

        if (anMatchedTuples.length > 0) {
	    Random rnd = new Random();
	    int iIdx = rnd.nextInt(anMatchedTuples.length);

            return anMatchedTuples[iIdx].getActorName();
        } else {
            throw new TupleSpaceException("Cannot Find Any Actor Name");
        }
    }
    

    /**
     *  Returns actor names in all actor tuples 
     *      matched with the specified tuple template.
     * 
     *  @param p_atTemplate a tuple template to be used for matching with
     *                      actor tuples in this tuple space.
     * 
     *  @return actor names in all actor tuples matched with the specified tuple template.
     * 
     *  @throws TupleSpaceException 
     *              if this tuple space cannot find any actor tuple 
     *              that are matched with the specified tuple template.
     */
    public final ActorName[] searchAll(ActorTuple p_atTemplate)
	throws TupleSpaceException
    {
	ActorTuple[] anMatchedTuples = getMatchedTupleList(p_atTemplate, false);
	LinkedList llActorNames = new LinkedList();

        if (anMatchedTuples.length > 0) {
	    for (int i=0; i<anMatchedTuples.length; i++) {
	    	llActorNames.add(anMatchedTuples[i].getActorName());
	    }

//          return (ActorName[])llActorNames.toArray();
	    ActorName anaActorNames[] = new ActorName[llActorNames.size()];
	    for (int i=0; i<llActorNames.size(); i++) {
	    	anaActorNames[i] = (ActorName)llActorNames.get(i);
	    }
	    return anaActorNames;
        } else {
            throw new TupleSpaceException("Cannot Find Any Actor Name");
        }
    }
}
