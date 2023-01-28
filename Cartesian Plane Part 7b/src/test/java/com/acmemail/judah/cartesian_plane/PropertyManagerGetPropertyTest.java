package com.acmemail.judah.cartesian_plane;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.PropertyTesterApp;
import util.TestUtils;

class PropertyManagerGetPropertyTest
{
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
    
    private Process         childProcess    = null;
    private BufferedReader  childStdout     = null;
    private PrintWriter     childStdin      = null;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
    }
    
    /**
     * 
     */
    @AfterEach
    public void afterEach()
    {
        killChildProcess();
        closeResource( childStdout );
        childStdout = null;
        closeResource( childStdin );
        childStdin = null;
    }

    @Test
    void test()
    {
        // List of all propertied under test and their default values
        // from CPConstants; must be at least 6.
        List<Pair>  allProps    = new ArrayList<>();
        allProps.add( getNameDValuePair( "MW_WIDTH" ) );
        allProps.add( getNameDValuePair( "MW_HEIGHT" ) );
        allProps.add( getNameDValuePair( "MW_BG_COLOR" ) );
        allProps.add( getNameDValuePair( "GRID_UNIT" ) );
        allProps.add( getNameDValuePair( "AXIS_COLOR" ) );
        allProps.add( getNameDValuePair( "AXIS_WEIGHT" ) );
        
        // put about 2/3 of them in the environment with unique values
        List<Pair>  envProps    = new ArrayList<>();
        int lastEnv = 2 * allProps.size() / 3;
        for ( int inx = 0 ; inx < lastEnv ; ++inx )
        {
            Pair    pair    = allProps.get( inx );
            String  newVal  = pair.propName + "_ENV";
            Pair    newPair = new Pair( pair.propName, newVal );
            envProps.add( newPair );
        }
        
        // put half of those on the command line with unique values
        List<Pair>  clProps = new ArrayList<>();
        int         lastCL  = envProps.size() / 2;
        for ( int inx = 0 ; inx < lastCL ; ++inx )
        {
            Pair    pair    = envProps.get( inx );
            String  newVal  = pair.propName + "_CL";
            Pair    newPair = new Pair( pair.propName, newVal );
            clProps.add( newPair );
        }
        
        // make some maps for convenience
        Map<String,String>  envMap  = cvtListToMap( envProps );
        Map<String,String>  clMap   = cvtListToMap( clProps );
        
        // start the child process; interrogate PropertyManager
        Class<?>    clazz   = PropertyTesterApp.class;
        startChildProcess( clazz, envProps, clProps );
        TestUtils.pause( 1000 );
        for ( Pair pair : allProps )
        {
            String  propName    = pair.propName;    
            String  pmVal       = getPropVal( propName );
            String  expVal      = null;
            if ( (expVal = clMap.get( propName )) != null )
                assertEquals( expVal, pmVal );
            else if ( (expVal = envMap.get( propName )) != null )
                assertEquals( expVal, pmVal );
            else
                assertEquals( pair.propValue, pmVal );
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
        System.err.println( childProcess.isAlive() );
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
    
    private Map<String,String> cvtListToMap( List<Pair> list )
    {
        Map<String,String>  map = new HashMap<>();
        for ( Pair pair : list )
            map.put( pair.propName, pair.propValue );
        return map;
    }
    
    /**
     * Convenience method to get the name of a property
     * and its default value from CPConstants.
     * Given a prefix such as AXIS_COLOR,
     * returns the equivalent of 
     * <em>new Pair(AXIS_COLOR_PN, AXIS_COLOR_DV ).</em>
     * 
     * @param prefix    the given prefix
     * 
     * @return  a Pair representing the name and default value
     *          of the property beginning with <em>prefix</em>
     */
    private Pair getNameDValuePair( String prefix )
    {
        Pair    nameValPair     = null;
        String  expNameVar      = prefix + "_PN";
        String  expDValueVar    = prefix + "_DV";
        try
        {
            Field   expNameField    = 
                CPConstants.class.getField( expNameVar );
            Field   expDValueField  = 
                CPConstants.class.getField( expDValueVar );
            String  expName         = (String)expNameField.get( null );
            String  expDValue       = (String)expDValueField.get( null );
            nameValPair = new Pair( expName, expDValue );
        }
        catch ( NoSuchFieldException| IllegalAccessException exc )
        {
            String  msg =
                "Reflection failure: "
                + exc.getClass().getName()
                + ", "
                + exc.getMessage();
            fail( msg );
        }
        return nameValPair;
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
     * @param envVars   name/value pairs to place in child environment
     * @param props     name/value pairs to define as options
     *                  on the command line that starts the child
     */
    private void 
    startChildProcess( Class<?> clazz, List<Pair> envVars, List<Pair> props )
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
        
        // Get the classpath to use to start the child process
        String          classpath   = System.getProperty( "java.class.path" );
        
        // Get the name of the class the encapsulates the child process
        String          className   = clazz.getName();
        
        // Initiate the list that will encapsulate the command used to execute
        // the child process. Each token in the list will wind up as an.
        // argument on the command line, for example:
        //     JAVA_HONME\bin\java --class-path .;lib/toots.jar SampleChildClass
        List<String>    command     = new ArrayList<>();
        command.add( javaBin );
        command.add(  "--class-path" );
        command.add( classpath );
        
        // Add all the property declarations to the command line list
        for ( Pair pair : props )
        {
            StringBuilder   prop    = new StringBuilder( "-D" );
            prop.append( pair.propName )
                .append( "=" )
                .append( pair.propValue );
            command.add( prop.toString() );
        }
        // Name of class to execute is last on command line
        command.add( className );
        
        // Create the process builder and configure the environment
        // that will be used when the child process is executed.
        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> env = builder.environment();
        env.clear();
        for ( Pair pair : envVars )
            env.put( pair.propName, pair.propValue );
        
        try
        {
            // Start the child process. Note that the start method can
            // throw an IOEception.
            childProcess = builder.start();
        
            // Get an input stream to read the child process's stdout.
            InputStream         inStream    = childProcess.getInputStream();
            InputStreamReader   inReader    = new InputStreamReader( inStream );
            childStdout = new BufferedReader( inReader );
            
            // Get an output stream to write to child's stdin
            OutputStream        outStream  = childProcess.getOutputStream();
            childStdin = new PrintWriter( outStream, true );
            System.err.println( childProcess.isAlive() );
        }
        catch ( IOException exc )
        {
            String  msg =
                "Failed to create child process " + exc.getMessage();
            exc.printStackTrace();
            fail( msg );
        }
        System.err.println( childProcess.isAlive() );
    }
    
    private void killChildProcess()
    {
        if ( childProcess != null )
        {
            if ( childStdin != null )
                childStdin.println( PropertyTesterApp.EXIT_COMMAND );
            try
            {
                childProcess.waitFor( waitForTimeout, waitForTimeUnit );
            }
            catch ( InterruptedException exc )
            {
                String  msg =
                    "Unexpected InterruptedException from child process";
                exc.printStackTrace();
                fail( msg );
            }
            childProcess = null;
        }
    }

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
    }
}
