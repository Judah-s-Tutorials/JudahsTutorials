package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.LineGenerator;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;

public class GraphManager
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
        
    private Graphics2D          gtx         = null;
    private JComponent          comp;
    
    private float               gridUnit;
    private Color               saveColor;

    private final GraphPropertySet          mainWindow  =
        new GraphPropertySetMW();
    
    private final LinePropertySetAxes       axis        =
        new LinePropertySetAxes();
    
    private final LinePropertySetTicMajor   ticMajor    =
        new LinePropertySetTicMajor();
    
    private final LinePropertySetTicMinor   ticMinor    =
        new LinePropertySetTicMinor();
    
    private final LinePropertySetGridLines  gridLine    =
        new LinePropertySetGridLines();
    
    public GraphManager( JComponent comp )
    {
        this.comp = comp;
        reset();
    }
    
    public void apply()
    {
        mainWindow.apply();
        Stream.of( axis, ticMajor, ticMinor, gridLine )
            .forEach( p -> p.apply() );
    }

    public void reset()
    {
        gridUnit = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        mainWindow.reset();
        axis.reset();
        gridLine.reset();
        ticMajor.reset();
        ticMinor.reset();
    }

    public void refresh( Graphics2D graphics )
    {
        if ( gtx != null )
            gtx.dispose();
        gtx = (Graphics2D)graphics.create();
    }
    
    public void drawBackground()
    {
        setColor( mainWindow.getBGColor() );
        gtx.fillRect( 0, 0, comp.getWidth(), comp.getHeight() );
        setColor( null );
    }
    
    public void drawGridLines()
    {
        if ( gridLine.getDraw() )
        {
            setColor( gridLine.getColor() );
            float           spacing = gridLine.getSpacing();
            float           stroke  = gridLine.getStroke();
            Rectangle       rect    = comp.getVisibleRect();
            LineGenerator   lineGen = 
                new LineGenerator( rect, gridUnit, spacing );
            gtx.setStroke( new BasicStroke( stroke ) );
            gtx.setColor( gridLine.getColor() );
            lineGen.forEach( gtx::draw );
            setColor( null );
        }
    }
    
    public void drawText()
    {
        drawHorizontalLabels();
        drawVerticalLabels();
    }
    
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
        LineGenerator       lineGen = 
            new LineGenerator( 
                rect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                LineGenerator.VERTICAL
            );
        int         numLeft     = 
            (int)(lineGen.getTotalVerticalLines() / 2);
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
        LineGenerator   lineGen = 
            new LineGenerator( 
                rect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                LineGenerator.HORIZONTAL
            );
        int         numAbove    = 
            (int)(lineGen.getTotalHorizontalLines() / 2);
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

    public void drawAxes()
    {
        setColor( axis.getColor() );
        gtx.setStroke( new BasicStroke( axis.getStroke() ) );
        
        // Set the gridUnit to the width of the grid...
        // ... set the LPU to 1...
        // ... LineGenerator will iterate lines only for the axes.
        Rectangle       rect    = comp.getVisibleRect();
        float   gridUnit    = comp.getWidth();
        LineGenerator   lineGen = 
            new LineGenerator( rect, gridUnit, 1 );
        lineGen.forEach( gtx::draw );
        setColor( null );
    }

    public void drawMinorTics()
    {
        if ( ticMinor.getDraw() )
        {
            setColor( ticMinor.getColor() );
            float           ticMinorMPU = ticMinor.getSpacing();
            float           ticMinorLen = ticMinor.getLength();
            Rectangle       rect        = comp.getVisibleRect();
            LineGenerator   lineGen     = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMinorMPU,
                    ticMinorLen,
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMinor.getStroke() ) );
            gtx.setColor( ticMinor.getColor() );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .filter( Predicate.not(lineGen::isXAxis) )
                .filter( Predicate.not(lineGen::isYAxis) )
                .forEach( gtx:: draw );
            setColor( null );
        }
    }

    public void drawMajorTics()
    {
        if ( ticMajor.getDraw() )
        {
            setColor( ticMajor.getColor() );
            Rectangle       rect    = comp.getVisibleRect();
            LineGenerator   lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMajor.getSpacing(),
                    ticMajor.getLength(),
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMajor.getStroke() ) );
            gtx.setColor( ticMajor.getColor() );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .filter( Predicate.not(lineGen::isXAxis) )
                .filter( Predicate.not(lineGen::isYAxis) )
                .forEach( gtx::draw );
            setColor( null );
        }
    }
    
    public Color getColor()
    {
        Color   color   = mainWindow.getBGColor();
        return color;
    }

    /**
     * @return the mainWindow
     */
    public GraphPropertySet getMainWindow()
    {
        return mainWindow;
    }

    /**
     * @return the axis
     */
    public LinePropertySetAxes getAxis()
    {
        return axis;
    }

    /**
     * @return the ticMajor
     */
    public LinePropertySetTicMajor getTicMajor()
    {
        return ticMajor;
    }

    /**
     * @return the ticMinor
     */
    public LinePropertySetTicMinor getTicMinor()
    {
        return ticMinor;
    }

    /**
     * @return the gridLine
     */
    public LinePropertySetGridLines getGridLine()
    {
        return gridLine;
    }

    /**
     * @return the gridUnit
     */
    public float getGridUnit()
    {
        return gridUnit;
    }

    /**
     * @param gridUnit the gridUnit to set
     */
    public void setGridUnit(float gridUnit)
    {
        this.gridUnit = gridUnit;
    }
    
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

    private static boolean equal( float fVal1, float fVal2 )
    {
        final float epsilon = .0001f;
        float       diff    = Math.abs( fVal1 - fVal2 );
        boolean     equal   = diff < epsilon;
        return equal;
    }
}