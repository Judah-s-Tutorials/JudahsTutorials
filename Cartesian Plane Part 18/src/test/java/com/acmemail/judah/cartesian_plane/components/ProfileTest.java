package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

class ProfileTest
{
    /**
     * Prototype Profile; 
     * this is an unmodifiable list.
     * Contains the values of the profile properties
     * obtained from the PropertyManager
     * before any new property values
     * are committed.
     * 
     * @see #afterEach()
     */
    private final Profile   protoProfile    = new Profile();
    /**
     * Profile initialized to values guaranteed to be different
     * from protoProfile.
     * 
     * @see ProfileUtils#getDistinctProfile(Profile)
     */
    private final Profile   distinctProfile = 
        ProfileUtils.getDistinctProfile( protoProfile );
    
    /** 
     * Profile to be modified as needed by tests;
     * updated in {@link #beforeEach()}.
     */
    private Profile workingProfile;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        workingProfile = new Profile();
    }
    
    @AfterEach
    public void afterEach()
    {
        protoProfile.apply();
    }
    
    @Test
    public void testGridUnit()
    {
        Profile pmgrProfile = new Profile();
        testFloatProperty(
            pmgrProfile,
            protoProfile::getGridUnit,
            distinctProfile::getGridUnit,
            workingProfile::getGridUnit,
            pmgrProfile::getGridUnit,
            workingProfile::setGridUnit
        );
        
    }
    
    public void testMainWindowProperties()
    {
        Profile             pmgrProfile     = new Profile();
        GraphPropertySet    protoProps      = protoProfile.getMainWindow();
        GraphPropertySet    distinctProps   = 
            distinctProfile.getMainWindow();
        GraphPropertySet    workingProps    = 
            workingProfile.getMainWindow();
        GraphPropertySet    pmgrProps       = pmgrProfile.getMainWindow();

        testFloatProperty(
            pmgrProfile,
            protoProps::getFontSize,
            distinctProps::getFontSize,
            workingProps::getFontSize,
            pmgrProps::getFontSize,
            workingProps::setFontSize
        );
    }
    
    @Test
    public void testGridUnit2()
    {
        float   protoGridUnit       = protoProfile.getGridUnit();
        float   distinctGridUnit    = distinctProfile.getGridUnit();
        // sanity test
        assertNotEquals( protoGridUnit, distinctGridUnit );

        assertEquals( protoGridUnit, workingProfile.getGridUnit() );
        // change the grid unit; make sure the set sticks
        workingProfile.setGridUnit( distinctGridUnit );
        assertEquals( distinctGridUnit, workingProfile.getGridUnit() );
        
        // Reset the working profile; the working profile should return
        // to the prototype value, and the PropertyManager should
        // *not* be updated.
        workingProfile.reset();
        Profile currProfile = new Profile();
        assertEquals( protoGridUnit, workingProfile.getGridUnit() );
        assertEquals( protoGridUnit, currProfile.getGridUnit() );
        
        // Change the working profile, then apply. The working profile
        // and the PropertyManager should reflect the updated value.
        workingProfile.setGridUnit( distinctGridUnit );
        workingProfile.apply();
        currProfile.reset();
        assertEquals( distinctGridUnit, workingProfile.getGridUnit() );
        assertEquals( distinctGridUnit, currProfile.getGridUnit() );
    }

    @Test
    public void testGetMainWindow()
    {
        GraphPropertySet    set     = workingProfile.getMainWindow();
        assertNotNull( set );
    }

    @ParameterizedTest
    @ValueSource(strings= {
        "LinePropertySetAxes",
        "LinePropertySetGridLines",
        "LinePropertySetTicMajor",
        "LinePropertySetTicMinor",
        }
    )
    public void testGetLinePropertySet( String name )
    {
        LinePropertySet set     = workingProfile.getLinePropertySet( name );
        assertNotNull( set );
        String          actName = set.getClass().getSimpleName();
        assertEquals( name, actName );
    }
    
    private void testFloatProperty(
        Profile        testProfile,
        Supplier<Float> protoGetter,
        Supplier<Float> distinctGetter,
        Supplier<Float> workingGetter,
        Supplier<Float> pmgrGetter,
        Consumer<Float> workingSetter
    )
    {
        float  protoVal    = protoGetter.get();
        float  distinctVal = distinctGetter.get();
        float  workingVal  = workingGetter.get();
        // Sanity test
        assertEquals( protoVal, workingVal );
        assertNotEquals( protoVal, distinctVal );
        
        // change the working profile, verify getter;
        // verify PropertyManager not updated.
        workingSetter.accept( distinctVal );
        testProfile.reset();
        assertEquals( distinctVal, workingGetter.get() );
        assertEquals( protoVal, pmgrGetter.get() );
        
        // reset the working profile; verify that it resets
        workingProfile.reset();
        assertEquals( protoVal, workingGetter.get() );
        
        // Change the property and apply. Verify that the 
        // working profile and property manager are changed.
        workingSetter.accept( distinctVal );
        workingProfile.apply();
        testProfile.reset();
        assertEquals( distinctVal, workingGetter.get() );
        assertEquals( distinctVal, pmgrGetter.get() );
    }
}
