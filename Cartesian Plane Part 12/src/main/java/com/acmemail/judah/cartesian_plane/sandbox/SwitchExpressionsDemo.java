package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;

import com.acmemail.judah.cartesian_plane.math.Polar;

/**
 * This class exists to validate the syntax
 * of the code in the <em>Switch Expression digression</em>.
 * It is of little interest for any other use.
 */
@SuppressWarnings("unused")
public class SwitchExpressionsDemo
{
    /**
     * Default constructor; not used.
     */
    private SwitchExpressionsDemo()
    {
        // not used
    }

    private void demo1( String input )
    {
        switch ( input )
        {
        case "January":
            System.out.println( "1/Jan" );
            break;
        case "February":
            System.out.println( "2/Feb" );
            break;
        default:
            System.out.println( "eh?" );
        }
        
        switch ( input )
        {
            case "January" -> System.out.println( "1/Jan" );
            case "February" -> System.out.println( "2/Feb" );
            default -> System.out.println( "eh?" );
        }

    }

    private void demo2( String input )
    {
        switch ( input )
        {
        case "January":
        case "February":
        case "March":
            System.out.println( "Q1" );
            break;
        case "April":
        case "May":
        case "June":
            System.out.println( "Q2" );
            break;
        default:
            System.out.println( "eh?" );
        }
        
        switch ( input )
        {
            case "January", "February", "March" -> 
                System.out.println( "Q1" );
            case "April", "May", "June" -> 
                System.out.println( "Q2" );
            default -> System.out.println( "eh?" );
        }

    }

    private void demo3( String input )
    {
        String result = "";
        switch ( input )
        {
        case "January":
            result = "1/Jan";
            break;
        case "February":
            result = "2/Feb";
            break;
        default:
            result = "eh?";
        }
        System.out.println( result );

        String result2 = 
            switch ( input )
            {
                case "January" -> "1/Jan";
                case "February" -> "2/Feb";
                default -> "eh?";
            };
        System.out.println( result2 );
    }

    private void demo4( String property )
    {
        double  radius  = 5;
        double  theta   = Math.PI;
        double  t       = 3;
        
        Point2D point   = null;
        switch ( property )
        {
        case "Polar":
            double  rad = 5 * theta + 1;
            Polar   pol = Polar.ofRTheta( rad, theta );
            point = pol.toPoint();
            break;
        case "Param":
            double  xco = 5 * Math.cos( theta );
            double  yco = 2 * Math.sin( theta );
            point = new Point2D.Double( xco, yco );
            break;
        default:
            point = new Point2D.Double( 0, 0 );
        }

    point = 
        switch ( property )
        {
            case "Polar" -> {
                double  rad = 5 * theta + 1;
                Polar   pol = Polar.ofRTheta( rad, theta );
                point = pol.toPoint();
                yield point;
            }
            case "Param" -> {
                double  xco = 5 * Math.cos( theta );
                double  yco = 2 * Math.sin( theta );
                point = new Point2D.Double( xco, yco );
                yield point;
            }
            default -> {
                System.err.println( "unsupported" );
                yield new Point2D.Double( 0, 0 );
            }
        };
        System.out.println( point );
    }
}
