 
package aa.tool;

import aa.core.Platform;

/**
 *  This class loader handles actor classes created on different systems
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.3
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 25, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class MessageClassLoader extends ClassLoader
{
    // ========== Object Variables ========== //
    
    /**
     *  Specifies bytes for a class.
     */
    private byte[] m_baData;
	
	
    // ========== Object Methods ========== //

    /**
     *  Loads the class with the specified name.
     * 
     *  @param p_strClassName the name of the class.
     *  @param p_bResolve if it is true then reolve the class.
     * 
     *  @return the resulting class object.
     */
    public Class loadClass(String p_strClassName, boolean p_bResolve)
    {
    	//
    	//  check if the class has already been loaded.
    	//
    	Class classNew = findLoadedClass(p_strClassName);

	if (classNew == null) {
	    try {
	    	//
	    	//  check if the class is a system class.
	    	//
	        classNew = findSystemClass(p_strClassName);
		if (classNew != null) {
		    return classNew;
		}
	    } catch (ClassNotFoundException e) {
	    	if (Platform.bDEBUG) {
		    System.out.println("% MessageClassLoader.loadClass: " +
		    		       "Cannot find the class [" +
		    		       p_strClassName + "],\n" +
		    		       "    and so this class will be resigered.");
	    	}
	    }
	
	    //
	    //  convert an array of bytes into an instance of a class.
	    //
	    classNew = defineClass(p_strClassName, m_baData, 0, m_baData.length);
	}
	
	//
	//  link the specified class.
	//	
	if (p_bResolve) {
	    resolveClass(classNew);
	}

	return classNew;
    }
    
    
    /**
     *  Sets the bytes for a class.
     * 
     *  @param p_baData bytes for a class.
     */
    public void setBytesForClass(byte[] p_baData)
    {
    	m_baData = p_baData;
    }
}
