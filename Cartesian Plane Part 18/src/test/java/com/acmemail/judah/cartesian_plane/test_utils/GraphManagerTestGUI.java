package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
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
    /** Application frame title. */
    private static final String title       = "GraphManager Test GUI";
    
    /** Application frame. */
    private final JFrame        frame       = new JFrame( title );
    /** Profile used in testing. */
    private final Profile       profile;
    
    private final int           imageWidth  = 450;
    private final int           imageHeight = 500;
    private final int           imageType   = BufferedImage.TYPE_INT_RGB;
    private BufferedImage       image       =
        new BufferedImage( imageWidth, imageHeight, imageType );
    Rectangle2D                 imageRect   =
        new Rectangle2D.Double( 0, 0, imageWidth, imageHeight );
    /** GraphManager under test. */
    private final GraphManager  graphMgr;
    /** Place to draw sample graph. */
    private final JPanel        canvas      = new FBPanel();
    
    public GraphManagerTestGUI( Profile profile )
    {
        this.profile = profile;
        graphMgr =  new GraphManager( imageRect, profile );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( canvas, BorderLayout.CENTER );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Executes the drawBackground method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawBackground()
    {
        executeProc( () -> graphMgr.drawBackground() );
        return image;
    }
    
    /**
     * Executes the drawAxes method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawAxes()
    {
        executeProc( () -> graphMgr.drawAxes() );
        return image;
    }
    
    /**
     * Executes the drawMajorTics method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawMajorTics()
    {
        executeProc( () -> graphMgr.drawMajorTics() );
        return image;
    }
    
    /**
     * Executes the drawMinorTics method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawMinorTics()
    {
        executeProc( () -> graphMgr.drawMinorTics() );
        return image;
    }
    
    /**
     * Executes the drawGridLines method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawGridLines()
    {
        executeProc( () -> graphMgr.drawGridLines() );
        return image;
    }
    
    /**
     * Executes the drawHorizontalLabel method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawHorizontalLabels()
    {
        executeProc( () -> graphMgr.drawHorizontalLabels() );
        return image;
    }
    
    /**
     * Executes the drawVerticalLabel method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawVerticalLabels()
    {
        executeProc( () -> graphMgr.drawVerticalLabels() );
        return image;
    }
    
    /**
     * Executes the drawText method
     * in the GraphManager under test.
     * 
     * @return  the bitmap that the grid manager draws to
     */
    public BufferedImage drawText()
    {
        executeProc( () -> graphMgr.drawText() );
        return image;
    }
    
    /**
     * Sets the value of the gridUnit property
     * in the active Profile.
     * 
     * @param gridUnit  the value of the property
     */
    public void setGridUnit( float gridUnit )
    {
        setProperty( a -> profile.setGridUnit( (Float)a ), gridUnit );
    }
    
    /**
     * Sets the value of the gridColor property
     * in the active Profile.
     * 
     * @param color  the value of the property
     */
    public void setGridColor( Color color )
    {
        GraphPropertySet    win = profile.getMainWindow();
        setProperty( a -> win.setBGColor( (Color)a ), color );
    }
    
    /**
     * Sets the font size property
     * in the active Profile
     * from the given value
     * 
     * @param size   the given value
     */
    public void setGridFontSize( float size )
    {
        GraphPropertySet    win = profile.getMainWindow();
        setProperty( a -> win.setFontSize( (float)a ), size );
    }
    
    /**
     * Sets the font name property
     * in the active Profile
     * from the given value
     * 
     * @param name   the given value
     */
    public void setGridFontName( String name )
    {
        GraphPropertySet    win = profile.getMainWindow();
        setProperty( a -> win.setFontName( (String)a ), name );
    }
    
    /**
     * Sets the text color property
     * in the active Profile
     * from the given RGB value
     * 
     * @param name   the given value
     */
    public void setGridFontRGB( int rgb )
    {
        GraphPropertySet    win     = profile.getMainWindow();
        Color               fgColor = new Color( rgb );
        setProperty( a -> win.setFGColor( (Color)a ), fgColor );
    }
    
    /**
     * Sets the draw-labels property
     * in the active Profile
     * from the given value
     * 
     * @param draw   the given value
     */
    public void setGridFontLabels( boolean draw )
    {
        GraphPropertySet    win = profile.getMainWindow();
        setProperty( a -> win.setFontDraw( (Boolean)a ), draw );
        profile.getMainWindow().setFontDraw( draw );
    }
    
    /**
     * Gets the has-length property
     * of the given line property set.
     * 
     * @param propSet   the given line property set
     * 
     * @return  
     *      the value of the has-length property
     *      for the given LinePropertySet
     */
    public boolean getLineHasLength( String propSet )
    {
        LinePropertySet set     = profile.getLinePropertySet( propSet );
        boolean         draw    = getBoolean( () -> set.hasLength() );
        return draw;
    }
    
    /**
     * Sets the draw property
     * of the given property set
     * to the given value.
     * 
     * @param propSet   the given property set
     * @param draw      the given value
     */
    public void setLineDraw( String propSet, boolean draw )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setDraw( (Boolean)a ), draw );
    }
    
    /**
     * Sets the stroke property
     * of the given property set
     * to the given value.
     * 
     * @param propSet   the given property set
     * @param stroke      the given value
     */
    public void setLineStroke( String propSet, float stroke )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setStroke( (float)a ), stroke );
    }
    
    /**
     * Sets the length property
     * of the given property set
     * to the given value.
     * 
     * @param propSet   the given property set
     * @param length      the given value
     */
    public void setLineLength( String propSet, float length )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setLength( (float)a ), length );
    }
    
    /**
     * Sets the spacing property
     * of the given property set
     * to the given value.
     * 
     * @param propSet   the given property set
     * @param value      the given value
     */
    public void setLineSpacing( String propSet, float spacing )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setSpacing( (float)a ), spacing );
    }
    
    /**
     * Sets the color property
     * of the given property set
     * to the given value.
     * 
     * @param propSet   the given property set
     * @param color     the given value
     */
    public void setLineColor( String propSet, Color color )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setColor( (Color)a ), color );
    }
    
    /**
     * Invokes updateProfile in the graph manager under test.
     */
    public void invokeResetProfile()
    {
        GUIUtils.schedEDTAndWait( graphMgr::resetProfile );
    }
    
    /**
     * Executes the given procedure
     * in the context of the EDT.
     * 
     * @param runner    the given procedure
     */
    private void executeProc( Runnable runner )
    {
        GUIUtils.schedEDTAndWait( () -> {
            Graphics2D  gtx     = (Graphics2D)image.getGraphics();
            graphMgr.refresh( gtx, imageRect );
            runner.run();
            canvas.repaint();
        });
    }
    
    /**
     * Sets the given property value
     * via the given consumer.
     * Executed in the context of the EDT.
     * 
     * @param consumer  the given consumer
     * @param prop      the given property value
     */
    private void setProperty( Consumer<Object> consumer, Object prop )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( prop ) );
    }
    
    /**
     * Executes the given Boolean supplier
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the value returned by the given supplier
     */
    private boolean getBoolean( Supplier<Boolean> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof Boolean );
        return (boolean)obj;
    }
    
    /**
     * Executes the given Object supplier
     * in the context of the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the value returned by the given supplier
     */
    private Object getProperty( Supplier<Object> supplier )
    {
        Object[]    obj     = new Object[1];
        GUIUtils.schedEDTAndWait(() -> obj[0] = supplier.get() );
        assertNotNull( obj[0] );
        return obj[0];
    }
    
    /**
     * Encapsulates the feedback panel
     * utilized by this test GUI.
     * All it does is display
     * the buffered image being drawn to
     * by the test GUI.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class FBPanel extends JPanel
    {
        /**
         * Constructor.
         * Sets the size of this component.
         */
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
