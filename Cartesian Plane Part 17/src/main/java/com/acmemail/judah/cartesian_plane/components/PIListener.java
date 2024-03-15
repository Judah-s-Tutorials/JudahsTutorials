package com.acmemail.judah.cartesian_plane.components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * KeyListener to listen for PI^P
 * in a text component,
 * then substitutes the Greek letter &Pi;.
 * 
 * @author Jack Straub
 */
public class PIListener extends KeyAdapter
{
    /** The key code for P on the keyboard. */
    private static final int    keyCodeP    = KeyEvent.VK_P;
    /** Unicode for the Greek letter PI */
    private static final char   pii         = '\u03c0';

    /**
     * Listens for the operator to type the P key
     * while holding down the control key.
     * When this occurs the characters "pi"
     * in the text field are replaced
     * with the Greek letter &Pi;.
     */
    @Override 
    public void keyPressed( KeyEvent evt )
    {
        int     keyCode     = evt.getKeyCode();
        Object  src         = evt.getSource();            
        boolean isCtrl      = evt.isControlDown();
        boolean isText      = src instanceof JTextComponent;
        
        if ( isText && isCtrl && keyCode == keyCodeP )
        {
            JTextField  field   = (JTextField)evt.getSource();
            String      text    = field.getText();
            int         caret   = field.getCaretPosition();
            int         backPos = caret - 2;
            String      prev2   = "";
            if ( caret > 1 )
            {
                prev2 = text.substring( backPos, caret );    
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
