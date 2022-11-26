package com.acmemail.judah.blog_support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IntegerOvals extends JPanel
{
    private final Color     bgColor     = new Color( .9f, .9f, .9f );
    
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    private FontMetrics     fontMetrics;
    
    public IntegerOvals( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        fontMetrics = gtx.getFontMetrics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        // draw a line from lower left portion of window 
        // to upper right portion.
        gtx.setColor( Color.black );
        int     xco     = (int)(currWidth * .1);
        int     yco     = 100;
        int     width   = (int)(currWidth * .6);
        int     height  = (int)(currHeight * .9) - yco;
        drawOval( xco, yco, width, height );

        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawCoordinates( int xco, int yco, boolean above )
    {
        String      strCoords   = String.format( "(x=%d,y=%d)", xco, yco );
        Rectangle2D strRect     = 
            fontMetrics.getStringBounds( strCoords, gtx );
        float       strXco      = (float)(xco - strRect.getWidth() / 2);
        float       strYco      = 
            above? yco : (float)(yco + strRect.getHeight());
        gtx.drawString(strCoords, strXco, strYco);
    }
    
    private void drawOval( int xco, int yco, int width, int height )
    {
        int         oldSize     = gtx.getFont().getSize();
        Font        newFont     = new Font( "Monospaced", Font.PLAIN, oldSize );
        gtx.setFont( newFont );
        
        gtx.setColor( Color.RED );
        gtx.fillOval( xco, yco, width, height );
        gtx.setColor( Color.BLACK );
        gtx.drawOval(xco, yco, width, height);
        
        drawCoordinates( xco, yco, true );
        
        String      fmtWidth    = String.format( "width = %d", width );
        Rectangle2D rect        = 
            fontMetrics.getStringBounds( fmtWidth, gtx );
        float       strWidth    = (float)rect.getWidth();
        float       strHeight   = (float)rect.getHeight();
        float       widthXco    = (xco + width / 2 - strWidth / 2);
        float       widthYco    = (float)(yco - 5);
        gtx.drawString( fmtWidth, widthXco, widthYco );
        
        String      fmtHeight   = String.format( "height=%d", height);
        float       heightXco   = xco + width + 5;
        float       heightYco   = yco + height / 2.0f;
        gtx.drawString( fmtHeight, heightXco, heightYco );  
        
        StringBuilder   bldr    = new StringBuilder();
        String          line1   = "gtx.setColor( Color.RED );";
        bldr.append( "gtx.fillOval( ")
            .append( xco ).append(", " )
            .append( yco ).append(", " )
            .append( width ).append( ", " )
            .append( height ).append(" )");
        String          line2   = bldr.toString();
        String          line3   = "gtx.setColor( Color.BLACK );";
        bldr.setLength( 0 );
        bldr.append( "gtx.drawOval( ")
            .append( xco ).append(", " )
            .append( yco ).append(", " )
            .append( width ).append( ", " )
            .append( height ).append(" )");
        String          line4   = bldr.toString();
        float           lineXco = xco;
        float           lineYco = 1.5f * strHeight;
        gtx.drawString( line1, lineXco, lineYco );
        gtx.drawString( line2, lineXco, lineYco + strHeight );
        gtx.drawString( line3, lineXco, lineYco + 2 * strHeight );
        gtx.drawString( line4, lineXco, lineYco + 3 * strHeight );
        
        Stroke          dashes  =
            new BasicStroke(
                1,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND,
                1.0f,
                new float[] { 6 },
                5f
            );
        gtx.setColor( Color.BLACK );
        gtx.setStroke( dashes );
        gtx.drawRect(xco, yco, width, height);
    }
}
