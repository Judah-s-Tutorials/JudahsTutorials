package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Simple program to display an object
 * of the PlotPanel class.
 * The panel is set up with an equation
 * in which variables x, y, a, b, and c
 * have been declared
 * (i.e., you can experiment with expressions
 * using these variable names).
 * There options to start a new equation
 * or close the current equation.
 * 
 * @author Jack Straub
 */
public class ShowPlotPanel
{
    /** Activity log. */
    private static ActivityLog  log;
    
    private static JFrame   frame;
    
    /** Currently open equation; may be null */
    private static Equation equation    = null;
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
        frame   = new JFrame( "Show Variable Panel" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  placeHolder = new JPanel();
        placeHolder.setPreferredSize( new Dimension( 200, 100 ));
        placeHolder.setBackground( Color.ORANGE );
        contentPane.add( placeHolder, BorderLayout.CENTER );
        
        PlotPanel        pPanel      = new PlotPanel();
        contentPane.add( pPanel, BorderLayout.SOUTH );
        
        JPanel      leftPanel   = new JPanel();
        JPanel      buttonPanel = new JPanel( new GridLayout( 4, 1 ) );
        leftPanel.add( buttonPanel );
        
        JButton newEquation     = new JButton( "New" );
        buttonPanel.add( newEquation );
        newEquation.addActionListener( e-> pPanel.load( newEquation() ) );
        
        JButton nullEquation    = new JButton( "Close" );
        buttonPanel.add( nullEquation );
        nullEquation.addActionListener( e -> {
            equation = null;
            pPanel.load( null );
        });
        
        JButton print           = new JButton( "Print" );
        buttonPanel.add( print );
        print.setMnemonic( 'P' );
        print.addActionListener( e -> printAction() );
        
        JButton exit            = new JButton( "Exit" );
        buttonPanel.add( exit );
        exit.addActionListener( e -> System.exit( 1 ) );
        
        contentPane.add( leftPanel, BorderLayout.WEST );
        
        PropertyManager.INSTANCE.setProperty(
            CPConstants.DM_OPEN_EQUATION_PN, true
        );
        
        frame.setLocation( 100, 100 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        log = new ActivityLog( frame );
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 110 + frameSize.width, 100 );
        installKeyListener();
    }

    private static Equation newEquation()
    {
        equation = new Exp4jEquation();
        equation.setVar( "x", 0 );
        equation.setVar( "y", 0 );
        equation.setVar( "a", 0 );
        equation.setVar( "b", 0 );
        equation.setVar( "c", 0 );
        return equation;
    }
    
    private static void printAction()
    {
        if ( equation == null )
            log.append( "(null)" );
        else
        {
            log.append( "x=" + equation.getXExpression() );
            log.append( "y=" + equation.getYExpression() );
            log.append( "t=" + equation.getTExpression() );
            log.append( "r=" + equation.getRExpression() );
        }
        log.append( "********************" );
    }
    
    private static void installKeyListener()
    {
        int         kcP         = KeyEvent.VK_R;
        KeyAdapter  kAdapter    = new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent evt )
            {
                System.out.println( evt.getKeyCode() );
                if ( evt.isAltDown() && evt.getKeyCode() == kcP )
                    printAction();
            }
        };
        
        List<JFormattedTextField>   list    = new ArrayList<>();
        recurse( frame.getContentPane(), list );
        System.out.println( list.size() );
        
        list.stream()
            .peek( f -> f.setToolTipText( "alt-R to print" ) )
            .forEach( f -> f.addKeyListener( kAdapter ) );
    }
    
    private static void recurse( Container parent, List<JFormattedTextField> list )
    {
        for ( Component child : parent.getComponents() )
        {
            if ( child instanceof JFormattedTextField )
                list.add( (JFormattedTextField)child );
            if ( child instanceof Container )
                recurse( (Container)child, list );
        }
    }
}
