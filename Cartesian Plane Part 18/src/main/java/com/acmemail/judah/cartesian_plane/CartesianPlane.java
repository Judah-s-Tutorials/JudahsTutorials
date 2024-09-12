package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JPanel;

/**
 * This class encapsulates the display of a Cartesian plane.
 * The plane consists of the following components,
 * each of which is customizable by the user.
 * <p>
 * <img 
 *     src="doc-files/SamplePlane.png" 
 *     alt="Sample Plane"
 *     style="width:25%; height:auto;"
 * >
 * </p>
 * 
 *    <table class="js-plain">
 *         <caption>Summary of Properties</caption>
 *         <tr>
 *             <th class="js-plain">
 *                 Property
 *             </th>
 *             <th class="js-plain">
 *                 Type
 *             </th>
 *             <th class="js-plain">
 *                 Description
 *             </th>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridUnit
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The grid unit, 
 *                 in pixels per unit.
 *                 If this property has a value of 25
 *                 each unit of display
 *                 will have span 25 pixels.
 *                 The grid unit 
 *                 is always the same
 *                 for both the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 mwBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the main window.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginTopWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the top margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginTopBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the top margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginRightWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the right margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginRightBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the right margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginBottomWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the bottom margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginBottomBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the bottom margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginLeftWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the left margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginLeftBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the left margin.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorLen
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The length of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorMPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of minor tic marks per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the minor tic marks
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorLen
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The length of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorMPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of major tic marks per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the major tic marks
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the grid lines.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the grid lines.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineLPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of grid lines per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the grid lines
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 axisColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontName
 *             </td>
 *             <td class="js-plain">
 *                 String
 *             </td>
 *             <td class="js-plain">
 *                 The name of the font used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontStyle
 *             </td>
 *             <td class="js-plain">
 *                 int
 *             </td>
 *             <td class="js-plain">
 *                 The style of the font used to draw the labels on the x- and y-axes
 *                 (from the Font class,
 *                 e.g. Font.PLAIN).
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontSize
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The size of the font used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *    </table>
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class CartesianPlane 
    extends JPanel 
    implements PropertyChangeListener
{
    /** 
     * This declaration is just for convenience; it saves
     * having to write "PropertyManager.INSTANCE" over and over.
     */
    private static final PropertyManager pmgr   = PropertyManager.INSTANCE;
    
    private static final int    mainWindowWidthDV   =
        pmgr.asInt( CPConstants.MW_WIDTH_PN );
    private static final int    mainWindowHeightDV   =
        pmgr.asInt( CPConstants.MW_HEIGHT_PN );
    
    /////////////////////////////////////////////////
    //   General grid properties
    /////////////////////////////////////////////////
    /** Grid units (pixels-per-unit) default value: float. */
    private float   gridUnit            = 
        pmgr.asFloat( CPConstants.GRID_UNIT_PN );

    /////////////////////////////////////////////////
    //   Main window properties
    //   Note: "width" and "height" are included as
    //   main window properties in CPConstants,
    //   but it is not necessary to encapsulate
    //   their values in instance variables.
    //   See the default constructor.
    /////////////////////////////////////////////////
    private Color   mwBGColor           = 
        pmgr.asColor( CPConstants.MW_BG_COLOR_PN );
    
    /////////////////////////////////////////////////
    //   Margin properties
    /////////////////////////////////////////////////
    private float   marginTopWidth      =
        pmgr.asFloat( CPConstants.MARGIN_TOP_WIDTH_PN );
    private Color   marginTopBGColor    =
        pmgr.asColor( CPConstants.MARGIN_TOP_BG_COLOR_PN );
    private float   marginRightWidth      =
        pmgr.asFloat( CPConstants.MARGIN_RIGHT_WIDTH_PN );
    private Color   marginRightBGColor    =
        pmgr.asColor( CPConstants.MARGIN_RIGHT_BG_COLOR_PN );
    private float   marginBottomWidth      =
        pmgr.asFloat( CPConstants.MARGIN_BOTTOM_WIDTH_PN );
    private Color   marginBottomBGColor    =
        pmgr.asColor( CPConstants.MARGIN_BOTTOM_BG_COLOR_PN );
    private float   marginLeftWidth      =
        pmgr.asFloat( CPConstants.MARGIN_LEFT_WIDTH_PN );
    private Color   marginLeftBGColor    =
        pmgr.asColor( CPConstants.MARGIN_LEFT_BG_COLOR_PN );
    
    /** 
     * Supplier, set by the user,
     * to obtain a stream of PlotCommands.
     */
    private Supplier<Stream<PlotCommand>>   streamSupplier  = 
        () -> Stream.empty();
    
    /////////////////////////////////////////////////
    //   Plot properties (properties to use
    //   when plotting a point on the grid)
    /////////////////////////////////////////////////
    private Color   plotColor           =
        pmgr.asColor( CPConstants.PLOT_COLOR_PN );
    private PlotShape  plotShape       = new PointShape();
    
    private GraphManager  graphMgr;

    ///////////////////////////////////////////////////////
    //
    // The following values are recalculated every time 
    // paintComponent is invoked.
    //
    ///////////////////////////////////////////////////////
    private int                 currWidth;
    private int                 currHeight;
    private Graphics2D          gtx;
    private Rectangle2D         gridRect;
    private PlotShape           currPlotShape;
    private double              xOffset;
    private double              yOffset;
        
    /**
     * Constructor.
     * Builds a CartesianPlane with a default width and height.
     */
    public CartesianPlane()
    {
        this( mainWindowWidthDV, mainWindowHeightDV );
    }
    
    /**
     * Constructor.
     * Builds a CartesianPlane with a given width and height.
     *
     * @param width     the given width
     * @param height    the given height
     */
    public CartesianPlane( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
        pmgr.addPropertyChangeListener( this );
        
        // Register listener for redraw notifications
        NotificationManager.INSTANCE.addNotificationListener(
            CPConstants.REDRAW_NP,
            e -> repaint()
        );
        
        graphMgr = new GraphManager( gridRect, new Profile() );
    }
    
    /**
     * This method is where you do all your drawing.
     * Note the the window must be COMPLETELY redrawn
     * every time this method is called;
     * Java does not remember anything you previously drew.
     * 
     * @param graphics  Graphics context, for doing all drawing.
     */
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( mwBGColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate

        // Describe the rectangle containing the grid
        float   gridWidth   = currWidth - marginLeftWidth - marginRightWidth;
        float   minXco      = marginLeftWidth;
        float   gridHeight  = currHeight - marginTopWidth - marginBottomWidth;
        float   minYco      = marginTopWidth;
        gridRect = 
            new Rectangle2D.Float( minXco, minYco, gridWidth, gridHeight );


        // Set the clip region to the rectangle bounding the grid before
        // drawing any lines. Don't forget to restore the original clip
        // region after drawing the lines.
        Shape   origClip    = gtx.getClip();
        gtx.setClip( gridRect );
        
        // The plot color and plot shape are set to defaults
        // each time paintComponent is invoked. The user can
        // change the values but they will only be in effect
        // for the duration of one paintComponent execute; with
        // the next paintComponent execution they will return
        // to their default values.
        currPlotShape = plotShape;
        
        // Values to use in mapping Cartesian coordinates 
        // to pixel coordinates
        xOffset = gridRect.getX() + (gridRect.getWidth() - 1) / 2;
        yOffset = gridRect.getY() + (gridRect.getHeight() - 1) / 2;

        graphMgr.refresh( gtx, gridRect );
        graphMgr.drawAll();
        drawUserPlot();
        
        gtx.setClip( origClip );

        paintMargins();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    public void plotPoint( float userXco, float userYco )
    {
        double  xco = userXco * gridUnit + xOffset;
        double  yco = -userYco * gridUnit + yOffset;
        Shape   shape   = currPlotShape.getShape( xco, yco );
        gtx.draw( shape );
    }
    
    /**
     * Sets the color to use to draw plot points for
     * the duration of one execution of paintComponent.
     * 
     * @param color the color to use to draw plot points
     */
    public void setPlotColor( Color color )
    {
        gtx.setColor( color );
    }
    
    /**
     * Sets the shape to use to draw plot points for
     * the duration of one execution of paintComponent.
     * 
     * @param plotShape the shape to use to draw plot points
     */
    public void setPlotShape( PlotShape plotShape )
    {
        currPlotShape = plotShape;
    }
    
    public void propertyChange( PropertyChangeEvent evt )
    {
        String  pName   = evt.getPropertyName();
        String  newVal  = (String)evt.getNewValue();
        boolean update  = false;
        switch ( pName )
        {
        case CPConstants.GRID_UNIT_PN:
            gridUnit = CPConstants.asFloat( newVal );
            update = true;
            break;
        case CPConstants.MW_BG_COLOR_PN:
            mwBGColor = CPConstants.asColor( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_TOP_WIDTH_PN:
            marginTopWidth = CPConstants.asFloat( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_TOP_BG_COLOR_PN:
            marginTopBGColor = CPConstants.asColor( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_RIGHT_WIDTH_PN:
            marginRightWidth = CPConstants.asFloat( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_RIGHT_BG_COLOR_PN:
            marginRightBGColor = CPConstants.asColor( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_BOTTOM_WIDTH_PN:
            marginBottomWidth = CPConstants.asFloat( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_BOTTOM_BG_COLOR_PN:
            marginBottomBGColor = CPConstants.asColor( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_LEFT_WIDTH_PN:
            marginLeftWidth = CPConstants.asFloat( newVal );
            update = true;
            break;
        case CPConstants.MARGIN_LEFT_BG_COLOR_PN:
            marginLeftBGColor = CPConstants.asColor( newVal );
            update = true;
            break;
        case CPConstants.TIC_MINOR_COLOR_PN:
        case CPConstants.TIC_MINOR_WEIGHT_PN:
        case CPConstants.TIC_MINOR_LEN_PN:
        case CPConstants.TIC_MINOR_MPU_PN:
        case CPConstants.TIC_MINOR_DRAW_PN:
        case CPConstants.TIC_MAJOR_COLOR_PN:
        case CPConstants.TIC_MAJOR_WEIGHT_PN:
        case CPConstants.TIC_MAJOR_LEN_PN:
        case CPConstants.TIC_MAJOR_MPU_PN:
        case CPConstants.TIC_MAJOR_DRAW_PN:
        case CPConstants.GRID_LINE_COLOR_PN:
        case CPConstants.GRID_LINE_WEIGHT_PN:
        case CPConstants.GRID_LINE_LPU_PN:
        case CPConstants.GRID_LINE_DRAW_PN:
        case CPConstants.AXIS_COLOR_PN:
        case CPConstants.AXIS_WEIGHT_PN:
        case CPConstants.LABEL_FONT_COLOR_PN:
        case CPConstants.LABEL_FONT_NAME_PN:
        case CPConstants.LABEL_FONT_STYLE_PN:
        case CPConstants.LABEL_FONT_SIZE_PN:
        case CPConstants.LABEL_DRAW_PN:
            update = true;
            break;
        }
        
        if ( update )
        {
            graphMgr.resetProfile();
            repaint();
        }
    }
    
    /**
     * Sets the supplier
     * that will fetch a stream
     * to deliver commands
     * for plotting a curve.
     * 
     * @param supplier  the stream supplier
     */
    public void setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
    {
        if ( supplier != null )
            streamSupplier = supplier;
        else
            streamSupplier = () -> Stream.empty();
    }
    
    /**
     * Draws the user plot.
     */
    private void drawUserPlot()
    {
        gtx.setColor( plotColor );
        streamSupplier.get().forEach( c -> c.execute() );
    }
    
    private void paintMargins()
    {
        Rectangle2D rect    = new Rectangle2D.Float();
        
        // top margin
        rect.setRect( 0, 0, currWidth, marginTopWidth );
        gtx.setColor( marginTopBGColor );
        gtx.fill( rect );
        
        // right margin
        float   marginRightXco  = currWidth - marginRightWidth;
        rect.setRect( marginRightXco, 0, marginRightWidth, currHeight );
        gtx.setColor( marginRightBGColor );
        gtx.fill( rect );
        
        // bottom margin
        float   marginBottomXco  = currHeight - marginBottomWidth;
        rect.setRect( 0, marginBottomXco, currWidth, marginBottomWidth );
        gtx.setColor( marginBottomBGColor );
        gtx.fill( rect );
        
        // left margin
        rect.setRect( 0, 0, marginLeftWidth, currHeight );
        gtx.setColor( marginLeftBGColor );
        gtx.fill( rect );
    }
}
