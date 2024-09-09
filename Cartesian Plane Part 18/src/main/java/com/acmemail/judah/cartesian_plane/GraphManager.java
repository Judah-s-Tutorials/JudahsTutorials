package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;

/**
 * An object of this class
 * is used to control the detailed drawing
 * of the background of a graph,
 * color, axes, grid lines, tics,
 * and the labels drawn on the major tics.
 * The values of the various components
 * are encapsulated in a Profile
 * provided by the user.
 * 
 * @author Jack Straub
 * 
 * @see LineGenerator
 */
public class GraphManager
{
    /** Type of line-cap when instantiating Stroke. */
    private static final int    lineCap                 =
        BasicStroke.CAP_BUTT;
    /** Type of line-join when instantiating Stroke. */
    private static final int    lineJoin                =
        BasicStroke.JOIN_BEVEL;
    /** Name of the LinePropertiesSetAxes class. */
    private static final String axisPropertiesName      =
        LinePropertySetAxes.class.getSimpleName();
    /** Name of the LinePropertiesSetTicMajor class. */
    private static final String ticMajorPropertiesName  =
        LinePropertySetTicMajor.class.getSimpleName();
    /** Name of the LinePropertiesSetTicMinor class. */
    private static final String ticMinorPropertiesName  =
        LinePropertySetTicMinor.class.getSimpleName();
    /** Name of the LinePropertySetGridLines class. */
    private static final String gridLinesPropertiesName =
        LinePropertySetGridLines.class.getSimpleName();

    /** 
     * Set of properties that control the appearance of the 
     * graph window. Set in the constructor. This should be
     * a reference to the Profile used by the encapsulating editor,
     * so that when the editor makes changes to its profile the
     * changes will be reflected here.
     */
    private final Profile                   profile;
    /** 
     * Main window properties. Obtained from the profile, and 
     * referenced by an instance variable for convenience.
     */
    private final GraphPropertySet          mainWindow;
    /** 
     * Axis properties. Obtained from the profile, and 
     * referenced by an instance variable for convenience.
     */
    private final LinePropertySetAxes       axis;
    /** 
     * Major tic properties. Obtained from the profile, and 
     * referenced by an instance variable for convenience.
     */
    private final LinePropertySetTicMajor   ticMajor;
    /** 
     * Minor tic properties. Obtained from the profile, and 
     * referenced by an instance variable for convenience.
     */
    private final LinePropertySetTicMinor   ticMinor;
    /**
     * Grid line properties. Obtained from the profile, and 
     * referenced by an instance variable for convenience.
     */
    private final LinePropertySetGridLines  gridLine;
    
    
    /** 
     * The rectangle that determines the are of the client window
     * within which to draw the grid. Updated every time reset
     * is invoked.
     */
    private Rectangle2D         rect;
    /** The grid unit for the sample graph. */
    private float               gridUnit;
    /**  Graphics context; updated every time refresh() is invoked. */
    private Graphics2D          gtx;
    
    /**
     * Constructor.
     * Initializes this object with the given Profile
     * and an unconfigured Rectangle.
     * 
     * @param profile   the given Profile
     * 
     * @see GraphManager#GraphManager(Rectangle2D, Profile)
     */
    public GraphManager( Profile profile )
    {
        this( new Rectangle(), profile );
    }
    
    /**
     * Constructor.
     * Initializes this object with the given Profile
     * and a rectangle derived from the given Component.
     * 
     * @param comp      the given Component
     * @param profile   the given Profile
     * 
     * @see GraphManager#GraphManager(Rectangle2D, Profile)
     */
    public GraphManager( JComponent comp, Profile profile )
    {
        this( comp.getVisibleRect(), profile );
    }
    
