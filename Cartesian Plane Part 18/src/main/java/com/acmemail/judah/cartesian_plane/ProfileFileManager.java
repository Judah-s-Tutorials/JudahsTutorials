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
    static final JFileChooser chooser;
    private static File     currFile            = null;
    private static boolean  lastResult          = false;

    static
    {
        // This property typically contains the path to the directory
        // from which this application was executed.
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
    }
    
    public static JFileChooser getFileChooser()
    {
        return chooser;
    }
    
    public static File getCurrFile()
    {
        return currFile;
    }
    
    public static void clearCurrFile()
    {
        currFile = null;
    }
    
    public static boolean getLastResult()
    {
        return lastResult;
    }
    
    public static void close()
    {
        currFile = null;
    }
    
    public static Profile open()
    {
        lastResult = false;
        Profile profile = null;
        int result  = chooser.showOpenDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            profile = open( chooser.getSelectedFile() );
        return profile;
    }
    
    public static Profile open( Profile profile )
    {
        lastResult = false;
        int result  = chooser.showOpenDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            profile = open( chooser.getSelectedFile() );
        return profile;
    }
    
    public static Profile open( File file )
    {
        Profile profile = open( file, new Profile() );
        return profile;
    }
    
    public static Profile open( File file, Profile profile )
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
    
    public static boolean save( File file )
    {
        save( new Profile(), file );
        return lastResult;
    }
    
    public static boolean save()
    {
        save( new Profile() );
        return lastResult;
    }
    
    public static boolean save( Profile profile )
    {
        lastResult = currFile == null ? 
            saveAs( profile ) : save( profile, currFile );
        return lastResult;
    }
    
    public static boolean saveAs()
    {
        saveAs( new Profile() );
        return lastResult;
    }
    
    public static boolean saveAs( Profile profile )
    {
        lastResult = false;
        int result  = chooser.showSaveDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
            lastResult = save( profile, chooser.getSelectedFile() );
        return lastResult;
    }
    
    public static boolean save( Profile profile, File file )
    {
        lastResult = false;
//        currFile = null;
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
