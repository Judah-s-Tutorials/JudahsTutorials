package com.acmemail.judah.cartesian_plane.test_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.input.ProfileParser;

public class ProfileFileManagerTestData
{
    private static final String     testDataDirName = 
        "ProfileFileManagerTest";
    private static final  File      testDataDir     = 
        Utils.getTestDataDir( testDataDirName );
    private static final  String    baseName        = 
        "BaseProfile.txt";
    private static final  String    distinctName    = 
        "DistinctProfile.txt";
    private static final  String    adHocName       = 
        "AdHoc.txt";
    private static final  String    readOnlyName    = 
        "ReadonlyFile.txt";
    private static final  String    noSuchFileName  = 
        "NoSuchFile.txt";
    private static final File       baseFile        = 
        new File( testDataDir, baseName );
    private static final File       distinctFile    = 
        new File( testDataDir, distinctName );
    private static final File       adHocFile       = 
        new File( testDataDir, adHocName );
    private static final File       readOnlyFile    = 
        new File( testDataDir, readOnlyName );
    private static final File       noSuchFile      = 
        new File( testDataDir, noSuchFileName );
    
    private static final Profile    baseProfile     = new Profile();
    private static final Profile    distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );

    static
    {
        try
        {
            Utils.recursiveDelete( testDataDir );
            testDataDir.mkdirs();
            
            saveProfile( baseProfile, baseFile );
            saveProfile( distinctProfile, distinctFile );
            saveProfile( baseProfile, readOnlyFile );
            readOnlyFile.setWritable( false );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private ProfileFileManagerTestData()
    {
    }
    
    /**
     * Returns the Profile 
     * used to create the baseFile.
     * @return
     */
    public static Profile getBaseProfile()
    {
        return baseProfile;
    }
    
    /**
     * Returns the Profile 
     * used to create the distinctFile.
     * @return
     */
    public static Profile getDistinctProfile()
    {
        return distinctProfile;
    }

    /**
     * Get the object that encapsulates the test data directory.
     * 
     * @return the testDataDir object
     */
    public static File getTestDataDir()
    {
        return testDataDir;
    }

    /**
     * Returns the baseFile object.
     * 
     * @return the baseFile object
     */
    public static File getBaseFile()
    {
        return baseFile;
    }

    /**
     * Returns the distinctFile object.
     * 
     * @return the distinctFile object
     */
    public static File getDistinctFile()
    {
        return distinctFile;
    }

    /**
     * Returns the adHocFile object.
     * 
     * @return the adHocFile object
     */
    public static File getAdhocFile()
    {
        return adHocFile;
    }

    /**
     * Returns the readOnlyFile object.
     * 
     * @return the readOnlyFile object
     */
    public static File getReadonlyFile()
    {
        return readOnlyFile;
    }

    /**
     * Returns the noSuchFile object.
     * 
     * @return the noSuchFile object
     */
    public static File getNosuchFile()
    {
        return noSuchFile;
    }
    
    /**
     * Compare the names of two given File objects;
     * if both objects are null
     * the result is true,
     * otherwise, the file names are converted to uppercase
     * and compared for equality.
     * 
     * @param file1 the first given File object
     * @param file2 the second given File object
     * 
     * @return  true if the names of the given files are equal
     */
    public static boolean compareFileNames( File file1, File file2 )
    {
        boolean result  = false;
        if ( file1 == file2 )
            result = true;
        else if ( file1 == null || file2 == null )
            result = false;
        else
        {
            String  name1   = file1.getName().toUpperCase();
            String  name2   = file2.getName().toUpperCase();
            result  = name1.equals( name2 );
        }
        return result;
    }
    
    /**
     * Read the given file
     * and validate its contents
     * against the given profile.
     * It is assume that the given file exists,
     * is readable, and contains a valid Profile.
     * An I/O will result
     * in a thrown ComponentException
     * 
     * @param expProfile    the given profile
     * @param file          the given file
     * 
     * @throws ComponentException
     * if the given file cannot be successfully read
     */
    public static boolean validateFile( Profile expProfile, File file )
    {
        Profile actProfile  = null;
        try
        {
            actProfile  = getProfile( file );
        }
        catch ( IOException exc )
        {
            String  msg =
                "\"" + file.getName() + "\" read failure";
            throw new ComponentException( msg, exc );
        }
        boolean result      = expProfile.equals( actProfile );
        return result;
    }
    
    /**
     * Perform all necessary cleanup;
     * release all resources
     * and delete all test data
     * including the test data subdirectory.
     */
    public static void shutdown()
    {
        Utils.recursiveDelete( testDataDir );
    }

    /**
     * Reads a given file into a Profile
     * and returns the Profile.
     * 
     * @param file  the given file
     * 
     * @return  the generated Profile
     * 
     * @throws IOException  if the given file cannot be read
     */
    private static Profile getProfile( File file )
        throws IOException
    {
        Profile profile = new Profile();
        try ( 
            FileReader fReader = new FileReader( file );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            Stream<String>  lines = bReader.lines();
            ProfileParser   parser  = new ProfileParser( profile );
            parser.loadProperties( lines );
        }
        return profile;
    }

    /**
     * Saves the given profile
     * to the given file
     * without going through the file manager.
     * 
     * @param profile       the given profile
     * @param file          the given file
     * 
     * @throws IOException  if the operation fails
     */
    private static void saveProfile( Profile profile, File file ) 
        throws IOException
    {
        ProfileParser   parser      = new ProfileParser( profile );
        try ( 
            FileOutputStream fileStr = new FileOutputStream( file );
            PrintWriter writer = new PrintWriter( fileStr );
        )
        {
            parser.getProperties()
                .forEach( writer::println );
        }
    }
}