    /**
     * Constructor.
     * Fully initializes this object.
     * 
     * @param comp      the component where the sample graph is drawn
     * @param profile   
     *      the Profile containing the drawing configuration properties
     */
    public GraphManager( Rectangle2D rect, Profile profile )
    {
        this.rect = rect;
        this.profile = profile;
        mainWindow = profile.getMainWindow();
        axis = 
            (LinePropertySetAxes)profile
            .getLinePropertySet( axisPropertiesName );
        ticMajor = 
            (LinePropertySetTicMajor)profile
            .getLinePropertySet( ticMajorPropertiesName );
        ticMinor = 
            (LinePropertySetTicMinor)profile
            .getLinePropertySet( ticMinorPropertiesName );
        gridLine = 
            (LinePropertySetGridLines)profile
            .getLinePropertySet( gridLinesPropertiesName );
    }

    /**
     * Redraw the sample graph.
     * 
     * @param graphics  graphics context for redrawing the sample graph
     */
    public void refresh( Graphics2D graphics, Rectangle2D rect )
    {
        this.rect = rect;
        refresh( graphics );
    }
    
    /**
     * Update the graphics context used 
     * to draw in the client window.
     * 
     * @param graphics  the updated graphics context
     */
    public void refresh( Graphics2D graphics )
    {
        if ( gtx != null )
            gtx.dispose();
        gtx = (Graphics2D)graphics.create();
        gridUnit = profile.getGridUnit();
        
        Color   color       = profile.getMainWindow().getBGColor();
        gtx.setColor( color );
        gtx.fill( rect );
     }
    
    /**
     * Forces the encapsulated Profile
     * to be reinitialized
     * using the current property values
     * from the PropertyManager.
     */
    public void resetProfile()
    {
        profile.reset();
    }
    
    /**
     * Draw all the components of the sample graph.
     */
    public void drawAll()
    {
        drawBackground();
        drawGridLines();
        drawAxes();
        drawMinorTics();
        drawMajorTics();
        drawText();
    }
    
    /**
     * Draw the background of the sample graph.
     */
    public void drawBackground()
    {
        gtx.setColor( mainWindow.getBGColor() );
        gtx.fill( rect );
    }
    
    /**
     * Draw the grid lines of the sample graph.
     */
    public void drawGridLines()
    {
        drawLines( gridLine );
    }
    
    /**
     * Draw the labels on the major tic marks of the sample graph.
     */
    public void drawText()
    {
        drawHorizontalLabels();
        drawVerticalLabels();
    }
    
