package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
import com.acmemail.judah.cartesian_plane.test_utils.lp_panel.LPPTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.lp_panel.LPP_TA;
import com.acmemail.judah.cartesian_plane.test_utils.lp_panel.LPP_TADetail;

class LinePropertiesPanelVisualTest
{
    /** Path to the storage location of all test data files. */
    private static final File               lppPath     = 
        Utils.getTestDataDir( LPP_TA.LPP_DIR );
    /**
     * List of all LP_TADetail objects extracted from the 
     * test data files.
     */
    private static final List<LPP_TADetail> allDetails  =
        getAllDetailObjects();
    /** The test dialog which is used to generate the actual image. */
    private static LPPTestDialog            testDialog  = null;
    
    /** Dialog to display "expected" image from test data file. */
    private static ExpDialog                expDialog   = null;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        LinePropertySetInitializer.initProperties();

        testDialog = LPPTestDialog.getDialog();
        testDialog.setDialogVisible( true );
        testDialog.setLocation( 400, 100 );

        expDialog = new ExpDialog();
        expDialog.setLocation( 400, testDialog.getHeight() + 110 );
        expDialog.setVisible( true );
        
    }

    @ParameterizedTest
    @MethodSource( "streamDetail" )
    public void test( LPP_TADetail detail )
    {
        selectTestDialogOption( detail );
        expDialog.setIcon(detail);
        Utils.pause( 1000 );
        assertImageEquals( detail );
    }

    /**
     * Within a given LPP_TADetail record,
     * find the Class class value (lppType)
     * and match it to radio button
     * in the test dialog that encapsulates
     * a LinePropertySet with an equivalent type.
     * Then click the button.
     * 
     * @param detail    the given detail record
     */
    private void selectTestDialogOption( LPP_TADetail detail )
    {
        Class<?>        lppType = detail.getLPPType();
        PRadioButton<LinePropertySet>   target  =
            testDialog.getRadioButtons().stream()
                .filter( b -> b.get().getClass() == lppType )
                .findFirst().orElse( null );
        if ( target == null )
        {
            String  msg = 
                "PRadioButton for " + 
                lppType.getSimpleName() + 
                " not found.";
            fail( msg );
        }
        testDialog.doClick( target );
    }
    
    /**
     * Gets a list of all LPP_TADetail objects
     * encapsulated in test data files
     * in the target test directory.
     * 
     * @return a list of all LPP_TADetail objects from the test directory
     */
    private static List<LPP_TADetail> getAllDetailObjects()
    {
        File[]  allFiles    = 
            lppPath.listFiles( f -> f.getName().endsWith( ".ser" ) );
        List<LPP_TADetail>  detailObjects   =
            Stream.of( allFiles )
                .map( f -> getDetail( f ) )
                .toList();
        return detailObjects;
    }
    
    /**
     * Given a file descriptor,
     * read the file and extract
     * the encapsulated LPP_TADetail object.
     * 
     * @param file  given file descriptor
     * 
     * @return  the extracted LPP_TADetail object
     */
    private static LPP_TADetail getDetail( File file )
    {
        LPP_TADetail    detail  = null;
        try (
            FileInputStream fStream = new FileInputStream( file );
            ObjectInputStream oStream = new ObjectInputStream( fStream );
        )
        {
            Object  obj = oStream.readObject();
            if ( !(obj instanceof LPP_TADetail) )
            {
                String  message     =
                    "Expected type: LPP_TADetail, actual type: " + 
                        obj.getClass().getSimpleName();
                fail( message );
            }
            detail = (LPP_TADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            fail( exc );
        }
        
        return detail;
    }
    
    /**
     * Source for executing parameterized test.
     * Returns a stream of LPP_TADetail objects
     * extracted from the test data files
     * in the test data directory.
     * 
     * @return  
     *      a stream of LPP_Detail objects extracted from the
     *      test data files in the test directory
     */
    private static Stream<LPP_TADetail> streamDetail()
    {
        return allDetails.stream();
    }
    
    /**
     * Asserts that
     * the expected image
     * contained in a given detail record
     * matches the actual image
     * from the test dialog.
     * <p>
     * Precondition: 
     * The selected radio button 
     * in the test dialog
     * incorporates a LPPProperySet class
     * as the type recorded in the detail record.
     * 
     * @param detail    the given detail record
     */
    private void assertImageEquals( LPP_TADetail detail )
    {
        BufferedImage   expImage    = detail.getBufferedImage();
        BufferedImage   actImage    = testDialog.getPanelImage();
        assertTrue( Utils.equals( expImage, actImage ) );
    }
    
    /**
     * Simple dialog to display the image
     * contained in a LPP_TADetail object.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class ExpDialog extends JDialog
    {
        /** Label used to display the buffered image. */
        private JLabel   label   = null;
       
        /**
         * Constructor.
         * Configures this dialog.
         */
        public ExpDialog()
        {
            GUIUtils.schedEDTAndWait( () -> {
                label = new JLabel();
                JPanel   panel   = new JPanel( new BorderLayout() );
                panel.add( label,  BorderLayout.CENTER );
                setContentPane( panel );
                pack();
            });
        }
       
        /**
         * Displays the BufferedImage
         * encapsulated in a given LPP_TADetail record.
         * 
         * @param detail    the given LPP_TADetail record
         */
        public void setIcon( LPP_TADetail detail )
        {
            GUIUtils.schedEDTAndWait( () -> {
                BufferedImage   image   = detail.getBufferedImage();
                Icon            icon    = new ImageIcon( image );
                label.setIcon( icon );
                pack();
           });
        }
    }
}
