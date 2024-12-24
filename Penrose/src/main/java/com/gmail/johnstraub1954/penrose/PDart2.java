package com.gmail.johnstraub1954.penrose;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PDart2
{
    private static final double d36     = 36 * Math.PI / 180;
    private static final double d108    = 108 * Math.PI / 180;
    private static final double twoPI   = Math.PI * 2;
    
    private final double    midXOff;
    private final double    rightXOff;
    private final double    midYOff;
    private final double    bottomYOff;
    private final Path2D    path;
    
    private double  xco         = 0;
    private double  yco         = 0;
    private double  rotation    = 0;
    private Color   drawColor   = Color.BLACK;
    private Color   fillColor   = Color.WHITE;
     
    public PDart2( double longSide )
    {
        double  base    = longSide * ( Math.sin( d36 ) / Math.sin( d108 ) );
        
        System.out.println( longSide + ", " + base );
        double  rightXco    = longSide * Math.cos( d36 );
        double  midYco      = longSide * Math.sin( d36 );
        
        midXOff = rightXco - base;
        midYOff = midYco;
        rightXOff = rightXco;
        bottomYOff = 2 * midYOff;
        
        path = new Path2D.Double();
        computePath();
    }
    
    public void draw( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( drawColor );
        gtx.draw( path );
        gtx.setColor( save );
    }
    
    public void fill( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( fillColor );
        gtx.fill( path );
        gtx.setColor( save );
    }
    
    public void rotate( double radians )
    {
        rotateTo( rotation + radians );
    }
    
    public void rotateTo( double radians )
    {
        rotation = radians;
        computePath();
    }
    
    public void move( double xDelta, double yDelta )
    {
        moveTo( xco + xDelta, yco + yDelta );
    }
    
    public void moveTo( double xco, double yco )
    {
        this.xco = xco;
        this.yco = yco;
        computePath();
    }
    
    public Rectangle2D getBounds()
    {
        Rectangle2D bounds  = 
            new Rectangle2D.Double( xco, yco, rightXOff, bottomYOff );
        return bounds;
    }
    
    public void render( Graphics2D gtx )
    {
        Color           save            = gtx.getColor();
        gtx.setColor( fillColor );
        gtx.fill( path );
        gtx.setColor( drawColor );
        gtx.draw( path );
        
        double          xcoPin  = xco + rightXOff / 2;
        double          ycoPin  = yco + bottomYOff / 2;
        gtx.setColor( Color.RED );
        gtx.drawOval( (int)xcoPin, (int)ycoPin, 3, 3 );
        gtx.setColor( save );
    }
    
    public boolean contains( double xco, double yco ) 
    {
        return path.contains( xco, yco );
    }

    private void computePath()
    {
        double          xcoPin  = xco + rightXOff / 2;
        double          ycoPin  = yco + bottomYOff / 2;
        RotateContext   rotater = 
            new RotateContext( xcoPin, ycoPin, rotation );
        Point2D         point;
        
        path.reset();
        point = rotater.rotate( xco, yco );
        path.moveTo( point.getX(), point.getY() );

        point = rotater.rotate( xco + rightXOff, yco + midYOff );
        path.lineTo( point.getX(), point.getY() );

        point = rotater.rotate( xco, yco + bottomYOff );
        path.lineTo( point.getX(), point.getY() );

        point = rotater.rotate( xco + midXOff, yco + midYOff );
        path.lineTo( point.getX(), point.getY() );

        point = rotater.rotate( xco, yco );
        path.lineTo( point.getX(), point.getY() );
    }
}
