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

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class ProfileDecompilerDemo1
{
    private static final char   check               = '\u2713';
    private static final char   stop                = '\u20e0';
    private static final String[]      lineSets     =
    {
        LinePropertySetAxes.class.getSimpleName(),
        LinePropertySetGridLines.class.getSimpleName(),
        LinePropertySetTicMajor.class.getSimpleName(),
        LinePropertySetTicMinor.class.getSimpleName(),
    };

    private final Log           log                 = new Log();
    private final Profile       origProfile         = new Profile();
    private final Profile       expRevisedProfile   = new Profile();
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
    
    private String getDistinctFontStyle( int styleIn )
    {
        String  styleOut    = styleIn == Font.PLAIN ? "ITALIC" : "PLAIN";
        return styleOut;
    }
    
    private Color getDistinctColor( Color colorIn )
    {
        int     iColorIn    = colorIn.getRGB() & 0xFFFFFF;
        int     iColorOut   = ~iColorIn;
        Color   colorOut    = new Color( iColorOut );
        return colorOut;
    }
    
    private String getDistinctFontName( String nameIn )
    {
        String  nameOut = nameIn.equals( Font.MONOSPACED ) ? 
            Font.DIALOG : Font.MONOSPACED;
        return nameOut;
    }
    
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
    
    private void reportGridUnit()
    {
        float   orig    = origProfile.getGridUnit();
        float   exp     = expRevisedProfile.getGridUnit();
        float   act     = expRevisedProfile.getGridUnit();
        append( "GridUnit", orig, exp, act );
    }
    
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
    
    private void 
    append( String name, boolean orig, boolean exp, boolean act )
    {
        String  fmt     = "%10s %10.1s %10.1s %10.1s";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += stop;
            
        log.append( str );
    }
    
    private void append( String name, int orig, int exp, int act )
    {
        String  fmt     = "%10s %10d %10d %10d";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += stop;
            
        log.append( str );
    }
    
    private void append( String name, float orig, float exp, float act )
    {
        String  fmt     = "%10s %10.1f %10.1f %10.1f";
        String  str     = String.format( fmt, name, orig, exp, act );
        
        str += ' ';
        if ( exp == act )
            str += check;
        else
            str += stop;
            
        log.append( str );
    }
    
    private void 
    append( String name, String orig, String exp, String act )
    {
        String  fmt = "%10s %10s %10s %10s ";
        String  str = String.format( fmt, name, orig, exp, act );
        
        if ( exp.equals( act ) )
            str += check;
        else
            str += stop;
            
        log.append( str );
    }
    
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
            str += stop;
            
        log.append( str );
    }
    
    private static class Log extends JFrame
    {
        private static final String lineSep     = System.lineSeparator();
        private final JTextArea     textArea    = new JTextArea( 24, 50 );
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
        
        public void append( String text )
        {
            textArea.append( text );
            textArea.append( lineSep );
        }
    }
}

