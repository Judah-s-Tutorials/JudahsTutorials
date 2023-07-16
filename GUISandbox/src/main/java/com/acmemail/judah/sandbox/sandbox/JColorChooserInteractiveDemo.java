package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.ComponentException;
import com.acmemail.judah.sandbox.ComponentFinder;

/**
 * This is an application
 * that demonstrates how
 * a unit test method
 * can interact
 * with a JColorChooser.
 * The application displays a feedback window
 * of a designated. 
 * Next it starts
 * a JColorChooser dialog
 * in a dedicated thread.
 * Then the main thread
 * posts the first of two
 * JOptionPane dialogs
 * intended for operator use
 * (the operator is the person
 * who starts this application,
 * probably you).
 * <p>
 * When you dismiss the first dialog
 * by selecting its OK button
 * the application will programmatically select
 * a new color in the JColorChooser.
 * The application then posts
 * the second dialog.
 * </p>
 * <p>
 * The second dialog
 * offers the operator
 * the choice of OK and Cancel.
 * If you choose the OK button
 * the JColorChooser dialog
 * will be dismissed
 * by programmatically selecting
 * the JColorChooser's OK button,
 * resulting in the feedback window
 * changing to the designated color. 
 * If you select the cancel button
 * the JColorChooser dialog
 * will be dismissed 
 * by programmatically selecting
 * the dialog's Cancel button,
 * in which case
 * the color of the feedback window
 * should <em>not</em> change.
 * </p>
 * <p>
 * The process is executed 
 * a second time
 * with a third color,
 * allowing you to experiment
 * with dismissing the JColorChooser dialog
 * with both the OK and Cancel buttons.
 * </p>
 * @author Jack Straub
 */
public class JColorChooserInteractiveDemo
{
    /** The initial color of the feedback window. */
    private static final Color  START_COLOR = Color.RED;
    /** 
     * The color to be selected in the chooser dialog
     * for the first test.
     */
    private static final Color  NEW_COLOR1  = Color.BLUE;
    /** 
     * The color to be selected in the chooser dialog
     * for the second test.
     */
    private static final Color  NEW_COLOR2  = Color.YELLOW;
    /** Title to display on the posted dialogs. */
    private static final String TITLE       = "Color Chooser Demo";
    
    /** Used to locate components in the JColorChooser dialog. */
    private final ComponentFinder   finder  = 
        new ComponentFinder( true, false, false );
    /** The panel for use in the feedback window. */
    private JPanel  feedbackPanel;

    /**
     *  The JColorChooser component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startChooserThread()} method.
     */
    private JColorChooser   chooser;
    /**
     *  The OK button component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startChooserThread()} method.
     */
    private JButton         okButton;
    /**
     *  The Cancel button component of the 
     *  JColorChooser dialog. Set by the 
     *  {@linkplain #startChooserThread()} method.
     */
    private JButton         cancelButton;
    /**
     *  The value returned by the JColorChooser dialog
     *  when it is dismissed Set by the 
     *  {@linkplain #startChooser()} method.
     */
    private Color           colorChoice;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        new JColorChooserInteractiveDemo().execute();
    }
    
    /**
     * Principal method for executing the application.
     */
    public void execute()
    {
        invokeAndWait( this::showFeedback );
        runOnce( NEW_COLOR1 );
        runOnce( NEW_COLOR2 );
        
        JOptionPane.showMessageDialog( null, "OK to exit" );
        System.exit( 0 );
    }
    
    /**
     * Executes one pass of the process
     * embodied in the application.
     * Encapsulates all operator interaction.
     * 
     * @param newColor  the color to select in the JColorChooser
     *                  as part of the demonstration
     *                  
     * @see JColorChooserInteractiveDemo
     */
    private void runOnce( Color newColor )
    {
        // Start the JColorChooser dialog.
        Thread  thread  = startChooserThread();
        // First operator dialog; prompts the operator 
        // to proceed with the demo.
        JOptionPane.showMessageDialog(
            null, 
            "OK to change dialog color choice" 
        );
        
        // Select a color in the JColorChooser.
        chooser.setColor( newColor );
        // Second operator dialog; prompts the operator to 
        // dismiss the chooser dialog with OK or Cancel.
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
        
        // Selects the OK or Cancel button from the JColorChooser dialog
        // based on the choice made by the operator in the second 
        // operator dialog. Causes the colorChoice instance variable
        // to be set.
        JButton button  = 
            option == 0 ? okButton : cancelButton;
        invokeAndWait( () -> button.doClick() );
        SandboxUtils.join( thread );
        
        // If the chooser dialog returned a color when dismissed,
        // change the feedback window to that color.
        if ( colorChoice != null )
        {
            invokeAndWait( () -> {
                feedbackPanel.setBackground( colorChoice );
                feedbackPanel.repaint();
            });
        }
    }
    
    /**
     * Displays the feedback window in a JFrame.
     */
    private void showFeedback()
    {
        feedbackPanel = new JPanel();
        feedbackPanel.setBackground(START_COLOR);
        feedbackPanel.setPreferredSize( new Dimension( 200, 75 ) );
        
        JFrame  frame = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( feedbackPanel );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Starts the JColorChooser dialog,
     * and records its response.
     */
    private void startChooser()
    {
        colorChoice = 
            JColorChooser.showDialog( null, TITLE, START_COLOR );
    }
    
    /**
     * Starts a thread to display the JColorChooser dialog.
     * Interrogates the dialog after it's displayed,
     * and obtains the components needed
     * for programmatic interaction.
     * 
     * @return  the new Thread
     */
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
    
    /**
     * Helper method to obtain the JColorChooser component
     * of the JColorChooser dialog.
     * 
     * @return  the JColorChooser component of the JColorChooser dialog.
     * 
     * @throws ComponentException if the operation fails
     */
    private JColorChooser getChooser()
    {
        JComponent  comp    = 
            finder.find( c -> (c instanceof JColorChooser) );
        if ( comp == null || !(comp instanceof JColorChooser) ) 
            throw new ComponentException( "JColorChooser not found" );
        return (JColorChooser)comp;
    }
    
    /**
     * Helper method to obtain the JButton with the given text
     * from the JColorChooser dialog.
     * 
     * @param text  the given text
     * 
     * @return  the JButton with the given label
     * 
     * @throws ComponentException if the operation fails
     */
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
    
    /**
     * Schedules the given operation to execute
     * on the event manager thread,
     * and waits for its completion.
     * 
     * @param runner    the given operation
     */
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
