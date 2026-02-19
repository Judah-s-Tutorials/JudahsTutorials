package com.acmemail.judah.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.LineGenerator;

@SuppressWarnings("serial")
public class LineGeneratorDemoPanel extends JPanel
{
    private static final int    winWidth    = 1250;
    private static final int    winHeight   = 750;
    private static final Color  winColor    = Color.LIGHT_GRAY;
    private static final double rectXco     = 200;
    private static final double rectYco     = 50;
    private static final double rectWidth   = 1000;
    private static final double rectHeight  = 600;
    private static final Color  rectColor   = Color.BLACK;
    private static final Stroke rectStroke  = new BasicStroke( 1 );
    private static final float  gpu         = 100;
    private static final float  lpu         = 2;
    private static final float  lineWidth   = 1;
    private static final Color  lineColor   = Color.CYAN;
    private static final Stroke lineStroke  = 
        new BasicStroke( lineWidth );
    private static final Color  axisColor   = Color.GREEN;
    private static final float  guideWidth  = 5;
    private static final Color  guideColor  = Color.RED;
    private static final Color  fontColor   = Color.BLACK;
    private static final int    fontSize    = 10;
    private static final String fontName    = "MONOSPACED";
    
    private static final Font   font        = 
        new Font( fontName, Font.PLAIN, fontSize );
    private static final Rectangle2D    rect    =
        new Rectangle2D.Double( rectXco, rectYco, rectWidth, rectHeight );
    private static final Stroke guideStroke     = 
        new BasicStroke( guideWidth );
    private static final double originXco   = rect.getCenterX();
    private static final double originYco   = rect.getCenterY();
    
    private int                 width;
    private int                 height;
    private Graphics2D          gtx;
    private FontRenderContext   frc;
    
    public LineGeneratorDemoPanel()
    {
        Dimension   dim     = new Dimension( winWidth, winHeight );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        
        width = getWidth();
        height = getHeight();
        gtx = (Graphics2D)graphics;
        gtx.setFont( font );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( winColor );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( rectColor );
        gtx.setStroke( rectStroke );
        gtx.draw( rect );
        
        drawGuides();
        drawGridLines();
    }
    
    private void drawGuides()
    {
        Line2D  line    = new Line2D.Double();
        double  diff    = gpu / lpu;
        double  endXco  = rect.getMaxX();
        double  endYco  = rect.getMaxY();
        
        gtx.setColor( guideColor );
        gtx.setStroke( guideStroke );
        for ( double yco = originYco ; yco >= rectYco ; yco -= diff )
        {
            line.setLine( rectXco, yco, endXco, yco );
            gtx.draw( line );
            drawHorizontalLabel( line );
        }

        for ( double yco = originYco + diff ; yco <= endYco ; yco += diff )
        {
            line.setLine( rectXco, yco, endXco, yco );
            gtx.draw( line );
            drawHorizontalLabel( line );
        }
        
        for ( double xco = originXco ; xco >= rectXco ; xco -= diff )
        {
            line.setLine( xco, rectYco, xco, endYco );
            gtx.draw( line );
            drawVerticalLabel( line );
        }
        
        for ( double xco = originXco + diff ; xco <= endXco ; xco += diff )
        {
            line.setLine( xco, rectYco, xco, endYco );
            gtx.draw( line );
            drawVerticalLabel( line );
        }
    }
    
    private void drawGridLines()
    {
        LineGenerator  lineGen = 
            new LineGenerator( rect, gpu, lpu );
        gtx.setStroke( lineStroke );
        gtx.setColor( lineColor );
        for ( Line2D line : lineGen )
            gtx.draw( line );
        
        gtx.setColor( axisColor );        
        Iterator<Line2D>    axes    = lineGen.axesIterator();
        gtx.draw( axes.next() );
        gtx.draw( axes.next() );
    }
    
    private void drawHorizontalLabel( Line2D line )
    {
        double      xco         = line.getX1();
        double      yco         = line.getY1();
        String      strYco      = String.format( "%4.1f",  yco );
        TextLayout  layout      = new TextLayout( strYco, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        float       labelXco    = (float)(xco - bounds.getWidth() - 5);
        float       labelYco    = (float)(yco + bounds.getHeight() / 2);
        
        Color       save        = gtx.getColor();
        gtx.setColor( fontColor );
        layout.draw( gtx, labelXco, labelYco );
        gtx.setColor( save );
    }
    
    private void drawVerticalLabel( Line2D line )
    {
        double      xco         = line.getX1();
        double      yco         = line.getY2();
        String      strXco      = String.format( "%4.1f", xco );
        TextLayout  layout      = new TextLayout( strXco, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        float       labelXco    = (float)(xco - bounds.getWidth() / 2 );
        float       labelYco    = (float)(yco + bounds.getHeight() + 5 );
        
        Color       save        = gtx.getColor();
        gtx.setColor( fontColor );
        layout.draw( gtx, labelXco, labelYco );
        gtx.setColor( save );
    }
}
