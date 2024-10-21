package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.stream.Stream;

public class Utils
{
    /**
     * Base directory for storing test data.
     */
    public final static String  BASE_TEST_DATA_DIR  = "test_data";
    
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
     * Return a reference to the given subpath,
     * which is assumed to be rooted in the project test directory
     * indicated by BASE_TEST_DATA_DIR.
     * For example, if <em>subpath</em> 
     * refers to <em>LandMasses/Continents/Asia</em>
     * a reference to 
     * <em>BASE_TEST_DATA_DIR/LandMasses/Continents/Asia</em>
     * will be returned.
     * If necessary, 
     * any subdirectories in the path
     * will be created.
     * If the operation fails
     * null will be returned.
     * 
     * @param subpath   the given subpath
     * 
     * @return 
     *      a reference to subpath rooted in BASE_TEST_DATA_DIR,
     *      or null if the operation fails
     */
    public static File getTestDataDir( String subpath )
    {
        File    testDir = new File( BASE_TEST_DATA_DIR, subpath );
        if ( !testDir.exists() )
            if ( !testDir.mkdirs() )
                testDir = null;
        return testDir;
    }
    
    /**
     * Recursively deletes the given file.
     * If the given file is a directory
     * all contained files a recursively deleted.
     * 
     * @param file  the given file
     */
    public static void recursiveDelete( File file )
    {
        if ( file.isDirectory() )
            Stream.of( file.listFiles() )
            .forEach( Utils::recursiveDelete );
        file.delete();
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
    
    /**
     * Test two BufferedImages for equality.
     * If both images are null, 
     * they are considered equal,
     * otherwise:
     * <ol>
     * <li>Their dimensions must be equal;</li>
     * <li>Their types must be equal;</li>
     * <li>Corresponding RGB values must be equal.</li>
     * </ol>
     * @param image1    the first image to compare
     * @param image2    the second image to compare
     * 
     * @return  true if the images are equal
     */
    public static boolean 
    equals( BufferedImage image1, BufferedImage image2 )
    {
        boolean result  = image1 == image2;
        if ( result )
            ;
        else if ( image1 == null || image2 == null )
            result = false;
        else
        {
            int rows    = image1.getHeight();
            int cols    = image1.getWidth();
            if ( rows != image2.getHeight() )
                result = false;
            else if ( cols != image2.getWidth() )
                result = false;
            else if ( image1.getType() != image2.getType() )
                result = false;
            else
            {
                result = true;
                for ( int yco = 0 ; yco < rows && result ; ++yco )
                    for ( int xco = 0 ; xco < cols && result ; ++xco )
                    {
                        int rgb1 = image1.getRGB( xco, yco );
                        int rgb2 = image2.getRGB( xco, yco );
                        result = rgb1 == rgb2;
                    }
            }
        }
        return result;
    }
}
