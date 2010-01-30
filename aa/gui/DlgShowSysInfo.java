
package aa.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *  This class creates a dialog to show system information.
 * 
 *  <p>
 *  <b>History:</b>
 *  <ul>
 * 	<li> February 19, 2004 - version 0.0.4
 * 	<ul>
 * 	    <li> do minor modification.
 * 	</ul>
 *      <li> November 25, 2003 - version 0.0.3
 *      <ul>
 *          <li> change the format of dialog box.
 *      </ul>
 *      <li> May 9, 2003 - version 0.0.2
 *      <ul>
 *          <li> modify internal comments.
 *      </ul>
 *      <li> April 18, 2001 - version 0.0.1
 *      <ul>
 *          <li> create this file.
 *      </ul>
 *  </ul>
 *
 *  @author Myeong-Wuk Jang
 *  @version $Date: 2004/02/19$ $Revision: 0.0.4$
 */

public class DlgShowSysInfo extends JDialog implements ActionListener
{
    // ========== Object Contants ========== //

    private final static String[][] m_strProperties = {
	{ "java.version", 			"Java Runtime Environment version" },
	{ "java.vendor", 			"Java Runtime Environment vendor" },
	{ "java.vendor.url", 			"Java vendor URL" },
	{ "java.home", 				"Java installation directory" },
	{ "java.vm.specification.version",	"Java Virtual Machine specification version" },
	{ "java.vm.specification.vendor", 	"Java Virtual Machine specification vendor" },
	{ "java.vm.specification.name", 	"Java Virtual Machine specification name" },
	{ "java.vm.version", 			"Java Virtual Machine implementation version" },
	{ "java.vm.vendor", 			"Java Virtual Machine implementation vendor" },
	{ "java.vm.name", 			"Java Virtual Machine implementation name" },
	{ "java.specification.version", 	"Java Runtime Environment specification version" },
	{ "java.specification.vendor", 		"Java Runtime Environment specification vendor" },
	{ "java.specification.name", 		"Java Runtime Environment specification name" },
	{ "java.class.version", 		"Java class format version number" },
	{ "java.class.path", 			"Java class path" },
	{ "java.library.path", 			"List of paths to search when loading libraries" },
	{ "java.io.tmpdir", 			"Default temp file path" },
	{ "java.compiler", 			"Name of JIT compiler to use" },
	{ "java.ext.dirs", 			"Path of extension directory or directories" },
	{ "os.name", 				"Operating system name" },
	{ "os.arch", 				"Operating system architecture" },
	{ "os.version", 			"Operating system version" },
	{ "file.separator", 			"File separator ('/' on UNIX)" },
	{ "path.separator", 			"Path separator (':' on UNIX)" },
	{ "line.separator", 			"Line separator ('\\n' on UNIX)" },
	{ "user.name", 				"User's account name" },
	{ "user.home", 				"User's home directory" },
	{ "user.dir", 				"User's current working directory" }
    };

    
    // ========== Object Variables ========== //

    private JButton m_jbOk;		//  Ok button
	
	
    // ========== Object Methods ========== //

    /**
     *  Creates a dialog box to show the system information of this platform.
     */
    public DlgShowSysInfo() 
    {
    	super((Frame)null, "System Information", true);
    	
	//
	//  create a dialog box.
	//
    	createPane();  
    	  	
    	setSize(500, 600);

    	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dimWindow = getSize();
     	setLocation(dimScreen.width/2 - dimWindow.width/2, 
     		    dimScreen.height/2 - dimWindow.height/2);  	

   	setResizable(false);
    }
    
    
    /**
     *  Creates the main pane of this dialog box.
     */
    private void createPane()
    {
    	//
    	//  create information items.
    	//
    	JLabel jlaItem[] = new JLabel[m_strProperties.length];
    	
    	for (int i=0; i<m_strProperties.length; i++) {
    	    jlaItem[i] = new JLabel(m_strProperties[i][1] + ": " + System.getProperty(m_strProperties[i][0]));
    	}
    
  	JPanel jpInfo = new JPanel(new GridLayout(m_strProperties.length, 0));
 	jpInfo.setBorder(new EmptyBorder(20, 30, 0, 30));	//  (top, left, bottom, right)
 	
 	for (int i=0; i<m_strProperties.length; i++) {
 	    jpInfo.add(jlaItem[i]);
 	}

  	//
  	//  create the 'OK' button panel.
  	//
    	m_jbOk = new JButton("OK");
    	m_jbOk.addActionListener(this);
    	
	JPanel jpButtons = new JPanel(new BorderLayout());
	jpButtons.setBorder(new EmptyBorder(15, 215, 15, 215));
	jpButtons.add(m_jbOk, BorderLayout.CENTER);

	//
	//  create the main content pane.
	//
    	Container container = getContentPane();
	container.setLayout(new BorderLayout());
	container.add(jpInfo, BorderLayout.CENTER);
	container.add(jpButtons, BorderLayout.SOUTH);	
	
	getRootPane().setDefaultButton(m_jbOk);
    }
    
    
    /**
     *  Invoked when an action occurs. 
     * 
     *  @param p_ae an action event to be invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent p_ae)
    {
	Object objSource = p_ae.getSource();

	if (objSource == m_jbOk) {
	    dispose();
	}
    }
}
