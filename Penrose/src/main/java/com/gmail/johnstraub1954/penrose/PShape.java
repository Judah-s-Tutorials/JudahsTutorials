package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public abstract class PShape
{
    public static final double  D18     = 18 * Math.PI / 180;
    public static final double  D36     = 36 * Math.PI / 180;
    public static final double  D72     = 72 * Math.PI / 180;
    public static final double  D108    = 108 * Math.PI / 180;
    public static final double  D01     = Math.PI / 180;
    public static final double  TWO_PI  = 2 * Math.PI;
    
    private static double       dotDiam = 4;
    
    private final Path2D        path;
    private final Rectangle2D   rightBounds;
    
    private double  xco             = 0;
    private double  yco             = 0;
    private double  rotation        = 0;
    private Color   drawColor       = Color.BLACK;
    private Color   fillColor       = Color.WHITE;
    private Color   highlightColor  = Color.CYAN;
    private int     highlightWidth  = 1;
    private Shape   workShape;
    
    public abstract Path2D initPath( double longSide );
    
    public PShape( double longSide )
    {
        path = initPath( longSide );
        rightBounds = path.getBounds2D();
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
    
    public void highlight( Graphics2D gtx )
    {
        Color   saveColor   = gtx.getColor();
        Stroke  saveStroke  = gtx.getStroke();
        gtx.setColor( highlightColor );
        gtx.setStroke( new BasicStroke( highlightWidth ) );
        gtx.draw( workShape );
        gtx.setStroke( saveStroke );
        gtx.draw( workShape );
        gtx.setColor( saveColor );
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
        Rectangle2D bounds  = workShape.getBounds2D();
        return bounds;
    }
    
    public Rectangle2D getRightBounds()
    {
        double      width   = rightBounds.getWidth();
        double      height  = rightBounds.getHeight();
        Rectangle2D bounds  = 
            new Rectangle2D.Double( xco, yco, width, height );
        return bounds;
    }
    
    public void render( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        double  xcoPin  = xco + rightBounds.getWidth() / 2;
        double  ycoPin  = yco + rightBounds.getHeight() / 2;
        
        gtx.setColor( fillColor );
        gtx.fill( workShape );
        gtx.setColor( drawColor );
        gtx.draw( workShape );
        
        gtx.setColor( Color.RED );
        gtx.drawOval( (int)xcoPin, (int)ycoPin, 3, 3 );
        gtx.setColor( save );
    }
    
    public Color getDrawColor()
    {
        return drawColor;
    }

    public void setDrawColor(Color drawColor)
    {
        this.drawColor = drawColor;
    }

    public Color getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(Color fillColor)
    {
        this.fillColor = fillColor;
    }

    public Color getHighlightColor()
    {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor)
    {
        this.highlightColor = highlightColor;
    }

    public int getHighlightWidth()
    {
        return highlightWidth;
    }

    public void setHighlightWidth(int highlightWidth)
    {
        this.highlightWidth = highlightWidth;
    }

    public static double getDotDiam()
    {
        return dotDiam;
    }
    
    public static void setDotDiam( double diam )
    {
        dotDiam = diam;
    }
    
    private void computePath()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        workShape = transform.createTransformedShape( path );
    }
}
