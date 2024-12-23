package com.gmail.johnstraub1954.penrose;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class PShape
{
    private static final double d36     = 36 * Math.PI / 180;
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
    
    
    public PShape( double longSide )
    {
        midXOff = longSide * Math.cos( d36 );
        midYOff = longSide * Math.sin( d36 );
        rightXOff = longSide;
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
        rotation += radians;
        rotation %= twoPI;
    }
    
    public void rotateTo( double radians )
    {
        rotation = radians;
    }
    
    public void move( double xDelta, double yDelta )
    {
        this.xco += xDelta;
        this.yco += yDelta;
    }
    
    public void moveTo( double xco, double yco )
    {
        this.xco = xco;
        this.yco = yco;
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
        AffineTransform saveTransform   = gtx.getTransform();
        
        double          xcoPin          = xco + rightXOff / 2;
        double          ycoPin          = yco + bottomYOff / 2;
        
        gtx.rotate( rotation, xcoPin, ycoPin );
        gtx.translate( xco, yco );
        gtx.setColor( fillColor );
        gtx.fill( path );
        gtx.setColor( drawColor );
        gtx.draw( path );
        
        gtx.setTransform( saveTransform );
        gtx.setColor( Color.RED );
        gtx.drawOval( (int)xcoPin, (int)ycoPin, 3, 3 );
        gtx.setColor( save );
    }
    
    private void computePath()
    {
        path.reset();
        path.moveTo( xco, yco + midYOff );
        path.lineTo( xco + midXOff, yco );
        path.lineTo( xco + rightXOff, yco + midYOff );
        path.lineTo( xco + midXOff, yco + bottomYOff );
        path.lineTo( xco, yco + midYOff );
    }
    
    private void computePath2()
    {
        double  centerXco   = xco + midXOff;
        double  centerYco   = yco + midYOff;
    }
}
