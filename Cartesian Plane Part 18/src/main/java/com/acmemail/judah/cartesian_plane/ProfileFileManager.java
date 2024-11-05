package com.acmemail.judah.cartesian_plane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ProfileFileManager
{
    /** 
     * Represents the operator's choice of Save or Open 
     * in the file chooser dialog.
     * @see #getLastAction()
     */
    public static final int     APPROVE     = JFileChooser.APPROVE_OPTION;
    /** 
     * Represents the operator's choice of Cancel 
     * in the file chooser dialog.
     * @see #getLastAction()
     */
    public static final int     CANCEL      = JFileChooser.CANCEL_OPTION;
    
    /** The title of the file chooser dialog. */
    private static final String title       = "Profile File Manager";

    /** The JFileChooser used in this object. */
    private final JFileChooser  chooser;
    /** 
     * The last file successfully processed; null if none.
     * Set to null if an I/O error occurs during processing.
     * @see #close()
     */
    private File                currFile    = null;
    /**
     * The status of the last result obtained during an I/O operation,
     * true for success, false for failure.
     * @see #getLastResult()
     */
    private boolean             lastResult  = false;
    /** 
     * Indicates the last choice selected by the operator when presented
     * with the JFileChooser dialog. Will always be a value of APPROVE
     * or CANCEL. Value undetermined if the JFileChooser has not been
     * posted.
     * @see #getLastAction()
     */
    private int                 lastAction  = CANCEL;
    
    /**
     * Constructor.
     * Fully configures an object of this type.
     */
    public ProfileFileManager()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( title );
    }
    
    /**
     * Gets the JFileChooser encapsulated in this object.
     * 
     * @return  the JFileChooser encapsulated in this object
     */
    public JFileChooser getFileChooser()
    {
        return chooser;
    }
    
    /**
     * Returns the file that was the target
     * of the last operation,
     * of null if the last operation failed.
     * 
     * @return  the target of the last operation, or null if failed
     */
    public File getCurrFile()
    {
        return currFile;
    }
    
    /**
     * Return the result of the last file I/O operation,
     * true for success or false for failure.
     * 
     * @return  the result of the last file I/O operation
     */
    public boolean getLastResult()
    {
        return lastResult;
    }
    
    /**
     * Indicate the last choice made by the operator
     * when presented with the JFileChooser, either {@link #APPROVE}
     *  or {@link #CANCEL}.
     * 
     * @return  the last option used to terminate the JFileChooser
     */
    public int getLastAction()
    {
        return lastAction;
    }
    
    /**
     * Closes the currently open file, if any.
     */
    public void close()
    {
        currFile = null;
    }
    
    /**
     * Populates a newly instantiated Profile object
     * from a file of the operator's choice,
     * updating the {@link #lastAction} property.
     * If the operation is not canceled
     * the {@link #lastResult} and {@link #currFile}
     * properties are updated.
     *
     * @return  reference to Profile on success, null on failure
     * 
     * @see #open(File, Profile)
     */
    public Profile open()
    {
        Profile profile = null;
        lastAction = chooser.showOpenDialog( null );
        if ( lastAction == APPROVE )
            profile = open( chooser.getSelectedFile() );
        return profile;
    }
    
    /**
     * Populates the given Profile
     * from a file of the operator's choice,
     * updating the {@link #lastAction} property.
     * If the operation is not canceled
     * the {@link #lastResult} and {@link #currFile}
     * properties are updated.
     * 
     * @param profile   the given Profile
     * 
     * @return  reference to profile on success, null on failure
     * 
     * @see #open(File, Profile)
     */
    public Profile open( Profile profile )
    {
        lastAction = chooser.showOpenDialog( null );
        if ( lastAction == APPROVE )
            profile = open( chooser.getSelectedFile(), profile );
        return profile;
    }
    
    /**
     * Parse the given file
     * into a newly instantiate Profile object.
     * 
     * @param file  the given file
     * 
     * @return  reference to Profile on success, null on failure
     * 
     * @see #open(File, Profile)
     */
    public Profile open( File file )
    {
        Profile profile = open( file, new Profile() );
        return profile;
    }
    
    /**
     * Parse the given file into the given Profile.
     * On completion,
     * lastResult and currFile are updated.
     * 
     * @param file      the given file
     * @param profile   the given Profile
     * 
     * @return  reference to profile on success, null on failure
     */
    public Profile open( File file, Profile profile )
    {
        currFile = null;
        lastResult = false;
        try ( 
            FileReader fReader = new FileReader( file );
            BufferedReader  bReader = new BufferedReader( fReader );
        )
        {
            Stream<String>  lines = bReader.lines();
            ProfileParser   parser  = new ProfileParser( profile );
            parser.loadProperties( lines );
            currFile = file;
            lastResult = true;
        }
        catch ( IOException exc )
        {
            final String    lineSep = System.lineSeparator();
            profile = null;
            String  msg = 
                "Error reading \"" 
                    + file.getName() + "\": " 
                    + lineSep 
                    + exc.getMessage();
            JOptionPane.showMessageDialog( 
                null, 
                msg, 
                "Read File Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return profile;
    }
    
    /**
     * Creates a Profile
     * and writes it to
     * a file of the operator's choice.
     * 
     * @return  true for success, false for fail
     */
    public boolean newFile()
    {
        saveAs();
        return lastResult;
    }
    
    /**
     * Instantiates a new Profile
     * and writes it to the given file.
     * 
     * @param file  the given file
     * 
     * @return  true for success, false for failure
     */
    public boolean save( File file )
    {
        save( new Profile(), file );
        return lastResult;
    }
    

    /**
     * This method saves the given Profile
     * to the current file, if any.
     * If there is no current file,
     * this operation is equivalent to {@link #saveAs(Profile)}.
     * Returns true on successful completion
     * false for failure.
     * 
     * @param profile   the given Profile
     * 
     * @return  true on successful completion false for failure.
     */
    public boolean save( Profile profile )
    {
        if ( currFile == null )
            saveAs( profile );
        else
            save( profile, currFile );
        return lastResult;
    }
    
    /**
     * Instantiates a new Profile
     * and writes it to
     * a file of the operator's choice.
     * Returns true on successful completion of the operation,
     * false for failure.
     * If the operator cancels the operation
     * the object's state remains unchanged.
     * 
     * @return
     */
    public boolean saveAs()
    {
        saveAs( new Profile() );
        return lastResult;
    }
    
    /**
     * Saves the given Profile to a file
     * of the operator's choice,
     * updating the {@link #lastAction} property.
     * If not canceled by the operator,
     * updates the {@link #lastResult} and {@link #currFile}
     * properties.
     * Returns true on success,
     * false otherwise.
     * 
     * @param profile   the given Profile
     * 
     * @return  true on success, false otherwise
     */
    public boolean saveAs( Profile profile )
    {
        lastAction = chooser.showSaveDialog( null );
        if ( lastAction == APPROVE )
            save( profile, chooser.getSelectedFile() );
        return lastResult;
    }
    
    /**
     * Saves the given Profile to the given File.
     * I/O errors are reported via dialogs.
     * Returns true on successful completion,
     * false otherwise.
     * 
     * @param profile   the given Profile
     * @param file      the given File
     * @return  true on successful completion, false otherwise
     */
    public boolean save( Profile profile, File file )
    {
        lastResult = false;
        currFile = null;
        try ( PrintWriter pWriter = new PrintWriter( file ) )
        {
            ProfileParser   parser  = new ProfileParser( profile );
            parser.getProperties().forEach( pWriter::println );
            lastResult = true;
            currFile = file;
        }
        catch ( IOException exc )
        {
            String  msg = 
                "Error writing \"" 
                    + file.getAbsolutePath() + "\": " 
                    + exc.getMessage();
            JOptionPane.showMessageDialog( 
                null, 
                "Save File Error", 
                msg, 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return lastResult;
    }
}
