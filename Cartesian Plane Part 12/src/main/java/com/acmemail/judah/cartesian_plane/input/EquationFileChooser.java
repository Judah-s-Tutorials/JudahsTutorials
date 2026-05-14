package com.acmemail.judah.cartesian_plane.input;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Allow the client to open or save an equation file
 * via a JFileChooser.
 */
final public class EquationFileChooser
{
    private static final String PARSE_ERROR     = "Parse Error";
    private static final String READ_ERROR      = "Read Failure";
    private static final String WRITE_ERROR     = "Write Failure";
    
    private static final String userDirProp = 
        System.getProperty( "user.dir" );
    private static final File   userDir     = new File( userDirProp );
    
    private final JFileChooser  chooser;
    private final Component     parent;
    
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
     * an error occurs, or if a parse error occurs,
     * an error message is posted in a modal dialog
     * and an empty Optional is returned.
     * 
     * @return  
     *      If the operation completes successfully,
     *      an Optional containing the fetched equation,
     *      otherwise an empty Optional
     */
    public Optional<Equation> openDialog()
    {
        Optional<Equation>  status  = Optional.empty();
        int                 action  =
            chooser.showOpenDialog( parent );
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            File        file        = chooser.getSelectedFile();
            Equation    equation    = new Exp4jEquation();
            try
            {
                Result  result  = FileManager.load( file, equation );
                if ( !result.isSuccess() )
                    showError( PARSE_ERROR, result.getMessages() );
                else
                    status = Optional.of( equation );
            }
            catch ( IOException exc )
            {
                showError( READ_ERROR, List.of( exc.getMessage() ) );
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
     * an error message is displayed in a modal dialog
     * and false is returned.
     * 
     * @param equation  the equation to save
     * 
     * @return  true, if the operation completes successfully
     */
    public boolean saveDialog( Equation equation )
    {
        boolean result  = false;
        int     action  =
            chooser.showOpenDialog( parent );
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
                showError( WRITE_ERROR, List.of( exc.getMessage() ) );
            }
        }
        return result;
    }
    
    /**
     * Post a modal dialog with the given title
     * and containing the given messages.
     * 
     * @param title     the given title
     * @param messages  the given messages
     */
    private void showError( String title, List<String> messages )
    {
        String  message = String.join( "\n", messages );
        JOptionPane.showMessageDialog( 
            parent, 
            message, 
            title, 
            JOptionPane.ERROR_MESSAGE
        );
    }
}
