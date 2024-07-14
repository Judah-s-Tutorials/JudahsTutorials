package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.ProfileParser;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Simple application to create a Profile,
 * modify a copy of it,
 * and display the values
 * of its properties.
 * Differences between profiles are displayed in red.
 * To terminate this program,
 * close one of the activity logs.
 * 
 * 
 * @author Jack Straub
 */
public class ProfileInputDemo1
{
    /** 
     * List into which to compile modifications
     * to be applied to the target profile.
     */
    private static final List<String>   profileMods = new ArrayList<>();
    
    /** 
     * Log for displaying Profile properties 
     * as represented in the PropertyManager.
     */
    private static  ActivityLog     baseLog;
    /** 
     * Log for displaying Profile properties 
     * after modification by this application.
     */
    private static  ActivityLog     modLog;
    /** Profile properties as represented in the PropertyManager. */
    private static  Profile         baseProfile = new Profile();
    /** Profile after loading modifications. */
    private static  Profile         modProfile  = new Profile();
    /** List of properties from     baseProfile. */
    private static  List<String>    baseList     = new ArrayList<>();
    /** List of properties from modProfile. */
    private static  List<String>    modList     = new ArrayList<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GUIUtils.schedEDTAndWait( () -> build() );
        
        compileModifications();
        // Apply modifications to modProfile.
        ProfileParser   modParser   = new ProfileParser( modProfile );
        modParser.loadProperties( profileMods.stream() );
        
        ProfileParser   baseParser  = new ProfileParser( baseProfile );
        baseParser.getProperties()
            .forEach( baseList::add );
        modParser.getProperties()
            .forEach( modList::add );
        if ( baseList.size() != modList.size() )
            throw new ComponentException( "Unexpected error" );
        IntStream.range( 0, baseList.size() )
            .forEach( i -> 
                appendLog( baseList.get( i ), modList.get( i ) )
            );
    }
    
    /**
     * Compile a list of modifications
     * to make to modProfile.
     * Note that <u>no modifications are made to any profile.</u>
     * The list is compiled into {@link #profileMods} which,
     * after compilation,
     * will look something like this:
<pre>class GraphPropertySetMW
font_size 20.0
class LinePropertySetTicMajor
stroke 15.0
length 26.0
class LinePropertySetTicMinor
stroke 12.0
length 16.0
class LinePropertySetAxes
stroke 12.0
length 9.0
class LinePropertySetGridLines
stroke 11.0
length 9.0</pre>
     * <p>
     * Modifications will be applied later,
     * when {@link #profileMods} is streamed to
     * {@link ProfileParser#loadProperties(Stream)}.
     * 
     * @see #main(String[])
     */
    private static void compileModifications()
    {
        addMods( baseProfile.getMainWindow() );
        Stream.of( 
            LinePropertySetTicMajor.class.getSimpleName(),
            LinePropertySetTicMinor.class.getSimpleName(),
            LinePropertySetAxes.class.getSimpleName(),
            LinePropertySetGridLines.class.getSimpleName()
        )
            .map( baseProfile::getLinePropertySet )
            .forEach( s -> addMods( s ) );
    }
    
    /**
     * Append the given strings
     * to the activity logs.
     * If the second string is different from the first
     * it is displayed in red.
     * 
     * @param baseProperty
     *      first given string;
     *      to be displayed in the base-profile activity log
     * @param modProperty
     *      second given string;
     *      to be displayed in the modified-profile activity log
     */
    private static void 
    appendLog( String baseProperty, String modProperty )
    {
        final String    spanPrefix  =
            "<span style='color:red;'>";
        final String    spanSuffix  =
            "</span>";
        String  modItem = baseProperty.equals( modProperty ) ?
            modProperty :
            spanPrefix + modProperty + spanSuffix;
        baseLog.append( baseProperty );
        modLog.append( modItem );
    }
    
    /**
     * Creates and shows the dialog
     * used to display the Profile properties.
     */
    private static void build()
    {
        baseLog = new ActivityLog( "Base Profile Properties" );
        baseLog.setLocation( 200, 200 );
        baseLog.setVisible( true );
        Dimension   dimBase = baseLog.getPreferredSize();

        modLog = new ActivityLog( "Modified Profile Properties" );
        modLog.setLocation( 200 + dimBase.width, 200 );
        modLog.setVisible( true );
        
        WindowListener  winListener = new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent evt )
            {
                System.exit( 0 );
            }
        };
        baseLog.addWindowListener( winListener );
        modLog.addWindowListener( winListener );
    }
    
    /**
     * Compile modifications to be applied 
     * to a profile's main window property set.
     * Modifications are hard-coded.
     * Note that modifications are not made
     * to the property set itself; 
     * they are applied as a separate operation.
     * See {@link #main(String[])}.
     * 
     * @param graphSet  profile's main window property set
     */
    private static void addMods( GraphPropertySet graphSet )
    {
        String              className       = 
            graphSet.getClass().getSimpleName();
        profileMods.add( ProfileParser.CLASS + " " + className );
        float               baseFontSize    = graphSet.getFontSize();
        float               modFontSize     = baseFontSize + 10;
        profileMods.add( ProfileParser.FONT_SIZE + " " + modFontSize );
    }
    
    
    /**
     * Compile modifications to be applied 
     * to a profile's line property set.
     * Modifications are hard-coded.
     * Note that modifications are not made
     * to the property set itself; 
     * they are applied as a separate operation.
     * See {@link #main(String[])}.
     * 
     * @param graphSet  profile's main window property set
     */
    private static void addMods( LinePropertySet lineSet )
    {
        String              className       = 
            lineSet.getClass().getSimpleName();
        profileMods.add( ProfileParser.CLASS + " " + className );
        
        float               baseStroke      = lineSet.getStroke();
        float               modStroke       = baseStroke + 10;
        profileMods.add( ProfileParser.STROKE + " " + modStroke );
        
        float               baseLength      = lineSet.getLength();
        float               modLength       = baseLength + 10;
        profileMods.add( ProfileParser.LENGTH + " " + modLength );
    }
}
