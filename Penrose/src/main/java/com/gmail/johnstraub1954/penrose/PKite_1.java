package com.gmail.johnstraub1954.penrose;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PKite_1
{
    private static final double d36     = 36 * Math.PI / 180;
    
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
    private Shape   workShape;
    
    public PKite_1( double longSide )
    {
        midXOff = longSide * Math.cos( d36 );
        midYOff = longSide * Math.sin( d36 );
        rightXOff = longSide;
        bottomYOff = 2 * midYOff;
        
        path = new Path2D.Double();
        initPath();
        computePath();
    }
    
    public boolean contains( double xco, double yco ) 
    {
        return workShape.contains( xco, yco );
    }
    
    public void draw( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( drawColor );
        gtx.draw( workShape );
        gtx.setColor( save );
    }
    
    public void fill( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( fillColor );
        gtx.fill( workShape );
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
        
        double          xcoPin          = xco + rightXOff / 2;
        double          ycoPin          = yco + bottomYOff / 2;
        
        gtx.setColor( fillColor );
        gtx.fill( workShape );
        gtx.setColor( drawColor );
        gtx.draw( workShape );
        
        gtx.setColor( Color.RED );
        gtx.drawOval( (int)xcoPin, (int)ycoPin, 3, 3 );
        gtx.setColor( save );
    }
    
    private void computePath()
    {
        double          xcoPin  = xco + rightXOff / 2;
        double          ycoPin  = yco + bottomYOff / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        workShape = transform.createTransformedShape( path );
    }
    
    private void initPath()
    {
        path.reset();
        path.moveTo( 0,midYOff );
        path.lineTo( midXOff, 0 );
        path.lineTo( midXOff, 0 );
        path.lineTo( rightXOff, midYOff );
        path.lineTo( midXOff, bottomYOff );
        path.lineTo( xco, yco + midYOff );
    }
}
