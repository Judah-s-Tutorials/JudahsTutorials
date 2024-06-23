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
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
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
        frame.setVisible( true );
    }
    
    public BufferedImage refresh()
    {
        executeProc( () -> {} );
        return image;
    }
    
    public BufferedImage drawBackground()
    {
        executeProc( () -> graphMgr.drawBackground() );
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
    
    public BufferedImage drawMinorTics()
    {
        executeProc( () -> graphMgr.drawMinorTics() );
        return image;
    }
    
    public BufferedImage drawGridLines()
    {
        executeProc( () -> graphMgr.drawGridLines() );
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
     * Gets the value of the gridUnit property
     * from the active Profile.
     * 
     * @return  
     *      the value of the gridUnit property
     *      from the active Profile
     */
    public float getGridUnit()
    {
        float   gridUnit    = getFloat( () -> profile.getGridUnit() );
        return gridUnit;
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
     * Gets the value of the color property
     * from the active Profile.
     * 
     * @return  
     *      the value of the color property
     *      from the active Profile
     */
    public Color getGridColor()
    {
        GraphPropertySet    win     = profile.getMainWindow();
        Color               color   = getColor( () -> win.getBGColor( ));
        return color;
    }
    
    /**
     * Sets the value of the gridColor property
     * in the active Profile
     * from the given RGB value
     * 
     * @param rgb   the given RGB value
     */
    public void setGridColor( int rgb )
    {
        Color   color   = new Color( rgb );
        profile.getMainWindow().setBGColor( color );
    }
    
    /**
     * Gets the RGB value of the color property
     * from the active Profile.
     * 
     * @return  
     *      the RGB value of the color property
     *      from the active Profile
     */
    public int getGridColorRGB()
    {
        Color   color   = getGridColor();
        int     rgb     = color.getRGB() & 0xFFFFFF;
        return rgb;
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
     * Gets the font size
     * from the active Profile.
     * 
     * @return  
     *      the font size from the active Profile
     */
    public float getGridFontSize()
    {
        GraphPropertySet    win     = profile.getMainWindow();
        float               size    = getFloat( () -> win.getFontSize() );
        return size;
    }
    
    /**
     * Sets the font style property
     * in the active Profile
     * from the given value
     * 
     * @param style   the given value
     */
    public void setGridFontStyle( String style )
    {
        GraphPropertySet    win = profile.getMainWindow();
        setProperty( a -> win.setFontStyle( (String)a ), style );
    }
    
    /**
     * Gets the font style
     * from the active Profile.
     * 
     * @return  
     *      the font style from the active Profile
     */
    public int getFontStyle()
    {
        GraphPropertySet    win     = profile.getMainWindow();
        int                 style   = getInt( () -> win.getFontStyle() );
        return style;
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
     * Gets the font name
     * from the active Profile.
     * 
     * @return  name from the active Profile
     */
    public String getFontName()
    {
        GraphPropertySet    win     = profile.getMainWindow();
        String              name    = getString( () -> win.getFontName() );
        return name;
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
     * Gets the font name
     * from the active Profile.
     * 
     * @return  name from the active Profile
     */
    public boolean getGridFontLabelsDraw()
    {
        GraphPropertySet    win     = profile.getMainWindow();
        boolean             draw    = getBoolean( () -> win.isFontDraw() );
        return draw;
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
     * Gets the draw property
     * of the given line property set.
     * 
     * @param propSet   the given line property set
     * 
     * @return  
     *      the value of the draw property
     *      for the given LinePropertySet
     */
    public boolean getLineDraw( String propSet )
    {
        LinePropertySet set     = profile.getLinePropertySet( propSet );
        boolean         draw    = getBoolean( () -> set.getDraw() );
        assertNotNull( set );
        return draw;
    }
    
    public void setLineStroke( String propSet, float stroke )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setStroke( (float)a ), stroke );
    }
    
    public float getLineStroke( String propSet )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        float   val     = getFloat( () -> set.getStroke() );
        return val;
    }
    
    public void setLineLength( String propSet, float length )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setLength( (float)a ), length );
    }
    
    public float getLineLength( String propSet )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        float   val     = getFloat( () -> set.getLength() );
        return val;
    }
    
    public void setLineSpacing( String propSet, float spacing )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setSpacing( (float)a ), spacing );
    }
    
    public float getLineSpacing( String propSet )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        float   val     = getFloat( () -> set.getSpacing() );
        return val;
    }
    
    public void setLineColor( String propSet, Color color )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        setProperty( a -> set.setColor( (Color)a ), color );
    }
    
    public Color getLineColor( String propSet )
    {
        LinePropertySet set = profile.getLinePropertySet( propSet );
        assertNotNull( set );
        Color   val     = getColor( () -> set.getColor() );
        return val;
    }
    
    public int getLineRGB( String propSet )
    {
        Color   color   = getLineColor( propSet );
        int     rgb     = color.getRGB() & 0xFFFFFF;
        return rgb;
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
    
    private boolean getBoolean( Supplier<Boolean> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof Boolean );
        return (boolean)obj;
    }
    
    private int getInt( Supplier<Integer> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof Integer );
        return (int)obj;
    }
    
    private float getFloat( Supplier<Float> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof Float );
        return (float)obj;
    }
    
    private Color getColor( Supplier<Color> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof Color );
        return (Color)obj;
    }
    
    private String getString( Supplier<String> supplier )
    {
        Supplier<Object>    objGetter   = () -> supplier.get();
        Object              obj         = getProperty( objGetter );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    private Object getProperty( Supplier<Object> supplier )
    {
        Object[]    obj     = new Object[1];
        GUIUtils.schedEDTAndWait(() -> obj[0] = supplier.get() );
        assertNotNull( obj[0] );
        return obj[0];
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
