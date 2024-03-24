package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is a quick demo of how an InputVerifier works.
 * The application has two check boxes,
 * one which is "managed" the other which is not.
 * If the managed check box has focus,
 * you will only be able to change the focus
 * if it is in the same state as the unmanaged check box.
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
        JCheckBox   managed     = new JCheckBox( "Managed" );
        JCheckBox   unmanaged   = new JCheckBox( "Unmanaged" );
        contentPane.add( managed );
        contentPane.add( unmanaged );
        managed.setInputVerifier( new CheckBoxVerifier() );

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
    private static class CheckBoxVerifier extends InputVerifier
    {
        @Override
        public boolean verify( JComponent comp )
        {
            return true;
        }
        
        /**
         * Returns true if it is valid to switch focus
         * from the source component to the target component.
         * If source and target are not both JCheckBoxes,
         * the switch is allowed.
         * If they are both JCheckBoxes,
         * that switch is allowed only
         * if the source and target are in the same state.
         * 
         * @param source    component that currently has focus
         * @param target    candidate component to receive focus
         * 
         * @return true if the source component may surrender focus
         */
        @Override
        public boolean 
        shouldYieldFocus(JComponent source, JComponent target)
        {
            boolean result  = true;
            if ( 
                (source instanceof JCheckBox) 
                && (target instanceof JCheckBox)
            )
            {
                boolean sourceState = ((JCheckBox)source).isSelected();
                boolean targetState = ((JCheckBox)target).isSelected();
                result = sourceState == targetState;
            }
            return result;
        }
    }
}
