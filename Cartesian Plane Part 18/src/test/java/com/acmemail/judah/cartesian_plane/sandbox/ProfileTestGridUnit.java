package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

/**
 * This class helps to demonstrate
 * the strategy we will use
 * for testing
 * @author Jack Straub
 */
class ProfileTestGridUnit
{
    /**
     * Prototype Profile; 
     * contains the values of the profile properties
     * obtained from the PropertyManager
     * before any edited property values
     * are committed.
     */
    private final Profile   protoProfile    = new Profile();
    /**
     * Profile initialized to values guaranteed to be different
     * from protoProfile.
     * 
     * @see ProfileUtils#getDistinctProfile(Profile)
     */
    private final Profile   distinctProfile = getDistinctProfile();
    
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
    
    @Test
    public void testGridUnit()
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
        assertEquals( protoGridUnit, workingProfile.getGridUnit() );
        assertEquals( protoGridUnit, workingProfile.getGridUnit() );
        
        // Change the working profile, then apply. The working profile
        // and the PropertyManager should reflect the updated value.
        workingProfile.setGridUnit( distinctGridUnit );
        workingProfile.apply();
        assertEquals( distinctGridUnit, workingProfile.getGridUnit() );
        workingProfile.reset();
        assertEquals( distinctGridUnit, workingProfile.getGridUnit() );
    }
    
    private Profile getDistinctProfile()
    {
        Profile profile     = new Profile();
        float   newGridUnit = profile.getGridUnit() + 1;
        profile.setGridUnit( newGridUnit );
        return profile;
    }
}