    /**
     * Draw the labels on the major tic marks on the x-axis.
     * The origin is not labeled.
     */
    public void drawVerticalLabels()
    {
        if ( mainWindow.isFontDraw() )
        {
            // padding between tic mark and label
            final int   labelPadding    = 3;
            
            float   ticMajorMPU = ticMajor.getSpacing();
            float   ticMajorLen = ticMajor.getLength();
            String  fontName    = mainWindow.getFontName();
            int     fontSize    = (int)mainWindow.getFontSize();
            int     fontStyle   = mainWindow.getFontStyle();
            Color   fontColor   = mainWindow.getFGColor();
            Font    labelFont   = 
                new Font( fontName, fontStyle, fontSize );
            
            gtx.setColor( fontColor );
            gtx.setFont( labelFont );
            FontRenderContext   frc     = gtx.getFontRenderContext();
            LineGenerator       lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMajorMPU,
                    ticMajorLen,
                    LineGenerator.VERTICAL
                );
            float       labelIncr   = 1 / ticMajorMPU;
            float       originXco   = (float)rect.getCenterX();
            float       spacing     = gridUnit / ticMajorMPU;
            for ( Line2D line : lineGen )
            {
                float       xco1    = (float)line.getX2();
                int         dist    = (int)((xco1 - originXco) / spacing);
                float       next    = dist * labelIncr;
                String      label   = String.format( "%3.2f", next );
    
                TextLayout  layout  = 
                    new TextLayout( label, labelFont, frc );
                Rectangle2D bounds  = layout.getBounds();
                float       yOffset = 
                    (float)(bounds.getHeight() + labelPadding);
                float       xOffset = (float)(bounds.getWidth() / 2);
                float       xco     = xco1 - xOffset;
                float       yco     = (float)line.getY2() + yOffset;
                layout.draw( gtx, xco, yco );
            }
        }
    }

    /**
     * Draw the labels on the horizontal tic marks
     * (top to bottom of y-axis).
     * The origin is not labeled.
     */
    public void drawHorizontalLabels()
    {
        if ( mainWindow.isFontDraw() )
        {
            // padding between tic mark and label
            final int   labelPadding    = 5;
            
            float   ticMajorMPU = ticMajor.getSpacing();
            float   ticMajorLen = ticMajor.getLength();
            String  fontName    = mainWindow.getFontName();
            int     fontSize    = (int)mainWindow.getFontSize();
            int     fontStyle   = mainWindow.getFontStyle();
            Color   fontColor   = mainWindow.getFGColor();
            Font    labelFont   = 
                new Font( fontName, fontStyle, fontSize );
    
            gtx.setFont( labelFont );
            gtx.setColor( fontColor );
            FontRenderContext   frc = gtx.getFontRenderContext();
            LineGenerator   lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMajorMPU,
                    ticMajorLen,
                    LineGenerator.HORIZONTAL
                );
            float       labelIncr   = 1 / ticMajorMPU;
            float       originYco   = (float)rect.getCenterY();
            float       spacing     = gridUnit / ticMajorMPU;
    
            for ( Line2D line : lineGen )
            {
                float       xco2    = (float)line.getX2();
                float       yco1    = (float)line.getY1();
                int         dist    = (int)((originYco - yco1) / spacing);
                float       next    = dist * labelIncr;
                String      label   = String.format( "%3.2f", next );
                TextLayout  layout  = 
                    new TextLayout( label, labelFont, frc );
                Rectangle2D bounds  = layout.getBounds();
                float       yOffset = (float)(bounds.getHeight() / 2);
                float       xco     = xco2 + labelPadding;
                float       yco     = yco1 + yOffset;
                layout.draw( gtx, xco, yco );
            }
        }
    }

    /**
     * Draw the labels on the axes on the x-axis.
     */
    public void drawAxes()
    {
        gtx.setColor( axis.getColor() );
        gtx.setStroke( getStroke( axis.getStroke() ) );
        
        Iterator<Line2D>    iter    = LineGenerator.axesIterator( rect );
        gtx.draw( iter.next() );
        gtx.draw( iter.next() );
    }

    /**
     * Draw the minor tic marks on the sample graph.
     */
    public void drawMinorTics()
    {
        drawLines( ticMinor );
    }

    /**
     * Draw the major tic marks on the sample graph.
     */
    public void drawMajorTics()
    {
        drawLines( ticMajor );
    }

    /**
     * Draw the lines for the given line property set on the graph.
     *
     * @param   propSet the given line property set
     */
    private void drawLines( LinePropertySet propSet )
    {
        if ( propSet.getDraw() )
        {
            float           lpu     = propSet.getSpacing();
            float           len     = propSet.getLength();
            Stroke          stroke  = getStroke( propSet.getStroke() );
            Color           color   = propSet.getColor();
            LineGenerator   lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    lpu,
                    len,
                    LineGenerator.BOTH
                );
            gtx.setStroke( stroke );
            gtx.setColor( color );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .forEach( gtx::draw );
        }
    }
    
    /**
     * Instantiates a Stroke from the given width.
     * This method ensures that a Stroke
     * is always created consistently, 
     * with the given width
     * and consistent line-cap and line-join.
     * 
     * @param width the given width
     * 
     * @return  the instantiated Stroke
     */
    private Stroke getStroke( float width )
    {
        Stroke  stroke  = new BasicStroke( width, lineCap, lineJoin );
        return stroke;
    }
}