package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;

public class ProfileDecompiler
{
    /** Container for decompiled Profile. */
    private final Profile   profile     = new Profile();
    /** If quiet is true, input parsing will not report errors. */
    private final boolean   quiet;
    
    private Object currParseObj = null;
    
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
    
    public static Profile of( Stream<String> stream )
    {
        ProfileDecompiler   compiler    = new ProfileDecompiler( stream );
        return compiler.profile;
    }
    
    private ProfileDecompiler( Stream<String> stream )
    {
        this( stream, false );
    }
    
    /**
     * Constructor.
     * All properties are configured
     * from the given stream.
     * 
     * @param stream    the given stream
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

    private String[] splitArgString0( String str )
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
    
    private void decompile( String[] arr )
    {
        if ( Profile.CLASS.equals( arr[0] ) )
            decompileClass( arr );
        else
            decompileProperty( arr );
    }
    
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
        else 
            decompileProperty( (LinePropertySet)currParseObj, arr );
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
    
    private int parseInt( String sVal )
    {
        int iVal    = 0;
        try
        {
            iVal = Integer.parseInt( sVal );
        }
        catch ( NumberFormatException exc )
        {
            String  error   = "Invalid integer: " + sVal;
            postParseError( error );
        }
        return iVal;
    }
    
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
    
    private Color parseColor( String sVal )
    {
        int iVal    = 0;
        try
        {
            iVal = Integer.decode( sVal );
        }
        catch ( NumberFormatException exc )
        {
            String  error   = "Invalid Color: " + sVal;
            postParseError( error );
        }
        Color   color   = new Color( iVal );
        return color;
    }
    
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
