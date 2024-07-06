package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;

/**
 * This class parses a stream of strings,
 * producing a Profile containing the properties
 * encapsulated by the stream.
 * The syntax of the input
 * is described by the {@link Profile} class.
 * 
 * @author Jack Straub
 */
public class ProfileDecompiler
{
    /** Container for decompiled Profile. */
    private final Profile   profile     = new Profile();
    /** If quiet is true, input parsing will not report errors. */
    private final boolean   quiet;
    
    /** 
     * A reference to the GraphPropertySet or LinePropertySet
     * that is the current target of the parse operation.
     */
    private Object currParseObj = null;
    
    /**
     * Produces a Profile
     * initialized from a Stream&lt;String&gt;
     * obtained from the given InputStream.
     * 
     * @param inStream  the given input stream
     * 
     * @return  the Profile object
     */
    public static Profile of( InputStream inStream )
    {
        Profile profile = null;
        try ( InputStreamReader reader = 
            new InputStreamReader( inStream )
        )
        {
            profile = of( inStream );
        }
        catch ( IOException exc )
        {
            postIOError( exc );
            profile = new Profile();
        }
        return profile;
    }
    
    /**
     * Produces a Profile
     * initialized from a Stream&lt;String&gt;
     * obtained from the given InputStreamReader.
     * 
     * @param reader  the given InputStreamReader
     * 
     * @return  the Profile object
     */
    public static Profile of( InputStreamReader reader )
    {
        Profile profile = null;
        try ( BufferedReader bufReader = new BufferedReader( reader  ) )
        {
            ProfileDecompiler   compiler =
                new ProfileDecompiler( bufReader.lines() );
            profile = compiler.profile;
        }
        catch ( IOException exc )
        {
            postIOError( exc );
            profile = new Profile();
        }
        return profile;
    }
    
    /**
     * Produces a Profile
     * initialized from the given Stream&lt;String&gt;.
     * 
     * @param stream  the given Stream&lt;String&gt;
     * 
     * @return  the Profile object
     */
    public static Profile of( Stream<String> stream )
    {
        ProfileDecompiler   compiler    = new ProfileDecompiler( stream );
        return compiler.profile;
    }
    
    /**
     * Constructor.
     * Equivalent to invoking 
     * ProfileDecompiler(Stream&lt;String&gt;,boolean)
     * and passing false for the Boolean parameter.
     * 
     * @param stream
     */
    private ProfileDecompiler( Stream<String> stream )
    {
        this( stream, false );
    }
    
    /**
     * Constructor.
     * All properties are configured
     * from the given stream.
     * Errors will be reported
     * via error dialogs
     * unless the <em>quiet</em> parameter is true,
     * in which case errors
     * will be silently ignored.
     * Property values not provided in the given stream
     * are initialized from the PropertyManager.
     * 
     * @param stream    the given stream
     * @param quiet     true to suppress error reporting
     * 
     * @see Profile#getProperties()
     */
    public ProfileDecompiler( Stream<String> stream, boolean quiet )
    {
        this.quiet = quiet;
        stream
            .map( String::trim )
            .filter( s -> !s.isBlank() )
            .map( this::splitArgString )
            .filter( a -> a.length == 2 )
            .forEach( this::decompile );
    }

    /**
     * Parses a given string into two arguments
     * delimited by a colon and optional spaces.
     * If the string 
     * does not consist of exactly two arguments
     * an error is reported,
     * and an array of two empty strings is returned.
     * 
     * @param str   the given string
     * 
     * @return 
     *      a two-dimensional array 
     *      containing the result of the parsing
     */
    private String[] splitArgString( String str )
    {
        String[]    args    = str.split( " *: *" );
        if ( args.length != 2 )
        {
            String  error   = "Invalid input string: " + str;
            postParseError( error );
        }
        return args;
    }
/*
    // Alternative method that uses StringTokenizer instead of 
    // String.split to parse a string into two arguments
    private String[] splitArgString( String str )
    {
        String[]        args    = { "", "" };
        StringTokenizer tizer   = new StringTokenizer( ": " );
        if ( tizer.countTokens() != 2 )
        {
            String  error   = "Invalid input string: " + str;
            postParseError( error );
        }
        else
        {
            args[0] = tizer.nextToken();
            args[1] = tizer.nextToken();
        }
        return args;
    }
end of alternative splitArgString method. */
    
