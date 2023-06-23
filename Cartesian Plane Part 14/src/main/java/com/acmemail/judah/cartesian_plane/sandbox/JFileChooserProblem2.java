package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Component;
import java.awt.Window;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JFileChooserProblem2
{
    private static JFileChooser chooser     = new JFileChooser();
    private static JComponent   contentPane = null;
    private static JTextField   textField   = null;
    private static JButton      openButton  = null;
    
    public static void main(String[] args)
        throws InterruptedException
    {
        Predicate<JComponent>   isJTextField    = 
            c -> (c instanceof JTextField);
        Predicate<JComponent>   isOpenButton    = 
            c -> (c instanceof JButton) &&
                 ("Open".equals( ((JButton)c).getText() ) );
        
        Thread  thread  = startChooser();
        contentPane = getContentPane();
        textField = (JTextField)find( contentPane, isJTextField );
        openButton = (JButton)find( contentPane, isOpenButton );
        textField.setText( "\\Users\\johns\\test.txt" );
//        openButton.setText( "O" );
        Thread.sleep( 2000 );
        SwingUtilities.invokeLater( () -> openButton.doClick() );
        thread.join();
        
        System.out.println( chooser.getSelectedFile().getName() );
    }
    
    private static Thread startChooser()
        throws InterruptedException
    {
        Thread  thread  = new Thread( () -> chooser.showOpenDialog( null ) );
        thread.start();
        Thread.sleep( 500 );
        return thread;
    }
    
    private static JComponent 
    find( JComponent container, Predicate<JComponent> pred )
    {
        Predicate<Component>    isJComponent    =
            c -> (c instanceof JComponent);
        JComponent  comp    = null;
        if ( pred.test( container ) )
            comp = container;
        else
            comp = Arrays.stream( container.getComponents() )
                .filter( isJComponent )
                .map( c -> find( (JComponent)c, pred ) )
                .filter( c -> c != null )
                .findFirst()
                .orElse( null );
        
        return comp;
    }

    private static JComponent getContentPane()
    {
        JComponent contentPane =
            Arrays.stream( Window.getWindows() )
                .filter( w -> (w instanceof JDialog) )
                .map( w -> ((JDialog)w).getContentPane() )
                .filter( c -> (c instanceof JComponent) )
                .map( c -> (JComponent)c )
                .findFirst()
                .orElse( null );
        return contentPane;
    }
}
