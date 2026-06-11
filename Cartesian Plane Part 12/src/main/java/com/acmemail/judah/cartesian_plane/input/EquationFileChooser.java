package com.acmemail.judah.cartesian_plane.input;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Allow the client to open or save an equation file
 * via a JFileChooser.
 * When necessary,
 * error messages are posted to a {@link MessageConsumer}.
 * By default,
 * the message consumer is a modal dialog.
 * It can be replaced by invoking
 * {@link #setMessageConsumer(MessageConsumer)}.
 *
 * @author Jack Straub
 */
public final class EquationFileChooser
{
    /** The error message to post when a parse error is encountered. */
    private static final String PARSE_ERROR     = "Parse Error";
    /** The error message to post when a read error is encountered. */
    private static final String READ_ERROR      = "Read Failure";
    /** The error message to post when a write error is encountered. */
    private static final String WRITE_ERROR     = "Write Failure";
    
    /** The default user directory location property. */
    private static final String userDirProp = 
        System.getProperty( "user.dir" );
    /** The default user directory file. */
    private static final File   userDir     = new File( userDirProp );
    
    /**  
     * The encapsulated JFileChooser. 
     * Set in the constructor; will never be null.
     */
    private final JFileChooser  chooser;
    /** 
     * Parent component for the encapsulated JFileChooser. 
     * Set in the constructor, may be null.
     */
    private final Component     parent;
    
    /** Default MessageConsumer implementation. */
    private static final MessageConsumer defaultMessageConsumer = 
        JOptionPane::showMessageDialog;
            
    /** The current message consumer. */
    private static volatile MessageConsumer messageConsumer = 
        defaultMessageConsumer;
    
    /**
     * Default constructor.
     */
    public EquationFileChooser()
    {
        this( null );
    }
    
    /**
     * Constructor.
     * Initializes instance state.
     * 
     * @param parent    
     *      the parent to use for the JFileChooser parent; may be null
     */
    public EquationFileChooser( Component parent )
    {
        this.parent = parent;
        chooser = new JFileChooser( userDir );
    }
    
    /**
     * Allow the operator to choose an equation file;
     * convert the file to an equation
     * and return the equation in an Optional.
     * If the operator cancels the operation
     * an empty Optional is returned.
     * If an I/O error occurs,
     * or if a parse error occurs,
     * an error message is posted to the {@link MessageConsumer}
     * and an empty Optional is returned.
     * 
     * @return  
     *      If the operation completes successfully,
     *      an Optional containing the fetched equation,
     *      otherwise an empty Optional
     *      
     * @see MessageConsumer
     * @see #setMessageConsumer
     */
    public Optional<Equation> openDialog()
    {
        Optional<Equation>  status  = Optional.empty();
        int                 action  = chooser.showOpenDialog( parent );
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            File        file        = chooser.getSelectedFile();
            Equation    equation    = new Exp4jEquation();
            try
            {
                Result  result  = FileManager.load( file, equation );
                if ( !result.success() )
                    showError( PARSE_ERROR, result.messages() );
                else
                    status = Optional.of( equation );
            }
            catch ( IOException exc )
            {
                String  message = exc.getMessage();
                if ( message == null )
                    message = exc.getClass().getName();
                showError( READ_ERROR, List.of( message ) );
            }
        }
        return status;
    }
    
    /**
     * Allow the operator to save an equation
     * in a selected location.
     * Returns true if the operation completes successfully.
     * If the operator cancels the operation
     * false is returned.
     * If an error occurs,
     * an error message is posted to the 
     * {@link MessageConsumer}
     * and false is returned.
     * 
     * @param equation  the equation to save; must not be null
     *
     * @return  true, if the operation completes successfully
     *
     * @throws NullPointerException if {@code equation} is null
     *
     * @see MessageConsumer
     * @see #setMessageConsumer
     */
    public boolean saveDialog( Equation equation )
    {
        Objects.requireNonNull( equation, "equation" );
        boolean result  = false;
        int     action  = chooser.showSaveDialog( parent );
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            File    file    = chooser.getSelectedFile();
            try
            {
                FileManager.save( file, equation );
                result = true;
            }
            catch ( IOException exc )
            {
                String  message = exc.getMessage();
                if ( message == null )
                    message = exc.getClass().getName();
                showError( WRITE_ERROR, List.of( message ) );
            }
        }
        return result;
    }
    
    /**
     * Gets the parent of the encapsulated JFileChooser;
     * may be null.
     * 
     * @return  the parent of the encapsulated JFileChooser
     */
    public Component getParent()
    {
        return parent;
    }
    
    /**
     * Gets the current MessageConsumer.
     * 
     * @return  the current MessageConsumer
     * 
     * @see #setMessageConsumer(MessageConsumer)
     */
    public static MessageConsumer getMessageConsumer()
    {
        return messageConsumer;
    }
    
    /**
     * Sets the current MessageConsumer.
     * The caller may pass null,
     * in which case a default is applied.
     * 
     * @param consumer  the consumer to set, or null to restore default
     * 
     * @see #getMessageConsumer()
     */
    public static void setMessageConsumer( MessageConsumer consumer )
    {
        if ( consumer != null )
            messageConsumer = consumer;
        else
            messageConsumer = defaultMessageConsumer;
    }

    /**
     * Post the given title and messages
     * to the current MessageConsumer.
     * By default this displays a modal dialog.
     *
     * @param title     the given title
     * @param messages  the given messages
     *
     * @see #setMessageConsumer(MessageConsumer)
     */
    private void showError( String title, List<String> messages )
    {
        String  message = String.join( "\n", messages );
        messageConsumer.postMessage( 
            parent, 
            message, 
            title, 
            JOptionPane.ERROR_MESSAGE
        );
    }
}
