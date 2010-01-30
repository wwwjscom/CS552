
package aa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *  This class includes some useful functions to handle an object or objects.
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
 *      <li> March 26, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */

public class ObjectHandler 
{
    // ========== Object Methods ========== //

    /**
     *  Makes a deep copy of the specified object.
     *  <br>
     *  Reference: http://www.javaworld.com/javatips/jw-javatip76_p.html
     * 
     *  @param p_objOld an input object to be copied.
     * 
     *  @return a new object copied from the given object
     * 		     if the given object is not null;<br>
     * 		<code>null</code> if the given object is null.
     */
    public static Object deepCopy(Object p_objOld)
    {
    	Object objNew = null;
    	
    	if (p_objOld == null) {
    	    return null;
    	}
    	
    	try {
    	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	    ObjectOutputStream oos     = new ObjectOutputStream(baos);
    	
    	    oos.writeObject(p_objOld);
    	    oos.flush();
    	
    	    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    	    ObjectInputStream ois     = new ObjectInputStream(bais);
    	    
    	    objNew = ois.readObject();
    	} catch (IOException e) {
    	    System.err.println("ObjectCopy.deepCopy: " + e);
    	    e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    	    System.err.println("ObjectCopy.deepCopy: " + e);
    	    e.printStackTrace();
    	}
    	
    	return objNew;
    }


    /**
     *  Makes a deep copy of the specified array of objects.
     * 
     *  @param p_objaOld an input array of objects to be copied.
     * 
     *  @return a new array of objects copied from the given objects
     * 		     if the given parameter is not null;<br>
     * 		<code>null</code> if the given parameter is null.
     */
    public static Object[] deepCopy(Object[] p_objaOld)
    {
    	Object[] objaNew = null;
    	
    	if (p_objaOld == null) {
    	    return null;
    	}
    	
    	objaNew = new Object[p_objaOld.length];
    	for (int i=0; i<p_objaOld.length; i++) {
    	    objaNew[i] = deepCopy((Serializable)p_objaOld[i]);
    	}
    	
    	return objaNew;
    }
   

    /**
     *  Serializes an object into a byte stream.
     * 
     *  @param p_serObj the input serializable object.
     * 
     *  @return a byte stream corresponding to the specified object.
     * 
     *  @throws IOException any of the usual iput/output exception.
     */
    public static byte[] serialize(Serializable p_serObj)
        throws IOException
    {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ObjectOutputStream oos     = new ObjectOutputStream(baos);
    	
    	oos.writeObject(p_serObj);
    	oos.flush();
    	    
        return baos.toByteArray();
    }
    
    
    /**
     *  Deserialize a byte stream into an object.
     * 
     *  @param p_baStream the input byte stream of an object of the class.
     * 
     *  @return an object corresponding to the specified byte stream.
     * 
     *  @throws IOException any of the usual iput/output exception.
     *  @throws ClassNotFoundException if class of a serialized object cannot be found.
     */
    public static Object deserialize(byte[] p_baStream)
        throws IOException, ClassNotFoundException
    {
    	ByteArrayInputStream bais = new ByteArrayInputStream(p_baStream);
    	ObjectInputStream ois     = new ObjectInputStream(bais);
    	    
    	Object object = ois.readObject();
    
    	return object;	
    }


    /**
     *  Converts the specified array of objects into a string representation. 
     * 
     *  @param p_objaArgs an object array to be handled.
     * 
     *  @return a string representaiton of the given object array.
     */
    public static String toStringArgs(Object[] p_objaArgs)
    {
    	String strReturn = new String();
    	
    	if ( (p_objaArgs == null) || (p_objaArgs.length == 0) ) {
    	    strReturn = "( )";
    	    return strReturn;
    	}
    	
    	strReturn = "( ";
    	
    	int i;
    	for (i=0; i<p_objaArgs.length-1; i++) {
    	    strReturn += p_objaArgs[i].toString() + ", ";
    	}
    	
    	strReturn += p_objaArgs[i] + " )";
    	
    	return strReturn;
    }
}
