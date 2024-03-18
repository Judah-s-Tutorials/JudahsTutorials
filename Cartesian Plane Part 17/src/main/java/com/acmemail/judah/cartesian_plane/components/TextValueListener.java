package com.acmemail.judah.cartesian_plane.components;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

public class TextValueListener 
    extends KeyAdapter
    implements PropertyChangeListener
{
    public void addThis( JFormattedTextField textField )
    {
        textField.addKeyListener( this );
        textField.addPropertyChangeListener( "value", this );
    }
    
//    @Override
//    public void keyTyped( KeyEvent evt )
//    {
//        Object  src     = evt.getSource();
//        if ( src instanceof JFormattedTextField )
//        {
//            JFormattedTextField  comp    = (JFormattedTextField)src;
//            if ( !comp.getText().equals( comp.getValue() ) )
//            {
//                Font        font    = comp.getFont();
//                font = font.deriveFont( Font.ITALIC );
//                comp.setFont( font );
//            }
//        }
//    }
    
    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        if ( !evt.getPropertyName().equals( "value" ) )
            return;
        Object  src     = evt.getSource();
        if ( src instanceof JFormattedTextField )
        {
            JFormattedTextField  comp    = (JFormattedTextField)src;
            Font        font    = comp.getFont();
            font = font.deriveFont( Font.PLAIN );
            comp.setFont( font );
        }
    }

}
