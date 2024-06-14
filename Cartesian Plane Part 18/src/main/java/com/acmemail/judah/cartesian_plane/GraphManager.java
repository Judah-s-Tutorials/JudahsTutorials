package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.Profile;

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
 * @see NewLineGen
 */
public class GraphManager
{
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
        
    /** The component in which the sample graph is to be drawn. */
    private final JComponent        comp;
    /** The grid unit for the sample graph. */
    private float                   gridUnit;

    /** 
     * For transient use when temporarily switching colors
     * in the graphics context.
     */
    private Color               saveColor;

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
    
    /**  Graphics context; updated every time refresh() is invoked. */
    private Graphics2D          gtx         = null;

    /**
     * Constructor.
     * Fully initializes this object.
     * 
     * @param comp      the component where the sample graph is drawn
     * @param profile   
     *      the Profile containing the drawing configuration properties
     */
    public GraphManager( JComponent comp, Profile profile )
    {
        this.comp = comp;
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
    public void refresh( Graphics2D graphics )
    {
        if ( gtx != null )
            gtx.dispose();
        gtx = (Graphics2D)graphics.create();
        gridUnit = profile.getGridUnit();
        
        int     width       = comp.getWidth();
        int     height      = comp.getHeight();
        Color   color       = profile.getMainWindow().getBGColor();
        gtx.setColor( color );
        gtx.fillRect( 0, 0, width, height );
     }
    
    /**
     * Draw the background of the sample graph.
     */
    public void drawBackground()
    {
        setColor( mainWindow.getBGColor() );
        gtx.fillRect( 0, 0, comp.getWidth(), comp.getHeight() );
        setColor( null );
    }
    
    /**
     * Draw the grid lines of the sample graph.
     */
    public void drawGridLines()
    {
        if ( gridLine.getDraw() )
        {
            setColor( gridLine.getColor() );
            float           spacing = gridLine.getSpacing();
            float           stroke  = gridLine.getStroke();
            Rectangle       rect    = comp.getVisibleRect();
            NewLineGen   lineGen = 
                new NewLineGen( rect, gridUnit, spacing );
            gtx.setStroke( new BasicStroke( stroke ) );
            gtx.setColor( gridLine.getColor() );
            lineGen.forEach( gtx::draw );
            
            setColor( null );
        }
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
    private void drawVerticalLabels()
    {
        // padding between tic mark and label
        final int   labelPadding    = 3;
        
        float   ticMajorMPU = ticMajor.getSpacing();
        float   ticMajorLen = ticMajor.getLength();
        String  fontName    = mainWindow.getFontName();
        int     fontSize    = (int)mainWindow.getFontSize();
        int     fontStyle   = mainWindow.getFontStyle();
        Font    labelFont   = new Font( fontName, fontStyle, fontSize );
        
        gtx.setFont( labelFont );
        FontRenderContext   frc     = gtx.getFontRenderContext();
        Rectangle           rect    = comp.getVisibleRect();
        NewLineGen       lineGen = 
            new NewLineGen( 
                rect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                LineGenerator.VERTICAL
            );
        int         numLeft     = 
            (int)(lineGen.getVertLineCount() / 2);
        float       labelIncr   = 1 / ticMajorMPU;
        float       nextLabel   = -numLeft * labelIncr;
        for ( Line2D line : lineGen )
        {
            // Don't draw a label at the origin
            if ( !equal( nextLabel, 0 ) )
            {
                String      label   = String.format( "%3.2f", nextLabel );
                TextLayout  layout  = 
                    new TextLayout( label, labelFont, frc );
                Rectangle2D bounds  = layout.getBounds();
                float       yOffset = 
                    (float)(bounds.getHeight() + labelPadding);
                float       xOffset = (float)(bounds.getWidth() / 2);
                float       xco     = (float)line.getX2() - xOffset;
                float       yco     = (float)line.getY2() + yOffset;
                layout.draw( gtx, xco, yco );
            }
            nextLabel += labelIncr;
        }
    }

    /**
     * Draw the labels on the horizontal tic marks
     * (top to bottom of y-axis).
     * The origin is not labeled.
     */
    public void drawHorizontalLabels()
    {
        // padding between tic mark and label
        final int   labelPadding    = 5;
        
        float   ticMajorMPU = ticMajor.getSpacing();
        float   ticMajorLen = ticMajor.getLength();
        String  fontName    = mainWindow.getFontName();
        int     fontSize    = (int)mainWindow.getFontSize();
        int     fontStyle   = mainWindow.getFontStyle();
        Font    labelFont   = new Font( fontName, fontStyle, fontSize );

        gtx.setFont( labelFont );
        FontRenderContext   frc = gtx.getFontRenderContext();
        Rectangle       rect    = comp.getVisibleRect();
        NewLineGen   lineGen = 
            new NewLineGen( 
                rect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                NewLineGen.HORIZONTAL
            );
        int         numAbove    = 
            (int)(lineGen.getHorLineCount() / 2);
        float       labelIncr   = 1 / ticMajorMPU;
        float       nextLabel   = numAbove * labelIncr;
        for ( Line2D line : lineGen )
        {
            // Don't draw a label at the origin
            if ( !equal( nextLabel, 0 ) )
            {
                String      label   = String.format( "%3.2f", nextLabel );
                TextLayout  layout  = 
                    new TextLayout( label, labelFont, frc );
                Rectangle2D bounds  = layout.getBounds();
                float       yOffset = (float)(bounds.getHeight() / 2);
                float       xco     = (float)line.getX2() + labelPadding;
                float       yco     = (float)line.getY2() + yOffset;
                layout.draw( gtx, xco, yco );
            }
            nextLabel -= labelIncr;
        }
    }

    /**
     * Draw the labels on the axes on the x-axis.
     */
    public void drawAxes()
    {
        setColor( axis.getColor() );
        gtx.setStroke( new BasicStroke( axis.getStroke() ) );
        
        // Set the gridUnit to the width of the grid...
        // ... set the LPU to 1...
        // ... NewLineGen will iterate lines only for the axes.
        Rectangle       rect        = comp.getVisibleRect();
        float           gridUnit    = comp.getWidth();
        NewLineGen   lineGen     = 
            new NewLineGen( rect, gridUnit, 1, 0 );
        Iterator<Line2D>    iter    = lineGen.axesIterator();
        gtx.draw( iter.next() );
        gtx.draw( iter.next() );
        setColor( null );
    }

    /**
     * Draw the minor tic marks on the sample graph.
     */
    public void drawMinorTics()
    {
        if ( ticMinor.getDraw() )
        {
            setColor( ticMinor.getColor() );
            float           ticMinorMPU = ticMinor.getSpacing();
            float           ticMinorLen = ticMinor.getLength();
            Rectangle       rect        = comp.getVisibleRect();
            NewLineGen   lineGen     = 
                new NewLineGen( 
                    rect, 
                    gridUnit, 
                    ticMinorMPU,
                    ticMinorLen,
                    NewLineGen.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMinor.getStroke() ) );
            gtx.setColor( ticMinor.getColor() );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .forEach( gtx:: draw );
            setColor( null );
        }
    }


    /**
     * Draw the major tic marks on the sample graph.
     */
    public void drawMajorTics()
    {
        if ( ticMajor.getDraw() )
        {
            setColor( ticMajor.getColor() );
            Rectangle       rect    = comp.getVisibleRect();
            NewLineGen   lineGen = 
                new NewLineGen( 
                    rect, 
                    gridUnit, 
                    ticMajor.getSpacing(),
                    ticMajor.getLength(),
                    NewLineGen.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMajor.getStroke() ) );
            gtx.setColor( ticMajor.getColor() );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .forEach( gtx::draw );
            setColor( null );
        }
    }
    
    /**
     * Configure the color in the graphics context.
     * If newColor is non-null,
     * the saveColor variable
     * is set to the current color
     * of the graphics context,
     * then the color of the graphics context
     * is changed to newColor.
     * If newColor is null
     * the color of the graphics context
     * is changed to saveColor.
     * 
     * @param newColor  determines the color in the graphics context
     */
    private void setColor( Color newColor )
    {
        if ( newColor != null )
        {
            saveColor = gtx.getColor();
            gtx.setColor( newColor );
        }
        else
            gtx.setColor( saveColor );
    }

    /**
     * Compares two given decimal values,
     * using the epsilon test for equality.
     * 
     * @param fVal1 the first given decimal value
     * @param fVal2 the second given decimal value
     * 
     * @return  true if the given values are equal
     */
    private static boolean equal( float fVal1, float fVal2 )
    {
        final float epsilon = .0001f;
        float       diff    = Math.abs( fVal1 - fVal2 );
        boolean     equal   = diff < epsilon;
        return equal;
    }
}