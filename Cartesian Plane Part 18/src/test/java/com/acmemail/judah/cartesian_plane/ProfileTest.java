package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

/**
 * This test mainly involves getters and setters,
 * and the apply and reset functions
 * of the Profile class. 
 * 
 * @author Jack Straub
 * 
 * @see https://softwareengineering.stackexchange.com/questions/315815/method-returning-an-unmodifiable-list
 */
class ProfileTest
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
    public void testName()
    {
        String  orig        = workingProfile.getName();
        String  diff        = distinctProfile.getName();
        assertNotEquals( orig, diff );
        workingProfile.setName( diff );
        assertEquals( diff, workingProfile.getName() );
        workingProfile.reset();
        assertEquals( orig, workingProfile.getName() );
        
        workingProfile.setName( diff );
        workingProfile.apply();
        assertEquals( diff, workingProfile.getName() );
    }
    
    @Test
    public void testGridUnit()
    {
        testProperty(
            p -> p.getGridUnit(),
            (p,v) -> p.setGridUnit( (float)v )
        );
    }

    @Test
    public void testGetMainWindow()
    {
        GraphPropertySet    expSet  = protoProfile.getMainWindow();
        GraphPropertySet    actSet  = workingProfile.getMainWindow();
        assertEquals( expSet, actSet );
    }
    
    @Test
    public void testMainWindowProperties()
    {
        testProperty(
            p -> p.getMainWindow().getWidth(),
            (p,v) -> p.getMainWindow().setWidth( (float)v )
        );

        testProperty(
            p -> p.getMainWindow().getBGColor(),
            (p,v) -> p.getMainWindow().setBGColor( (Color)v )
        );

        testProperty(
            p -> p.getMainWindow().getFGColor(),
            (p,v) -> p.getMainWindow().setFGColor( (Color)v )
        );

        testProperty(
            p -> p.getMainWindow().getFontName(),
            (p,v) -> p.getMainWindow().setFontName( (String)v )
        );

        testProperty(
            p -> p.getMainWindow().getFontSize(),
            (p,v) -> p.getMainWindow().setFontSize( (float)v )
        );

        testProperty(
            p -> p.getMainWindow().isItalic(),
            (p,v) -> p.getMainWindow().setItalic( (boolean)v )
        );

        testProperty(
            p -> p.getMainWindow().isBold(),
            (p,v) -> p.getMainWindow().setBold( (boolean)v )
        );

        testProperty(
            p -> p.getMainWindow().isFontDraw(),
            (p,v) -> p.getMainWindow().setFontDraw( (boolean)v )
        );
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
        LinePropertySet set = protoProfile.getLinePropertySet( name );
        
        if ( set.hasDraw() )
            testProperty(
                p -> p.getLinePropertySet( name ).getDraw(),
                (p,v) -> p.getLinePropertySet( name ).setDraw( (boolean)v )
            );

        if ( set.hasStroke() )
            testProperty(
                p -> p.getLinePropertySet( name ).getStroke(),
                (p,v) -> p.getLinePropertySet( name ).setStroke( (float)v )
            );
            
        if ( set.hasLength() )
            testProperty(
                p -> p.getLinePropertySet( name ).getLength(),
                (p,v) -> p.getLinePropertySet( name ).setLength( (float)v )
            );
            
        if ( set.hasSpacing() )
            testProperty(
                p -> p.getLinePropertySet( name ).getSpacing(),
                (p,v) -> p.getLinePropertySet( name ).setSpacing( (float)v )
            );
            
        if ( set.hasColor() )
            testProperty(
                p -> p.getLinePropertySet( name ).getColor(),
                (p,v) -> p.getLinePropertySet( name ).setColor( (Color)v )
            );
    }
    
    @Test
    public void testEquals()
    {
        String   mutatedName    = distinctProfile.getName();
        testEqualsByField( p -> p.setName( mutatedName ) );
        float   mutatedGridUnit = distinctProfile.getGridUnit();
        testEqualsByField( p -> p.setGridUnit( mutatedGridUnit ) );
        float   mutatedFontSize = 
            distinctProfile.getMainWindow().getFontSize();
        testEqualsByField( p -> 
            p.getMainWindow().setFontSize( mutatedFontSize ) 
        );
        Stream.of( linePropertySetClasses )
            .forEach( s -> {
                LinePropertySet set = 
                    distinctProfile.getLinePropertySet( s );
                float   stroke  = set.getStroke();
                testEqualsByField( 
                    p -> p.getLinePropertySet( s ).setStroke( stroke )
                );
            });
    }
    
    /**
     * Verify the getter and setter for a given field;
     * verify that the given field 
     * is correctly processed by 
     * the Profile.apply() and Profile.reset() operations.
     * <p>
     * Precondition: 
     * {@link #protoProfile} has not been changed since initialization.
     * <p>
     * Precondition: 
     * {@link #distinctProfile} has not been changed since initialization.
     * <p>
     * Postcondition: 
     * {@link #workingProfile} has been modified.
     * @param getter    the given getter
     * @param setter    the given setter
     */
    private void testProperty(
        Function<Profile,Object> getter,
        BiConsumer<Profile,Object> setter
    )
    {
        // Initialize workingProfile so that it is equal to protoProfile
        protoProfile.apply();
        workingProfile = new Profile();
        
        // Get working value
        Object  protoVal    = getter.apply( protoProfile );
        Object  distinctVal = getter.apply( distinctProfile );
        Object  workingVal  = getter.apply( workingProfile );
        // Sanity test
        assertEquals( protoVal, workingVal );
        assertNotEquals( protoVal, distinctVal );
        
        // Change the working profile, verify getter;
        // verify PropertyManager not updated.
        setter.accept( workingProfile, distinctVal );
        assertEquals( distinctVal, getter.apply( workingProfile ) );
        
        // Resetting the working profile will refresh the profile
        // from the PropertyManager, after which the value of the 
        // test field must be restored to protoVal.
        workingProfile.reset();
        assertEquals( protoVal, getter.apply( workingProfile ) );
        
        // Change the property and apply. Verify that the 
        // working profile and property manager are changed.
        setter.accept( workingProfile, distinctVal );
        workingProfile.apply();
        assertEquals( distinctVal, getter.apply( workingProfile ) );
        
        // Resetting the working profile will refresh the profile
        // from the PropertyManager, after which the value of the 
        // test field must be equal to the applied value distinctVal).
        workingProfile.reset();
        assertEquals( distinctVal, getter.apply( workingProfile ) );
    }
    
    /**
     * Tests the equals and hash methods of the Profile class
     * when two profiles differ by one field
     * in the profile class
     * or one of the Profile's encapsulated collections.
     * The caller provides a mutator
     * that will change a property
     * encapsulated in a profile
     * to a unique value.
     * Two profiles are created
     * and their equality validated.
     * Then the mutator is applied to one of the profiles,
     * and the Profiles' inequality is validated.
     * Presumably this method is invoked
     * for each property in the Profile class,
     * and each property of the collections
     * encapsulated by a Profile object.
     * 
     * @param mutator   
     *      mutator to change the value of a single property
     *      encapsulated in the Profile class
     *      to a unique value
     */
    private void testEqualsByField( Consumer<Profile> mutator )
    {
        Profile profile1    = new Profile();
        Profile profile2    = new Profile();
        assertFalse( profile1.equals( null ) );
        assertFalse( profile1.equals( new Object() ) );
        assertTrue( profile1.equals( profile1 ) );
        assertTrue( profile1.equals( profile2 ) );
        assertTrue( profile2.equals( profile1 ) );
        assertEquals( profile1.hashCode(), profile2.hashCode() );
        
        mutator.accept( profile2 );
        assertFalse( profile1.equals( profile2 ) );
        assertFalse( profile2.equals( profile1 ) );
        
        mutator.accept( profile1 );
        assertTrue( profile1.equals( profile2 ) );
        assertTrue( profile2.equals( profile1 ) );
        assertEquals( profile1.hashCode(), profile2.hashCode() );
    }
}
