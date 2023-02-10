package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class DiamondDemo
{

    public static void main(String[] args)
    {
        Canvas  canvas  = new Canvas();
        Root    root    = new Root( canvas );
        root.start();
    }

    private static class Canvas extends JPanel
    {
        private static final long serialVersionUID = 5156973556079056823L;
        
        private final Color     bgColor     = new Color( .9f, .9f, .9f );
        private int             currWidth;
        private int             currHeight;
        private Graphics2D      gtx;
        
        private final Stroke    stroke      =
            new BasicStroke(
                1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                1.0f,
                new float[] { 5, 6 },
                1.0f
            );
        
        public Canvas()
        {
            Dimension   size    = new Dimension( 300, 500 );
            setPreferredSize( size );
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
            
            float   rectWidth   = currWidth / 3f * 2f;
            float   rectHeight  = currHeight / 3f * 2f;
            float   leftXco     = currWidth / 3f / 2f;
            float   rightXco    = leftXco + rectWidth;
            float   centerXco   = leftXco + rectWidth / 2;
            float   topYco      = currHeight / 3f / 2f;
            float   bottomYco   = topYco + rectHeight;
            float   centerYco   = topYco + rectHeight / 2f;
            Path2D  path        = new Path2D.Float();
            path.moveTo( leftXco, centerYco );
            path.lineTo( centerXco, topYco );
            path.lineTo( rightXco, centerYco );
            path.lineTo( centerXco, bottomYco );
            path.closePath();
            
            gtx.setColor( Color.RED );
            gtx.fill( path );
            
            float   leftMargin      = leftXco / 2;
            float   rightMargin     = 3 * leftMargin + rectWidth;
            float   topMargin       = topYco / 2;
            float   bottomMargin    = 3 * topMargin + rectHeight;
            Rectangle2D rect    = 
                new Rectangle2D.Float( 
                    leftXco, 
                    topYco, 
                    rectWidth, 
                    rectHeight
                );
            
            Line2D  vertLine   = 
                new Line2D.Float( 
                    centerXco, 
                    topMargin, 
                    centerXco, 
                    bottomMargin 
                );
            Line2D  horizLine  = 
                new Line2D.Float( 
                    leftMargin, 
                    centerYco, 
                    rightMargin, 
                    centerYco 
                );
            gtx.setColor( Color.BLUE );
            gtx.setStroke( stroke );
            gtx.draw( rect );
            gtx.draw( vertLine );
            gtx.draw( horizLine );
            
            // begin boilerplate
            gtx.dispose();
            // end boilerplate
        }
    }
}
