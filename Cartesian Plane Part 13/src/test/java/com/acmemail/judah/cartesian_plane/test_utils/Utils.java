package com.acmemail.judah.cartesian_plane.test_utils;

import java.io.File;

public class Utils
{
    /**
     * Path separator for this platform.
     * For example "\" for Windows, "/" for Unix.
     */
    @SuppressWarnings("unused")
    private static final String sep         = File.separator;
    /** 
     * Location of the temporary directory for this computer
     * represented as a string. 
     */
    private static final String tempDirStr  = 
        System.getProperty( "java.io.tmpdir" );
    /** 
     * Location of the temporary directory for this computer
     * encapsulated in a File object. 
     */
    private static final File tempDir       = new File( tempDirStr );

    /**
     * Private constructor to prevent instantiation.
     */
    private Utils()
    {
    }
    
    /**
     * Returns the location of the temporary directory
     * for this computer.
     * @return
     */
    public static File getTempDir()
    {
        return tempDir;
    }
    
    /**
     * Returns a File object 
     * encapsulating a file with the given name
     * in this computer's temporary directory.
     * <p>
     * Example:
     * </p>
     * <code>
     *     File nextFile = Utils.getTempFile( "WarpSpeed.txt" );
     * </code>
     * <p>
     * On Unix, will likely return a File encapsulating the path
     * "/tmp/WarpSpeed.txt".
     * 
     * @param fileName  the given file name
     * 
     * @return  
     *      File encapsulating a file with the given name
     *      placed in this computer's temporary directory.
     */
    public static File getTempFile( String fileName )
    {
        File file    = new File( tempDir, fileName );
        return file;
    }
    
    /**
     * Calls Thread.sleep for the given number of milliseconds.
     * Ignores InterruptedExceptions.
     * 
     * @param millis    the given number of milliseconds.
     */
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
        }
    }
    
    /**
     * Joins a given thread.
     * If an InterruptedException occurs
     * it is logged and ignored.
     * 
     * @param thread    the given thread
     */
    public static void join( Thread thread )
    {
        try
        {
            thread.join();
        }
        catch ( InterruptedException exc )
        {
            exc.printStackTrace();
        }
    }

}
