package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.LineSegment;
import com.acmemail.judah.cartesian_plane.test_utils.GraphManagerTestGUI;

class GraphManagerTest
{
    private static final String             AXES        =
        LinePropertySetAxes.class.getSimpleName();
    private static final String             GRID_LINES  =
        LinePropertySetGridLines.class.getSimpleName();
    private static final String             TIC_MAJOR   =
        LinePropertySetTicMajor.class.getSimpleName();
    private static final String             TIC_MINOR   =
        LinePropertySetTicMinor.class.getSimpleName();
    
    private static final PropertyManager    pMgr        = 
        PropertyManager.INSTANCE;
    private static final Profile            baseProfile = new Profile();
    
    private Profile             workingProfile;
    private GraphPropertySet    mainWindow;
    private GraphManager        graphManager;
    private static GraphManagerTestGUI testGUI;
        
    
    @BeforeAll
    public static void beforeAll()
    {
        GUIUtils.schedEDTAndWait( 
            () -> testGUI = new GraphManagerTestGUI()
        );
    }
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        baseProfile.apply();
        workingProfile = new Profile();
        mainWindow = workingProfile.getMainWindow();
    }

    @Test
    public void testGraphManager()
    {
//        Scanner         scanner = new Scanner( System.in );
//        BufferedImage   image   = null;
//        testGUI.drawAxes();
//        scanner.nextLine();
//        testGUI.refresh();
//        scanner.nextLine();
//        testGUI.setLineStroke( AXES, 5 );
//        testGUI.setLineColor( AXES, Color.RED );
//        image = testGUI.drawAxes();
//        scanner.nextLine();
//        testGUI.drawMajorTics();
//        scanner.nextLine();
//        testGUI.setLineStroke( TIC_MAJOR, 2 );
//        testGUI.setLineColor( TIC_MAJOR, Color.BLUE );
//        testGUI.drawMajorTics();
//        scanner.nextLine();
//        
//        testGUI.setLineStroke( AXES, 5 );
//        testGUI.setLineColor( AXES, Color.RED );
//        image = testGUI.drawAxes();
//        Rectangle2D rect    = getBoundingRectangle( image );
//        Point       left    = new Point( 0, (int)rect.getCenterY() );
//        LineSegment seg     = LineSegment.of( left, image );
//        System.out.println( seg );
//        
        fail("Not yet implemented");
    }

    @Test
    public void testRefresh()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawBackground()
    {
        int             rgb     = testGUI.getGridColorRGB();
        BufferedImage   image   = testGUI.drawBackground();
        validateFill( image, rgb );
        int             diffRGB = ~rgb & 0xFFFFFF;   
        testGUI.setGridColor( diffRGB );
        image = testGUI.drawBackground();
        validateFill( image, diffRGB );
    }

    @Test
    public void testDrawGridLines()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawText()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawHorizontalLabels()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawYAxis()
    {
        testDrawYAxis( Color.RED, 4 );
        testDrawYAxis( Color.BLUE, 6 );
    }
    
    private void testDrawYAxis( Color expColor, int expStroke )
    {
        int             expRGB      = expColor.getRGB() & 0xFFFFFF;
        testGUI.setLineColor( AXES, expColor );
        testGUI.setLineStroke( AXES, expStroke );
        BufferedImage   image       = testGUI.drawAxes();
        
        double          centerXco   = image.getWidth() / 2.;
        double          centerYco   = image.getHeight() / 2.;
        double          length      = image.getHeight();
        LineSegment     expSeg      = getVerticalLineSegment( 
            image, 
            centerYco,
            length, 
            expStroke,
            expRGB
        );
        Point2D         center      =
            new Point2D.Double( centerXco, 0 );
        LineSegment     actSeg      =
            LineSegment.of( center, image );
        assertEquals( expSeg, actSeg );
    }

    @Test
    public void testDrawMinorTics()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawMajorTics()
    {
        fail("Not yet implemented");
    }
    
    /**
     * Verify that every value in a given image
     * is that of a given color.
     * 
     * @param image the given image
     * @param rgb   the given color
     */
    private static void validateFill( BufferedImage image, int rgb )
    {
        int             width   = image.getWidth();
        int             height  = image.getHeight();
        for ( int xco = 0 ; xco < width ; ++xco )
            for ( int yco = 0 ; yco < height ; ++yco )
                assertEquals( rgb, image.getRGB( xco, yco ) & 0xFFFFFF );
    }
  
    private static LineSegment getVerticalLineSegment( 
        BufferedImage   image,
        double          centerYco,
        double          len, 
        double          stroke,
        int             rgb
    )
    {
        double  centerXco   = image.getWidth() / 2;
        double  ulcXco      = centerXco - stroke / 2;
        double  ulcYco      = centerYco - len / 2;
        Rectangle2D rect        =
            new Rectangle2D.Double( ulcXco, ulcYco, stroke, len );
        LineSegment seg     = LineSegment.of( rect, rgb );
        return seg;
    }

    private static Rectangle2D getBoundingRectangle( BufferedImage image )
    {
        int         width   = image.getWidth();
        int         height  = image.getHeight();
        Rectangle2D rect    = 
            new Rectangle2D.Double( 0, 0, width, height );
        return rect;
    }
}
