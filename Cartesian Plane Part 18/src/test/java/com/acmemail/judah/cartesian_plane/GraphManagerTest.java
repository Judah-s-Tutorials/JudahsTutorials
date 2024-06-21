package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;

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
        Scanner         scanner = new Scanner( System.in );
        BufferedImage   image   = null;
        testGUI.drawAxes();
        scanner.nextLine();
        testGUI.refresh();
        scanner.nextLine();
        testGUI.setLineStroke( AXES, 5 );
        testGUI.setLineColor( AXES, Color.RED );
        image = testGUI.drawAxes();
        scanner.nextLine();
        testGUI.drawMajorTics();
        scanner.nextLine();
        testGUI.setLineStroke( TIC_MAJOR, 2 );
        testGUI.setLineColor( TIC_MAJOR, Color.BLUE );
        testGUI.drawMajorTics();
        scanner.nextLine();
        
        testGUI.setLineStroke( AXES, 5 );
        testGUI.setLineColor( AXES, Color.RED );
        image = testGUI.drawAxes();
        Rectangle2D rect    = getBoundingRectangle( image );
        Point       left    = new Point( 0, (int)rect.getCenterY() );
        LineSegment seg     = new LineSegment( left, image );
        System.out.println( seg );
        
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
        fail("Not yet implemented");
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
    public void testDrawAxes()
    {
        fail("Not yet implemented");
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

    private static Rectangle2D getBoundingRectangle( BufferedImage image )
    {
        int         width   = image.getWidth();
        int         height  = image.getHeight();
        Rectangle2D rect    = 
            new Rectangle2D.Double( 0, 0, width, height );
        return rect;
    }
}
