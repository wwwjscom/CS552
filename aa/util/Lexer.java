
package aa.util;

/**
 *  This class performs tokenizing a input string to separate string tokens.
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
 *      <li> April 7, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.3$
 */

public class Lexer 
{
    // ========== Object Variables ========== //
    
    /**
     *  Maximum number of tokens.
     */
    private static final int m_iMAX_TOKENS = 11;

	
    // ========== Object Methods ========== //

    /**
     *  Removes blanks in the front part of the specified string.
     * 
     *  @param p_strInput an input string
     *  @return the string whose front blanks are removed
     *               if the input string has valid characters; <br>
     *          <code>null</code> otherwise.
     */
    private static String removeBlank(String p_strInput)
    {
    	if ( (p_strInput == null) || (p_strInput.length() == 0) ) {
    	    return null;
    	}
    	
    	int i;
    	for (i=0; ( (i<p_strInput.length()) && (p_strInput.charAt(i) == ' ') ) ; i++) {
    	}
    	
    	if (i == 0) {
    	    return p_strInput;
    	} else if (i < p_strInput.length()) {
    	    return p_strInput.substring(i);
    	} else {
    	    return null;
    	}
    }
    
    
    /**
     *  Returns an array of tokens that does not have null elements.
     * 
     *  @param p_straToken an array of string tokens which has null elements.
     *          p_iTokens the number of no-null string tokens in the <code>p_straToken</code> array.
     *  @return an array of tokens that does not have null elements 
     *               if the input array has valid tokens; <br>
     *          <code>null</code> otherwise.
     */
    private static String[] compactString(String[] p_straToken, int p_iTokens)
    {
    	if (p_iTokens == 0) {
    	    return null; 
    	}
    	
	String[] strReturn = new String[p_iTokens];
	    	
	for (int i=0; i<p_iTokens; i++) {
	    strReturn[i] = p_straToken[i];
	}
	    	
	return strReturn;
    }
     

    /**
     *  Extracts string tokens from the given input string, and returns them.
     *
     *  @param p_strInput the input string.
     * 
     *  @return an array of string tokens if the input has valid tokens; <br>
     *          <code>null</code> otherwise.
     */
    public static String[] getTokens(final String p_strInput)
    {
    	//
    	//  if the specified input string is null
    	//
    	if (p_strInput == null) {
    	    return null;
    	}
    	
    	//
    	//  process the specified input string
    	//
    	int p_iTokens = 0;
    	String[] strToken = new String[m_iMAX_TOKENS];
    	
    	String strLast = new String(p_strInput);
    	
	while (p_iTokens < m_iMAX_TOKENS) {
	    //
	    //  remove blanks in the front part of the string
	    //
	    strLast = removeBlank(strLast);
	    
	    //
	    //  there are no more characters
	    //
	    if (strLast == null) {
	    	return compactString(strToken, p_iTokens);
	    }
	    
	    //
	    //  find a token
	    //
	    int iLast = strLast.indexOf(" ");
	    
	    //
	    //  if this token is the last one
	    //
	    if (iLast == -1) {
	    	strToken[p_iTokens++] = strLast;
	    	return compactString(strToken, p_iTokens);
	    }
	    
	    //
	    //  extract a token from the string, and 
	    //  update the string
	    //
	    strToken[p_iTokens++] = strLast.substring(0, iLast);
	    strLast               = strLast.substring(iLast + 1);
	}
	
	return strToken;
    }
}
