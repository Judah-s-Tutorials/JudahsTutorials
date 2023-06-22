package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class JFileChooserProblem3
{
    private static JFileChooser chooser     = new JFileChooser();
    private static JComponent   contentPane = null;
    private static JTextField   textField   = null;
    private static JButton      openButton  = null;
    
    private static final String tempDirStr  = 
        "C:\\Users\\johns\\AppData\\Local\\Temp\\";
    private static final File   tempDir     = new File( tempDirStr );

    
    public static void main(String[] args)
        throws InterruptedException
    {
        Predicate<JComponent>   isJTextField    = 
            c -> (c instanceof JTextField);
        Predicate<JComponent>   isOpenButton    = 
            c -> (c instanceof JButton) &&
                 ("Open".equals( ((JButton)c).getText() ) );
        
        chooser.setCurrentDirectory( tempDir );
        Thread  thread  = startChooser();
        contentPane = getContentPane();
        System.out.println( find( contentPane, c -> (c instanceof JFileChooser) ) );
        textField = (JTextField)find( contentPane, isJTextField );
        openButton = (JButton)find( contentPane, isOpenButton );
        textField.setText( "C:\\\\Users\\\\johns\\\\AppData\\\\Local\\\\Temp\\\\FileManagerTest.tmp" );
//        textField.setText( "FileManagerTest.tmp" );
//        textField.setText( "\\Users\\johns\\test.txt" );
//        openButton.setText( "O" );
        Thread.sleep( 2000 );
        openButton.doClick();
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
