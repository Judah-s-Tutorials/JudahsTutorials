package com.gmail.johnstraub1954.penrose.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TwoLineTestFeedback extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final Stroke stroke  = new BasicStroke( 3 );
    private static Line2D   line1   = null;
    private static Line2D   line2   = null;
    public TwoLineTestFeedback()
    {
        Dimension   size    = new Dimension( 300, 300 );
        setPreferredSize( size );
        JFrame      frame   = new JFrame( "Two Line Feedback" );
        frame.setContentPane( this );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }

    public void show( Line2D line1, Line2D line2 )
    {
        TwoLineTestFeedback.line1 = line1;
        TwoLineTestFeedback.line2 = line2;
        try
        {
            SwingUtilities.invokeAndWait( () -> {
                repaint();
                JOptionPane.showMessageDialog( null, "Done" );
            });
        }
        catch( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        Graphics2D  gtx = (Graphics2D)graphics.create();
        
        gtx.setColor( Color.LIGHT_GRAY );
        gtx.fillRect( 0,  0,  getWidth(), getHeight() );
        
        gtx.setStroke( stroke );
        if ( line1 != null )
        {
            gtx.setColor( Color.BLACK );
            gtx.draw( line1 );
        }
        if ( line2 != null )
        {
            gtx.setColor( Color.RED );
            gtx.draw( line2 );
        }
        
        gtx.dispose();
    }
}
