package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Provides methods
 * to save and restore
 * configuration data for equations.
 * 
 * @author Jack Straub
 */
public class FileManager
{
    /** Used to assemble lines to be written to an output file. */
    private static final List<String> lines   = new ArrayList<>();
    /** Used to prompt operator to select a file. */
    public static final JFileChooser chooser;
    
    /**
     * True if the previously executed operation succeeded;
     * false if it failed, or if there is no previous operation.
     */
    private static boolean  lastResult  = false;
    
    // Instantiate and configure the JFileChooser.
    static
    {
        // This property typically contains the path to the directory
        // from which this application was executed.
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
    }
    
    /**
     * Prevent this class from being instantiated.
     */
    private FileManager()
    {
    }
    
    /**
     * Returns a File object encapsulating
     * the last opened file.
     * 
     * @return
     */
    public static File getLastFile()
    {
        File    file    = chooser.getSelectedFile();
        return file;
    }
    
    /**
     * Returns the status of the last executed operation.
     * 
     * @return  the status of the last executed operation
     */
    public static boolean getLastResult()
    {
        return lastResult;
    }
    
    /**
     * Saves the given equation to a file.
     * The file is selected by the operator
     * using a file-selection dialog.
     * 
     * @param equation the given equation
     */
    public static void save( Equation equation )
    {
        int     action  = chooser.showSaveDialog( null );
        lastResult = false;
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            lastResult = true;
            save( chooser.getSelectedFile(), equation );
        }
    }
    
    /**
     * Create an equation from configuration data
     * found in a text file.
     * The target text file
     * is selected by the operator
     * using a file-selection dialog.
     * If successful,
     * the new equation is returned,
     * otherwise null is returned.
     * 
     * @return the target equation, or null if operation fails
     */
    public static Equation open()
    {
        int         action      = chooser.showOpenDialog( null );
        Equation    equation    = null;
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            equation = open( chooser.getSelectedFile() );
        }
        lastResult = equation != null;
        return equation;
    }
    
    /**
     * Saves the given equation
     * to a given file.
     * 
     * @param path      the given file
     * @param equation  the given equation
     */
    public static void save( String path, Equation equation )
    {
        File    file    = new File( path );
        save( file, equation );
    }
    
    /**
     * Creates an equation
     * from configuration data
     * read from a given file.
     * If successful, the equation is returned,
     * otherwise null is returned.
     * 
     * @param path  the given file
     * 
     * @return the target equation, or null if operation fails
     */
    public static Equation open( String path )
    {
        File        file        = new File( path );
        Equation    equation    = open( file );
        return equation;
    }
    
    /**
     * Saves the given equation
     * to a given file.
     * 
     * @param file      the given file
     * @param equation  the given equation
     */
    public static void save( File file, Equation equation )
    {
        try ( PrintWriter pWriter = new PrintWriter( file ) )
        {
            save( pWriter, equation );
            lastResult = true;
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
            lastResult = false;
        }
    }
    
    /**
     * Creates an equation
     * from configuration data
     * read from a given file.
     * If successful, the equation is returned,
     * otherwise null is returned.
     * 
     * @param file  the given file
     * 
     * @return the target equation, or null if operation fails
     */
    public static Equation open( File file )
    {
        Equation    equation    = null;
        try ( 
            FileReader fReader = new FileReader( file );
            BufferedReader  bReader = new BufferedReader( fReader );
        )
        {
            equation = open( bReader );
        }
        catch ( IOException exc )
        {
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
        lastResult = equation != null;
        return equation;
    }

    /**
     * Saves the given equation
     * to a given output stream.
     * 
     * @param pWriter   the given output stream
     * @param equation  the given equation
     * 
     * @throws  IOException if an I/O error occurs
     */
    public static void save( PrintWriter pWriter, Equation equation ) 
        throws IOException
    {
        lines.clear();
        lines.add( Command.EQUATION.toString() + " " + equation.getName() );
        writeRange( equation );
        writeVars( equation );
        writeParameterNames( equation );
        writeExpressions( equation );
        writeMiscellaneous( equation );
        lines.forEach( pWriter::println );
        lastResult = true;
    }
    
    /**
     * Creates an equation
     * from configuration data
     * read from a given input stream.
     * If successful, the equation is returned,
     * otherwise null is returned.
     * 
     * @param bufReader the given input stream
     * 
     * @return the target equation, or null if operation fails
     * 
     * @throws IOException if an I/O error occurs
     */
    public static Equation open( BufferedReader bufReader )
        throws IOException
    {
        InputParser     parser  = new InputParser();
        CommandReader   reader  = new CommandReader( bufReader );
        reader.stream().forEach( parser::parseInput );
        
        Equation    equation    = parser.getEquation();
        lastResult = true;
        return equation;
    }
   
    /**
     * Generates the commands
     * to specify the iteration range 
     * of a given equation.
     * 
     * @param equation  the given equation
     */
    private static void writeRange( Equation equation )
    {
        lines.add( "start " + equation.getRangeStartExpr() );
        lines.add( "end " + equation.getRangeEndExpr() );
        lines.add( "step " + equation.getRangeStepExpr() );
    }
    
    /**
     * Generates the commands
     * to configure the parameter names
     * (<em>parameter, radius, theta</em>)
     * determined from a given equation.
     * 
     * @param equation  the given equation
     */
    private static void writeParameterNames( Equation equation )
    {
        lines.add( "param " + equation.getParamName() );
        lines.add( "radius " + equation.getRadiusName() );
        lines.add( "theta " + equation.getThetaName() );
    }
    
    /**
     * Generates the commands
     * to configure the variable declarations
     * determined by a given equation.
     * 
     * @param equation  the given equation
     */
    private static void writeVars( Equation equation )
    {
        StringBuilder       bldr    = new StringBuilder( "set " );
        Map<String,Double>  varMap  = equation.getVars(); 
        varMap.forEach( (n,v) -> 
            bldr.append( String.format( "%s=%f,", n, v ) )
        );
        // delete the last comma
        bldr.deleteCharAt( bldr.length() - 1 );
        lines.add( bldr.toString() );
    }
    
    /**
     * Generates the commands
     * to configure the expressions
     * represented in a given equation.
     * 
     * @param equation  the given equation
     */
    private static void writeExpressions( Equation equation )
    {
        lines.add("y= " + equation.getYExpression() );
        lines.add("x= " + equation.getXExpression() );
        lines.add("t= " + equation.getTExpression() );
        lines.add("r= " + equation.getRExpression() );
    }
    
    /**
     * Generates the commands to set the plot and precision.
     */
    private static void writeMiscellaneous( Equation equation )
    {
        lines.add( "prec " + equation.getPrecision() );
        lines.add( "plot " + equation.getPlot() );
    }
}
