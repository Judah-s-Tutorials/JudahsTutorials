package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import javax.swing.InputVerifier;
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
public class InputVerifierDemo1
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new InputVerifierDemo1() );
    }
    
    /**
     * Creates and displays the main application frame.
     */
    public InputVerifierDemo1()
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
     * InputVerifier for.
     * Returns true if the given component
     * is in a valid state.
     * 
     * @param   comp    the given component
     * 
     * @return  true if the given component is in a valid state
     * 
     * @author Jack Straub
     */
    private static class TextFieldVerifier extends InputVerifier
    {
        @Override
        public boolean verify( JComponent comp )
        {
            boolean result  = true;
            if ( comp instanceof JTextField ) 
            {
                String  text    = ((JTextField)comp).getText();
                result = text.startsWith( "a" );
            }
            return result;
        }
    }
}
