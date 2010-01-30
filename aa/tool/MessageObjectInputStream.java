 
package aa.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 *  This class defines a new object input stream for communication messages.
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
 *      <li> May 25, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class MessageObjectInputStream extends ObjectInputStream
{
    // ========== Object Variables ========== //
    
    /**
     *  Reference to new class loaderto handle the class of the actor
     */
    public MessageClassLoader m_mslActorLoader;
	
	
    // ========== Object Methods ========== //

    /**
     *  Creates an MessageObjectInputStream that reads form the specified InputStream.
     * 
     *  @param p_is an input stream to read from.
     *  @param p_strClassName the class name.
     *  @param p_baClass an input byte stream of the class.
     * 
     *  @throws IOException if an input/output error occurs while readin stream header.
     */
    public MessageObjectInputStream(InputStream p_is, String p_strClassName, byte[] p_baClass) 
        throws IOException 
    {
	super(p_is);
	
    	m_mslActorLoader = new MessageClassLoader();    	
    	m_mslActorLoader.setBytesForClass(p_baClass);
    }


    /**
     *  Loads the local class equivalent of the specified stream class description.
     * 
     *  @param p_osc an instance of class ObjectStreamClass.
     * 
     *  @return a Class object corresponding to desc.
     * 
     *  @throws IOException any of the usual iput/output exception.
     *  @throws ClassNotFoundException if class of a serialized object cannot be found.
     */
    public Class resolveClass(ObjectStreamClass p_osc) 
	throws IOException, ClassNotFoundException 
    {
        Class class0 = m_mslActorLoader.loadClass(p_osc.getName());

   	return class0;
    }
}
