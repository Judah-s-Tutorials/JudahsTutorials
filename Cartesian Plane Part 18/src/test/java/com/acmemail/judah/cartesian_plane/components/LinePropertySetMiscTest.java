package com.acmemail.judah.cartesian_plane.components;

/**
 * This test exists solely to get coverage
 * of a couple of odd lines
 * in LinePropertySet,
 * notably anything involving
 * hasColor() and hasStroke().
 * 
 * @author Jack Straub
 */
public class LinePropertySetMiscTest extends LinePropertySetTest
{   
    public LinePropertySetMiscTest()
    {
        super( 
            "",
            "",
            "",
            "",
            "",
            () -> new LinePropertySetMisc()
        );
    }

    // This class must be public so that it can be instantiated from
    // com.acmemail.judah.cartesian_plane.test_suites.LinePropertySetTestSuite.
    public static class LinePropertySetMisc 
        extends LinePropertySetAxes
    {
        @Override
        public boolean hasStroke()
        {
            return false;
        }
        
        @Override
        public boolean hasColor()
        {
            return false;
        }
    }
}
