package com.acmemail.judah.cartesian_plane;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import util.PropertyTesterApp;

/**
 * This test is strictly to verify that the PropertyManager
 * initializes properties correctly. 
 * In particular:
 * <ul>
 * <li>
 * Any property declared
 * in the application's ini file
 * takes precedence over
 * a property's default value.
 * </li>
 * <li>
 * Any property declared
 * in the user's ini file
 * takes precedence over
 * properties declared
 * in the application ini file.
 * </li>
 * <li>
 * Any property declared
 * in the environment
 * takes precedence over
 * properties declared
 * in the user's ini file.
 * </li>
 * <li>
 * Any property declared
 * on the command line
 * takes precedence over
 * properties declared
 * anywhere else.
 * </li>
 * </ul>
 * <p>
 * Note that no effort is made
 * to verify the type
 * of a property's value.
 * In fact, for convenience,
 * every property's value
 * is treated as a string.
 * </p>
 * 
 * @author Jack Straub
 */
class PropertyManagerGetPropertyTest
{
    /** 
     * Class path separator (usually ":" or ";");
     * declared here for convenience.
     */
    private static final String     classPathSep    =
        System.getProperty( "path.separator" );
    
    /** Prefix for creating temporary ini file. */
    private static final String     userFilePrefix  =
        "CartesianPlaneTempFile";
    /** Suffix for creating temporary ini file. */
    private static final String     userFileSuffix  = "Ini.tmp";
    
    /** 
     * Suffix to ensure that the value of a property declared 
     * on the command line is unique.
     */
    private static final String     cmdIdent        = "_cmd";
    /** 
     * Suffix to ensure that the value of a property declared 
     * in the environment is unique.
     */
    private static final String     envIdent        = "_env";
    /** 
     * Suffix to ensure that the value of a property declared 
     * in the user ini file is unique.
     */
    private static final String     userIdent       = "_user";
    /** 
     * Suffix to ensure that the value of a property declared 
     * in the app ini file is unique.
     */
    private static final String     appIdent        = "_app";
    
    /*
     * Encapsulates the user's ini file in the system's
     * temp directory.
     */
    private static File             userIniFile     = null;
    
    /*
     * Encapsulates the app's ini file; created in the same directory
     * as the users's ini file.
     */
    private static File             appIniFile      = null;
    
    /** Directory in which user and app ini files reside. */
    private static String           iniDir      = null;
    
    /** 
     * Length of time to wait for the child process to exit.
     * Must be coordinated with {@linkplain #waitForTimeUnit}.
     */
    private static final long       waitForTimeout  = 500;
    /** 
     * Unit of measurement to use when waiting
     * for child process to exit.
     * Must be coordinated with {@linkplain #waitForTimeout}.
     */
    private static final TimeUnit   waitForTimeUnit = TimeUnit.MILLISECONDS;
    
    /** 
     * All CPConstants and their default values.
     * Declared here for the convenience of methods
     * that need test data.
     */
    private static final List<Pair> allProps        = new ArrayList<>();
    
    /** Encapsulate the child process used in testing. */
    private Process         childProcess    = null;
    /** Pipe from the child process's stdout. */
    private BufferedReader  childStdout     = null;
    /** Pipe to the child process's stdin. */
    private PrintWriter     childStdin      = null;
    
    /**
     * Executed before any test.
     * Responsible for overall initialization tasks,
     * particularly creation of the temporary user 
     * and app ini files.
     * IOExceptions are treated as fatal errors;
     * test will be terminated.
     * 
     * @throws IOException  if an error is encountered
     *                      during ini file creation
     */
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        // Create the user ini file
        userIniFile = File.createTempFile( userFilePrefix, userFileSuffix );
        userIniFile.deleteOnExit();
        
        // get the path to the temporary dir
        iniDir = userIniFile.getParent();
        
        // create the app ini file
        appIniFile = new File( iniDir, CPConstants.APP_PROPERTIES_NAME );
        if ( !appIniFile.exists() )
            appIniFile.createNewFile();
        appIniFile.deleteOnExit();
        
