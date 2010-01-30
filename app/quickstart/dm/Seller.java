
package app.quickstart.dm;

import java.util.Random;

import aa.core.Actor;
import aa.core.ActorName;
import aa.tool.ActorTuple;

/**
 *  A seller actor.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
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
 *  @version $Date: 2004/02/28$ $Revision: 0.0.3$
 */

public class Seller extends Actor
{
    // ========== Object Variables ========== //
    
    private int m_iPrice;		//  price of a product
    
    private String m_strModel;		//  model name of a computer.
    
	
    // ========== Object Methods ========== //

    /**
     *  Creates this actor.
     * 
     *  @param p_strModel the model name of a computer.
     */
    public Seller(String p_strModel)
    {
    	m_strModel = p_strModel;
    	
    	Random rnd = new Random();
    	m_iPrice = 500 + rnd.nextInt(500);
    	
    	ActorName anDM = getDefaultDirectoryManager();
    	
    	ActorTuple tuple = new ActorTuple(getActorName(), "seller", m_strModel);
    	send(anDM, "register", tuple);
    }
    
    
    /**
     *  Increases the price of this product.
     */
    public void increasePrice()
    {
    	m_iPrice += 10;
    	System.out.println("My current price is " + m_iPrice);
    }
    
  
    /**
     *  Decreases the price of this product.
     */  
    public void decreasePrice()
    {
    	m_iPrice -= 10;
    	System.out.println("My current price is " + m_iPrice);
    }
    
    
    /**
     *  Prints the current price of this product.
     */
    public void checkPrice()
    {
    	System.out.println("My current price is " + m_iPrice);
    }
    
    
    /**
     *  Sends the current price of this product.
     * 
     *  @param p_anSender the name of an actor that requested this service.
     */
    public void checkPrice(ActorName p_anSender)
    {
    	send(p_anSender, "price", getActorName(), m_strModel, new Integer(m_iPrice));
    }
}
