
package aa.core;

import java.io.Serializable;

/**
 *  This class creates a window for MAA (Multi-Actor Architecture).
 *  <br>
 *  An actor name is represented in two different types:
 *  <ul>
 *      <li> UAN: Universal Actor Name
 *      <li> LAN: Location-based Actor Name
 *  </ul>
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.2
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> March 05, 2003 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 * 
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.2$
 */

public class ActorName implements Comparable, Serializable
{
    // ========== Object Variables ========== //

    private int    m_iID;		//  identifier
    private String m_strHostAddress;	//  host address
    private String m_strLocationAddress;//  location address

    private boolean m_bValid;		//  whether or not this instance is valid


    // ========== Object Methods ========== //

    /**
     *  Creates unique actor name for an actor 
     *  with host address of the platform where the actor is created, and
     *  unique identifier ot the actor.
     * 
     *  @param p_strHostAddress the host address of this platform.
     *  @param p_iID a unique identifier of this actor.
     */
    public ActorName(String p_strHostAddress, int p_iID)
    {
	m_iID                = p_iID;
	m_strHostAddress     = p_strHostAddress;
	m_strLocationAddress = p_strHostAddress;
	m_bValid             = true;
    }


    /**
     *  Creates unique actor name for an actor with another actor name.
     * 
     *  @param p_anActor another actor name to be copied.
     */
    public ActorName(ActorName p_anActor)
    {
    	m_iID                = p_anActor.getID();
    	m_strHostAddress     = p_anActor.getHostAddress();
    	m_strLocationAddress = p_anActor.getLocationAddress();
    	m_bValid             = p_anActor.isValid();
    }


    /**
     *  Creates unique actor name for an actor
     *  with string representation of a UAN.
     * 
     *  @param p_strUAN the string representation of a UAN.
     */
    public ActorName(String p_strUAN)
    {
    	int iStart = p_strUAN.indexOf("://");
    	
    	if (iStart != -1) {
     	    String strTemp = p_strUAN.substring(iStart + 3);
    	
    	    iStart = strTemp.lastIndexOf(":");

	    if (iStart != -1) {
	    	try {
    		    m_iID                = Integer.parseInt(strTemp.substring(iStart + 1));
    		    m_strHostAddress     = strTemp.substring(0, iStart);
    		    m_strLocationAddress = m_strHostAddress;
    		    m_bValid             = true;
    		
    		    return;
	    	} catch (NumberFormatException e) {
	    	}
	    }    		
    	}
    	
    	m_strHostAddress     = null;
    	m_strLocationAddress = null;
    	m_iID                = 0;
    	m_bValid             = false;
    }


    /** 
     *  Compares this actor name with the specified actor name for ordering.
     * 
     *  @param p_objOther another object to be compared.
     * 
     *  @return 1 if this actor name >  the other actor name; <br>
     *          0 if this actor name == the other actor name; <br>
     *         -1 otherwise.
     */
    public int compareTo(Object p_objOther) 
    {
    	if (p_objOther instanceof ActorName) {
    	    return getUAN().compareTo(((ActorName)p_objOther).getUAN());
    	} else {
    	    return -1;
    	}
    }
    
    
    /**
     *  Checkes whether this actor name is same as the specified object.
     * 
     *  @param p_objOther the reference to actor name of another actor to be checked
     * 
     *  @return <code>true</code> if this actor name is the same as the specified object; <br>
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object p_objOther)
    {
        return(equals((ActorName)p_objOther));
    }
    

    /**
     *  Checkes whether this actor name is same as the specified actor name.
     * 
     *  @param p_anOther the name of another actor to be checked.
     * 
     *  @return <code>true</code> if this actor name is the same as the specified actor name; <br>
     *          <code>false</code> otherwise.
     */
    public boolean equals(ActorName p_anOther)
    {
    	if (p_anOther == null) {
    	    return false;
    	} 
    	
    	if (getUAN().compareTo(p_anOther.getUAN()) == 0) {
    	    return true;
    	} else {
    	    return false;
    	}
    }
    
    
    /**
     *  Returns the identification number of this actor name.
     * 
     *  @return the identification number of this actor name.
     */
    public int getID()
    {
    	return m_iID;
    }
    
    
    /**
     *  Returns the host address of this actor name.
     * 
     *  @return the host address of this actor name.
     */
    public String getHostAddress()
    {
    	return new String(m_strHostAddress);
    }
    
    
    /**
     *  Sets the current location address.
     * 
     *  @param p_strLocationAddress the current location address.
     */
    public void setLocationAddress(String p_strLocationAddress)
    {
    	m_strLocationAddress = new String(p_strLocationAddress);
    }
    
    
    /**
     *  Returns the current location address.
     * 
     *  @return the current location address.
     */
    public String getLocationAddress()
    {
    	return new String(m_strLocationAddress);
    }
    
    
    /**
     *  Checkes wheter the current actor name is valid.
     *
     *  @return true if the current actor name is valid; <br>
     *           false otherwise.
     */
    public boolean isValid()
    {
    	return m_bValid;
    }
    
    
    /**
     *  Returns the UAN of this actor name.
     * 
     *  @return the UAN of this actor name.
     */
    public String getUAN()
    {
    	return "uan://" + m_strHostAddress + ":" + m_iID;
    }


    /**
     *  Returns the LAN of this actor name.
     * 
     *  @return the LAN of this actor name.
     */
    public String getLAN()
    {
    	return "lan://" + m_strLocationAddress + ":" + m_strHostAddress + ":" + m_iID;
    }
    
    
    /**
     *  Returns a string representation of this object.
     * 
     *  @return a string representation of this object. <br>
     *           UAN if the location of an actor is same as the original location 
     *               where the actor was created, <br>
     *           UAL otherwise. <br>
     */
    public String toString()
    {
    	if (m_strHostAddress.equals(m_strLocationAddress)) {
    	    return getUAN();
    	} else {
    	    return getLAN();
    	}
    }
}
