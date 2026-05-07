package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.math.Complex;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PolarTest
{
    /**  
     * To test polar/rectangular conversions, 
     * this class uses the same algorithms as the Polar class.
     * As a sanity check,
     * the following rectangular/polar pairs
     * were calculated independently and hard-coded.
     * They are used to validate the internal
     * implementation of the conversion algorithms.
     */
    private static final SanityPair[]   sanityPairs =
    {
        new SanityPair( 
            new Point2D.Double( -1, 1 ), 
            Polar.of( 1.41421356237, 2.35619449 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, 1 ), 
            Polar.of( 1.41421356237, .7853981634 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, -1 ), 
            Polar.of( 1.41421356237, -.7853981634 )
        ),
        new SanityPair( 
            new Point2D.Double( -1, -1 ), 
            Polar.of( 1.41421356237, -2.35619449 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, 0 ), 
            Polar.of( 1, 0 )
        ),
        new SanityPair( 
            new Point2D.Double( 0, 1 ), 
            Polar.of( 1, 1.570796326 )
        ),
        new SanityPair( 
            new Point2D.Double( 0, 0 ), 
            Polar.of( 0, 0 )
        ),
    };
    
    @Test
    @Order( 1 )
    public void sanityCheck()
    {
        for ( SanityPair pair : sanityPairs )
            pair.confirm();
    }

    @Test
    public void testGetRadius()
    {
        double  radius  = 5;
        double  theta   = 3 * radius;
        Polar   zed     = Polar.of( radius, theta );
        assertEquals( radius, zed.getRadius() );
    }

    @Test
    public void testGetTheta()
    {
        double  radius  = 5;
        double  theta   = 3 * radius;
        Polar   zed     = Polar.of( radius, theta );
        assertEquals( theta, zed.getTheta() );
    }

    @Test
    public void testToString()
    {
        double  testRadius      = 5;
        double  testTheta       = 6;
        Polar   testPolar       = Polar.of( testRadius, testTheta );
        int     maxLen          = 4;
        String  testRadiusStr   = Double.toString( testRadius );
        if ( testRadiusStr.length() > maxLen )
            testRadiusStr = testRadiusStr.substring( 0, maxLen );
        String  testThetaStr    = Double.toString( testTheta );
        if ( testThetaStr.length() > maxLen )
            testThetaStr = testThetaStr.substring( 0, maxLen );
        
        String  testStr = testPolar.toString();
        assertTrue( testStr.contains( testRadiusStr ), testRadiusStr );
        assertTrue( testStr.contains( testThetaStr ), testThetaStr );
    }

    @Test
    public void testRadiusOfXY()
    {
        for ( SanityPair pair : sanityPairs )
        {
            double  xco         = pair.cartesian().getX();
            double  yco         = pair.cartesian().getY();
            double  expRadius   = pair.polar().getRadius();
            double  actRadius   = Polar.radiusOfXY( xco, yco );
            assertEquals( expRadius, actRadius, .0001 );
        }
    }

    @Test
    public void testThetaOfXY()
    {
        for ( SanityPair pair : sanityPairs )
        {
            double  xco         = pair.cartesian().getX();
            double  yco         = pair.cartesian().getY();
            double  expTheta    = pair.polar().getTheta();
            double  actTheta    = Polar.thetaOfXY( xco, yco );
            assertEquals( expTheta, actTheta, .0001 );
        }
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testToPoint( DoublePair pair )
    {
        Polar               zed     = pair.getPolar();
        Point2D             point   = zed.toPoint();
        XYToPolarChecker    checker = new XYToPolarChecker( zed );
        checker.confirm( point );
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testToComplex( DoublePair pair )
    {
        Polar               pzed    = pair.getPolar();
        Complex             zed     = pzed.toComplex();
        XYToPolarChecker    checker = new XYToPolarChecker( zed );
        checker.confirm( zed );
    }

    @Test
    public void testOfComplexNPE()
    {
        Complex zed = null;
        assertThrows( NullPointerException.class, () -> Polar.of( zed ) );
    }

    @Test
    public void testOfPointNPE()
    {
        Point2D point   = null;
        assertThrows( NullPointerException.class, () -> Polar.of( point ) );
    }
    
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testOfPoint( DoublePair pair )
    {
        Point2D             point   = pair.getPoint();
        XYToPolarChecker    checker = new XYToPolarChecker( point );
        Polar               pzed    = Polar.of( point );
        checker.confirm( pzed );
    }
    
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testOfXY( DoublePair pair )
    {
        XYToPolarChecker    checker = 
            new XYToPolarChecker( pair.left(), pair.right() );
        Polar               pzed    = Polar.ofXY( pair.left(), pair.right() );
        checker.confirm( pzed );
    }
    
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testOfComplex( DoublePair pair )
    {
        Complex             zed     = pair.getComplex();
        XYToPolarChecker    checker = new XYToPolarChecker( zed );
        Polar               pzed    = Polar.of( zed );
        checker.confirm( pzed );
    }
    
    /**
     * This method asserts that, given the limited test data,
     * unequal objects have unequal hashcodes.
     * THIS IS NOT REQUIRED BY THE JAVA SPECIFICATION.
     * The justification for testing this is that,
     * given a small data set,
     * if unequal objects generate the same hashcodes
     * there's probably something wrong 
     * with the hashcode algorithm,
     * and that could have significant negative effects
     * on operations that rely on hashing.
     * (In the context of hash tables, 
     * for instance,
     * it would generate a lot of collisions.)
     */
    @Test
    public void testEqualsHash()
    {
        double  radius1 = 16;
        double  radius2 = 2 * radius1;
        double  theta1  = 2;
        double  theta2  = 2 * theta1;
        Polar   polarA  = Polar.of( radius1, theta1 );
        // Sanity check; radius should be different from theta
        // so swapping fields would be detected
        assertNotEquals( radius1, theta1 );
        
        assertEquals( polarA, polarA );
        assertNotEquals( polarA, new Object() );
        assertNotEquals( polarA, null );
        
        Polar   polarB  = Polar.of( radius1, theta1 );
        assertEquals( polarA, polarB );
        assertEquals( polarB, polarA );
        assertEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.of( radius2, theta1 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.of( radius1, theta2 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.of( radius2, theta2 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        // Make sure NaN values work
        polarA = Polar.of( radius1, theta1 );
        polarB = Polar.of( Double.NaN, theta1 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarA = Polar.of( radius1, theta1 );
        polarB = Polar.of( radius1, Double.NaN );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarA = Polar.of( Double.NaN, theta1 );
        polarB = Polar.of( Double.NaN, theta1 );
        assertEquals( polarA, polarB );
        assertEquals( polarB, polarA );
        assertEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarA = Polar.of( radius1, Double.NaN );
        polarB = Polar.of( radius1, Double.NaN );
        assertEquals( polarA, polarB );
        assertEquals( polarB, polarA );
        assertEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarA = Polar.of( Double.NaN, Double.NaN );
        polarB = Polar.of( Double.NaN, Double.NaN );
        assertEquals( polarA, polarB );
        assertEquals( polarB, polarA );
        assertEquals( polarA.hashCode(), polarB.hashCode() );
    }
    
    /**
     * Generates a stream of DoublePair objects
     * for use in miscellaneous testing.
     * 
     * @return  a stream of DoublePair objects
     */
    private static Stream<DoublePair> doublePairProvider()
    {
        double          leftBase    = -1;
        double          rightBase   = -2;
        double          modMin      = .1;
        double          modMax      = 4;
        double          incr        = .1;
        Stream<DoublePair> stream  = 
            DoubleStream.iterate( modMin, d -> d < modMax, d -> d + incr )
            .mapToObj( d -> new DoublePair( leftBase + d, rightBase + d ) );
        return stream;
            
    }
    
    /**
     * Convenient class for storing
     * two double values,
     * labeled "left" and "right".
     * Performs no computations.
     */
    private record DoublePair( double left, double right )
    {
        /**
         * Using the left and right fields
         * as the radius and angle, respectively,
         * of a Polar object,
         * creates and returns the object.
         *
         * @return  the created Polar object
         */
        public Polar getPolar()
        {
            Polar   pzed    = Polar.of( left, right );
            return pzed;
        }

        /**
         * Using the left and right fields
         * as the x- and y-coordinates, respectively,
         * of a Point object,
         * creates and returns the object.
         *
         * @return  the created Point object
         */
        public Point2D getPoint()
        {
            Point2D point   = new Point2D.Double( left, right );
            return point;
        }

        /**
         * Using the left and right fields
         * as the real and imaginary parts, respectively,
         * of a Complex object,
         * creates and returns the object.
         *
         * @return  the created Complex object
         */
        public Complex getComplex()
        {
            Complex zed = new Complex( left, right );
            return zed;
        }
    }
    
    /**
     * Converts a Point or Complex object
     * to Polar object or vice versa
     * (generating the "expected result").
     * Confirms the expected result
     * against an actual result.
     */
    private static class XYToPolarChecker
    {
        private static final double epsilon = .001;
        private final double        xco;
        private final double        yco;
        private final double        radius;
        private final double        theta;
        
        /**
         * Converts a given point object
         * to the radius and angle of the 
         * equivalent Polar object
         * and stores the result for later validation.
         * 
         * @param point the given point object
         */
        public XYToPolarChecker( Point2D point )
        {
            this( point.getX(), point.getY() );
        }
        
        /**
         * Converts a given Complex object
         * to the radius and angle of the 
         * equivalent Polar object
         * and stores the result for later validation.
         * 
         * @param zed the given Complex object
         */
        public XYToPolarChecker( Complex zed )
        {
            this( zed.re(), zed.im() );
        }
        
        /**
         * Converts given x- and y-coordinates
         * to the radius and angle of the 
         * equivalent Polar object
         * and stores the result for later validation.
         * 
         * @param xco   the given x-coordinate
         * @param yco   the given y-coordinate
         */
        public XYToPolarChecker( double xco, double yco )
        {
            this.xco = xco;
            this.yco = yco;
            this.radius = Math.hypot( xco, yco );
            this.theta = Math.atan2( yco, xco );
        }
        
        /**
         * Converts a given Polar object
         * into the x- and y-coordinates
         * of the equivalent Point or Complex object
         * and stores the result for later validation.
         * 
         * @param pzed  the given Polar object
         */
        public XYToPolarChecker( Polar pzed )
        {
            this.radius = pzed.getRadius();
            this.theta = pzed.getTheta();
            double  cosTheta    = Math.cos( theta );
            double  sinTheta    = Math.sin( theta );
            this.xco = cosTheta * radius;
            this.yco = sinTheta * radius;
        }
        
        /**
         * Confirms that the radius and angle
         * of a given Polar object
         * are equivalent to the stored radius and angle.
         * 
         * @param pzed  the given Polar object
         */
        public void confirm( Polar pzed )
        {
            double  actRadius   = pzed.getRadius();
            double  actTheta    = pzed.getTheta();
            assertEquals( radius, actRadius, epsilon, "radius" );
            assertEquals( theta, actTheta, epsilon, "theta" );
        }
        
        /**
         * Confirms that the x- and y-coordinates
         * of a given Point object
         * are equivalent to the stored x- and y-coordinates.
         * 
         * @param point the given Point object
         */
        public void confirm( Point2D point )
        {
            confirm( point.getX(), point.getY() );
        }
        
        
        /**
         * Confirms that the real and imaginary parts
         * of a given Complex object
         * are equivalent to the stored x- and y-coordinates.
         * 
         * @param zed the given Complex object
         */
        public void confirm( Complex zed )
        {
            confirm( zed.re(), zed.im() );
        }
        
        /**
         * Confirms that the given x- and y-coordinates
         * are equivalent to the stored x- and y-coordinates.
         * 
         * @param actXco    the given x-coordinate
         * @param actYco    the given y-coordinate
         */
        public void confirm( double actXco, double actYco )
        {
            assertEquals( xco, actXco, epsilon, "xco" );
            assertEquals( yco, actYco, epsilon, "yco" );
        }
    }
    
    /**
     * This class supports validation of the internal algorithms
     * for rectangular/polar conversion.
     * The components are a Point object and a Polar object;
     * presumably these object are equivalent,
     * and independently generated
     * (by a web calculator, for example).
     */
    private record SanityPair( Point2D cartesian, Polar polar )
    {
        /**
         * Validates the internal algorithms
         * for converting between polar and rectangular coordinates;
         * note that the algorithms for converting between
         * polar and complex are equivalent.
         * <p>
         * Uses an XYToPolarChecker to convert this.cartesian
         * to a Polar object, and confirms that the checker object
         * matches this.polar.
         * Then it uses an XYToPolarChecker to convert this.polar
         * into a point,
         * and confirms that the checker object matches
         * this.cartesian.
         * </p>
         */
        public void confirm()
        {
            XYToPolarChecker    checker = new XYToPolarChecker( polar );
            checker.confirm( cartesian );
            checker = new XYToPolarChecker( cartesian );
            checker.confirm( polar );
        }

        @Override
        public String toString()
        {
            return polar + ", " + cartesian;
        }
    }
}
