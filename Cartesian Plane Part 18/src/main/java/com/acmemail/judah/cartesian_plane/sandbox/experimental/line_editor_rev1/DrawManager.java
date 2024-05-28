package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev1;

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
import java.util.stream.StreamSupport;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.LineGenerator;
import com.acmemail.judah.cartesian_plane.PropertyManager;

public class DrawManager
{
    private final PropertyManager   pMgr        = PropertyManager.INSTANCE;
        
    private Graphics2D          gtx         = null;
    private JComponent          comp;
    
    private float               gridUnit;
    private float               axisWeight;
    private Color               axisColor;
    
    private int                 labelFontStyle;
    private Color               labelFontColor;
    private String              labelFontName;
    private int                 labelFontSize;
    private Font                labelFont;
    
    private float               ticMajorMPU;
    private float               ticMajorLen;
    private float               ticMajorWeight;
    private Color               ticMajorColor;
    private boolean             ticMajorDraw;
    
    private float               ticMinorMPU;
    private float               ticMinorLen;
    private float               ticMinorWeight;
    private Color               ticMinorColor;
    private boolean             ticMinorDraw;
    
    private float               gridLineLPU;
    private float               gridLineWeight;
    private Color               gridLineColor;
    private boolean             gridLineDraw;
    
    public DrawManager( JComponent comp )
    {
        this.comp = comp;
        reset();
    }
    
    public void update( Graphics2D graphics )
    {
        if ( gtx != null )
            gtx.dispose();
        gtx = (Graphics2D)graphics.create();
    }
    
    public void drawGridLines()
    {
        if ( gridLineDraw )
        {
            Rectangle       rect    = comp.getVisibleRect();
            LineGenerator   lineGen = 
                new LineGenerator( rect, gridUnit, gridLineLPU );
            gtx.setStroke( new BasicStroke( (float) gridLineWeight ) );
            gtx.setColor( gridLineColor );
            lineGen.forEach( gtx::draw );
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
        
        gtx.setFont( labelFont );
        FontRenderContext   frc = gtx.getFontRenderContext();
        Rectangle       rect    = comp.getVisibleRect();
        LineGenerator   lineGen = 
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
        gtx.setColor( axisColor );
        gtx.setStroke( new BasicStroke( axisWeight ) );
        
        // Set the gridUnit to the width of the grid...
        // ... set the LPU to 1...
        // ... LineGenerator will iterate lines only for the axes.
        Rectangle       rect    = comp.getVisibleRect();
        float   gridUnit    = comp.getWidth();
        LineGenerator   lineGen = 
            new LineGenerator( rect, gridUnit, 1 );
        lineGen.forEach( gtx::draw );
    }

    public void drawMinorTics()
    {
        if ( ticMinorDraw )
        {
            Rectangle       rect    = comp.getVisibleRect();
            LineGenerator   lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMinorMPU,
                    ticMinorLen,
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMinorWeight ) );
            gtx.setColor( ticMinorColor );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .filter( Predicate.not(lineGen::isXAxis) )
                .filter( Predicate.not(lineGen::isYAxis) )
                .forEach( gtx:: draw );
        }
    }

    public void drawMajorTics()
    {
        if ( ticMajorDraw )
        {
            Rectangle       rect    = comp.getVisibleRect();
            LineGenerator   lineGen = 
                new LineGenerator( 
                    rect, 
                    gridUnit, 
                    ticMajorMPU,
                    ticMajorLen,
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMajorWeight ) );
            gtx.setColor( ticMajorColor );
            StreamSupport
                .stream( lineGen.spliterator(), false )
                .filter( Predicate.not(lineGen::isXAxis) )
                .filter( Predicate.not(lineGen::isYAxis) )
                .forEach( gtx::draw );
        }
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

    /**
     * @return the ticMajorMPU
     */
    public float getTicMajorMPU()
    {
        return ticMajorMPU;
    }

    /**
     * @param ticMajorMPU the ticMajorMPU to set
     */
    public void setTicMajorMPU(float ticMajorMPU)
    {
        this.ticMajorMPU = ticMajorMPU;
    }

    /**
     * @return the ticMinorMPU
     */
    public float getTicMinorMPU()
    {
        return ticMinorMPU;
    }

    /**
     * @param ticMinorMPU the ticMinorMPU to set
     */
    public void setTicMinorMPU(float ticMinorMPU)
    {
        this.ticMinorMPU = ticMinorMPU;
    }

    /**
     * @return the gridLineLPU
     */
    public float getGridLineLPU()
    {
        return gridLineLPU;
    }

    /**
     * @param gridLineLPU the gridLineLPU to set
     */
    public void setGridLineLPU(float gridLineLPU)
    {
        this.gridLineLPU = gridLineLPU;
    }

    private static boolean equal( float fVal1, float fVal2 )
    {
        final float epsilon = .0001f;
        float       diff    = Math.abs( fVal1 - fVal2 );
        boolean     equal   = diff < epsilon;
        return equal;
    }

    private void reset()
    {
        gridUnit = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        axisWeight = pMgr.asFloat( CPConstants.AXIS_WEIGHT_PN );
        axisColor = pMgr.asColor( CPConstants.AXIS_COLOR_PN );
        
        labelFontStyle = pMgr.asFontStyle( CPConstants.LABEL_FONT_STYLE_PN );
        labelFontName = pMgr.asString( CPConstants.LABEL_FONT_NAME_PN );
        labelFontColor = pMgr.asColor( CPConstants.LABEL_FONT_COLOR_PN );
        labelFontSize = pMgr.asInt( CPConstants.LABEL_FONT_SIZE_PN );
        labelFont = new Font( labelFontName, labelFontStyle, labelFontSize );
        
        ticMajorMPU = pMgr.asFloat( CPConstants.TIC_MAJOR_MPU_PN );
        ticMajorLen = pMgr.asFloat( CPConstants.TIC_MAJOR_LEN_PN );
        ticMajorWeight = pMgr.asFloat( CPConstants.TIC_MAJOR_WEIGHT_PN );
        ticMajorColor = pMgr.asColor( CPConstants.TIC_MAJOR_COLOR_PN );
        ticMajorDraw = pMgr.asBoolean( CPConstants.TIC_MAJOR_DRAW_PN );
        
        ticMinorMPU = pMgr.asFloat( CPConstants.TIC_MINOR_MPU_PN );
        ticMinorLen = pMgr.asFloat( CPConstants.TIC_MINOR_LEN_PN );
        ticMinorWeight = pMgr.asFloat( CPConstants.TIC_MINOR_WEIGHT_PN );
        ticMinorColor = pMgr.asColor( CPConstants.TIC_MINOR_COLOR_PN );
        ticMinorDraw = pMgr.asBoolean( CPConstants.TIC_MINOR_DRAW_PN );
        
        gridLineLPU = pMgr.asFloat( CPConstants.GRID_LINE_LPU_PN );
        gridLineWeight = pMgr.asFloat( CPConstants.GRID_LINE_WEIGHT_PN );
        gridLineColor = pMgr.asColor( CPConstants.GRID_LINE_COLOR_PN );
        gridLineDraw = pMgr.asBoolean( CPConstants.GRID_LINE_DRAW_PN );
    }
}