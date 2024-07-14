package com.acmemail.judah.cartesian_plane;

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

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

/**
 * This test mainly involves getters and setters,
 * and the apply and reset function. 
 * My position is that
 * we don't have to put every single property
 * to the test.
 * For example,
 * making sure that every property in the GraphPropertySetMW class
 * is handled correctly
 * is the job of GraphPropertiesMWTest;
 * the job of validating operations
 * for the LinePropertySet subclasses
 * is the responsibility 
 * of the JUnit test class
 * for each subclass.
 * I propose the we should test the getter and setter,
 * and the apply and reset logic,
 * for one property in each of the
 * GraphPropertySetMW class
 * and the LinePropertySet subclasses.
 * We also have to test the logic
 * for the Grid Unit property,
 * which is not a member
 * of the above classes.
 * 
 * @author Jack Straub
 * 
 * @see https://softwareengineering.stackexchange.com/questions/315815/method-returning-an-unmodifiable-list
 */
class ProfileTest
{
    /**
     * Prototype Profile; 
     * contains the values of the profile properties
     * obtained from the PropertyManager
     * before any edited property values
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
        testFloatProperty(
            protoProfile::getGridUnit,
            distinctProfile::getGridUnit,
            workingProfile::getGridUnit,
            workingProfile::setGridUnit
        );
    }
    
    @Test
    public void testMainWindowProperties()
    {
        GraphPropertySet    protoProps      = protoProfile.getMainWindow();
        GraphPropertySet    distinctProps   = 
            distinctProfile.getMainWindow();
        GraphPropertySet    workingProps    = 
            workingProfile.getMainWindow();

        testFloatProperty(
            protoProps::getFontSize,
            distinctProps::getFontSize,
            workingProps::getFontSize,
            workingProps::setFontSize
        );
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
        String          simpleName  = LinePropertySetTicMajor.class.getSimpleName();
        LinePropertySet set     = 
            workingProfile.getLinePropertySet( name );
        assertNotNull( set );
        String          actName = set.getClass().getSimpleName();
        assertEquals( name, actName );
        
        LinePropertySet     protoProps      = 
            protoProfile.getLinePropertySet( name );
        LinePropertySet     distinctProps   = 
            distinctProfile.getLinePropertySet( name );
        LinePropertySet     workingProps    = 
            workingProfile.getLinePropertySet( name );

        // Not all LinePropertySet classes support all properties, but
        // they all support Stroke, which we will use in the test.
        testFloatProperty(
            protoProps::getStroke,
            distinctProps::getStroke,
            workingProps::getStroke,
            workingProps::setStroke
        );
    }
    
    private void testFloatProperty(
        Supplier<Float> protoGetter,
        Supplier<Float> distinctGetter,
        Supplier<Float> workingGetter,
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
        assertEquals( distinctVal, workingGetter.get() );
        workingProfile.reset();
        assertEquals( protoVal, workingGetter.get() );
        
        // Change the property and apply. Verify that the 
        // working profile and property manager are changed.
        workingSetter.accept( distinctVal );
        workingProfile.apply();
        assertEquals( distinctVal, workingGetter.get() );
        workingProfile.reset();
        assertEquals( distinctVal, workingGetter.get() );
    }
}
