package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PropertyManager;

public class LinePropertyTester
{
    /** Property manager singleton. Declared here for convenience. */
    private static final PropertyManager    pmgr    = PropertyManager.INSTANCE;
    
    /** Grid background color. */
    private static final Color  gridBGColor     = Color.WHITE;    
    /** Grid width; puts y-axis at x = 100. */
    private static final int    testGridWidth   = 201;
    /** Grid height; puts x-axis at y = 200. */
    private static final int    testGridHeight  = 401;
    /** Center of y-axis */
    private static final int    yAxisXco        = testGridWidth / 2;
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
     *      x-axis: x = 206, 212, 218
     * <li>
     * <li>
     *      y-axis: y = 106, 112, 118
     * <li>
     * </ul>
     */
    private static final int    ticMajorMPU     = 10;
    /** 
     * Grid-line lines-per-unit.
     * Puts a grid line at:
     * <ul>
     * <li>
     *      x-axis: x = 215, 230, 245
     * <li>
     * <li>
     *      y-axis: y = 115, 130, 145
     * <li>
     * </ul>
     */
    private static final int    gridLPU         = 15;
    
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
    
    private int             buffWidth   = 500;
    private int             buffHeight  = 500;
    private int             buffType    = BufferedImage.TYPE_INT_ARGB;
    private BufferedImage   image       = 
        new BufferedImage( buffWidth, buffHeight, buffType );
    
    public static void main( String[] args )
    {
        new LinePropertyTester().execute();
    }
    
    public void initGrid( CartesianPlane plane )
    {
        pmgr.setProperty( CPConstants.MW_BG_COLOR_PN, gridBGColor );

        pmgr.setProperty( CPConstants.TIC_MINOR_COLOR_PN, ticMinorLD.color );
        pmgr.setProperty( CPConstants.TIC_MINOR_DRAW_PN, "TRUE" );
        pmgr.setProperty( CPConstants.TIC_MINOR_WEIGHT_PN, ticMinorLD.width );
        pmgr.setProperty( CPConstants.TIC_MINOR_LEN_PN, ticMinorLD.len );

        pmgr.setProperty( CPConstants.TIC_MAJOR_COLOR_PN, ticMajorLD.color );
        pmgr.setProperty( CPConstants.TIC_MAJOR_DRAW_PN, "TRUE" );
        pmgr.setProperty( CPConstants.TIC_MAJOR_WEIGHT_PN, ticMajorLD.width );
        pmgr.setProperty( CPConstants.TIC_MAJOR_LEN_PN, ticMajorLD.len );

        pmgr.setProperty( CPConstants.GRID_LINE_COLOR_PN, gridLineLD.color );
        pmgr.setProperty( CPConstants.GRID_LINE_DRAW_PN, "TRUE" );
        pmgr.setProperty( CPConstants.GRID_LINE_WEIGHT_PN, gridLineLD.width );

        pmgr.setProperty( CPConstants.AXIS_COLOR_PN, axesLD.color );
        pmgr.setProperty( CPConstants.AXIS_WEIGHT_PN, axesLD.width );
        
        pmgr.setProperty( CPConstants.MARGIN_BOTTOM_WIDTH_PN, "0" );
        pmgr.setProperty( CPConstants.MARGIN_TOP_WIDTH_PN, "0" );
        pmgr.setProperty( CPConstants.MARGIN_LEFT_WIDTH_PN, "0" );
        pmgr.setProperty( CPConstants.MARGIN_RIGHT_WIDTH_PN, "0" );
        
        pmgr.setProperty( CPConstants.LABEL_DRAW_PN, "false" );
        
//        Graphics    graphics    = image.getGraphics();
//        
//        plane = (CartesianPlane)TestUtils
//            .getComponent( c -> c instanceof CartesianPlane );
//        if ( plane != null )
//            plane.paintComponent( graphics );
    }
    
    public BufferedImage getBufferedImage()
    {
        return image;
    }
    
    private void execute()
    {
        Color   expColor    = Color.RED;
        int     iExpColor   = expColor.getRGB();
        System.out.println( image.getWidth() );
        System.out.println( image.getHeight() );
        IntStream
            .range( 0, buffWidth )
            .forEach( i -> image.setRGB( i, i, iExpColor ) );
        
        List<Point2D>    points =
            IntStream
            .range( 0, buffWidth )
            .mapToObj( i -> new Point2D.Double( i, i ) )
            .collect( Collectors.toList() );
        
        int testLoc = 10;
        System.out.println( "1. " + testList( points, iExpColor ) );
        image.setRGB( testLoc, testLoc, 0X000000 );
        System.out.println( "2. " + testList( points, iExpColor ) );
        image.setRGB( testLoc, testLoc, iExpColor );
        System.out.println( "3. " + testList( points, iExpColor ) );
        points.add( new Point2D.Double( 10, 9 ) );
        System.out.println( "4. " + testList( points, iExpColor ) );
        
        getAllLines().forEach( System.out::println );
    }
    
    private boolean testList( List<Point2D> points, int expColor )
    {
        Point failedPoint =
            points.stream()
                .map( p -> new Point( (int)p.getX(), (int)p.getY() ) )
                .filter( Predicate.not(
                    ((Predicate<Point>)(p -> p.x >= 0))
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
     
    private Stream<TLiner> getAllLines()
    {
        List<TLiner>    lineList    = 
            Stream.of( axesLD, ticMinorLD, ticMajorLD, gridLineLD )
                .flatMap( ld -> 
                    Stream.of( 
                        TLiner.ofVertical( ld ), 
                        TLiner.ofHorizontal( ld )
                    )
                )
                .collect( Collectors.toList() );

        Stream<TLiner>    lineIter    = lineList.stream();
        
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
        
        @Override
        public String toString()
        {
            // Strip alpha bits from Color.getRGB()
            int     iExtColor   = 0x00ffffff & exterior.getRGB();
            int     iIntColor   = 0x00ffffff & interior.getRGB();
            
            // Convert color to hex string with leading 0s
            String  sExtColor   = String.format( "%08x", iExtColor );
            String  sIntColor   = String.format( "%08x", iIntColor );
            
            StringBuilder   bldr    =
                new StringBuilder( "[" )
                    .append( tLine )
                    .append( "],e=" )
                    .append( sExtColor )
                    .append( ",i=" )
                    .append( sIntColor );
            return bldr.toString();
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
