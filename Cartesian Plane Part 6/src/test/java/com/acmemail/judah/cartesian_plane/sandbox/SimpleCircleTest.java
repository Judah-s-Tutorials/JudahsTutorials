package com.acmemail.judah.cartesian_plane.sandbox;

public class SimpleCircleTest
{
    public static void main( String[] args )
    {
        boolean result  = true;
        result = result && ctorTest();
        result = result && getAreaTest();
        result = result && getCircumferenceTest();
        result = result && getRadiusTest();
        result = result && setRadiusTest();
        result = result && getXcoTest();
        result = result && setXcoTest();
        result = result && getYcoTest();
        result = result && setYcoTest();
        
        String  msg = 
            result ? "Unit test passed" : "unit test failed";
        System.err.println( msg );
    }
    
    private static boolean ctorTest()
    {
        double  xco         = 10;
        double  yco         = 20;
        double  radius      = 30;
        Circle  circle      = new Circle( xco, yco, radius );
        
        double  actXco      = circle.getXco();
        double  actYco      = circle.getYco();
        double  actRadius   = circle.getRadius();
        
        boolean result      = true;
        result = result && testEquals( "ctorTest", xco, actXco );
        result = result && testEquals( "ctorTest", yco, actYco );
        result = result && testEquals( "ctorTest", radius, actRadius );
        
        return result;
    }
    
    private static boolean getCircumferenceTest()
    {
        double  radius      = 30;
        double  expCircum   = Math.PI * 2 * radius;
        Circle  circle      = new Circle( 0, 0, radius );
        double  actCircum   = circle.getCircumference();
        
        boolean result      = 
            testEquals( "getCircumferenceTest", expCircum, actCircum );
        
        return result;
    }
    
    private static boolean getAreaTest()
    {
        double  radius      = 30;
        double  expArea     = Math.PI * radius * radius;
        Circle  circle      = new Circle( 0, 0, radius );
        double  actArea     = circle.getArea();
        
        boolean result      = testEquals( "getAreaTest", expArea, actArea );
        
        return result;
    }
    
    private static boolean getXcoTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setXco( expVal );
        double  actVal      = circle.getXco();
        
        boolean result      = testEquals( "getXcoTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean setXcoTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setXco( expVal );
        double  actVal      = circle.getXco();
        
        boolean result      = testEquals( "setXcoTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean getYcoTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setYco( expVal );
        double  actVal      = circle.getYco();
        
        boolean result      = testEquals( "getAreaTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean setYcoTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setYco( expVal );
        double  actVal      = circle.getYco();
        
        boolean result      = testEquals( "setYcoTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean setRadiusTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setRadius( expVal );
        double  actVal      = circle.getRadius();
        
        boolean result      = testEquals( "setRadiusTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean getRadiusTest()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setRadius( expVal );
        double  actVal      = circle.getRadius();
        
        boolean result      = testEquals( "getRadiusTest", expVal, actVal );
        
        return result;
    }
    
    private static boolean 
    testEquals( String testName, double expValue, double actValue )
    {
        final double    epsilon = .001;
        double  diff    = Math.abs( expValue - actValue );
        boolean result  = diff < epsilon;
        {
            if ( !result )
            {
                String  msg = 
                    testName + ": expected = " + expValue + 
                    " actual = " + actValue; 
                System.err.println( msg );
            }
            return result;
        }
    }
}
