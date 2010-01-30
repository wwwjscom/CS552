
package aa;

import aa.gui.View;
import aa.core.Platform;

/**
 *  This class starts an AAA (Adaptive Actor Architecture) platform.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.5
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.4
 *      <ul>
 *          <li> modify how to display the version number and date.
 *      </ul>
 *      <li> June 18, 2003 - version 0.0.3
 * 	<ul>
 * 	    <li> modify code for printing the usage
 *      </ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> March 5, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.5$
 */

public class Start 
{
    // ========== Public Constants ========== //
    
    /**
     *  Specifies the version of this platform.
     */
    public final static String AA_VERSION = new String("Version 0.1.3");
    
    /**
     *  Specifies the date of this version.
     */
    public final static String AA_DATE    = new String("March 9, 2004");
	
	
    // ========== Object Variables ========== //
    
    /** 
     *  Specifies whether this platform shows the graphic user interface. 
     */
    private boolean m_bFlagGUI = true;	
    
    /**
     *  Specifies the port number of a server socket for in-coming messages.
     */
    private Integer m_intPort = new Integer(8070);


    // ========== Object Methods ========== //

    /**
     *  Invoked when this program is executed. 
     * 
     *  @param args string arguments given on the command mode.
     */
    public static void main(String[] args) 
    {
    	new Start(args);
    }
    
    
    /**
     *  Creates a multi-actor paltform.  
     * 
     *  @param p_straArgs string arguments given on the command mode.
     */
   public Start(String[] p_straArgs)
   {
   	View vMain = null;		//  GUI of this platform
	Platform pPatform = null;	//  this platform

	//
	//  process the given arguments.
	//   	
    	boolean bExitFlag = procArgs(p_straArgs);
    	
    	if (bExitFlag) {
    	    return;
    	}

    	//
    	//  create a user interface window.
    	//
    	if (m_bFlagGUI) {
    	    vMain = new View(m_intPort);
    	}
    	
    	//
    	//  create a platform.
    	//
	pPatform = new Platform(vMain, m_intPort);
	pPatform.start();	
    }


    /**
     *  Displays the version of this program.
     */
    private void displayVersion()
    {
    	System.out.println();
        System.out.println("=== AA (Actor Architecture) ===");
//        System.out.println("=== AAA (Adaptive Actor Architecture) ===");
        System.out.println("    - " + Start.AA_VERSION + " - " + Start.AA_DATE + " -");
    	System.out.println();
    }


    /**
     *  Displays the usage of this program.
     */
    private void displayUsage()
    {
    	displayVersion();
    	System.out.println("% Usage: java aa.Start [options]");
//    	System.out.println("% Usage: java aaa.Start [options]");
    	System.out.println("    where 'options' are one or more of the followings:");
    	System.out.println("        -gui        create and show the GUI window");
    	System.out.println("        -nogui      do not create the GUI window");
    	System.out.println("        -h");
    	System.out.println("        -help       print out usage information");
    	System.out.println("        -p <number>");
    	System.out.println("        -port <number> \n" +
    	                   "                    identify the port number for inter-platform communication");
    	System.out.println("        -v");
    	System.out.println("        -version    print the version number of this MAA platform");
    	System.out.println();
    }
    
    
    /**
     *  Processes input arguments.
     * 
     *  Arguments
     *  ---------
     *  -gui
     *  -h
     *  -help
     *  -nogui
     *  -port <number>
     *  -v
     *  -version
     * 
     *  @param p_straArgs string arguments given on the command mode.
     * 
     *  @return true if the platform should exit;
     *          false otherwise.
     */
    private boolean procArgs(String[] p_straArgs)
    {
    	if ( (p_straArgs != null) && (p_straArgs.length != 0) ) {
    	    for (int i=0; i<p_straArgs.length; i++) {
    	    	String strArg = p_straArgs[i];
    	    	
    	    	if (strArg.equalsIgnoreCase("-gui")) {
    	    	    m_bFlagGUI = true;
    	    	} else if (strArg.equalsIgnoreCase("-nogui")) {
    	    	    m_bFlagGUI = false;
    	    	} else if ( (strArg.equalsIgnoreCase("-h")) ||
    	    		     (strArg.equalsIgnoreCase("-help")) ) {
    	    	    displayUsage();
    	    	    return(true);
    	        } else if ( (strArg.equalsIgnoreCase("-p")) ||
    	        	     (strArg.equalsIgnoreCase("-port")) ) {
    	            try {
    	                m_intPort = new Integer(p_straArgs[++i]);
    	            } catch (NumberFormatException e) { 
    	            	displayUsage();
    	            	System.err.println("% illegal port number: " + p_straArgs[i]);
    	            	return(true);
    	            }
    	    	} else if ( (strArg.equalsIgnoreCase("-v")) ||
    	    		     (strArg.equalsIgnoreCase("-version")) ) {
    	    	    displayVersion();
		    return(true);
    	        } else {
    	            displayUsage();
    	            System.err.println("% illegal argument: " + strArg);
    	            return(true);
    	        }
    	    }
    	}
    	
    	return(false);
    }
}
