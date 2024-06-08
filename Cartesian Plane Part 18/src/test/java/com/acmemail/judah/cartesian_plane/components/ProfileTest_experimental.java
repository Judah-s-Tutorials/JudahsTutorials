package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

class ProfileTest_experimental
{
    /** LinePropertySet subclass simple names. */
    private static final String[]   simpleNames =
    {
        LinePropertySetAxes.class.getSimpleName(),
        LinePropertySetGridLines.class.getSimpleName(),
        LinePropertySetTicMajor.class.getSimpleName(),
        LinePropertySetTicMinor.class.getSimpleName(),
    };
    
    private static final String[]   graphPropertyNames  =
        compileProperties( GraphPropertySet.class );
    private static final String[]   linePropertyNames   =
        compileProperties( LinePropertySet.class );
    
    /**
     * Prototype Profile.
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
    
    @ParameterizedTest
    @ValueSource( strings={ "a" })
    public void testProperty( String propName )
    {
        
    }

    @Test
    public void testProfile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testReset()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testApply()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetGridUnit()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSetGridUnit()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetMainWindow()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLinePropertySet()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetProperties()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testFromClass()
    {
        fail("Not yet implemented");
    }
    
    private void 
    testProperty( String name, Class<GraphPropertySet> clazz )
        throws NoSuchMethodException, SecurityException
    {
        int     rawNameInx  = name.startsWith( "is" ) ? 2 : 3;
        String  rawName     = name.substring( rawNameInx );
        String  setterName  = "set" + rawName;
        
        Profile             pmgrVals    = new Profile();
        
        GraphPropertySet    pmgrSet     = pmgrVals.getMainWindow();
        GraphPropertySet    protoSet    = protoProfile.getMainWindow();
        GraphPropertySet    distinctSet = distinctProfile.getMainWindow();
        GraphPropertySet    workingSet  = workingProfile.getMainWindow();
        
        Class<? extends GraphPropertySet> pmgrClass     = 
            pmgrSet.getClass();
        Class<? extends GraphPropertySet> protoClass    = 
            protoSet.getClass();
        Class<? extends GraphPropertySet> distinctClass = 
            protoSet.getClass();
        Class<? extends GraphPropertySet> workingClass  = 
            protoSet.getClass();
        
        Method  pmgrGetter      = protoClass.getMethod( name, null );
        Method  protoGetter     = protoClass.getMethod( name, null );
        Method  distinctGetter  = 
            distinctSet.getClass().getMethod( name, null );
        Method  workingGetter   = 
            workingSet.getClass().getMethod( name, null );
        
        Class<?>    type        = pmgrGetter.getReturnType();
        Method  pmgrSetter      = protoClass.getMethod( name, type );
        Method  protoSetter     = protoClass.getMethod( name, type );
        Method  distinctSetter  = 
            distinctSet.getClass().getMethod( name, type );
        Method  workingSetter   = 
            distinctSet.getClass().getMethod( name, type );
    }
    
    private static String[] compileProperties( Class<?> clazz )
    {
        Predicate<String>   getFilter   = s -> s.startsWith( "get" );
        Predicate<String>   allFilter   = 
            getFilter.or( s -> s.startsWith( "is" ) );
        List<String>    names   =Stream.of( clazz.getMethods() )
            .map( m -> m.getName() )
            .filter( allFilter )
            .map( s -> s.substring( 3 ) )
            .toList();
        int         listSize    = names.size();
        String[]    namesArr    = 
            names.toArray( new String[listSize] );
        return namesArr;
    }
}