        // initialize test data
        initTestData();
    }
    
    /**
     * Performs cleanup after each test.
     * In particular,
     * terminates the child process and 
     * closes the pipes to/from the child.
     */
    @AfterEach
    public void afterEach()
    {
        killChildProcess();
        closeResource( childStdout );
        childStdout = null;
        closeResource( childStdin );
        childStdin = null;
        appIniFile.delete();
        userIniFile.delete();
    }
    
    /**
     * Can we read from the user's ini file.
     */
    @Test
    public void testUserIniFile()
    {
        ChildProcess    childProcess    = new ChildProcess();
        
        // all properties under test
        List<Pair>      testProps       = new ArrayList<>();
        int             numAllProps     = allProps.size();
        int             numTestProps    = 10 < numAllProps ? 10 : numAllProps;
        for ( int inx = 0 ; inx > numTestProps ; ++inx )
            testProps.add( allProps.get( inx ) );
        
        List<Pair>  userProps   = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  userVal     = pair.propValue + appIdent;
            Pair    userPair    = new Pair( pair.propName, userVal );
            userProps.add( userPair );
        }
        makeUserIniFile( userProps );

        // add the -D option that identifies the location
        // of the user's ini file
        childProcess.addUserIniOption();
        // start the child process; interrogate PropertyManager
        childProcess.startChildProcess();

        for ( Pair pair : userProps )
        {
            String  actVal  = getPropVal( pair.propName );
            assertEquals( pair.propValue, actVal );
        }
    }
    
    /**
     * Can we read from the application's ini file.
     */
    @Test
    public void testAppIniFile()
    {
        ChildProcess    childProcess    = new ChildProcess();
        List<Pair>      testProps       = new ArrayList<>();
        int             numAllProps     = allProps.size();
        int             numTestProps    = 10 < numAllProps ? 10 : numAllProps;
        for ( int inx = 0 ; inx > numTestProps ; ++inx )
            testProps.add( allProps.get( inx ) );
        
        List<Pair>  appProps    = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  appVal      = pair.propValue + appIdent;
            Pair    appPair     = new Pair( pair.propName, appVal );
            appProps.add( appPair );
        }
        makeAppIniFile( appProps );
        System.out.println( userIniFile.getAbsolutePath() );

        // start the child process; interrogate PropertyManager
        childProcess.startChildProcess();

        for ( Pair pair : appProps )
        {
            String  actVal  = getPropVal( pair.propName );
            assertEquals( pair.propValue, actVal );
        }
    }
    
    /**
     * Properties declared in user ini file
     * take precedence app ini file.
     */
    @Test
    public void testUserOverAppIniFile()
    {
        ChildProcess    childProcess    = new ChildProcess();
        
        // all properties under test
        List<Pair>      testProps       = new ArrayList<>();
        int             numAllProps     = allProps.size();
        int             numTestProps    = 20 < numAllProps ? 20 : numAllProps;
        for ( int inx = 0 ; inx > numTestProps ; ++inx )
            testProps.add( allProps.get( inx ) );
        
        // Declare all properties in app ini file
        List<Pair>  appProps    = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  userVal     = pair.propValue + appIdent;
            Pair    userPair    = new Pair( pair.propName, userVal );
            appProps.add( userPair );
        }
        makeUserIniFile( appProps );
        
        // Declare about half the properties in the user ini file
        int         half        = testProps.size() / 2;
        List<Pair>  userProps   = new ArrayList<>();
        for ( int inx = 0 ; inx < half ; ++inx )
        {
            Pair    pair        = testProps.get( inx );
            String  userVal     = pair.propValue + userIdent;
            Pair    userPair    = new Pair( pair.propName, userVal );
            userProps.add( userPair );
        }
        makeUserIniFile( userProps );

        // add the -D option that identifies the location
        // of the user's ini file
        childProcess.addUserIniOption();
        // start the child process; interrogate PropertyManager
        childProcess.startChildProcess();

        // make some maps for convenience
        Map<String,String>  userMap = cvtListToMap( userProps );
        Map<String,String>  appMap  = cvtListToMap( appProps );
        for ( Pair pair : testProps )
        {
            String  actVal      = getPropVal( pair.propName );
            String  expVal      = userMap.get( pair.propName );
            if ( expVal != null )
                assertEquals( expVal, actVal );
            else
            {
                expVal = appMap.get( pair.propName );
                assertEquals( expVal, actVal );
            }
        }
    }
    
    /**
     * Properties declared in environment
     * take precedence over ini files.
     */
    @Test
    public void testEnvOverIniFiles()
    {
        ChildProcess    childProcess    = new ChildProcess();
        
        // all properties under test
        List<Pair>      testProps       = new ArrayList<>();
        int             numAllProps     = allProps.size();
        int             numTestProps    = 20 < numAllProps ? 20 : numAllProps;
        for ( int inx = 0 ; inx > numTestProps ; ++inx )
            testProps.add( allProps.get( inx ) );
        
        // Declare all properties in app ini file
        List<Pair>  appProps    = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  userVal     = pair.propValue + appIdent;
            Pair    userPair    = new Pair( pair.propName, userVal );
            appProps.add( userPair );
        }
        makeUserIniFile( appProps );
        
        // Declare all properties in the user ini file
        List<Pair>  userProps   = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  userVal     = pair.propValue + userIdent;
            Pair    userPair    = new Pair( pair.propName, userVal );
            userProps.add( userPair );
        }
        makeUserIniFile( userProps );
        
        // Declare about half the properties in the environment
        int         half        = testProps.size() / 2;
        List<Pair>  envProps    = new ArrayList<>();
        for ( int inx = 0 ; inx < half ; ++inx )
        {
            Pair    pair        = testProps.get( inx );
            String  envVal      = pair.propValue + envIdent;
            Pair    envPair     = new Pair( pair.propName, envVal );
            envProps.add( envPair );
        }
        childProcess.addEnvProperties( envProps );

        // add the -D option that identifies the location
        // of the user's ini file
        childProcess.addUserIniOption();
        // start the child process; interrogate PropertyManager
        childProcess.startChildProcess();

        // make some maps for convenience
        Map<String,String>  userMap = cvtListToMap( userProps );
        Map<String,String>  envMap  = cvtListToMap( envProps );
        for ( Pair pair : testProps )
        {
            // If it's not declared in the environment
            // it must come from the user ini file
            String  actVal      = getPropVal( pair.propName );
            String  expVal      = envMap.get( pair.propName );
            if ( expVal != null )
                assertEquals( expVal, actVal );
            else
            {
                expVal = userMap.get( pair.propName );
                assertEquals( expVal, actVal );
            }
        }
    }
    
    /**
     * Properties declared on command line
     * take precedence over environment.
     */
    @Test
    public void testCmdOverEnv()
    {
        ChildProcess    childProcess    = new ChildProcess();
        
        // all properties under test
        List<Pair>      testProps       = new ArrayList<>();
        int             numAllProps     = allProps.size();
        int             numTestProps    = 20 < numAllProps ? 20 : numAllProps;
        for ( int inx = 0 ; inx < numTestProps ; ++inx )
            testProps.add( allProps.get( inx ) );
        
        // Declare all properties in the environment
        List<Pair>  envProps    = new ArrayList<>();
        for ( Pair pair : testProps )
        {
            String  envVal      = pair.propValue + envIdent;
            Pair    envPair     = new Pair( pair.propName, envVal );
            envProps.add( envPair );
        }
        childProcess.addEnvProperties( envProps );
        
        // Declare about half the properties on the command line
        int         half        = testProps.size() / 2;
        List<Pair>  cmdProps     = new ArrayList<>();
        for ( int inx = 0 ; inx < half ; ++inx )
        {
            Pair    pair        = testProps.get( inx );
            String  cmdVal      = pair.propValue + envIdent;
            Pair    clPair      = new Pair( pair.propName, cmdVal );
            cmdProps.add( clPair );
        }
        childProcess.addCmdProperties( cmdProps );

        // add the -D option that identifies the location
        // of the user's ini file
        childProcess.addUserIniOption();
        // start the child process; interrogate PropertyManager
        childProcess.startChildProcess();

        // make some maps for convenience
        Map<String,String>  cmdMap  = cvtListToMap( cmdProps );
        Map<String,String>  envMap  = cvtListToMap( envProps );
        for ( Pair pair : testProps )
        {
            // If it's not declared on the command line
            // it must come from the environment
            String  actVal      = getPropVal( pair.propName );
            String  expVal      = cmdMap.get( pair.propName );
            if ( expVal != null )
                assertEquals( expVal, actVal );
            else
            {
                expVal = envMap.get( pair.propName );
                assertEquals( expVal, actVal );
            }
        }
    }
    
    
    /**
     * Comprehensive test.
     * Properties with unique values are declared
     * in both ini files, the environment 
     * and on the command line.
     * Some property values are allowed to default.
     * Verify that their values
     * are picked up 
     * according to the priority
     * of their residence.
     * <p>
     * Precondition:
     * There are at least 25 properties.
     */
    @Test
    public void testComprehensive()
    {
        Map<String,String>  expMap          = new HashMap<>();
        List<Pair>          toAddAppIni     = new ArrayList<>();
        List<Pair>          toAddUserIni    = new ArrayList<>();
        List<Pair>          toAddEnv        = new ArrayList<>();
        List<Pair>          toAddCmd        = new ArrayList<>();
        
        int chunkSize   = 5;
        int mainIndex   = 0;
        int maxInx      = 4 * chunkSize;
        
        assertTrue( allProps.size() > maxInx );
        for ( int inx = 0 ; inx < maxInx ; ++inx )
        {
            int     chunk       = mainIndex % chunkSize;
            Pair    basePair    = allProps.get( mainIndex++ );
            String  baseName    = basePair.propName;
            String  baseValue   = basePair.propValue;
            Pair    workPair    = null;
            
            if ( chunk >= 0 )
            {
                workPair = new Pair( baseName, baseValue + appIdent );
                toAddUserIni.add( workPair );
            }
            
            if ( chunk >= 1 )
            {
                workPair = new Pair( baseName, baseValue + userIdent );
                toAddUserIni.add( workPair );
            }
            
            if ( chunk >= 2 )
            {
                workPair = new Pair( baseName, baseValue + envIdent );
                toAddEnv.add( workPair );
            }
            
            if ( chunk >= 3 )
            {
                workPair = new Pair( baseName, baseValue + cmdIdent );
                toAddCmd.add( workPair );
            }
            expMap.put( workPair.propName, workPair.propValue );
        }
        
        // Remaining properties default
        for ( ; mainIndex < allProps.size() ; ++mainIndex )
        {
            Pair    pair    = allProps.get( mainIndex );
            if ( !pair.propName.equals( CPConstants.USER_PROPERTIES_PN ) )
                expMap.put( pair.propName, pair.propValue );
        }
        String  user    = CPConstants.USER_PROPERTIES_PN;
        expMap.put( user, userIniFile.getAbsolutePath() );
        
        ChildProcess    childProcess    = new ChildProcess();
        childProcess.addEnvProperties( toAddEnv );
        childProcess.addCmdProperties( toAddCmd );
        makeAppIniFile( toAddAppIni );
        makeUserIniFile( toAddUserIni );
        childProcess.addUserIniOption();
        
        childProcess.startChildProcess();
        for ( Entry<String,String> entry : expMap.entrySet() )
        {
            String  name    = entry.getKey();
            String  expVal  = entry.getValue();
            String  actVal  = getPropVal( name );
            assertEquals( expVal, actVal, name );
        }
    }
    
    /**
     * Initialize allProps with the names and 
     * default values of all properties.
     * The intent is to have test data available
     * for each test.
     */
    private static void initTestData()
    {
        // Get all property names and their default values.
        for ( Field pnField : CPConstants.class.getFields() )
        {
            String  fieldName   = pnField.getName();
            if ( fieldName.endsWith( "_PN" ) )
            {
                int     pNameLen    = fieldName.length();
                String  pNamePrefix = fieldName.substring( 0, pNameLen - 3 );
                String  dvName      = pNamePrefix + "_DV";
                
                try
                {
                    Field   dvField = CPConstants.class.getField( dvName );
                    String  propName = (String)pnField.get( null );
                    String  propDefault = (String)dvField.get( null );
                    allProps.add( new Pair( propName, propDefault ) );
                }
                catch ( NoSuchFieldException | IllegalAccessException exc )
                {
                    // These exceptions are fatal.
                    String  msg = dvName + ": field not found";
                    System.err.println( msg );
                    exc.printStackTrace();
                    System.exit( 1 );
                }
            }
        }
    }
    
    /**
     * Given a property name, get the corresponding value
     * from the child process.
     * An IOException will cause the test to fail.
     * 
     * @param propName  the given property name
     * 
     * @return  the value corresponding to propName
     */
    private String getPropVal( String propName )
    {
        String  propVal = null;
        try
        {
            childStdin.println( propName );
            propVal = childStdout.readLine();
        }
        catch ( IOException exc )
        {
            String  msg = exc.getMessage();
            exc.printStackTrace();
            fail( msg );
        }
        
        return propVal;
    }
    
    /**
     * Convert a list of name/value pairs
     * to a Map that maps name to value.
     * 
     * @param list  list to convert
     * 
     * @return the converted map
     */
    private Map<String,String> cvtListToMap( List<Pair> list )
    {
        Map<String,String>  map = new HashMap<>();
        for ( Pair pair : list )
            map.put( pair.propName, pair.propValue );
        return map;
    }
    
    /**
     * Sends the exit command to the child process (if any)
     * and waits for it to terminate.
     */
    private void killChildProcess()
    {
        if ( childProcess != null )
        {
            assertNotNull( childStdin );
            childStdin.println( PropertyTesterApp.EXIT_COMMAND );
            boolean status  = false;
            try
            {
                status = 
                    childProcess.waitFor( waitForTimeout, waitForTimeUnit );
                if ( !status )
                    childProcess.destroyForcibly();
                childProcess = null;
                assertTrue( status, "Child process failed to terminate" );
            }
            catch ( InterruptedException exc )
            {
                String  msg =
                    "Unexpected exception while closing "
                    + "child process";
                exc.printStackTrace();
                childProcess = null;
                fail( msg, exc );
            }
        }
    }

    /**
     * Close a given resource.
     * The given resource may be null,
     * in which case the operation is skipped.
     * 
     * @param closeable the given resource; may be null.
     */
    private void closeResource( AutoCloseable closeable )
    {
        try
        {
            if ( closeable != null )
                closeable.close();
        }
        catch ( Exception exc )
        {
            String  className   = closeable.getClass().getName();
            String  msg         =
                "Unexpected error attempting to close " + className;
            exc.printStackTrace();
            fail( msg );
        }
    }
    
    /**
     * Given a list of name/value property pairs,
     * initializes the user ini file.
     * To initial the file to an empty state
     * pass an empty list.
     * <p>
     * Precondition: 
     * the file exists and is writable.
     * </p>
     * 
     * @param properties    the given list of property pairs
     */
    private void makeUserIniFile( List<Pair> properties )
    {
        try ( 
            FileOutputStream  outStream   = 
                new FileOutputStream( userIniFile );
            PrintWriter writer = new PrintWriter( outStream )
        )
        {
            for ( Pair pair : properties )
                writer.println( pair );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            String  msg = 
                "Initialize app ini file failure: " + exc.getMessage();
            fail( msg );
        }
    }
    
    /**
     * Given a list of name/value property pairs,
     * initializes the user ini file.
     * To initial the file to an empty state
     * pass an empty list.
     * <p>
     * Precondition: 
     * the file exists and is writable.
     * </p>
     * 
     * @param properties    the given list of property pairs
     */
    private void makeAppIniFile( List<Pair> properties )
    {
        try ( 
            FileOutputStream  outStream   = 
                new FileOutputStream( appIniFile );
            PrintWriter writer = new PrintWriter( outStream )
        )
        {
            for ( Pair pair : properties )
                writer.println( pair );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            String  msg = 
                "Initialize user ini file failure: " + exc.getMessage();
            fail( msg );
        }
    }
    
    /**
     * Encapsulates a child process.
     * This includes the command line options
     * to pass to the child,
     * the environment within which
     * the child is started,
     * and the class path
     * to add to the child's command line.
     * 
     * @author Jack Straub
     */
    private class ChildProcess
    {
        private final List<Pair>    cmdPairs        = new ArrayList<>();
        private final List<Pair>    envPairs        = new ArrayList<>();
        private final String        classPath       =
            System.getProperty( "java.class.path" );
        private final StringBuilder classPathBldr   =
            new StringBuilder( classPath );
        
        /**
         * Adds a list of name/value pairs
         * to be defined as system properties
         * on the child process's command line.
         * 
         * @param pairs the name/value pairs to add
         */
        public void addCmdProperties( List<Pair> pairs )
        {
            for ( Pair pair : pairs )
                cmdPairs.add( pair );
        }
        
        /**
         * Adds a list of name/value pairs
         * to be defined in child process's environment.
         * 
         * @param pairs the name/value pairs to add
         */
        public void addEnvProperties( List<Pair> pairs )
        {            
            for ( Pair pair : pairs )
                envPairs.add( pair );
        }
        
        /**
         * Adds a file path 
         * to the <em>beginning of the class path</em>
         * under which to start the child process.
         * 
         * @param path  the file path to add
         */
        public void addClassPath( String path )
        {
            classPathBldr.insert( 0, classPathSep );
            classPathBldr.insert( 0, path );
        }
        
        /**
         * Convenience method 
         * to add the location
         * of the user's ini file
         * as an option
         * on the child process's command line.
         */
        public void addUserIniOption()
        {
            Pair    pair    =
                new Pair( CPConstants.USER_PROPERTIES_PN,
                          userIniFile.getAbsolutePath()
                        );
            cmdPairs.add( pair );
        }
        
        /**
         * Start a child process, 
         * applying the give environment variables
         * and properties.
         * Child input/output streams 
         * are established in instance variables.
         * It is a fatal mistake
         * to attempt to start a child
         * when another child instance
         * is being executed.
         * 
         * @param clazz     Class class containing 
         *                  the main method of the child process
         */
        public void startChildProcess()
        {
            assertNull( childProcess );
            assertNull( childStdin );
            assertNull( childStdout );
            
            // Find the java executable to start the child process
            String          javaHome    = System.getProperty( "java.home" );
            StringBuilder   bldr        = new StringBuilder( javaHome );
            bldr.append( File.separator ).append( "bin" )
                .append( File.separator ).append( "java" );
            String          javaBin     = bldr.toString();
            
            // Add the temp dir to the class path so that the app ini
            // file that it contains will take precedence over the 
            // normal app ini file.
            addClassPath( iniDir );
            
            // Initiate the list that will encapsulate the command used to execute
            // the child process. Each token in the list will wind up as an.
            // argument on the command line, for example:
            //     JAVA_HOME\bin\java --class-path .;lib/toots.jar SampleChildClass
            List<String>    command     = new ArrayList<>();
            command.add( javaBin );
            command.add(  "--class-path" );
            command.add( classPathBldr.toString() );
            
            // Add all the property declarations to the command line list
            for ( Pair pair : cmdPairs  )
            {
                StringBuilder   prop    = 
                    new StringBuilder( "-D" ).append( pair );
                command.add( prop.toString() );
            }
            // Name of class to execute is last on command line
            command.add( PropertyTesterApp.class.getName() );
            
            // Create the process builder and configure the environment
            // that will be used when the child process is executed.
            ProcessBuilder procBldr = new ProcessBuilder(command);
            Map<String, String> env = procBldr.environment();
            env.clear();
            for ( Pair pair : envPairs )
                env.put( pair.propName, pair.propValue );
            try
            {
                // Start the child process. Note that the start method can
                // throw an IOEception.
                childProcess = procBldr.start();
            
                // Get an input stream to read the child process's stdout.
                InputStream         inStream    = childProcess.getInputStream();
                InputStreamReader   inReader    = new InputStreamReader( inStream );
                childStdout = new BufferedReader( inReader );
                
                // Get an output stream to write to child's stdin
                OutputStream        outStream  = childProcess.getOutputStream();
                childStdin = new PrintWriter( outStream, true );
            }
            catch ( IOException exc )
            {
                String  msg =
                    "Failed to create child process " + exc.getMessage();
                exc.printStackTrace();
                fail( msg );
            }
        }
    }
    
    /**
     * Encapsulates a property name/value pair.
     * 
     * @author Jack Straub
     *
     */
    private static class Pair
    {
        /** Name of the encapsulated property. */
        public final String propName;
        /** Value of the encapsulated property. */
        public final String propValue;
        
        /**
         * Constructor.
         * 
         * @param propName      name of the encapsulated property
         * @param propValue     value of the encapsulated property
         */
        public Pair( String propName, String propValue )
        {
            this.propName = propName;
            this.propValue = propValue;
        }
        
        /**
         * Constructor.
         * Create a new Pair
         * using the name from an existing pair
         * and a given property value.
         * 
         * @param pair          given Pair
         * @param propValue     given property value
         */
        public Pair( Pair pair, String propValue )
        {
            this.propName = pair.propName;
            this.propValue = propValue;
        }
        
        @Override
        public String toString()
        {
            StringBuilder   bldr    = 
                new StringBuilder( propName )
                    .append( "=" ).append( propValue );
            return bldr.toString();
        }
    }
}
