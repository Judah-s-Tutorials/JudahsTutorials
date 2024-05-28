package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.CPFrame;
import com.acmemail.judah.cartesian_plane.components.NamePanel;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Simple program to display an object
 * of the NamePanel class.
 * 
 * @author Jack Straub
 */
public class ShowNamePanel
{
    /** Convenient declaration of PropertyManager singleton. */
    private static final PropertyManager    pmgr    =
        PropertyManager.INSTANCE;
    private static final NamePanel          nPanel  = new NamePanel();
    /** Current equation */
    private static Equation     equation    = null;
    /** Counter useful for constructing unique equation name. */
    private static int          counter     = 0;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    /**
     * Creates and displays the main application frame.
     */
    public static void buildGUI()
    {
        JFrame  frame   = new JFrame( "Show Name Panel" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  placeHolder = new JPanel();
        placeHolder.setPreferredSize( new Dimension( 200, 300 ));
        placeHolder.setBackground( Color.ORANGE );
        contentPane.add( placeHolder, BorderLayout.CENTER );
        

        contentPane.add( getLeftPanel(), BorderLayout.WEST );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        PropertyManager.INSTANCE.setProperty(
            CPConstants.DM_OPEN_EQUATION_PN, true
        );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Creates a panel to be used as the parent content pane's
     * west child.
     * 
     * @return
     *      panel to be used as the parent content pane's
     *      west child
     *      
     * @see CPFrame
     */
    private static JPanel getLeftPanel()
    {
        JPanel      leftPanel   = new JPanel();
        BoxLayout   boxLayout   = 
            new BoxLayout( leftPanel, BoxLayout.Y_AXIS );
        leftPanel.setLayout( boxLayout );
        leftPanel.add( nPanel );
        
        // "filler" is to simulate a West panel in a BorderLayout
        // where the NamePanel is followed by additional components.
        Dimension   dim         = nPanel.getPreferredSize();
        dim.height = 50;
        JPanel      filler      = new JPanel();
        filler.setPreferredSize( dim );
        filler.setBackground( Color.DARK_GRAY );
        leftPanel.add( filler );
        
        // At this point leftPanel's height is going to be stretched,
        // and the BoxLayout will distribute the extra space among
        // all its components, causing the height of the text field
        // to increase alarmingly. To fix this, put leftPanel inside
        // of another JPanel with a FlowLayout then return the outer
        // JPanel.
        JPanel      outer   = new JPanel();
        outer.setBackground( Color.YELLOW );
        outer.add( leftPanel );
        return outer;
    }
    
    /**
     * Gets a panel containing the GUI's control buttons.
     * 
     * @return  a panel containing the GUI's control buttons
     */
    private static JPanel getButtonPanel()
    {
        JPanel      buttonPanel = new JPanel();

        JButton newEquation     = new JButton( "New" );
        buttonPanel.add( newEquation );
        newEquation.addActionListener( e-> nPanel.load( newEq() ) );
        
        JButton nullEquation    = new JButton( "Close" );
        buttonPanel.add( nullEquation );
        nullEquation.addActionListener( e-> nPanel.load( closeEq() ) );
        
        JButton exit            = new JButton( "Exit" );
        buttonPanel.add( exit );
        exit.addActionListener( e-> System.exit( 0 ) );
        
        return buttonPanel;
    }

    /**
     * Invoked when the GUIs's Close button is pushed.
     * Simulates an equation being closed;
     * configures the application's DM_... properties,
     * and sets the currently open equation to null.
     * 
     * @return null
     */
    private static Equation closeEq()
    {
        equation = null;
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        return equation;
    }

    /**
     * Instantiates a new equation
     * with a unique name.
     * Simulates an equation being opened;
     * configures the application's DM_... properties,
     * and sets the currently open equation 
     * to the newly instantiated equation.
     * 
     * @return  the newly instantiated equation
     */
    private static Equation newEq()
    {
        equation = new Exp4jEquation();
        equation.setName( "Name... " + ++counter );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        return equation;
    }
}
