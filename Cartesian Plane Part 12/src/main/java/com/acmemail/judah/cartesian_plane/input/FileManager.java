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

public class FileManager
{
    private static JFileChooser chooser;
    private static List<String> lines   = new ArrayList<>();
    
    static
    {
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
    
    public static void save( Equation equation )
    {
        int     action  = chooser.showSaveDialog( null );
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            save( chooser.getSelectedFile(), equation );
        }
    }
    
    public static Equation open()
    {
        int         action      = chooser.showOpenDialog( null );
        Equation    equation    = null;
        if ( action == JFileChooser.APPROVE_OPTION )
        {
            equation = open( chooser.getSelectedFile() );
        }
        return equation;
    }
    
    public static void save( String path, Equation equation )
    {
        File    file    = new File( path );
        save( file, equation );
    }
    
    public static Equation open( String path )
    {
        File        file        = new File( path );
        Equation    equation    = open( file );
        return equation;
    }
    
    public static void save( File file, Equation equation )
    {
        try ( PrintWriter pWriter = new PrintWriter( file ) )
        {
            save( pWriter, equation );
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
    }
    
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
        return equation;
    }

    public static void save( PrintWriter pWriter, Equation equation ) 
        throws IOException
    {
        writeRange( equation );
        writeVars( equation );
        writeParameterNames( equation );
        writeExpressions( equation );
        lines.forEach( pWriter::println );
    }
    
    public static Equation open( BufferedReader bufReader )
        throws IOException
    {
        InputParser     parser  = new InputParser();
        CommandReader   reader  = new CommandReader( bufReader );
        reader.stream().forEach( parser::parseInput );
        
        Equation    equation    = parser.getEquation();
        return equation;
    }
   
    private static void writeRange( Equation equation )
    {
        lines.add( "start " + equation.getRangeStart() );
        lines.add( "end " + equation.getRangeEnd() );
        lines.add( "step " + equation.getRangeStep() );
    }
    
    private static void writeParameterNames( Equation equation )
    {
        lines.add( "param " + equation.getParam() );
        lines.add( "radius " + equation.getRadius() );
        lines.add( "theta " + equation.getTheta() );
    }
    
    private static void writeVars( Equation equation )
    {
        StringBuilder       bldr    = new StringBuilder( "set " );
        Map<String,Double>  varMap  = equation.getVars(); 
        varMap.forEach( (n,v) -> 
            bldr.append( String.format( "%s=%f,", n, v ) )
        );
        // delete the las comma
        bldr.deleteCharAt( bldr.length() - 1 );
        lines.add( bldr.toString() );
    }
    
    private static void writeExpressions( Equation equation )
    {
        lines.add("y= " + equation.getYExpression() );
        lines.add("x= " + equation.getXExpression() );
        lines.add("t= " + equation.getTExpression() );
        lines.add("r= " + equation.getRExpression() );
    }
}
