package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * Test GUI for the GraphManager.
 * 
 * width/height = 601
 *     center pixel = 300
 * gridunit = 100
 * gridlines/unit = 1
 *      gridline 100, 200, 400, 500
 * gridlines/unit = 2
 *      gridline 50, 100, 150, 200, 250, 400, 450, 500, 550
 * 
 * @author Jack Straub
 */
public class GraphManagerTestGUI
{
    /** Convenient PropertyManager singleton declaration. */
    private static final PropertyManager    pMgr    = 
        PropertyManager.INSTANCE;
    
    /** Application frame title. */
    private static final String title       = "GraphManager Test GUI";
    
    /** Application frame. */
    private final JFrame        frame       = new JFrame( title );
    /** Profile used in testing. */
    private final Profile       profile     = new Profile();
    
    private final int           imageWidth  = 400;
    private final int           imageHeight = 500;
    private final int           imageType   = BufferedImage.TYPE_INT_RGB;
    private BufferedImage       image       =
        new BufferedImage( imageWidth, imageHeight, imageType );
    Rectangle2D                 imageRect   =
        new Rectangle2D.Double( 0, 0, imageWidth, imageHeight );
    /** GraphManager under test. */
    private GraphManager        graphMgr    = 
        new GraphManager( imageRect, profile );
    /** Place to draw sample graph. */
    private final JPanel        canvas      = new FBPanel();
    
    public GraphManagerTestGUI()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( canvas, BorderLayout.CENTER );
        
        frame.setContentPane( contentPane );
        frame.pack();
//        GUIUtils.center( frame );
        frame.setVisible( true );
    }
    
    public BufferedImage refresh()
    {
        executeProc( () -> {} );
        return image;
    }
    
    public BufferedImage drawAxes()
    {
        executeProc( () -> graphMgr.drawAxes() );
        return image;
    }
    
    public BufferedImage drawMajorTics()
    {
        executeProc( () -> graphMgr.drawMajorTics() );
        return image;
    }
    
    public void setGridUnit( float gridUnit )
    {
        profile.setGridUnit( gridUnit );
    }
    
    public void setGridColor( Color color )
    {
        profile.getMainWindow().setBGColor( color );
    }
    
    public void setGridFontSize( float size )
    {
        profile.getMainWindow().setFontSize( size );
    }
    
    public void setGridFontStyle( String style )
    {
        profile.getMainWindow().setFontStyle( style );
    }
    
    public void setGridFontName( String name )
    {
        profile.getMainWindow().setFontName( name );
    }
    
    public void setGridFontLabels( boolean draw )
    {
        profile.getMainWindow().setFontDraw( draw );
    }
    
    public void setLineDraw( String propSet, boolean draw )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        set.setDraw( draw );
    }
    
    public void setLineStroke( String propSet, float stroke )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        set.setStroke( stroke );
    }
    
    public void setLineLength( String propSet, float length )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        set.setLength( length );
    }
    
    public void setLineSpacing( String propSet, float spacing )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        set.setLength( spacing );
    }
    
    public void setLineColor( String propSet, Color color )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        set.setColor( color );
    }
    
    private void executeProc( Runnable runner )
    {
        GUIUtils.schedEDTAndWait( () -> {
            Graphics2D  gtx     = (Graphics2D)image.getGraphics();
            graphMgr.refresh( gtx, imageRect );
            runner.run();
            canvas.repaint();
        });
    }
    
    private void setProperty( Consumer<Object> consumer, Object prop )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( prop ) );
    }
    
    @SuppressWarnings("serial")
    private class FBPanel extends JPanel
    {
        public FBPanel()
        {
            Dimension   size    = new Dimension( imageWidth, imageHeight );
            setPreferredSize( size );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            graphics.setColor( Color.WHITE );
            graphics.fillRect( 0, 0, getWidth(), getHeight() );
            graphics.drawImage( image, 0, 0, this );
        }
    }
}
