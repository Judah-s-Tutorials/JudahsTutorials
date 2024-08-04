package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

class ProfileParserTest
{
    /** Names of the LinePropertySet subclasses. */
    private static final String[]   linePropertySetClasses  =
    {
        LinePropertySetAxes.class.getSimpleName(),
        LinePropertySetGridLines.class.getSimpleName(),
        LinePropertySetTicMajor.class.getSimpleName(),
        LinePropertySetTicMinor.class.getSimpleName()
    };
    
    /**
     * Prototype Profile; 
     * contains the values of the profile properties
     * obtained from the PropertyManager
     * before any edited property values
     * are committed.
     * Never changed after initialization;
     * used to update the PropertyManager to original value.
     * 
     * @see #afterEach()
     */
    private final Profile   protoProfile    = new Profile();
    /**
     * Profile initialized to values guaranteed to be different
     * from protoProfile.
     * Never changed after initialization.
     * 
     * @see ProfileUtils#getDistinctProfile(Profile)
     */
    private final Profile   distinctProfile = 
        ProfileUtils.getDistinctProfile( protoProfile );
    
    /** 
     * Profile to be modified as needed by tests;
     * returned to initial value in {@link #beforeEach()}.
     */
    private Profile workingProfile;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        // Refresh the workingProfile from the PropertyManager;
        // the refreshed Profile must be equal to protoProfile.
        workingProfile = new Profile();
    }
    
    @AfterEach
    public void afterEach()
    {
        // Restore the property manager fields to their original values
        protoProfile.apply();
    }

    @Test
    public void testProfileParser()
    {
        // Default constructor instantiates a profile that is expected
        // to be initialized with default values from the PropertyManager.
        ProfileParser   parser  = new ProfileParser();
        Profile         profile = parser.getProfile();
        assertNotNull( profile );
        assertEquals( protoProfile, profile );
    }

    @Test
    public void testProfileParserProfile()
    {
        // Instantiate parser with explicit profile and make sure
        // the profile is encapsulated.
        float           diffGridUnit    = distinctProfile.getGridUnit();
        workingProfile.setGridUnit( diffGridUnit );
        ProfileParser   parser          = 
            new ProfileParser( workingProfile );
        Profile         testProfile     = parser.getProfile();
        assertEquals( diffGridUnit, testProfile.getGridUnit() );
    }

    @Test
    public void testGetProperties()
    {
        ProfileParser   outParser   = new ProfileParser( distinctProfile );
        Stream<String>  stream      = outParser.getProperties();
        ProfileParser   inParser    = new ProfileParser();
        inParser.loadProperties( stream );
        assertEquals( distinctProfile, inParser.getProfile() );
    }

    @Test
    public void testLoadProperties()
    {
        // Create and validate input stream in which some of the profile
        // properties are changed.
        List<String>    list        = new ArrayList<>();
        
        // Change the profile name and grid unit
        float           newFloat    = distinctProfile.getGridUnit();
        String          newName     = distinctProfile.getName();
        addNVS( list, ProfileParser.PROFILE, newName );
        addNVS( list, ProfileParser.GRID_UNIT, newFloat );
        workingProfile.setGridUnit( newFloat );
        workingProfile.setName( newName );
        
        // Change the GraphPropertySet font size
        GraphPropertySet    graphSet    = distinctProfile.getMainWindow();
        String              className   = 
            graphSet.getClass().getSimpleName();
        newFloat = graphSet.getFontSize();
        addNVS( list, ProfileParser.CLASS, className );
        addNVS( list, ProfileParser.FONT_SIZE, newFloat );
        graphSet.setFontSize( newFloat );
        
        // Change the color in the LinePropertySetAxes properties
        LinePropertySet lineSet     = null;
        className = LinePropertySetAxes.class.getSimpleName();
        lineSet = distinctProfile.getLinePropertySet( className );
        Color   newColor            = lineSet.getColor();
        addNVS( list, ProfileParser.CLASS, className );
        addNVS( list, ProfileParser.COLOR, newColor );
        lineSet.setColor( newColor );
        
        // Change the stroke in the LinePropertySetGridLines properties
        className = LinePropertySetGridLines.class.getSimpleName();
        lineSet = distinctProfile.getLinePropertySet( className );
        addNVS( list, ProfileParser.CLASS, className );
        addNVS( list, ProfileParser.STROKE, lineSet.getStroke() );
        
        // Change the length in the LinePropertySetTicMajor properties
        className = LinePropertySetTicMajor.class.getSimpleName();
        lineSet = distinctProfile.getLinePropertySet( className );
        addNVS( list, ProfileParser.CLASS, className );
        addNVS( list, ProfileParser.LENGTH, lineSet.getLength() );
        
        // Change the spacing in the LinePropertySetTicMinor properties
        className = LinePropertySetTicMinor.class.getSimpleName();
        lineSet = distinctProfile.getLinePropertySet( className );
        addNVS( list, ProfileParser.CLASS, className );
        addNVS( list, ProfileParser.LENGTH, lineSet.getLength() );
        
        list.forEach( System.out::println );
    }

    @Test
    public void testGetProfile()
    {
        // see testProfileParserProfile
    }
    
    private void changeFloat( 
        List<String> list,
        String property,
        String propSet, 
        Function< LinePropertySet, Float> getter,
        BiConsumer<LinePropertySet, Float> setter
    )
    {
        LinePropertySet workingProps    = 
            workingProfile.getLinePropertySet( propSet );
        LinePropertySet distinctProps   =
            distinctProfile.getLinePropertySet( propSet );
        float           distinctFloat   = 
            getter.apply( distinctProps );
        setter.accept( workingProps, distinctFloat );
        String          classStr        =
            ProfileParser.CLASS + ": " + propSet;
        String          propStr         =
            property + ": " + distinctFloat;
        list.add( classStr );
        list.add( propStr );
    }

    private void addNVS( List<String> list, String name, Object value )
    {
        String  str = name + ": " + value;
        list.add( str );
    }
}
