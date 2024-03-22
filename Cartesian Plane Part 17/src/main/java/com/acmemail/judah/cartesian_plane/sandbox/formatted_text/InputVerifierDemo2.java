package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This is a quick demo of how an InputVerifier works.
 * The application has two fields
 * that may contain only text that begins with 'a'.
 * If a text field with the focus contains invalid data
 * it will not be allowed to surrender focus.
 * 
 * @author Jack Straub
 */
public class InputVerifierDemo2
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new InputVerifierDemo2() );
    }
    
    /**
     * Creates and displays the main application frame.
     */
    public InputVerifierDemo2()
    {
        JFrame  frame   = new JFrame( "Show Variable Panel" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel();
        JTextField  field1      = new JTextField( "a rat in the house" );
        JTextField  field2      = new JTextField( "a clever remark" );
        contentPane.add( field1 );
        contentPane.add( field2 );
        field1.setInputVerifier( new TextFieldVerifier() );
        field2.setInputVerifier( new TextFieldVerifier() );

        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * InputVerifier for the managed check box.
     * 
     * @author Jack Straub
     */
    private static class TextFieldVerifier extends InputVerifier
    {
        @Override
        public boolean verify( JComponent comp )
        {
            return true;
        }
        
        /**
         * Returns true if it is valid 
         * for the source component to surrender the focus.
         * It may give up focus only if its text
         * begins with 'a'.
         * 
         * @param source    component that currently has focus
         * @param target    candidate component to receive focus; not used
         * 
         * @return true if the source component may surrender focus
         */
        @Override
        public boolean 
        shouldYieldFocus(JComponent source, JComponent target)
        {
            boolean result  = true;
            if ( source instanceof JTextField ) 
            {
                String  text    = ((JTextField)source).getText();
                result = text.startsWith( "a" );
            }
            return result;
        }
    }
}
