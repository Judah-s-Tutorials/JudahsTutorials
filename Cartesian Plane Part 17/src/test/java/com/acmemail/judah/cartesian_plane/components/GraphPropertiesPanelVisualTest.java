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
import java.util.stream.Collectors;
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
import com.acmemail.judah.cartesian_plane.test_utils.Utils;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPPTestDataInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPPTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPP_TA;
import com.acmemail.judah.cartesian_plane.test_utils.gp_panel.GPP_TADetail;

class GraphPropertiesPanelVisualTest
{
    /** Path to the storage location of all test data files. */
    private static final File               gppPath     = 
        Utils.getTestDataDir( GPP_TA.GPP_DIR );
    /**
     * List of all GP_TADetail objects extracted from the 
     * test data files.
     */
    private static final List<GPP_TADetail> allDetails  =
        getAllDetailObjects();
    /** The test dialog which is used to generate the actual image. */
    private static GPPTestDialog            testDialog  = null;
    
    /** Dialog to display "expected" image from test data file. */
    private static ExpDialog                expDialog   = null;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GPPTestDataInitializer.setFixedFontSize( 6. );
        new GPPTestDataInitializer();

        testDialog = GPPTestDialog.getDialog();
        testDialog.setDialogVisible( true );
        testDialog.setLocation( 400, 0 );

        expDialog = new ExpDialog();
        expDialog.setLocation( 400, testDialog.getHeight() );
        expDialog.setVisible( true );
        
    }

    @ParameterizedTest
    @MethodSource( "streamDetail" )
    public void test( GPP_TADetail detail )
    {
        selectTestDialogOption( detail );
        expDialog.setIcon(detail);
        Utils.pause( 2000 );
        assertImageEquals( detail );
    }

    /**
     * Within a given GPP_TADetail record,
     * find the Class class value (gppType)
     * and match it to radio button
     * in the test dialog that encapsulates
     * a GraphPropertySet with an equivalent type.
     * Then click the button.
     * 
     * @param detail    the given detail record
     */
    private void selectTestDialogOption( GPP_TADetail detail )
    {
        Class<?>        gppType = detail.getGPPType();
        PRadioButton<GraphPropertySet>   target  =
            testDialog.getRBList().stream()
                .filter( b -> b.get().getClass() == gppType )
                .findFirst().orElse( null );
        if ( target == null )
        {
            String  msg = 
                "PRadioButton for " + 
                gppType.getSimpleName() + 
                " not found.";
            fail( msg );
        }
        testDialog.doClick( target );
    }
    
    /**
     * Gets a list of all GPP_TADetail objects
     * encapsulated in test data files
     * in the target test directory.
     * 
     * @return a list of all GPP_TADetail objects from the test directory
     */
    private static List<GPP_TADetail> getAllDetailObjects()
    {
        File[]  allFiles    = 
            gppPath.listFiles( f -> f.getName().endsWith( ".ser" ) );
        List<GPP_TADetail>  detailObjects   =
            Stream.of( allFiles )
                .map( f -> getDetail( f ) )
                .collect( Collectors.toList() );

        return detailObjects;
    }
    
    /**
     * Given a file descriptor,
     * read the file and extract
     * the encapsulated GPP_TADetail object.
     * 
     * @param file  given file descriptor
     * 
     * @return  the extracted GPP_TADetail object
     */
    private static GPP_TADetail getDetail( File file )
    {
        GPP_TADetail    detail  = null;
        try (
            FileInputStream fStream = new FileInputStream( file );
            ObjectInputStream oStream = new ObjectInputStream( fStream );
        )
        {
            Object  obj = oStream.readObject();
            if ( !(obj instanceof GPP_TADetail) )
            {
                String  message     =
                    "Expected type: GPP_TADetail, actual type: " + 
                        obj.getClass().getSimpleName();
                fail( message );
            }
            detail = (GPP_TADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            fail( exc );
        }
        
        return detail;
    }
    
    /**
     * Source for executing parameterized test.
     * Returns a stream of GPP_TADetail objects
     * extracted from the test data files
     * in the test data directory.
     * 
     * @return  
     *      a stream of GPP_Detail objects extracted from the
     *      test data files in the test directory
     */
    private static Stream<GPP_TADetail> streamDetail()
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
     * incorporates a GPPProperySet class
     * as the type recorded in the detail record.
     * 
     * @param detail    the given detail record
     */
    private void assertImageEquals( GPP_TADetail detail )
    {
        BufferedImage   expImage    = detail.getBufferedImage();
        BufferedImage   actImage    = testDialog.getPanelImage();
        assertTrue( Utils.equals( expImage, actImage ) );
    }
    
    /**
     * Simple dialog to display the image
     * contained in a GPP_TADetail object.
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
         * encapsulated in a given GPP_TADetail record.
         * 
         * @param detail    the given GPP_TADetail record
         */
        public void setIcon( GPP_TADetail detail )
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
