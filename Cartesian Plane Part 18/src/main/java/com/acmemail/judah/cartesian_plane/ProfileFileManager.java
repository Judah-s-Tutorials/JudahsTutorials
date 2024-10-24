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
    private final  JFileChooser chooser;
    private File     currFile            = null;
    private boolean  lastResult          = false;
    
    /**
     * Constructor.
     * Fully configures an object of this type.
     */
    public ProfileFileManager()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
    }
    
    public JFileChooser getFileChooser()
    {
        return chooser;
    }
    
    public File getCurrFile()
    {
        return currFile;
    }
    
    public boolean getLastResult()
    {
        return lastResult;
    }
    
    public void close()
    {
        currFile = null;
    }
    
    public Profile open()
    {
        lastResult = false;
        Profile profile = null;
        int result  = chooser.showOpenDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            profile = open( chooser.getSelectedFile() );
        return profile;
    }
    
    public Profile open( Profile profile )
    {
        lastResult = false;
        int result  = chooser.showOpenDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            profile = open( chooser.getSelectedFile(), profile );
        return profile;
    }
    
    public Profile open( File file )
    {
        Profile profile = open( file, new Profile() );
        return profile;
    }
    
    public Profile open( File file, Profile profile )
    {
        Stream<String>  lines   = null;
        currFile = null;
        try ( 
            FileReader fReader = new FileReader( file );
            BufferedReader  bReader = new BufferedReader( fReader );
        )
        {
            lines = bReader.lines();
            ProfileParser   parser  = new ProfileParser( profile );
            parser.loadProperties( lines );
            profile = parser.getProfile();
            currFile = file;
        }
        catch ( IOException exc )
        {
            profile = null;
            String  msg = 
                "Error reading \"" 
                    + file.getAbsolutePath() + "\": " 
                    + exc.getMessage();
            JOptionPane.showMessageDialog( 
                null, 
                msg, 
                "Read File Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        lastResult = profile != null;
        return profile;
    }
    
    public boolean newFile()
    {
        Profile profile = new Profile();
        saveAs( profile );
        return lastResult;
    }

    public boolean save( File file )
    {
        save( new Profile(), file );
        return lastResult;
    }
    
    public boolean save( Profile profile )
    {
        lastResult = currFile == null ? 
            saveAs( profile ) : save( profile, currFile );
        return lastResult;
    }
    
    public boolean saveAs()
    {
        saveAs( new Profile() );
        return lastResult;
    }
    
    public boolean saveAs( Profile profile )
    {
        lastResult = false;
        int result  = chooser.showSaveDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            lastResult = save( profile, chooser.getSelectedFile() );
        return lastResult;
    }
    
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
