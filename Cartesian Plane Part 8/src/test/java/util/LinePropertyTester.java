package util;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LinePropertyTester
{
    /** Grid background color. */
    private static final Color  gridBGColor     = Color.WHITE;    
    /** Grid width; puts y-axis at x = 100. */
    private static final int    testGridWidth   = 201;
    /** Center of y-axis */
    private static final int    yAxisXco        = testGridWidth / 2;
    /** Grid width; puts x-axis at y = 200. */
    private static final int    testGridHeight  = 401;
    /** Center of x-axis */
    private static final int    xAxisYco        = testGridHeight / 2;
    /** 
     * Pixels per unit. Locates points at:
     * <ul>
     * <li>
     *      x-axis: x = 216, 232, 248, 264
     * <li>
     * <li>
     *      y-axis: y = 116, 132, 148, 164
     * <li>
     * </ul>
     */
    private static final int    gridUnit        = 16;
    /** 
     * Minor tic marks-per-unit.
     * Puts a minor tic mark at:
     * <ul>
     * <li>
     *      x-axis: x = 204, 208, 212
     * <li>
     * <li>
     *      y-axis: y = 104, 108, 112
     * <li>
     * </ul>
     */
    private static final int    ticMinorMPU     = 4;
    /** 
     * Major tic marks-per-unit.
     * Puts a major tic mark at:
     * <ul>
     * <li>
     *      x-axis: x = 204, 208, 212
     * <li>
     * <li>
     *      y-axis: y = 104, 108, 112
     * <li>
     * </ul>
     */
    private static final int    ticMajorMPU     = 6;
    /** 
     * Grid-line lines-per-unit.
     * Puts a grid line at:
     * <ul>
     * <li>
     *      x-axis: x = 208, 216, 224
     * <li>
     * <li>
     *      y-axis: y = 108, 116, 124
     * <li>
     * </ul>
     */
    private static final int    gridLPU         = 8;
    
    /** Initial descriptor of axes. */
    private final LineDescriptor    axesLD      = 
        new LineDescriptor(
            yAxisXco,               // vertical axis x-coordinate
            xAxisYco,               // vertical axis y-coordinate
            xAxisYco,               // horizontal axis x-coordinate
            yAxisXco,               // horizontal axis y-coordinate
            -1,                     // initial length
            1,                      // initial width
            Color.BLACK             // initial color
        );

    /** Initial descriptor of minor tic mark. */
    private final LineDescriptor    ticMinorLD  = 
        new LineDescriptor(
            yAxisXco + ticMinorMPU, // first tic vertical x-coordinate
            xAxisYco,               // first tic vertical y-coordinate
            xAxisYco,               // first tic horizontal x-coordinate
            yAxisXco + ticMinorMPU, // first tic horizontal y-coordinate
            5,                      // initial length
            1,                      // initial width
            Color.RED               // initial color
        );
    
    /** Initial descriptor of major tic mark. */
    private final LineDescriptor    ticMajorLD  = 
        new LineDescriptor(
            yAxisXco + ticMajorMPU, // first tic vertical x-coordinate
            xAxisYco,               // first tic vertical y-coordinate
            xAxisYco,               // first tic horizontal x-coordinate
            yAxisXco + ticMajorMPU, // first tic horizontal y-coordinate
            2 * ticMinorLD.len,     // initial length
            1,                      // initial width
            Color.GREEN             // initial color
        );
    
    /** Initial descriptor of grid line. */
    private final LineDescriptor    gridLineLD  = 
        new LineDescriptor(
            yAxisXco + gridLPU,     // first vertical x-coordinate
            xAxisYco,               // first vertical y-coordinate
            xAxisYco,               // first horizontal x-coordinate
            yAxisXco + gridLPU,     // first horizontal y-coordinate
            -1,                     // initial length
            1,                      // initial width
            Color.BLUE              // initial color
        );
    
    private int             buffWidth   = 100;
    private int             buffHeight  = 100;
    private int             buffType    = BufferedImage.TYPE_INT_ARGB;
    private BufferedImage   image       = 
        new BufferedImage( buffWidth, buffHeight, buffType );
    
    private boolean testList( List<Point> points, int expColor )
    {
        Predicate<Point>    xGE0        = p -> p.x >= 0;
        Point failedPoint =
            points.stream()
                .map( p -> new Point( (int)p.getX(), (int)p.getY() ) )
                .filter( Predicate.not(
                    xGE0
                    .and( p -> p.x < buffWidth )
                    .and( p -> p.y >= 0 )
                    .and( p -> p.y < buffHeight )
                    .and( p -> image.getRGB( p.x, p.y ) == expColor )
                    )
                )
                .findAny().orElse( null );
        boolean result  = failedPoint == null;
        return result;
    }
     
    private Iterator<TLiner> getAllLines()
    {
        List<TLiner>    lineList    = new ArrayList<>();
        Stream.of( axesLD, ticMinorLD, ticMajorLD, gridLineLD )
            .forEach( ld -> {
                lineList.add( TLiner.ofVertical( ld ) ); 
                lineList.add( TLiner.ofHorizontal( ld ) );
            });

        Iterator<TLiner>    lineIter    = lineList.iterator();
        
        return lineIter;
    }
    
    private static class TLiner
    {
        private TLine   tLine;
        private Color   exterior;
        private Color   interior;
        
        public TLiner( TLine tLine, Color interior, Color exterior )
        {
            this.tLine = tLine;
            this.exterior = exterior;
            this.interior = interior;
        }
        
        private static TLiner ofVertical( LineDescriptor lDesc )
        {
            int     topXco  = lDesc.vXco;
            int     topYco  = lDesc.vYco - lDesc.len / 2;
            VLine   vLine   = 
                new VLine( topXco, topYco, lDesc.len, lDesc.width );
            TLiner  tLiner  = new TLiner( vLine, lDesc.color, gridBGColor );
            return tLiner;
        }
        
        private static TLiner ofHorizontal( LineDescriptor lDesc )
        {
            int     leftXco = lDesc.hXco - lDesc.len / 2;
            int     leftYco = lDesc.vYco;
            HLine   hLine   = 
                new HLine( leftXco, leftYco, lDesc.len, lDesc.width );
            TLiner  tLiner  = new TLiner( hLine, lDesc.color, gridBGColor );
            return tLiner;
        }
    }
    
    private static class LineDescriptor
    {
        private final int   vXco;
        private final int   vYco;
        private final int   hXco;
        private final int   hYco;
        private int         len;
        private int         width;
        private Color       color;
        public LineDescriptor(
            int vXco, 
            int vYco, 
            int hXco, 
            int hYco, 
            int len, 
            int width, 
            Color color
        )
        {
            this.vXco = vXco;
            this.vYco = vYco;
            this.hXco = hXco;
            this.hYco = hYco;
            this.len = len;
            this.width = width;
            this.color = color;
        }
        
        public void configForTest()
        {
            int oldColor    = color.getRGB();
            int newColor    = oldColor + 0x101010;
            color = new Color( newColor );
            if ( len != -1 )
                len *= 2;
            width = 5;
        }
    }
}
