package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

public class JFileChooserSearch
{
    private static final ComponentFinder finder = new ComponentFinder();
    private static final JFileChooser    chooser = new JFileChooser();
    
    public static void main(String[] args) throws InterruptedException
    {
        printIds( () -> chooser.showSaveDialog( null ) );
        printIds( () -> chooser.showOpenDialog( null ) );
        System.exit( 0 );
    }
    
    private static void printIds( Runnable runner )
        throws InterruptedException
    {
        Thread  thread          = start( runner );
        getJDialog();
        getTextField();
        JButton cancelButton    = getButton( "Cancel" );
        JButton saveButton      = getButton( "Save" );
        JButton openButton      = getButton( "Open" );
        System.out.println( "save == open: " + (saveButton == openButton) );
        System.out.println( "====================" );
        
        Thread.sleep( 3000 );
        if ( cancelButton != null )
            cancelButton.doClick();
        thread.join();
    }
    
    private static JDialog getJDialog()
    {
        Window  window  = finder.findWindow( w -> true );
        String  ident   = null;
        JDialog dialog  = null;
        
        if ( window == null )
            ;
        else if ( !(window instanceof JDialog) )
            ident = "??? " + window.getClass().getName();
        else
        {
            dialog = (JDialog)window;
            ident = Integer.toHexString( window.hashCode() ) + " ";
            ident += window.getClass().getName();
        }
        System.out.println( "JDialog: " + ident );
        return dialog;
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
    
    private static JTextField getTextField()
    {
        JComponent  comp    = finder.find( c -> (c instanceof JTextField) );
        JTextField  text    = null;
        String      ident   = null;
        if ( comp != null && comp instanceof JTextField ) 
        {
            text = (JTextField)comp;
            ident = Integer.toHexString( text.hashCode() );
        }
        System.out.println( "TextField: " + ident );
        return text;
    }
    
    private static Thread start( Runnable funk )
        throws InterruptedException
    {
        Thread          thread  = new Thread( funk );
        thread.start();
        Thread.sleep( 500 );
        
        return thread;
    }
}
