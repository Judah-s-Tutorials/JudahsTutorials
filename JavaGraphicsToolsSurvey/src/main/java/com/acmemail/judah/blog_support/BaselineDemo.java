package com.acmemail.judah.blog_support;

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

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BaselineDemo extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    /** The font to use to draw the sample string. */
    private Font            strFont;
    /** Font to draw labels on the baseline graphic and coordinates. */
    private Font            labelFont;
    
    public static void main( String[] args )
    {
        BaselineDemo    demo    = new BaselineDemo( 400, 500 );
        Root            root    = new Root( demo );
        root.start();
    }
    
    public BaselineDemo( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
        strFont = new Font( "monospaced", Font.BOLD, 20 );
        labelFont = new Font( "dialog", Font.PLAIN, 12 );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        drawString( "Brique peter junquet", 50, 75 );
        drawString( "11.21", 50, 150 );

        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawString( String str, int xco, int yco )
    {
        gtx.setColor( Color.BLACK );
        gtx.setFont( strFont );
        gtx.setStroke( new BasicStroke( 1 ) );
        
        // Draw the string at (xco, yco)
        FontRenderContext   strFRC = gtx.getFontRenderContext();
        TextLayout  strLayout   = new TextLayout( str, strFont, strFRC );
        Rectangle2D strRect     = strLayout.getBounds();
        strLayout.draw( gtx, xco, yco );
        float       strWidth    = (float)strRect.getWidth();
        
        // Draw the baseline, from xco to a little past
        // the end of the string
        float       baseXco1    = xco;
        float       baseXco2    = baseXco1 + strWidth + 40;
        Line2D      baseline    = 
            new Line2D.Float( baseXco1, yco, baseXco2, yco );
        gtx.draw( baseline );
        
        // Draw the bounding box around the string
        float       rectXco     = xco + (float)strRect.getX();
        float       rectYco     = yco + (float)strRect.getY();
        float       rectWidth   = (float)strRect.getWidth();
        float       rectHeight  = (float)strRect.getHeight();
        Rectangle2D rect        = 
            new Rectangle2D.Float( rectXco, rectYco, rectWidth, rectHeight );        
        Stroke      dashes  =
            new BasicStroke(
                1,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND,
                1.0f,
                new float[] { 6 },
                5f
            );
        gtx.setColor( Color.RED );
        gtx.setStroke( dashes );
        gtx.draw( rect );
        
        // draw the baseline label
        gtx.setFont( labelFont );
        FontRenderContext   labelFRC = gtx.getFontRenderContext();
        gtx.setColor( Color.BLACK );
        String      baseLabelStr    = "baseline";
        TextLayout  baseLabelLayout = 
            new TextLayout( baseLabelStr, labelFont, labelFRC );
        Rectangle2D baseLabelRect   = baseLabelLayout.getBounds();
        float       baseLabelXco    = baseXco2 + 5;
        float       baseLabelYco    = 
            yco + (float)baseLabelRect.getHeight() / 2;
        baseLabelLayout.draw( gtx, baseLabelXco, baseLabelYco );
        
        // draw the coordinates, below and to the left
        // of the start of the string
        String      xyStr       = String.format( "(x=%d,y=%d)", xco, yco );
        TextLayout  xyLayout    = 
            new TextLayout( xyStr, labelFont, labelFRC );
        Rectangle2D xyRect      = xyLayout.getBounds();
        float       xyXco       = xco - (float)xyRect.getWidth() / 2;
        float       xyYco       = yco + (float)xyRect.getHeight() + 3;
        xyLayout.draw( gtx, xyXco, xyYco );
        
        // draw the label to the right of the bounding box
        String      boxLabel    = "bounding rectangle";
        TextLayout  boxLayout   = 
            new TextLayout( boxLabel, labelFont, labelFRC );
        float       boxLabelXco = rectXco + rectWidth - strWidth / 2;
        float       boxLabelYco = rectYco - 5;
        boxLayout.draw( gtx, boxLabelXco, boxLabelYco );
    }
}