    /**
     * Determines whether a given name/value pair
     * contains a class declaration
     * or a property declaration
     * and decompiles it accordingly.
     * If the name is Profile.CLASS,
     * the given pair 
     * is assumed to be a class declaration,
     * otherwise it is assumed to be 
     * a property declaration.
     * 
     * @param arr   the given name/value pair
     */
    private void decompile( String[] arr )
    {
        if ( Profile.CLASS.equals( arr[0] ) )
            decompileClass( arr );
        else
            decompileProperty( arr );
    }
    
    /**
     * Uses the value part of a given name/value pair
     * to locate the GraphPropertySet object
     * of one of the LinePropertySets
     * contained in the Profile being initialized.
     * The value is the simple class name
     * of the target object, for example
     * LinePropertySetGridLines or GraphPropertySetMW.
     * The {@linkplain #currParseObj} instance variable
     * is initialized with a reference
     * to the target object.
     * If a target object cannot be found
     * {@linkplain #currParseObj} is set to null.
     * 
     * @param arr   the given name/value pair
     */
    private void decompileClass( String[] arr )
    {
        currParseObj = null;
        if ( arr[1].equals( GraphPropertySetMW.class.getSimpleName() ) )
            currParseObj = profile.getMainWindow();
        else
            currParseObj = profile.getLinePropertySet( arr[1] );
        if ( currParseObj == null )
        {
            String  error   = "Invalid class tag value: " + arr[1];
            postParseError( error );
        }
    }
    
    /**
     * Decompiles a name/value pair
     * into a property value.
     * There are four possibilities:
     * <ol>
     * <li>
     *      The property name is Profile.GRID_UNIT,
     *      in which case grid-unit property
     *      of the target profile is initialized
     *      from the value.
     * </li>
     * <li>
     *      The {@linkplain #currParseObj} instance variable
     *      is a reference to a GraphPropertySet object,
     *      in which case the name/value pair
     *      is assumed to apply to a property
     *      in the GraphPropertySet.
     * </li>
     * <li>
     *      The {@linkplain #currParseObj} instance variable
     *      is a reference to a LinePropertySet object,
     *      in which case the name/value pair
     *      is assumed to apply to a property
     *      in the LinePropertySet.
     * </li>
     * </ol>
     * 
     * Except in the case where
     * property name is Profile.GRID_UNIT,
     * it is an error 
     * if {@linkplain #currParseObj} is not a reference to
     * anything but a LinePropertySet or GraphPropertySet object.
     * 
     * @param arr
     */
    private void decompileProperty( String[] arr )
    {
        if ( Profile.GRID_UNIT.equals( arr[0] ) )
        {
            float   gridUnit    = parseFloat( arr[1] );
            profile.setGridUnit( gridUnit );
        }
        else if ( currParseObj == null )
        {
            String  error   =
                "No context for parsing property \"" + arr[0]
                + "\"; current parse object is null";
            postParseError( error );
        }
        else if ( currParseObj instanceof GraphPropertySet )
            decompileProperty( (GraphPropertySet)currParseObj, arr );
        else if ( currParseObj instanceof LinePropertySet )
            decompileProperty( (LinePropertySet)currParseObj, arr );
        else
        {
            String  error   = 
                "Internal error; currParseObj is an unexpected value.";
            System.err.println( error );
            postParseError( error );
        }
    }
    
    private void decompileProperty( GraphPropertySet set, String[] arr )
    {
        switch ( arr[0] )
        {
        case Profile.BG_COLOR:
            set.setBGColor( parseColor( arr[1] ) );
            break;
        case Profile.DRAW:
            set.setFontDraw( parseBoolean( arr[1] ) );
            break;
        case Profile.FG_COLOR:
            set.setFGColor( parseColor( arr[1] ) );
            break;
        case Profile.FONT_NAME:
            set.setFontName( arr[1] );
            break;
        case Profile.FONT_SIZE:
            set.setFontSize( parseFloat( arr[1] ) );
            break;
        case Profile.FONT_STYLE:
            set.setFontStyle( arr[1] );
            break;
        default:
            String  error   =
            "\"" + arr[1] + "\" is not a valid graph property";
            postParseError( error );
        }
    }
    
