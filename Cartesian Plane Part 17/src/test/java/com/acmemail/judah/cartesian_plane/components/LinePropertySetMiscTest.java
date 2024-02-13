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
class LinePropertySetMiscTest extends LinePropertySetTest
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

    private static class LinePropertySetMisc 
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
