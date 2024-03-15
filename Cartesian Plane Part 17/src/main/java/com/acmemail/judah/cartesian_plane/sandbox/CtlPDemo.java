package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class CtlPDemo
{
    private static final int    keyCodeP    = KeyEvent.VK_P;
    private static final String endl        = System.lineSeparator();
    private static final char   pii         = '\u03c0';
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new CtlPDemo().buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame   = new JFrame( "PI Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane    = new JPanel( new BorderLayout() );
        JTextField  field   = new JTextField( 10 );
        field.addKeyListener( new PIListener() );
        pane.add( field, BorderLayout.NORTH );
        
        JPanel      buttons = new JPanel();
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );
        buttons.add( exit );
        pane.add( buttons, BorderLayout.SOUTH );
        
        JButton     print   = new JButton( "Print" );
        print.addActionListener( e -> {
            String  text    = field.getText();
            text += " " + text.length();
            System.out.println( text );
        });
        buttons.add( print );
        pane.add( buttons, BorderLayout.SOUTH );

        frame.setContentPane( pane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
    
    private class PIListener extends KeyAdapter
    {
        @Override 
        public void keyPressed( KeyEvent evt )
        {
            int     mods        = evt.getModifiersEx();
            int     keyCode     = evt.getKeyCode();
            if ( mods == KeyEvent.CTRL_DOWN_MASK && keyCode == keyCodeP )
            {
                JTextField  field   = (JTextField)evt.getSource();
                String      text    = field.getText();
                int         caret   = field.getCaretPosition();
                int         len     = text.length();
                int         backPos = caret - 2;
                String      prev2   = "\"\"";
                if ( caret > 1 )
                    prev2 = text.substring( backPos, caret );
                StringBuilder   bldr    = new StringBuilder();
                bldr.append( "Text: " ).append( text ).append( endl )
                    .append( "Length: " ).append( len ).append( endl )
                    .append( "Caret: " ).append( caret ).append( endl )
                    .append( "Prev 2: " ).append( prev2 ).append( endl )
                    .append( "*****************" );
                System.out.println( bldr );
                if ( prev2.toLowerCase().equals( "pi" ) )
                {
                    String  newText =
                        text.substring( 0, backPos ) 
                        + pii 
                        + text.substring( caret );
                    field.setText( newText );
                }
            }
        }
    }
}
