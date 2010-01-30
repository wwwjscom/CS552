package app.quickstart.sum;

import aa.core.Actor;

/**
 *  This class creates the Add actor to perform the operation for "2+3=5".
 * 
 *  <p>
 *  <b>History</b>
 *  <ul>
 *      <li> February 28, 2004
 * 	<ul>
 *          <li> create this file.
 * 	</ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang and Abha Gupta
 *  @version $Date: 2004/02/28$ $Revision: 0.0.1$
 */

public class Add extends Actor
{
    // ========== Object Methods ========== //

    private int m_iNum1 = 2;
    private int m_iNum2 = 3;

    
    // ========== Object Methods ========== //

    /**
     *  Adds two numbers (2 and 3) and prints the results.
     */
    public void addNumber() 
    {
    	int iSum = m_iNum1 + m_iNum2;
    	System.out.println(m_iNum1 + " + " + m_iNum2 + " = " + iSum);
    }
}
