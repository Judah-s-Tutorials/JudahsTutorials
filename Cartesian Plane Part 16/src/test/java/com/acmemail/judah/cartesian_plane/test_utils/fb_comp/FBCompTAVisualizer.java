package com.acmemail.judah.cartesian_plane.test_utils.fb_comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public abstract class FBCompTAVisualizer
{
    private static final String baseSubdir      = 
        Utils.BASE_TEST_DATA_DIR + "/" + FBCompTA.FEEDBACK_DIR;
    private int                 nextFileInx    = 0; 
    private final File          subdir;
    private final File[]        allFiles;
    
    private final Feedback      feedback;
    private final JLabel        expFeedback = new JLabel();
    private final JLabel        actFeedback = new JLabel();
    private final JLabel        testClass   = new JLabel();
    private final JLabel        testDir     = new JLabel();
    private final JLabel        currFile    = new JLabel();
    private final JLabel        currVal     = new JLabel();
    private final JLabel        currWeight  = new JLabel();
    private final JLabel        result      = new JLabel();
    
    private BufferedImage       expImage;
    private BufferedImage       actImage;
    private float               value       = 0;
    private float               weight      = 0;
    
    public FBCompTAVisualizer(
        String  subdir,
        Function<DoubleSupplier,Feedback> fbSupplier
    )
    {
        File    baseDir     = new File( baseSubdir );
        this.subdir = new File( baseDir, subdir );
        if ( !this.subdir.exists() )
        {
            String  msg = 
                "Test directory doesn't exist: " + this.subdir.getName();
            System.err.println( msg );
            System.exit( 1 );
        }
        allFiles = this.subdir.listFiles( (d,f) -> f.endsWith( ".ser" ) );
        if ( allFiles.length == 0 )
        {
            String  msg = 
                "Directory has no test files: " + this.subdir.getName();
            System.err.println( msg );
            System.exit( 1 );
        }
        
        feedback = fbSupplier.apply( () -> value );
        feedback.setPreferredSize( FBCompTA.COMP_SIZE );
        nextFile();
        makeGUI();
        getActualImage();
        
        result.setText( "Success" );
    }
    
    private void makeGUI()
    {
        String  title   = "Feedback Component Test Data Visualizer";
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getCenterPanel(), BorderLayout.CENTER );
        
        frame.setContentPane( pane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getCenterPanel()
    {
        JPanel      panel   = new JPanel();
        panel.add( getParameterPanel() );
        panel.add( getReviewPanel() );
        return panel;
    }
    
    private JPanel getParameterPanel()
    {
        String  simpleName  = feedback.getClass().getSimpleName();
        testClass.setText( simpleName );
        String  dirName     = subdir.getName();
        testDir.setText( dirName );
        
        JPanel  classParamPanel = 
            getParameterPanel( "Class: ", testClass );
        JPanel  dirParamPanel   = 
            getParameterPanel( "Test Dir: ", testDir );
        JPanel  fileParamPanel  = 
            getParameterPanel( "Test File: ", currFile );
        JPanel  valParamPanel   = 
            getParameterPanel( "Curr Value:  ", currVal );
        JPanel  wghtParamPanel  = 
            getParameterPanel( "Curr Weight:  ", currWeight );
        
        JPanel      masterPanel = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( masterPanel, BoxLayout.Y_AXIS );
        masterPanel.setLayout( layout );
        masterPanel.add( classParamPanel );
        masterPanel.add( dirParamPanel );
        masterPanel.add( fileParamPanel );
        masterPanel.add( valParamPanel );
        masterPanel.add( wghtParamPanel );
        
        Border  border  = 
            BorderFactory.createTitledBorder( "Parameters" );
        masterPanel.setBorder( border );
        return masterPanel;
    }
    
    private JPanel getReviewPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );

        JPanel  fbPanel = new JPanel();
        fbPanel.add( feedback );
        panel.add( fbPanel );
        
        panel.add( getImagePanel() );
        
        JPanel  resultPanel = new JPanel();
        resultPanel.add( result );
        panel.add( resultPanel );

        return panel;
    }
    
    private JPanel getImagePanel()
    {
        Border      border  =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getExpectedPanel() );
        panel.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
        panel.add( getActualPanel() );
        return panel;
    }
    
    /**
     * Constructs the panel
     * that contains the expected image.
     * 
     * @return  the panel that contains the expected image
     */
    private JPanel getExpectedPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Expected:" ) );
        expFeedback.setIcon( new ImageIcon( expImage ) );
        panel.add( expFeedback );
        return panel;
    }
    
    /**
     * Constructs the panel
     * that contains the actual image.
     * 
     * @return  the panel that contains the expected image
     */
    private JPanel getActualPanel()
    {
        // Don't have an actual image yet, but we need a label
        // of appropriate size, so make a dummy.
        int         width       = FBCompTA.COMP_SIZE.width;
        int         height      = FBCompTA.COMP_SIZE.height;
        int         type        = BufferedImage.TYPE_INT_RGB;
        actImage = new BufferedImage( width, height, type ); 
        actFeedback.setIcon( new ImageIcon( actImage ) );
        
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Actual:" ) );
        panel.add( actFeedback );
        return panel;
    }

    /**
     * Helper method for {@linkplain #getParameterPanel()}.
     * Formulates a panel 
     * containing the label of a descriptor
     * (e.g. "Data File: ")
     * and the associated descriptor
     * (e.g. fileDescriptor).
     * 
     * @param idStr text to display in the label
     * @param right descriptor to display
     * 
     * @return
     *      panel containing the label of a descriptor
     *      and the associated descriptor
     */
    private JPanel getParameterPanel( String idStr, JLabel right )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        int     margin  = 3;
        Border  border  = 
            BorderFactory.createEmptyBorder( 
                margin,
                margin,
                margin, 
                margin
            );
        panel.setBorder( border );
        
        JLabel  label   = new JLabel( idStr, SwingConstants.RIGHT );
        panel.add( label );
        panel.add( right );
        
        return panel;
    }
    
    /**
     * Obtains an image of the feedback component
     * as it is currently displayed.
     * 
     * @return  an image of the feedback component
     */
    private void getActualImage()
    {
        int             width       = FBCompTA.COMP_SIZE.width;
        int             height      = FBCompTA.COMP_SIZE.height;
        int             type        = expImage.getType();
        actImage = new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        feedback.paintComponent( graphics );
        
        ImageIcon       icon        = new ImageIcon( actImage );
        actFeedback.setIcon( icon );
    }
    
    private void nextFile()
    {
        final String    fmt = "%3.1f";
        if ( nextFileInx < allFiles.length )
        {
            File            nextFile    = allFiles[nextFileInx++];
            FBCompTADetail  detail      = getDetail( nextFile );
            value = (float)detail.getPropertyValue();
            weight = (float)detail.getWeight();
            String          strWeight   = String.format( fmt, weight );
            String          strValue    = String.format( fmt, value );
            currVal.setText( strValue );
            currWeight.setText( strWeight );
            currFile.setText( nextFile.getName() );
            expImage = detail.getBufferedImage();
            
            feedback.setWeight( weight );
            expFeedback.setIcon( new ImageIcon( expImage ) );
        }
    }
    
    private FBCompTADetail getDetail( File file )
    {
        FBCompTADetail  detail  = null;
        try (
            FileInputStream fileStream = new FileInputStream( file );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object  obj     = inStream.readObject();
            if ( !(obj instanceof FBCompTADetail) )
            {
                String  className   = obj.getClass().getName();
                String  error       = 
                    "Unexpected object type: " + className;
                System.err.println( error );
                System.exit( 1 );
            }
            detail = (FBCompTADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return detail;
    }
}
