package com.acmemail.judah.cartesian_plane.blog_support;

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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AxesPositioning extends JPanel
{
    private static final int            winWidth    = 360;
    private static final int            winHeight   = 400;
    private static final Color          winColor    = Color.WHITE;
    private static final Color          rectColor   = 
        new Color( 0xefefef );
    private static final Color          axisColor   = 
        new Color( 0xFF474C );
    private static final Color          textColor   = Color.BLACK;
    private static final Stroke         axisStroke  = 
        new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
    private static final Stroke         baseStroke  = new BasicStroke( 1 );
    
    private static final double         dimLinePC   = .33;
    
    private static final double         ulcXco      = 70;
    private static final double         ulcYco      = 50;
    private static final Point2D        ulc         = 
        new Point2D.Double( ulcXco, ulcYco );
    private static final int            rectWidth   = 250;
    private static final int            rectHeight  = 325;
    private static final Rectangle2D    rect        = 
        new Rectangle2D.Double( ulcXco, ulcYco, rectWidth, rectHeight );
    private static final double         midXco      = rect.getCenterX();
    private static final double         midYco      = rect.getCenterY();
    private static final double         leftXco     = ulcXco;
    private static final double         rightXco    = leftXco + rectWidth;
    private static final double         topYco      = ulcYco;
    private static final double         bottomYco   = topYco + rectHeight;
    
    private final Line2D    line    = new Line2D.Double();
    private Graphics2D          gtx;
    private int                 width;
    private int                 height;
    private Font                font;
    private FontRenderContext   frc;
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame( "Axes Positioning Figure" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            frame.setLocation( 100, 100 );
            frame.setContentPane( new AxesPositioning() );
            frame.pack();
            frame.setVisible( true );
        });
    }
    
    public AxesPositioning()
    {
        Dimension   dim     = new Dimension( winWidth, winHeight );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        gtx = (Graphics2D)graphics.create();
        width = getWidth();
        height = getHeight();
        font = gtx.getFont();
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( winColor );
        gtx.fillRect( 0, 0, width, height );
        gtx.setColor( rectColor );
        gtx.fill( rect );
        
        gtx.setColor( axisColor );
        gtx.setStroke( axisStroke );
        line.setLine( midXco, topYco, midXco, bottomYco);
        gtx.draw( line );
        line.setLine( leftXco, midYco, rightXco, midYco);
        gtx.draw( line );
        
        gtx.setColor( textColor );
        gtx.setStroke( baseStroke );
        drawLabel( leftXco, topYco, Position.LEFT_TOP );
        drawLabel( midXco, topYco, Position.MID_TOP );
        drawLabel( rightXco, topYco, Position.RIGHT_TOP );
        drawLabel( leftXco, midYco, Position.LEFT_MID );
        drawLabel( midXco, midYco, Position.MID_MID );
        drawLabel( rightXco, midYco, Position.RIGHT_MID );
        drawLabel( leftXco, bottomYco, Position.LEFT_BOTTOM );
        drawLabel( midXco, bottomYco, Position.MID_BOTTOM );
        drawLabel( rightXco, bottomYco, Position.RIGHT_BOTTOM );
        drawWidth();
        drawHeight();
        
        gtx.dispose();
    }
    
    private void 
    drawLabel( double xco, double yco, Position pos )
    {
        String      label       = String.format( "(%.0f,%.0f)", xco, yco );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      xOffset     = bounds.getWidth() / 2;
        double      yOffset     = bounds.getHeight();
        double      textXco     = xco;
        double      textYco     = yco;
        
        switch ( pos )
        {
        case LEFT_TOP:
        case MID_TOP:
        case RIGHT_TOP:
            textXco -= xOffset;
            textYco += yOffset;
            break;
        case LEFT_MID:
        case RIGHT_MID:
            textXco -= xOffset;
            textYco -= 2;
            break;
        case MID_MID:
            textXco += 2;
            textYco += yOffset;
            break;
        case LEFT_BOTTOM:
        case MID_BOTTOM:
        case RIGHT_BOTTOM:
            textXco -= xOffset;
            textYco -= 2;
            break;
        default:
            System.out.println( "MALFUNCTION" );
        }
        layout.draw( gtx, (float)textXco, (float)textYco );
    }
    
    private void drawWidth()
    {
        double      lineLen     = dimLinePC * rectWidth;
        String      label       = String.format( "%d", rectWidth );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = midXco - bounds.getWidth() / 2;
        double      textYco     = topYco - bounds.getHeight() / 2;
        double      xco2        = textXco - 10;
        double      yco2        = topYco - bounds.getHeight();
        double      xco1        = xco2 - lineLen;
        double      yco1        = yco2;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
        
        layout.draw( gtx, (float)textXco, (float)textYco );
        
        xco1 = textXco + bounds.getWidth() + 10;
        xco2 = xco1 + lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
    }
    
    private void drawHeight()
    {
        double      lineLen     = dimLinePC * rectHeight;
        String      label       = String.format( "%d", rectHeight );
        TextLayout  layout      = new TextLayout( label, font, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = leftXco - 2.5 * bounds.getWidth();
        double      textYco     = midYco + bounds.getHeight() / 2;
        double      xco2        = textXco + bounds.getWidth() / 2;
        double      yco2        = textYco - 1.5 * bounds.getHeight();
        double      xco1        = xco2;
        double      yco1        = yco2 - lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
        
        layout.draw( gtx, (float)textXco, (float)textYco );
         
        yco1 = textYco + bounds.getHeight() / 2;
        yco2 = yco1 + lineLen;
        line.setLine( xco1, yco1, xco2, yco2 );
        gtx.draw( line );
    }
    
    private enum Position
    {
        LEFT_TOP,
        MID_TOP,
        RIGHT_TOP,
        LEFT_MID,
        MID_MID,
        RIGHT_MID,
        LEFT_BOTTOM,
        MID_BOTTOM,
        RIGHT_BOTTOM,
    }
}
