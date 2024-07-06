package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;

/**
 * This is a demonstration
 * of the {@linkplain ProfileDecompiler}.
 * It proceeds as follows:
 * <ol>
 * <li>
 *      A Profile is created (the <em>original</em> profile).
 *      It will contain default data 
 *      obtained from the PropertyManager.
 * </li>
 * <li>
 *      A second Profile is created 
 *      (the <em>expected revised</em> profile).
 *      The data it contains will be unique
 *      from that contained in the original profile.
 * </li>
 * <li>
 *      A stream from the revised profile is obtained.
 *      The stream is fed to the 
 *      {@linkplain ProfileDecompiler#of(Stream)} method
 *      to obtain a third Profile (the <em>actual revised</em> profile).
 * </li>
 * <li>
 *      A report is produced
 *      comparing the three Profiles.
 *      Examining the report,
 *      you should be able to confirm
 *      that the expected revised profile
 *      contains data distinct
 *      from the original profile,
 *      and that the data contained
 *      in the actual revised profile
 *      is equivalent to the data
 *      in the expected revised profile.
 * </li>
 * </ol>
 * 
 * @author Jack Straub
 */
public class ProfileDecompilerDemo1
{
    /**
     * Unicode check mark. Displayed at the end of a line item
     * in the report if the expected data matches the actual data.
     */
    private static final char   check               = '\u2713';
    /**
     * Unicode "no" symbol. Displayed at the end of a line item
     * in the report if the expected data differs from the actual data.
     */
    private static final char   wrong               = '\u20e0';
    
    /**
     * Simple names of all the LinePropertySet subclasses.
     * Use to obtain property sets from the profiles.
     */
    private static final String[]      lineSets     =
    {
        LinePropertySetAxes.class.getSimpleName(),
        LinePropertySetGridLines.class.getSimpleName(),
        LinePropertySetTicMajor.class.getSimpleName(),
        LinePropertySetTicMinor.class.getSimpleName(),
    };

