package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

public class JOptionPaneSearch
{
    private static final ComponentFinder    finder  = new ComponentFinder();
    
    public static void main(String[] args) throws InterruptedException
    {
        Thread  thread  = start( () -> showErrorDialog() );
        JButton button  = getButton( "OK" );
        JLabel  label   = getLabel( "error" );
        System.out.println( button );
        Thread.sleep( 2000 );
        
        if ( button != null )
            button.doClick();
        thread.join();
        System.exit( 0 );
    }
        
    private static JButton getButton( String text )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( text );
        String                  ident   = "?????";
        JButton                 button  = null;
        
        JComponent              comp    = finder.find( pred );
        if ( comp == null )
            ident = "null";
        else if ( !(comp instanceof JButton) )
            ident = comp.getClass().getName();
        else 
        {
            button = (JButton)comp;
            ident = Integer.toHexString( comp.hashCode() );
        }
        
        System.out.println( text + ": " + ident );
        return button;
    }
    
    private static JLabel getLabel( String containedText )
    {
        Predicate<JComponent>   notNull     = jc -> jc != null;
        Predicate<JComponent>   isLabel     = jc -> jc instanceof JLabel;
        Predicate<JComponent>   contains    = 
            jc -> contains( containedText, ((JLabel)jc).getText() );
        Predicate<JComponent>   pred        = 
            notNull.and( isLabel ).and( contains );
        JComponent  comp    = finder.find( pred );
        JLabel      label   = null;
        if ( comp != null && comp instanceof JLabel )
            label = (JLabel)comp;
        
        if ( label != null )
            System.out.println( "Label: " + label.getText() );
        return label;
    }
    
    private static boolean contains( String contained, String container )
    {
        boolean result  = false;
        if ( contained != null && container != null )
            result = container.contains( contained );
        return result;
    }
    
    private static Thread start( Runnable funk )
        throws InterruptedException
    {
        Thread          thread  = new Thread( funk );
        thread.start();
        Thread.sleep( 500 );
        
        return thread;
    }
    
    private static void showErrorDialog()
    {
        String  message = "This is an error message";
        String  title   = "Error Dialog Title";
        int     type    = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            type
        );
    }
}
