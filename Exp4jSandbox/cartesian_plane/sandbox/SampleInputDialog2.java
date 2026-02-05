package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * This dialog does nothing
 * except draw a picture
 * of what an input dialog
 * for entering an expression
 * would look like.
 * 
 * @author Jack Straub
 */
public class SampleInputDialog2
{
    private final JDialog   jDialog     = new JDialog();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SampleInputDialog2  app     = new SampleInputDialog2();
        app.jDialog.setVisible( true );
        System.exit( 0 );
    }
    
    public SampleInputDialog2()
    {
        jDialog.setTitle( "Expression Manager" );
        jDialog.setModal( true );
        
        Container   contentPane = jDialog.getContentPane();
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( getIntroduction(), BorderLayout.NORTH );
        contentPane.add( getInput(), BorderLayout.CENTER );
        contentPane.add( getButtons(), BorderLayout.SOUTH );
        jDialog.pack();
    }
    
    private JComponent getIntroduction()
    {
        String  introduction    = 
            "<html>" +
                "Manage an expression to control the " +
                "x- or y- coordinate of a point<br>" +
                "on the Cartesian plane." +
                "The major operations are:" +
                "<ul>" +
                    "<li>Declare the variables in an expression;</li>" +
                    "<li>Enter an expression to be evaluated;</li>" +
                    "<li>Set the iteration range for plotting an equation;</li>" +
                    "<li>Plot the equation." +
                    "<ul>" +
                "<p style=\"text-align: left;\">" +
                "For addittional information see " +
                "<a href=\"https:Overview.html\">Overview</a" +
                "</p>" +
            "</html>";
        
        JPanel  panel   = new JPanel();
        JLabel  label   = new JLabel( introduction, SwingConstants.LEFT );
        Border  border  = BorderFactory.createEmptyBorder( 10, 5, 0, 5 );
        panel.setBorder(border);
        panel.add( label );
        
        return panel;
    }
    
    private JComponent getInput()
    {
        JTextField  textField   = new JTextField( 30 );
        JPanel      panel       = new JPanel();
        Border  border  = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        panel.setBorder( border );
        panel.add( textField );
        return panel;
    }
    
    private JComponent getButtons()
    {
        String[]    labels  =
        {
            "new",
            "x=",
            "y=",
            "set",
            "plot y",
            "plot xy",
            "load",
            "save"
        };
        
        JPanel  panel   = new JPanel( new GridLayout( 3, 3, 2, 2 ) );
        Border  border  = 
            BorderFactory.createEmptyBorder( 5, 20, 10, 20 );
        panel.setBorder( border );
        Arrays.stream( labels )
            .map( JButton::new )
            .forEach( panel::add );
        JButton done    = new JButton( "Done" );
        done.addActionListener( e -> jDialog.setVisible( false ) );
        panel.add( done );
        return panel;
    }
}
