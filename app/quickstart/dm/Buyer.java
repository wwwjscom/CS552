
package app.quickstart.dm;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.tool.ActorTuple;

/**
 *  A buyer actor.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> March 1, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> change the name of methods.
 * 	</ul>
 * 	<li> February 28, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
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
 *  @version $Date: 2004/03/01$ $Revision: 0.0.4$
 */

public class Buyer extends Actor
{
    // ========== Object Variables ========== //
    
    private ActorName m_anDM;		//  reference to Directory Manager
    
	
    // ========== Object Methods ========== //

    /**
     *  Creates this actor.
     */
    public Buyer()
    {
    	m_anDM = getDefaultDirectoryManager();
    }


    /**
     *  Checks the price of an item whose type is selected.
     * 
     *  @param p_strItem an item type selected.
     */
    public void search(String p_strItem)
    {   	
    	ActorTuple atTemplate = new ActorTuple(null, "seller", p_strItem);

    	try {
    	    ActorName anSeller = (ActorName) call(m_anDM, "search", atTemplate);
    	    send(anSeller, "checkPrice", getActorName());
    	} catch (CommunicationException e) {
    	    System.err.println("> Buyer.check: " + e);
    	}
    }
   
   
    /**
     *  Checks the price of any item.
     */
    public void search()
    {
    	search(null);
    }
    
    
    /**
     *  Checks prices of items whose type is selected.
     * 
     *  @param p_strItem an item type selected.
     */
    public void searchAll(String p_strItem)
    {
    	ActorTuple atTemplate = new ActorTuple(null, "seller", p_strItem);

    	try {
    	    ActorName[] anSeller = (ActorName[]) call(m_anDM, "searchAll", atTemplate);
    	    
    	    for (int i=0; i<anSeller.length; i++) {
    	        send(anSeller[i], "checkPrice", getActorName());
    	    }
    	} catch (CommunicationException e) {
    	    System.err.println("> Buyer.check: " + e);
    	}
    }
     
     
    /**
     *  Checks prices of all items.
     */
    public void searchAll()
    {
    	searchAll(null);
    }
    
    
    /**
     *  Requests the price of an item whose type is selected.
     * 
     *  @param p_strItem an item type selected.
     */
    public void deliver(String p_strItem)
    {
    	ActorTuple atTemplate = new ActorTuple(null, "seller", p_strItem);

	send(m_anDM, "deliver", atTemplate, "checkPrice", getActorName());    	
    }
    

    /**
     *  Requests the price of any item.
     */
    public void deliver()
    {
    	deliver(null);
    }
    
    
    /**
     *  Requests prices of items whose type is selected.
     * 
     *  @param p_strItem an item type selected.
     */
    public void deliverAll(String p_strItem)
    {
    	ActorTuple atTemplate = new ActorTuple(null, "seller", p_strItem);

	send(m_anDM, "deliverAll", atTemplate, "checkPrice", getActorName());    	
    }
    

    /**
     *  Requests prices of all items.
     */
    public void deliverAll()
    {
    	deliverAll(null);
    }
    
      
    /**
     *  Prints the price of a product.
     *  <br>
     *  This method is called by a seller actor.
     * 
     *  @param p_anActor the name of an actor that activate this method.
     *  @param p_strItem the name of a product item.
     *  @param p_intPrice the price of the item.
     */
    public void price(ActorName p_anActor, String p_strItem, Integer p_intPrice)
    {
    	System.out.println("> Seller Actor [" + p_anActor + "] sells " + p_strItem + 
			   " for " + p_intPrice + "."); 
    }
}