    /**
     * Converts a given name/value pair
     * into the value of a property
     * in a given LinePropertySet.
     * An error is reported
     * if the name does not match
     * the name of a property,
     * or the value is not appropriate
     * for the property type.
     * 
     * @param set   the given LinePropertySet
     * @param arr   the given name/value pair
     */
    private void decompileProperty( LinePropertySet set, String[] arr )
    {
        switch ( arr[0] )
        {
        case Profile.COLOR:
            set.setColor( parseColor( arr[1] ) );
            break;
        case Profile.DRAW:
            set.setDraw( parseBoolean( arr[1] ) );
            break;
        case Profile.LENGTH:
            set.setLength( parseFloat( arr[1] ) );
            break;
        case Profile.SPACING:
            set.setSpacing( parseFloat( arr[1] ) );
            break;
        case Profile.STROKE:
            set.setStroke( parseFloat( arr[1] ) );
            break;
        default:
            String  error   =
            "\"" + arr[1] + "\" is not a valid line property";
            postParseError( error );
        }
    }
    
    /**
     * Converts a given string to a decimal value
     * and returns the value.
     * If the string is not a valid number
     * an error is reported
     * and 0 is returned.
     * 
     * @param sVal  the given string
     * 
     * @return  the decimal value of the given string
     */
    private float parseFloat( String sVal )
    {
        float   fVal    = 0f;
        try
        {
            fVal = Float.parseFloat( sVal );
        }
        catch ( NumberFormatException exc )
        {
            String  error   = "Invalid decimal number: " + fVal;
            postParseError( error );
        }
        return fVal;
    }
    
    
    /**
     * Converts a given string to an integer value
     * and returns the value.
     * The string may be encoded
     * using decimal, hexadecimal, or octal notation.
     * If the string is not a valid integer
     * an error is reported
     * and 0 is returned.
     * 
     * @param sVal  the given string
     * 
     * @return  the integer value of the given string
     */
    private int parseInt( String sVal )
    {
        int iVal    = 0;
        try
        {
            iVal = Integer.decode( sVal );
        }
        catch ( NumberFormatException exc )
        {
            String  error   = "Invalid integer: " + sVal;
            postParseError( error );
        }
        return iVal;
    }
    
    
    /**
     * Converts a given string to a Boolean value
     * and returns the value.
     * If the string is not a Boolean value,
     * an error is reported
     * and false is returned.
     * 
     * @param sVal  the given string
     * 
     * @return  the decimal value of the given string
     */
    private boolean parseBoolean( String sVal )
    {
        boolean bVal    = false;
        try
        {
            bVal = Boolean.parseBoolean( sVal );
        }
        catch ( IllegalArgumentException exc )
        {
            String  error   = "Invalid boolean value: " + bVal;
            postParseError( error );
        }
        return bVal;
    }
    
    
    /**
     * Converts a given string to a color
     * and returns the color.
     * The color is assumed 
     * to be represented by an integer (type int).
     * If the string is not a valid integer
     * an error is reported
     * and BLACK (rgb(0,0,0)) is returned.
     * 
     * @param sVal  the given string
     * 
     * @return  the decimal value of the given string
     */
    private Color parseColor( String sVal )
    {
        int iVal    = parseInt( sVal );
        Color   color   = new Color( iVal );
        return color;
    }
    
    /**
     * Posts a dialog containing an error message,
     * unless the quiet instance variable is true
     * in which case the error is silently ignored.
     * 
     * @param error the error message to post
     */
    private void postParseError( String error )
    {
        if ( !quiet )
        {
            JOptionPane.showMessageDialog(
                null, 
                error, 
                "Parse Error",
                JOptionPane.ERROR_MESSAGE
            );
            throw new Error( error );
        }
    }
    
    /**
     * Posts an error message
     * obtained from the given IOException object.
     * This operation is not affected
     * by the quiet instance variable.
     * 
     * @param exc   the given exception object
     */
    private static void postIOError( IOException exc )
    {
        String  error   = "I/O error: " + exc.getMessage();
        JOptionPane.showMessageDialog(
            null, 
            error, 
            "Parse Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
