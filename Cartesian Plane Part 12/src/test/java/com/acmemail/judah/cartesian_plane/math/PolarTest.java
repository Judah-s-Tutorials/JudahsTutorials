package com.acmemail.judah.cartesian_plane.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PolarTest
{ 
    private static final    Class<IllegalArgumentException>  illArgClass =
        IllegalArgumentException.class;
    private static final    double       NaN     = Double.NaN;
    private static final    double       NEG_INF = Double.NEGATIVE_INFINITY;
    private static final    double       POS_INF = Double.POSITIVE_INFINITY;
    
    // Convenient constants //
    /** PI */
    private static final double PI      = Math.PI;
    private static final double PI_2    = PI * 2;
    private static final double epsilon = .001;
    /** Square root of 2 */
    private static final double ROOT_2  = Math.sqrt( 2 );
    private static final Random randy   = new Random( 1 );

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
            new Point2D.Double( 0, 0 ), 
            new PolarPair( 0, 0 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, 0 ), 
            new PolarPair( 1, 0 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, 1 ), 
            new PolarPair( ROOT_2, PI / 4 )
        ),
        new SanityPair( 
            new Point2D.Double( 0, 1 ), 
            new PolarPair( 1, PI / 2 )
        ),
        new SanityPair( 
            new Point2D.Double( -1, 1 ), 
            new PolarPair( ROOT_2, 3 * PI / 4 )
        ),
        new SanityPair( 
            new Point2D.Double( -1, 0 ), 
            new PolarPair( 1, PI )
        ),
        new SanityPair( 
            new Point2D.Double( -1, -1 ), 
            new PolarPair( ROOT_2, 5 * PI / 4 )
        ),
        new SanityPair( 
            new Point2D.Double( 0, -1 ), 
            new PolarPair( 1, 3 * PI / 2 )
        ),
        new SanityPair( 
            new Point2D.Double( 1, -1 ), 
            new PolarPair( ROOT_2, 7 * PI / 4 )
        ),
    };
    
    @Test
    @Order( 1 )
    public void sanityCheck()
    {
        for ( SanityPair pair : sanityPairs )
        {
            System.out.println( pair );
            pair.confirm();
        }
    }

    @Test
    public void testGetRadius()
    {
        double  radius  = 5;
        double  theta   = 3 * radius;
        Polar   zed     = Polar.ofRTheta( radius, theta );
        assertEquals( radius, zed.getRadius() );
    }

    @Test
    public void testGetTheta()
    {
        double  radius      = 5;
        double  theta       = 3 * radius;
        double  expTheta    = theta % PI_2;
        Polar   zed         = Polar.ofRTheta( radius, theta );
        assertEquals( expTheta, zed.getTheta(), epsilon );
    }

    @Test
    public void testToString()
    {
        double  testRadius      = 5;
        double  testTheta       = 6;
        Polar   testPolar       = Polar.ofRTheta( testRadius, testTheta );
        
        String  testStr         = testPolar.toString();
        assertTrue( testStr.contains( Double.toString( testRadius ) ) );
        assertTrue( testStr.contains( Double.toString( testTheta ) ) );
        assertTrue( testStr.startsWith( "Polar[radius=" ), testStr );
        assertTrue( testStr.endsWith( " radians]" ), testStr );
    }

    @Test
    public void testRadiusOfXY()
    {
        for ( SanityPair pair : sanityPairs )
        {
            double  xco         = pair.cartesian().getX();
            double  yco         = pair.cartesian().getY();
            double  expRadius   = pair.polarPair().radius();
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
            double  expTheta    = pair.polarPair().theta();
            double  actTheta    = Polar.thetaOfXY( xco, yco );
            assertEquals( expTheta, actTheta, .0001 );
        }
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testToPoint( DoublePair pair )
    {
        Polar               polar       = pair.getPolar();
        PolarPair           polarPair   = new PolarPair( polar );
        Point2D             actPoint    = polar.toPoint();
        Point2D             expPoint    = polarPair.toPoint();
        confirm( actPoint, expPoint );
    }

    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testToComplex( DoublePair pair )
    {
        Polar               pzed        = pair.getPolar();
        PolarPair           polarPair   = new PolarPair( pzed );
        Complex             actComplex  = pzed.toComplex();
        Complex             expComplex  = polarPair.toComplex();
        confirm( expComplex, actComplex );
    }

    @Test
    public void testOfComplexNPE()
    {
        Complex zed = null;
        assertThrows( NullPointerException.class, () -> Polar.ofComplex( zed ) );
    }

    @Test
    public void testOfPointNPE()
    {
        Point2D point   = null;
        assertThrows( NullPointerException.class, () -> Polar.ofPoint( point ) );
    }
    
    @Test
    public void testOfRThetaIllegalArgumentException()
    {
        testIllegalArg( (radius,theta) -> Polar.ofRTheta( radius, theta ) );
    }
    
    @Test
    public void testThetaOfXYIllegalArgumentException()
    {
        testIllegalArg( (xco,yco) -> Polar.thetaOfXY( xco, yco ) );
    }
    
    @Test
    public void testRadiusOfXYIllegalArgumentException()
    {
        testIllegalArg( (xco,yco) -> Polar.radiusOfXY( xco, yco ) );
    }
    
    @Test
    public void testOfComplexIllegalArgumentException()
    {
        testIllegalArg( (real,imag) ->
            Polar.ofComplex( new Complex( real, imag ) )
        );
    }
    
    @Test
    public void testOfPointIllegalArgumentException()
    {
        testIllegalArg( (xco,yco) ->
            Polar.ofPoint( new Point2D.Double( xco, yco ) )
        );
    }
    
    @ParameterizedTest
    @MethodSource( "doublePairProvider" )
    public void testOfPoint( DoublePair pair )
    {
        Point2D             point       = pair.getPoint();
        PolarPair           polarPair   = xyToPolar( point );
        Polar               pzed        = Polar.ofPoint( point );
        polarPair.confirm( pzed );
    }
    
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testOfXY( DoublePair pair )
    {
        double      xco         = pair.left();
        double      yco         = pair.right();
        PolarPair   expPolar    = xyToPolar( xco, yco );
        Polar       actPolar    = Polar.ofXY( xco, yco );
        expPolar.confirm( actPolar );
    }
    
    @ParameterizedTest
    @MethodSource("doublePairProvider")
    public void testOfComplex( DoublePair pair )
    {
        Complex             zed         = pair.getComplex();
        PolarPair           expPolar    = xyToPolar( zed.re(), zed.im() );
        Polar               actPolar    = Polar.ofComplex( zed );
        expPolar.confirm( actPolar );
    }
    
    @Test
    public void testEdgeCases1()
    {
        final double  epsilon = 1.0e-12;
        final double  tiny    = epsilon / 100;
        
        Polar   base    = Polar.ofRTheta( 1, PI_2 - tiny );
        Point2D point   = base.toPoint();
        Polar   recon   = Polar.ofPoint( point );
        assertEquals( PI_2, base.getTheta(), epsilon );
        confirm( base, recon );
        
        base    = Polar.ofRTheta( 1, PI_2 + tiny );
        point   = base.toPoint();
        recon   = Polar.ofPoint( point );
        assertEquals( 0, base.getTheta(), epsilon );
        confirm( base, recon );
        
        base    = Polar.ofRTheta( 1, 0 - tiny );
        point   = base.toPoint();
        recon   = Polar.ofPoint( point );
        assertEquals( PI_2, base.getTheta(), epsilon );
        confirm( base, recon );
        
        base    = Polar.ofRTheta( 1, 0 + tiny );
        point   = base.toPoint();
        recon   = Polar.ofPoint( point );
        assertEquals( 0, base.getTheta(), epsilon );
        confirm( base, recon );
        
        base    = Polar.ofRTheta( 0 - tiny, PI / 2 );
        point   = base.toPoint();
        recon   = Polar.ofPoint( point );
        assertEquals( 3 * PI / 2, base.getTheta(), epsilon );
        confirm( base, recon );
        
        base    = Polar.ofRTheta( 0 + tiny, PI / 2 );
        point   = base.toPoint();
        recon   = Polar.ofPoint( point );
        assertEquals( PI / 2, base.getTheta(), epsilon );
        confirm( base, recon );
    }
    
    @Test
    public void testEdgeCases2()
    {
        Polar   actPolar    = Polar.ofRTheta( -1, Math.PI );
        Polar   expPolar    = Polar.ofRTheta( 1, 0 );
        confirm( actPolar, expPolar );
        
        actPolar = Polar.ofRTheta( -1, 3 * Math.PI / 2 );
        expPolar = Polar.ofRTheta( 1, Math.PI / 2 );
        confirm( actPolar, expPolar );
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
        Polar   polarA  = Polar.ofRTheta( radius1, theta1 );
        // Sanity check; radius should be different from theta
        // so swapping fields would be detected
        assertNotEquals( radius1, theta1 );
        
        assertEquals( polarA, polarA );
        assertNotEquals( polarA, new Object() );
        assertNotEquals( polarA, null );
        
        Polar   polarB  = Polar.ofRTheta( radius1, theta1 );
        assertEquals( polarA, polarB );
        assertEquals( polarB, polarA );
        assertEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.ofRTheta( radius2, theta1 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.ofRTheta( radius1, theta2 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
        
        polarB = Polar.ofRTheta( radius2, theta2 );
        assertNotEquals( polarA, polarB );
        assertNotEquals( polarB, polarA );
        assertNotEquals( polarA.hashCode(), polarB.hashCode() );
    }
    
    /**
     * Verifies that a given consumer will raise 
     * an IllegalArgumentException
     * when presented with NaN or infinite values.
     * 
     * @param consumer  the given consumer
     */
    private static void 
    testIllegalArg( BiConsumer<Double,Double> consumer )
    {
        assertThrows( illArgClass, () -> consumer.accept( NaN, PI ) );
        assertThrows( illArgClass, () -> consumer.accept( PI, NaN ) );
        assertThrows( illArgClass, () -> consumer.accept( NaN, NaN ) );
        
        assertThrows( illArgClass, () -> consumer.accept( NEG_INF, PI ) );
        assertThrows( illArgClass, () -> consumer.accept( PI, NEG_INF ) );
        assertThrows( illArgClass, () -> consumer.accept( NEG_INF, NEG_INF ) );

        assertThrows( illArgClass, () -> consumer.accept( POS_INF, PI ) );
        assertThrows( illArgClass, () -> consumer.accept( PI, POS_INF ) );
        assertThrows( illArgClass, () -> consumer.accept( POS_INF, POS_INF ) );
    }
    
    /**
     * Generates a stream of DoublePair objects
     * for use in miscellaneous testing.
     * Approximately one quarter of the stream
     * will contain values from 
     * each of the four quadrants of the Cartesian plane.
     * 
     * @return  a stream of DoublePair objects
     */
    private static Stream<DoublePair> doublePairProvider()
    {
        Stream<DoublePair>  quad1   = generator( 0, 10, 0, 10 );
        Stream<DoublePair>  quad2   = generator( 0, 10, -10, 0 );
        Stream<DoublePair>  quad3   = generator( -10, 0, -10, 0 );
        Stream<DoublePair>  quad4   = generator( -10, 0, 0, 10 );
        Stream<DoublePair>  stream  = Stream.of( quad1, quad2, quad3, quad4 )
            .flatMap( s -> s);
        return stream;
    }

    /**
     * Generates a stream of DoublePairs
     * where the left values are between
     * the given minimum and maximum X values,
     * that the right values are between
     * the given min and max Y values.
     * 
     * @param minX  the given minimum X value
     * @param maxX  the given maximum X value
     * @param minY  the given minimum Y value
     * @param maxY  the given maximum Y value
     * @return
     */
    private static Stream<DoublePair> generator( 
        double minX, 
        double maxX, 
        double minY, 
        double maxY 
    )
    {
        Stream<DoublePair> stream =
            IntStream.range( 0, 10 )
                .mapToObj( 
                    i -> new DoublePair(
                            randy.nextDouble( minX, maxX ),  
                            randy.nextDouble( minY, maxY )
                        )
                );
        return stream;
    }
    
    /**
     * Derives a PolarPair object
     * from rectangular x- and y-coordinates.
     * The contents of the derived object
     * will be valid, normalized polar coordinates.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the computed polar coordinates
     */
    private static PolarPair xyToPolar( Point2D point )
    {
        PolarPair polarPair = xyToPolar( point.getX(), point.getY() );
        return polarPair;
    }
    
    /**
     * Computes the polar coordinates
     * from rectangular x- and y-coordinate.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the computed polar coordinates
     */
    private static PolarPair xyToPolar( double xco, double yco )
    {
        double      radius  = xyToRadius( xco, yco );
        double      theta   = xyToTheta( xco, yco );
        PolarPair   pair    = new PolarPair( radius, theta );
        return pair;
    }
    
    /**
     * Computes the radius component of polar coordinates
     * from rectangular x- and y-coordinate.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the computed radius component
     */
    private static double xyToRadius( double xco, double yco )
    {
        double  radius  = xco == 0 && yco == 0 ? 
            0 : Math.hypot( xco, yco );
        return radius;
    }
    
    /**
     * Computes the angle component of polar coordinates
     * from rectangular x- and y-coordinate.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the computed angle component
     */
    private static double xyToTheta( double xco, double yco )
    {
        double  theta   = xco == 0 && yco == 0 ? 
            0 : Math.atan2( yco, xco );
        if ( theta < 0 )
            theta += 2 * PI;
        return theta;
    }
    
    /**
     * Confirms that two given points
     * are approximately equal.
     * 
     * @param polarA    the first given point
     * @param polarB    the second given point
     */
    private static void confirm( Point2D pointA, Point2D pointB )
    {
        assertEquals( pointA.getX(), pointB.getX(), epsilon );
        assertEquals( pointA.getY(), pointB.getY(), epsilon );
    }
    
    /**
     * Confirms that two given Complex objects
     * are approximately equal.
     * 
     * @param polarA    the first given Complex object
     * @param polarB    the second given Complex object
     */
    private static void confirm( Complex pointA, Complex pointB )
    {
        assertEquals( pointA.re(), pointB.re(), epsilon );
        assertEquals( pointA.im(), pointB.im(), epsilon );
    }
    
    /**
     * Confirms that two given Polar objects
     * are approximately equal.
     * 
     * @param polarA    the first given object
     * @param polarB    the second given object
     */
    private static void confirm( Polar polarA, Polar polarB )
    {
        assertEquals( polarA.getRadius(), polarB.getRadius(), epsilon );
        assertEquals( polarA.getTheta(), polarB.getTheta(), epsilon );
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
            Polar   pzed    = Polar.ofRTheta( left, right );
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
     * This class represents a pair of values
     * that are taken to be polar coordinates.
     * The pair is not authenticated or normalized in any way,
     * since they may be used in tests
     * that require the pair to be authenticated or normalized.
     * In addition,
     * it has convenience methods for converting the pair
     * to rectangular coordinates 
     * without invoking the algorithms in the Polar class.
     */
    private record PolarPair( double radius, double theta )
    {
        /**
         * Non-canonical constructor that imports
         * a Polar object.
         * 
         * @param polar the object to import
         */
        public PolarPair( Polar polar )
        {
            this( polar.getRadius(), polar.getTheta() );
        }
        
        /**
         * Confirms that a Polar object
         * is approximately equal to the coordinates
         * encapsulated in a PolarPair object.
         * 
         * @param polar the Polar object to confirm
         */
        public void confirm( Polar polar )
        {
            String  note    = polar.toString();
            assertEquals( radius, polar.getRadius(), epsilon, note );
            assertEquals( theta, polar.getTheta(), epsilon, note );
        }
        
        /**
         * Computes a Cartesian point
         * from the encapsulated coordinates.
         * 
         * @return  
         *      a Cartesian point equivalent to
         *      the encapsulated Polar coordinates
         */
        public Point2D toPoint()
        {
            double  xco     = radius * Math.cos( theta );
            double  yco     = radius * Math.sin( theta );
            Point2D point   = new Point2D.Double( xco, yco );
            return point;
        }
        
        /**
         * Computes a complex number in normal form
         * from the encapsulated coordinates.
         * 
         * @return  
         *      a complex number in normal form equivalent to
         *      the encapsulated Polar coordinates
         */
        public Complex toComplex()
        {
            Point2D point   = toPoint();
            double  real    = point.getX();
            double  imag    = point.getY();
            Complex complex = new Complex( real, imag );
            return complex;
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
    private record SanityPair( Point2D cartesian, PolarPair polarPair )
    {
        /**
         * Validates the internal algorithms
         * for converting between polar and rectangular coordinates;
         * note that the algorithms for converting between
         * polar and complex are equivalent.
         */
        public void confirm()
        {
            Polar   polarTest       = Polar.ofPoint( cartesian );
            Point2D cartesianTest   = polarTest.toPoint();
            polarPair.confirm( polarTest );
            PolarTest.confirm( cartesian, cartesianTest );
        }

        @Override
        public String toString()
        {
            return polarPair + ", " + cartesian;
        }
    }
}
