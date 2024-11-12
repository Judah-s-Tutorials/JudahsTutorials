package com.acmemail.judah.cartesian_plane.test_utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.ProfileParser;

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

    static
    {
        try
        {
            Profile baseProfile     = new Profile();
            Profile distinctProfile = 
                ProfileUtils.getDistinctProfile( baseProfile );
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
