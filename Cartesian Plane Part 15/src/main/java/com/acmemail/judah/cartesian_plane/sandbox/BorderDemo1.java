package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Application to examine the appearance
 * of miscellaneous borders types.
 *  
 * @author Jack Straub
 */
public class BorderDemo1
{
    /** Initial x-coordinate for positioning frames. */
    private static final int    locX    = 300;
    /** Initial y-coordinate for positioning frames. */
    private static final int    locY    = 300;
    
    /** 
     * List of all frames that have been created but not disposed.
     * @see #finishAndShow(String, JPanel...)
     * @see #closeAll()
     */
    private List<JFrame>    allFrames   = new ArrayList<>();
    /** 
     * Main panel containing the GUI for initiating demos.
     * Declared here for the convenience of GUI initialization.
     */
    private JPanel          demoPanel;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new BorderDemo1().makeGUI() );
    }
    
    /**
     * Constructs the GUI.
     * Must be called on the EDT.
     */
    private void makeGUI()
    {
        JFrame  frame       = new JFrame( "Border Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        demoPanel   = new JPanel( new GridLayout( 3, 3, 5, 5 ) );
        
        getDemoButton( "Show Empty Border", () -> emptyBorderDemo() );
        getDemoButton( "Show Bevel Border (1)", () -> bevelBorderDemo1() );
        getDemoButton( "Show Bevel Border (2)", () -> bevelBorderDemo2() );
        getDemoButton( "Show Etched Border", () -> etchedBorderDemo() );
        getDemoButton( "Show Titled Border (1)", () -> titledBorderDemo1() );
        getDemoButton( "Show Titled Border (2)", () -> titledBorderDemo2() );
        getDemoButton( "Show Line Border", () -> lineBorderDemo() );
        getDemoButton( "Show Compound Border", () -> compoundBorderDemo() );
        
        JButton reserved2   = new JButton();
        reserved2.setEnabled( false );
        demoPanel.add( reserved2 );
        
        contentPane.add( demoPanel, BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( locX - 150, locY - 150 );
        frame.setVisible( true );
    }
    
    /**
     * Creates a JButton
     * with the given text
     * and given action listener.
     * The button is added
     * to <em>demoPanel</em>.
     * 
     * @param text      the given text
     * @param callBack  the given action listener
     */
    private void 
    getDemoButton( String text, Runnable callBack )
    {
        JButton button  = new JButton( text );
        button.addActionListener( e -> {
            closeAll();
            callBack.run();
        });
        demoPanel.add( button );
    }

    /**
     * Displays two frames
     * illustrating the EmptyBorder.
     */
    private void emptyBorderDemo()
    {
        JPanel  panel1  = getMainPanel( "JPanel Without Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Empty Border" );
        Border  border  =
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        panel2.setBorder( border );
        finishAndShow( "Empty Border Demo", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the BevelBorder
     * with default colors.
     */
    private void bevelBorderDemo1()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Raised Bevel Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Lowered Bevel Border" );
        Border  border1 =
            BorderFactory.createRaisedBevelBorder();
        Border  border2 =
            BorderFactory.createLoweredBevelBorder();
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Bevel Border Demo (1)", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the BevelBorder
     * with user-specified colors.
     */
    private void bevelBorderDemo2()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Raised Bevel Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Lowered Bevel Border" );
        Border  border1 =
            BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                Color.RED,
                Color.GREEN
        );
        Border  border2 =
            BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                Color.RED,
                Color.GREEN
        );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Bevel Border Demo (2)", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the EtchedBorder
     * with default colors.
     */
    private void etchedBorderDemo()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Raised Etched Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Lowered Etched Border" );
        Border  border1 =
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  border2 =
            BorderFactory.createEtchedBorder( EtchedBorder.LOWERED );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Etched Border Demo", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the LineBorder.
     */
    private void lineBorderDemo()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Thin Line Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Thick Line Border" );
        Border  border1 =
            BorderFactory.createLineBorder( Color.RED );
        Border  border2 =
            BorderFactory.createLineBorder( Color.RED, 10 );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Line Border Demo", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the TitledBorder
     * with all default property value.
     */
    private void titledBorderDemo1()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Titled Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Line/Titled Border" );
        Border  border1 =
            BorderFactory.createTitledBorder( "Your Title Goes Here" );
        Border  line    = BorderFactory.createLineBorder( Color.RED, 2 );
        Border  border2 =
            BorderFactory.createTitledBorder( line, "Your Title Goes Here" );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Titled Border Demo (1)", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the TitledBorder
     * electing to set some properties
     * with custom values.
     */
    private void titledBorderDemo2()
    {
        JPanel  panel1  = getMainPanel( "JPanel With Bevel/Titled Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Etched/Titled Border" );
        Border  bevel   = BorderFactory.createRaisedBevelBorder();
        Border  border1 =
            BorderFactory.createTitledBorder( 
                bevel,
                "Your Title Goes Here",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.ABOVE_BOTTOM
            );
        Border  etched  =
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  border2 =
            BorderFactory.createTitledBorder( 
                etched, 
                "Your Title Goes Here",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.BOTTOM
            );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        finishAndShow( "Titled Border Demo (2)", panel1, panel2 );
    }

    /**
     * Displays two frames
     * illustrating the CompoundBorder.
     */
    private void compoundBorderDemo()
    {
        JPanel  panel1  = 
            getMainPanel( "JPanel With Titled Border Inside Etched" );
        JPanel  panel2  = 
            getMainPanel( "JPanel With Titled Border Outside Etched" );
        Border  etched  = BorderFactory.createEtchedBorder();
        Border  titled  = 
            BorderFactory.createTitledBorder( "Your Title Goes Here" );
        Border  border1 =
            BorderFactory.createCompoundBorder( etched, titled );
        Border  border2 =
            BorderFactory.createCompoundBorder( titled, etched );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );        
        finishAndShow( "Compound Border Demo", panel1, panel2 );
    }
    
    /**
     * Composes a panel
     * from a template
     * containing two buttons
     * and a given caption.
     * 
     * @param caption   the given caption
     *    
     * @return  the composed panel
     */
    private JPanel getMainPanel( String caption )
    {
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        JPanel  centerPanel = new JPanel();
        centerPanel.add( new JButton( "Pause Reality" ) );
        centerPanel.add( new JButton( "Resume Reality" ) );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        
        JLabel  label   = new JLabel( caption );
        label.setHorizontalTextPosition( SwingUtilities.CENTER );
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setBorder( BorderFactory.createLineBorder( Color.BLACK));
        mainPanel.add( new JLabel( caption ), BorderLayout.SOUTH );
        
        return mainPanel;
    }
    
    /**
     * Gets a panel 
     * containing the exit button.
     * The button is configured
     * with and action listener
     * that calls System.exit.
     * 
     * @return  a panel containing the exit button
     */
    private JPanel getButtonPanel()
    {
        Border  border  = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        JPanel  panel   = new JPanel();
        panel.setBorder(border);
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( exit );
        return panel;
    }
    
    /**
     * Executes the boilerplate
     * for configuring and displaying
     * a demonstration panel.
     * The user passes a title
     * and all the JPanels
     * to be configured and displayed
     * for a given demo.
     * (The panels have presumably
     * been initialized
     * via {@linkplain #getMainPanel(String)}
     * and been decorated
     * with borders.)
     * For each panel
     * a JFrame and content pane,
     * and the panel 
     * is added to the content pane.
     * Each frame is positioned horizontally
     * beginning at {@linkplain #locX},
     * packed and made visible.
     * 
     * @param title     the given title
     * @param panels    the JPanels to be displayed
     */
    private void finishAndShow( String title, JPanel... panels )
    {
        int nextX   = locX;
        for ( int inx = 0 ; inx < panels.length ; ++inx )
        {
            // The main panel goes inside another JPanel with an empty
            // border; this makes the border around the main panel clearer.
            Border  empty           = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
            JPanel  contentPane     = new JPanel();
            contentPane.setBackground( Color.DARK_GRAY );
            contentPane.setBorder( empty );
            contentPane.add( panels[inx] );
            
            JFrame  frame   = new JFrame( title );
            allFrames.add( frame );
            frame.setContentPane( contentPane );
            frame.pack();
            frame.setLocation( nextX, locY );
            frame.setVisible( true );
            
            nextX += frame.getWidth() + 10;
        }
    }
    
    /**
     * Closes and disposes
     * all frames
     * in the {@linkplain #allFrames} list.
     */
    private void closeAll()
    {
       allFrames.stream()
           .peek( f -> f.setVisible( false ) )
           .forEach( JFrame::dispose );
       allFrames.clear();
    }
}