    /** Window for displaying the report. */
    private final Log           log                 = new Log();
    /** The original profile. */
    private final Profile       origProfile         = new Profile();
    /** The expected revised profile. */
    private final Profile       expRevisedProfile   = new Profile();
    /** The actual revised profile. */
    private final Profile       actRevisedProfile;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ProfileDecompilerDemo1  demo    = new ProfileDecompilerDemo1();
        demo.report();
    }
    
    /**
     * Constructor.
     * Creates the three Profiles.
     * Initializes, but does not make visible,
     * the log that displays the report.
     */
    private ProfileDecompilerDemo1()
    {
        float   origGridUnit    = expRevisedProfile.getGridUnit();
        float   newGridUnit     = origGridUnit + 10;
        expRevisedProfile.setGridUnit( newGridUnit );
        revise( expRevisedProfile.getMainWindow() );
        Stream.of( lineSets )
            .map( expRevisedProfile::getLinePropertySet )
            .forEach( this::revise );
        
        Stream<String>  stream  = expRevisedProfile.getProperties();
        actRevisedProfile = ProfileDecompiler.of( stream );
        
        Dimension   logDim  = log.getPreferredSize();
        logDim.width = 300;
        log.setPreferredSize( logDim );
        log.setLocation( 200, 200 );
    }
    
    /**
     * Assigns unique values to
     * the properties of a given GraphPropertySet
     * that are guaranteed to be distinct
     * from the original values.
     * 
     * @param set   the given GraphPropertySet
     */
    private void revise( GraphPropertySet set )
    {
        Color   newBGColor  = getDistinctColor( set.getBGColor() );
        Color   newFGColor  = getDistinctColor( set.getFGColor() );
        String  newName     = getDistinctFontName( set.getFontName() );
        String  newStyle    = getDistinctFontStyle( set.getFontStyle() );
        float   newSize     = (int)set.getFontSize() + 10;
        boolean newDraw     = !set.isFontDraw();
        
        set.setBGColor( newBGColor );
        set.setFGColor( newFGColor );
        set.setFontName( newName );
        set.setFontSize( newSize );
        set.setFontStyle( newStyle );
        set.setFontDraw( newDraw );
    }
    
    
    /**
     * Assigns unique values to
     * the properties of a given LinePropertySet
     * that are guaranteed to be distinct
     * from the original values.
     * 
     * @param set   the given LinePropertySet
     */
    private void revise( LinePropertySet set )
    {
        Color   newColor    = getDistinctColor( set.getColor() );
        boolean newDraw     = !set.getDraw();
        float   newLength   = set.getLength() + 10;
        float   newSpacing  = set.getSpacing() + 10;
        float   newStroke   = set.getStroke() + 10;
        
        set.setColor( newColor );
        set.setDraw( newDraw );
        set.setLength( newLength );
        set.setSpacing( newSpacing );
        set.setStroke( newStroke );
    }
    
    /**
     * From a given font style value,
     * derive a new value
     * that is distinct from the given value.
     * 
     * @param styleIn   the given font style value
     * 
     * @return  a font style value distinct from styleIn
     */
    private String getDistinctFontStyle( int styleIn )
    {
        String  styleOut    = styleIn == Font.PLAIN ? "ITALIC" : "PLAIN";
        return styleOut;
    }
    
    /**
     * From a given color,
     * derive a new color
     * that is distinct from the given color.
     * 
     * @param colorIn   the given color
     * 
     * @return  a Color distinct from colorIn
     */
    private Color getDistinctColor( Color colorIn )
    {
        int     iColorIn    = colorIn.getRGB() & 0xFFFFFF;
        int     iColorOut   = ~iColorIn;
        Color   colorOut    = new Color( iColorOut );
        return colorOut;
    }
    
    /**
     * From a font name,
     * derive a new font name
     * that is distinct from the given font name.
     * 
     * @param nameIn   the given color
     * 
     * @return  a Color distinct from colorIn
     */
    private String getDistinctFontName( String nameIn )
    {
        String  nameOut = nameIn.equals( Font.MONOSPACED ) ? 
            Font.DIALOG : Font.MONOSPACED;
        return nameOut;
    }
    
    /**
     * Issues a report
     * comparing the values
     * of the three Profiles
     * encapsulated by this object.
     */
    private void report()
    {
        String  headFmt = "%10s %10s %10s %10s";
        String  header1 = 
            String.format( headFmt, "Prop", "Orig", "Exp", "Act" );
        StringBuilder   bldr    = new StringBuilder();
        IntStream.range( 0, header1.length() + 2 )
            .forEach( i -> bldr.append( '=' ) );
        String header2 = bldr.toString();
        
        log.append( header1 );
        log.append( header2 );
        
        reportGridUnit();
        reportMW();
        Stream.of( lineSets )
            .forEach( this::reportLineSet );

        log.setVisible( true );
    }
    
    /**
     * Formulates a line item
     * that compares the grid unit
     * from the three profiles
     * encapsulated by this object.
     */
    private void reportGridUnit()
    {
        float   orig    = origProfile.getGridUnit();
        float   exp     = expRevisedProfile.getGridUnit();
        float   act     = expRevisedProfile.getGridUnit();
        append( "GridUnit", orig, exp, act );
    }
    
    /**
     * Formulates a line item
     * for each property in the main window property set
     * comparing the property values
     * from the three profiles
     * encapsulated by this object.
     */
    private void reportMW()
    {
        GraphPropertySet    orig    = origProfile.getMainWindow();
        GraphPropertySet    exp     = expRevisedProfile.getMainWindow();
        GraphPropertySet    act     = actRevisedProfile.getMainWindow();
        log.append( GraphPropertySetMW.class.getSimpleName() );
        append( 
            "BGColor", 
            orig.getBGColor(), 
            exp.getBGColor(),
            act.getBGColor()
        );
        append( 
            "FGColor", 
            orig.getFGColor(), 
            exp.getFGColor(),
            act.getFGColor()
        );
        append( 
            "FontDraw", 
            orig.isFontDraw(), 
            exp.isFontDraw(),
            act.isFontDraw()
        );
        append( 
            "FontName", 
            orig.getFontName(), 
            exp.getFontName(),
            act.getFontName()
        );
        append( 
            "FontSize", 
            orig.getFontSize(), 
            exp.getFontSize(),
            act.getFontSize()
        );
        append( 
            "FontStyle", 
            orig.getFontStyle(), 
            exp.getFontStyle(),
            act.getFontStyle()
        );
    }
    
    /**
     * Formulates a line item
     * for each property in a line property set
     * comparing the property values
     * from the three profiles
     * encapsulated by this object.
     * 
     * @param simpleName
     *      the simple class name of the property set
     *      to interrogate
     */
    private void reportLineSet( String simpleName )
    {
        LinePropertySet orig    = 
            origProfile.getLinePropertySet( simpleName );
        LinePropertySet exp     = 
            expRevisedProfile.getLinePropertySet( simpleName );
        LinePropertySet act     = 
            actRevisedProfile.getLinePropertySet( simpleName );
        log.append( simpleName );
        append(
            "Color",
            orig.getColor(),
            exp.getColor(),
            act.getColor()
        );
        append(
            "Draw",
            orig.getDraw(),
            exp.getDraw(),
            act.getDraw()
        );
        append(
            "Length",
            orig.getLength(),
            exp.getLength(),
            act.getLength()
        );
        append(
            "Spacing",
            orig.getSpacing(),
            exp.getSpacing(),
            act.getSpacing()
        );
        append(
            "Stroke",
            orig.getStroke(),
            exp.getStroke(),
            act.getStroke()
        );
    }
    
    /**
     * Formulates a line item
     * from a property name
     * and three Boolean values.
     * The Boolean values
     * are assumed to come
     * from the GraphPropertySet
     * or LinePropertySets
     * encapsulated in the three profiles
     * managed by this object.
     * 
     * @param name  the property name
     * @param orig  the property value from the original profile
     * @param exp   the property value from the expected revised profile
     * @param act   the property value from the actual revised profile
     */
    private void 
    append( String name, boolean orig, boolean exp, boolean act )
    {
        String  fmt     = "%10s %10.1s %10.1s %10.1s";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += wrong;
            
        log.append( str );
    }
    
    
    /**
     * Formulates a line item
     * from a property name
     * and three integer values.
     * The integer values
     * are assumed to come
     * from the GraphPropertySet
     * or LinePropertySets
     * encapsulated in the three profiles
     * managed by this object.
     * 
     * @param name  the property name
     * @param orig  the property value from the original profile
     * @param exp   the property value from the expected revised profile
     * @param act   the property value from the actual revised profile
     */
    private void append( String name, int orig, int exp, int act )
    {
        String  fmt     = "%10s %10d %10d %10d";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += wrong;
            
        log.append( str );
    }
    
    
    /**
     * Formulates a line item
     * from a property name
     * and three decimal values.
     * The decimal values
     * are assumed to come
     * from the GraphPropertySet
     * or LinePropertySets
     * encapsulated in the three profiles
     * managed by this object.
     * 
     * @param name  the property name
     * @param orig  the property value from the original profile
     * @param exp   the property value from the expected revised profile
     * @param act   the property value from the actual revised profile
     */
    private void append( String name, float orig, float exp, float act )
    {
        String  fmt     = "%10s %10.1f %10.1f %10.1f";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += wrong;
            
        log.append( str );
    }
    
    
    /**
     * Formulates a line item
     * from a property name
     * and three string values.
     * The string values
     * are assumed to come
     * from the GraphPropertySet
     * or LinePropertySets
     * encapsulated in the three profiles
     * managed by this object.
     * 
     * @param name  the property name
     * @param orig  the property value from the original profile
     * @param exp   the property value from the expected revised profile
     * @param act   the property value from the actual revised profile
     */
    private void 
    append( String name, String orig, String exp, String act )
    {
        String  fmt = "%10s %10s %10s %10s ";
        String  str = String.format( fmt, name, orig, exp, act );
        
        if ( exp.equals( act ) )
            str += check;
        else
            str += wrong;
            
        log.append( str );
    }
    
    
    /**
     * Formulates a line item
     * from a property name
     * and three color values.
     * The color values
     * are assumed to come
     * from the GraphPropertySet
     * or LinePropertySets
     * encapsulated in the three profiles
     * managed by this object.
     * 
     * @param name  the property name
     * @param orig  the property value from the original profile
     * @param exp   the property value from the expected revised profile
     * @param act   the property value from the actual revised profile
     */
    private void append( String name, Color orig, Color exp, Color act )
    {
        int     iOrig   = orig.getRGB() & 0xFFFFFF;
        int     iExp    = exp.getRGB() & 0xFFFFFF;
        int     iAct    = act.getRGB() & 0xFFFFFF;
        String  fmt = "%10s %10x %10x %10x ";
        String  str = String.format( fmt, name, iOrig, iExp, iAct );
        
        if ( exp.equals( act ) )
            str += check;
        else
            str += wrong;
            
        log.append( str );
    }
    
    /**
     * This class encapsulates a simple GUI
     * used to display the report
     * produced by the containing class.
     * 
     * @author Jack Straub
     */
    private static class Log extends JFrame
    {
        /** Default serial version UID. */
        private static final long serialVersionUID = 1L;
        /** Platform dependent line separator. */
        private static final String lineSep     = System.lineSeparator();
        /** Text area in which to display the report. */
        private final JTextArea     textArea    = new JTextArea( 24, 50 );
        
        /**
         * Constructor.
         * Fully initializes the GUI
         * encapsulated by this class,
         * but does <em>not</em> make it visible.
         */
        public Log()
        {
            super( "ProfileDecompilerDemo 1" );
            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            JPanel          contentPane = new JPanel();
            JScrollPane     scrollPane  = new JScrollPane( textArea );
            contentPane.add( scrollPane );
            
            Font    font    = new Font( Font.MONOSPACED, Font.PLAIN, 14 );
            textArea.setFont( font );
            
            setContentPane( contentPane );
            pack();
        }
        
        /**
         * Appends to the text area
         * the given text
         * plus a line separator.
         * 
         * @param text  the given text.
         */
        public void append( String text )
        {
            textArea.append( text );
            textArea.append( lineSep );
        }
    }
}

