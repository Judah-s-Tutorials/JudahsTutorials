package com.acmemail.judah.sandbox.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.ComponentException;
import com.acmemail.judah.sandbox.ComponentFinder;

public class JColorChooserDemo3
{
    private static final Color  START_COLOR = Color.RED;
    private static final Color  NEW_COLOR   = Color.BLUE;
    private static final String TITLE       = "Color Chooser Demo";
    
    private final ComponentFinder   finder  = 
        new ComponentFinder( true, false, false );
    private JFrame  frame;
    private JPanel  contentPane;
    private JPanel  feedBackPanel;

    private JColorChooser   chooser;
    private JButton         okButton;
    private JButton         cancelButton;
    private Color           colorChoice;
    
    public static void main(String[] args) throws InterruptedException
    {
        new JColorChooserDemo3().execute();
    }
    
    public void execute() throws InterruptedException
    {
        invokeAndWait( this::showFeedback );
        runOnce( Color.BLUE );
        runOnce( Color.YELLOW );
        
        JOptionPane.showMessageDialog( null, "OK to exit" );
        System.exit( 0 );
    }
    
    private void runOnce( Color newColor )
    {
        Thread  thread  = startChooserThread();
        JOptionPane.showMessageDialog(
            null, 
            "OK to change dialog color choice" 
        );
        chooser.setColor( newColor );
        int option = JOptionPane.showOptionDialog(
            null, 
            "Choose color dialog OK or Cancel button",
            "JColorChooser Demo",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[] {"OK", "Cancel"},
            "OK"
        );
        JButton button  = 
            option == 0 ? okButton : cancelButton;
        invokeAndWait( () -> button.doClick() );
        SandboxUtils.join( thread );
        
        if ( colorChoice != null )
        {
            invokeAndWait( () -> {
                feedBackPanel.setBackground( colorChoice );
                feedBackPanel.repaint();
            });
        }
    }
    
    private void showFeedback()
    {
        feedBackPanel = new JPanel();
        feedBackPanel.setBackground(START_COLOR);
        feedBackPanel.setPreferredSize( new Dimension( 200, 75 ) );
        
        frame = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( feedBackPanel );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void startChooser()
    {
        colorChoice = 
            JColorChooser.showDialog( null, TITLE, START_COLOR );
    }
    
    private Thread startChooserThread() 
    {
        Thread  thread  = new Thread( () -> startChooser() );
        thread.start();
        SandboxUtils.pause( 500 );
        
        chooser = getChooser();
        okButton = getButton( "OK" );
        cancelButton = getButton( "Cancel" );
        
        return thread;
    }
    
    private JColorChooser getChooser()
    {
        JComponent  comp    = 
            finder.find( c -> (c instanceof JColorChooser) );
        if ( comp == null || !(comp instanceof JColorChooser) ) 
            throw new ComponentException( "JColorChooser not found" );
        return (JColorChooser)comp;
    }
    
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( text );
        JComponent  comp    = 
            finder.find( pred );
        if ( comp == null || !(comp instanceof JButton) )
            throw new ComponentException( text + " button not found" );
        return (JButton)comp;
    }
    
    private void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
        } 
        catch ( InterruptedException | InvocationTargetException exc )
        {
            throw new ComponentException( exc );
        }
    }
}
